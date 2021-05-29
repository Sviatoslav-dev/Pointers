package sample;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Stack;

public class Field {
    public static final int MatrixSize = 5;
    public final AnchorPane pane;
    public final Button Cancel;

    private ArrayList<ArrayList<Integer>> matrix;
    public ArrayList<ArrayList<Integer>> directions;
    public ArrayList<ArrayList<Button>> arrows;
    public static int MatrixSizeSquared;

    public Field (AnchorPane pane, Button Cancel) {
        MatrixSizeSquared = MatrixSize * MatrixSize;
        this.pane = pane;
        this.Cancel = Cancel;
        InputMatrix ();
        InputDirections ();
        create_buttons ();
    }

    void InputMatrix () {
        ArrayList<Stack<Integer>> ways = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        ArrayList<Pair> able = new ArrayList<>();
        matrix = new ArrayList<>();
        for (int i = 0; i < MatrixSize; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < MatrixSize; j++) {
                matrix.get(i).add(0);
            }
        }

        int currentY = 0;
        int currentX = 0;
        int currentNum = 1;

        matrix.get(0).set(0, 1);
        matrix.get(MatrixSize - 1).set(MatrixSize - 1, MatrixSizeSquared);

        stack.push(1);

        while (currentNum != MatrixSizeSquared) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        for (int y = currentY, x = currentX; x < MatrixSize && x >= 0 && y >= 0 && y < MatrixSize; x += i, y += j) {
                            if (matrix.get(y).get(x) == 0 && !HaveWay(stack, ArrowNum(y, x), ways)) {
                                able.add(new Pair(y, x));
                            } else if (matrix.get(y).get(x) == MatrixSizeSquared && stack.size() == MatrixSizeSquared - 1) {
                                able.add(new Pair(y, x));
                            }
                        }
                    }
                }
            }

            if (able.size() > 0) {
                int rand = (int)(Math.random() * able.size());

                currentX = able.get(rand).getX();
                currentY = able.get(rand).getY();

                currentNum++;
                stack.push(ArrowNum(currentY, currentX));

                System.out.println(stack);

                matrix.get(currentY).set(currentX, currentNum);

                able.clear();

            } else {
                ways.add((Stack<Integer>) stack.clone());
                stack.pop();
                System.out.println(stack);

                matrix.get(currentY).set(currentX, 0);

                currentY = ArrowY(stack.peek());
                currentX = ArrowX(stack.peek());
                currentNum--;
            }
        }
    }

    boolean HaveWay (Stack<Integer> s, int a, ArrayList<Stack<Integer>> wayss) {
        boolean res = false;
        Stack<Integer> newWay = (Stack<Integer>) s.clone();
        newWay.push(a);

        for (Stack<Integer> way : wayss) {
            if (way.equals(newWay)) {
                res = true;
                break;
            }
        }

        return res;
    }

    void create_buttons () {
        arrows = new ArrayList<>();
        Image image;
        image = new Image(getClass().getResourceAsStream("picture.png"));

        for (int i = 0; i < MatrixSize; i++) {
            arrows.add(new ArrayList<>());
        }

        int X, Y;
        for (int i = 0; i < MatrixSize; i ++) {
            for (int j = 0; j < MatrixSize; j++) {
                X = 100 + j * 40;
                Y = 200 + i * 40;

                Button but = new Button();
                but.setStyle("-fx-border-width: 2");
                but.setLayoutX(X);
                but.setLayoutY(Y);
                but.setPrefHeight(45);
                but.setPrefWidth(45);

                if (i != MatrixSize - 1 || j != MatrixSize - 1) {
                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    imageView.setFitWidth(25);
                    imageView.setFitHeight(25);
                    System.out.println(directions.get(i).get(j));
                    imageView.setRotate(directions.get(i).get(j) * 45 + 90);

                    but.graphicProperty().setValue(imageView);
                } else {
                    but.setText(Integer.toString(matrix.get(i).get(j)));
                }


                pane.getChildren().add(but);

                arrows.get(i).add(but);
            }
        }
    }

    void InputDirections () {
        int y1 = 0, y2 = 0, x1 = 0, x2 = 0;

        directions = new ArrayList<>();
        for (int i = 0; i < MatrixSize; i++) {
            directions.add(new ArrayList<>());
            for (int j = 0; j < MatrixSize; j++) {
                directions.get(i).add(-1);
            }
        }

        for (int i = 1; i < MatrixSizeSquared; i++) {
            for (int n = 0; n < MatrixSize; n++) {
                for (int k = 0; k < MatrixSize; k++) {
                    if (matrix.get(n).get(k) == i) {
                        y1 = n;
                        x1 = k;
                    }
                    if (matrix.get(n).get(k) == i + 1) {
                        y2 = n;
                        x2 = k;
                    }
                }
            }

            directions.get(y1).set(x1, DirectionNum (y1, y2, x1, x2));
        }
    }

    int DirectionNum (int y1, int y2, int x1, int x2) {
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
        return i * MatrixSize + j + 1;
    }

    int ArrowY (int n) {
        return (n - 1) / MatrixSize;
    }

    int ArrowX (int n) {
        return (n - 1) % MatrixSize;
    }
}
