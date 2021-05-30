package sample;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Stack;

public class Field {
    public static final int FieldSize = 5;
    public static int ArrowsNum;
    private final AnchorPane pane;
    private final Button CancelButton;

    private ArrayList<ArrayList<Integer>> directions;
    private ArrayList<ArrayList<Button>> arrows;

    public Field (AnchorPane pane, Button CancelButton) {
        ArrowsNum = FieldSize * FieldSize;
        this.pane = pane;
        this.CancelButton = CancelButton;
        ArrayList<ArrayList<Integer>> StartWay = InputStartWay();
        InputDirections (StartWay);
        create_buttons ();
    }

    ArrayList<ArrayList<Integer>> InputStartWay() {
        ArrayList<Stack<Integer>> ways = new ArrayList<>();
        Stack<Integer> way = new Stack<>();
        ArrayList<Pair> able = new ArrayList<>();
        ArrayList<ArrayList<Integer>> StartWay = new ArrayList<>();
        for (int i = 0; i < FieldSize; i++) {
            StartWay.add(new ArrayList<>());
            for (int j = 0; j < FieldSize; j++) {
                StartWay.get(i).add(0);
            }
        }

        int currentY = 0;
        int currentX = 0;
        int currentNum = 1;

        StartWay.get(0).set(0, 1);
        StartWay.get(FieldSize - 1).set(FieldSize - 1, ArrowsNum);

        way.push(1);

        while (currentNum != ArrowsNum) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        for (int y = currentY, x = currentX; x < FieldSize && x >= 0 && y >= 0 && y < FieldSize; x += i, y += j) {
                            if (StartWay.get(y).get(x) == 0 && !isDeadEnd(way, ArrowNum(y, x), ways)) {
                                able.add(new Pair(y, x));
                            } else if (StartWay.get(y).get(x) == ArrowsNum && way.size() == ArrowsNum - 1) {
                                able.add(new Pair(y, x));
                            }
                        }
                    }
                }
            }

            if (able.size() > 0) {
                int randArrow = (int)(Math.random() * able.size());

                currentX = able.get(randArrow).getX();
                currentY = able.get(randArrow).getY();

                currentNum++;
                way.push(ArrowNum(currentY, currentX));

                System.out.println(way);

                StartWay.get(currentY).set(currentX, currentNum);

                able.clear();

            } else {
                ways.add((Stack<Integer>) way.clone());
                way.pop();
                System.out.println(way);

                StartWay.get(currentY).set(currentX, 0);

                currentY = ArrowY(way.peek());
                currentX = ArrowX(way.peek());
                currentNum--;
            }
        }
        return StartWay;
    }

    boolean isDeadEnd (Stack<Integer> way, int step, ArrayList<Stack<Integer>> DeadEnds) {
        boolean res = false;
        Stack<Integer> WayCopy = (Stack<Integer>) way.clone();
        WayCopy.push(step);

        for (Stack<Integer> DeadEnd : DeadEnds) {
            if (DeadEnd.equals(WayCopy)) {
                res = true;
                break;
            }
        }

        return res;
    }

    void create_buttons () {
        arrows = new ArrayList<>();
        Image ArrowImage;
        ArrowImage = new Image(getClass().getResourceAsStream("arrow.png"));
        Image FinishImage;
        FinishImage = new Image(getClass().getResourceAsStream("finish.png"));

        for (int i = 0; i < FieldSize; i++) {
            arrows.add(new ArrayList<>());
        }

        int ArrowsPositionX, ArrowPositionY;
        for (int i = 0; i < FieldSize; i ++) {
            for (int j = 0; j < FieldSize; j++) {
                ArrowsPositionX = 100 + j * 40;
                ArrowPositionY = 150 + i * 40;

                Button ArrowButton = new Button();
                ArrowButton.setStyle("-fx-border-width: 2");
                ArrowButton.setLayoutX(ArrowsPositionX);
                ArrowButton.setLayoutY(ArrowPositionY);
                ArrowButton.setPrefHeight(45);
                ArrowButton.setPrefWidth(45);

                ImageView ArrowImageView = new ImageView();

                if (i != FieldSize - 1 || j != FieldSize - 1) {
                    ArrowImageView.setImage(ArrowImage);
                    ArrowImageView.setRotate(directions.get(i).get(j) * 45);
                } else {
                    ArrowImageView.setImage(FinishImage);
                }
                ArrowImageView.setFitWidth(25);
                ArrowImageView.setFitHeight(25);
                ArrowButton.graphicProperty().setValue(ArrowImageView);

                pane.getChildren().add(ArrowButton);

                arrows.get(i).add(ArrowButton);
            }
        }
    }

    void InputDirections (ArrayList<ArrayList<Integer>> StartWay) {
        int y1 = 0, y2 = 0, x1 = 0, x2 = 0;

        directions = new ArrayList<>();
        for (int i = 0; i < FieldSize; i++) {
            directions.add(new ArrayList<>());
            for (int j = 0; j < FieldSize; j++) {
                directions.get(i).add(-1);
            }
        }

        for (int i = 1; i < ArrowsNum; i++) {
            for (int n = 0; n < FieldSize; n++) {
                for (int k = 0; k < FieldSize; k++) {
                    if (StartWay.get(n).get(k) == i) {
                        y1 = n;
                        x1 = k;
                    }
                    if (StartWay.get(n).get(k) == i + 1) {
                        y2 = n;
                        x2 = k;
                    }
                }
            }
            directions.get(y1).set(x1, CalculateDirection(y1, y2, x1, x2));
        }
    }

    int CalculateDirection(int y1, int y2, int x1, int x2) {
        if (y2 < y1 && x2 < x1) {
            return 7;
        } else if (y2 < y1 && x2 == x1) {
            return 0;
        } else if (y2 < y1) {
            return 1;
        } else if (y2 == y1 && x2 < x1) {
            return 6;
        } else if (y2 == y1 && x2 > x1) {
            return 2;
        } else if (y2 > y1 && x2 < x1) {
            return 5;
        } else if (y2 > y1 && x2 == x1) {
            return 4;
        } else if (y2 > y1) {
            return 3;
        }
        return -1;
    }

    int ArrowNum (int i, int j) {
        return i * FieldSize + j + 1;
    }

    int ArrowY (int n) {
        return (n - 1) / FieldSize;
    }

    int ArrowX (int n) {
        return (n - 1) % FieldSize;
    }

    AnchorPane getPane () {
        return pane;
    }

    Button getCancelButton() {
        return CancelButton;
    }

    int getDirection (int i, int j) {
        return directions.get(i).get(j);
    }

    Button getArrow (int i, int j) {
        return arrows.get(i).get(j);
    }
}
