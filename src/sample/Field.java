package sample;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Stack;

public class Field {
    public static final int FieldSize = 5; //Розмір поля
    public static int CellNum; //Кількість клітинок
    private final AnchorPane pane; //Панель

    private ArrayList<ArrayList<Integer>> directions; //Кут нарямку вказівників
    private ArrayList<ArrayList<Button>> cells; //Клітинка

    public Field (AnchorPane pane) { //Створення поля
        CellNum = FieldSize * FieldSize;
        this.pane = pane;
        ArrayList<ArrayList<Integer>> StartWay = InputStartWay();
        InputDirections (StartWay);
        create_cells();
    }

    private ArrayList<ArrayList<Integer>> InputStartWay() { //Генерація шляху
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

        int currentY = 0, currentX = 0, currentNum = 1;

        StartWay.get(0).set(0, 1);
        StartWay.get(FieldSize - 1).set(FieldSize - 1, CellNum);

        way.push(1);

        while (currentNum != CellNum) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        for (int y = currentY, x = currentX; x < FieldSize && x >= 0 && y >= 0 && y < FieldSize; x += i, y += j) {
                            if (StartWay.get(y).get(x) == 0 && !isDeadEnd(way, CellNum(y, x), ways)) {
                                able.add(new Pair(y, x));
                            } else if (StartWay.get(y).get(x) == CellNum && way.size() == CellNum - 1) {
                                able.add(new Pair(y, x));
                            }
                        }
                    }
                }
            }

            if (able.size() > 0) {
                int randArrow = (int)(Math.random() * able.size());

                currentX = able.get(randArrow).getSecond();
                currentY = able.get(randArrow).getFirst();

                currentNum++;
                way.push(CellNum(currentY, currentX));

                System.out.println(way);

                StartWay.get(currentY).set(currentX, currentNum);

                able.clear();

            } else {
                ways.add((Stack<Integer>) way.clone());
                way.pop();
                System.out.println(way);

                StartWay.get(currentY).set(currentX, 0);

                currentY = CellY(way.peek());
                currentX = CellX(way.peek());
                currentNum--;
            }
        }
        return StartWay;
    }

    private boolean isDeadEnd (Stack<Integer> way, int step, ArrayList<Stack<Integer>> DeadEnds) { //Перевірка чи не являється даний крок глухим кутом
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

    private void create_cells() { //Створення клітинок
        cells = new ArrayList<>();
        Image ArrowImage;
        ArrowImage = new Image(getClass().getResourceAsStream("arrow.png"));
        Image FinishImage;
        FinishImage = new Image(getClass().getResourceAsStream("finish.png"));

        for (int i = 0; i < FieldSize; i++) {
            cells.add(new ArrayList<>());
        }

        int CellPositionX, CellPositionY;
        for (int i = 0; i < FieldSize; i ++) {
            for (int j = 0; j < FieldSize; j++) {
                CellPositionX = 100 + j * 40;
                CellPositionY = 150 + i * 40;

                Button CellButton = new Button();
                CellButton.setStyle("-fx-border-width: 2");
                CellButton.setLayoutX(CellPositionX);
                CellButton.setLayoutY(CellPositionY);
                CellButton.setPrefHeight(45);
                CellButton.setPrefWidth(45);

                ImageView ArrowImageView = new ImageView();

                if (i != FieldSize - 1 || j != FieldSize - 1) {
                    ArrowImageView.setImage(ArrowImage);
                    ArrowImageView.setRotate(directions.get(i).get(j));
                } else {
                    ArrowImageView.setImage(FinishImage);
                }
                ArrowImageView.setFitWidth(25);
                ArrowImageView.setFitHeight(25);
                CellButton.graphicProperty().setValue(ArrowImageView);

                pane.getChildren().add(CellButton);

                cells.get(i).add(CellButton);
            }
        }
    }

    private void InputDirections (ArrayList<ArrayList<Integer>> StartWay) { //Визначення кутів напрямку вказівників за згенерованим шляхом
        int y1 = 0, y2 = 0, x1 = 0, x2 = 0;

        directions = new ArrayList<>();
        for (int i = 0; i < FieldSize; i++) {
            directions.add(new ArrayList<>());
            for (int j = 0; j < FieldSize; j++) {
                directions.get(i).add(-1);
            }
        }

        for (int i = 1; i < CellNum; i++) {
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

    private int CalculateDirection(int y1, int y2, int x1, int x2) { //Визначення кута
        if (y2 < y1 && x2 < x1) {
            return 315;
        } else if (y2 < y1 && x2 == x1) {
            return 0;
        } else if (y2 < y1) {
            return 45;
        } else if (y2 == y1 && x2 < x1) {
            return 270;
        } else if (y2 == y1 && x2 > x1) {
            return 90;
        } else if (y2 > y1 && x2 < x1) {
            return 225;
        } else if (y2 > y1 && x2 == x1) {
            return 180;
        } else if (y2 > y1) {
            return 135;
        }
        return -1;
    }

    public int CellNum(int i, int j) {
        return i * FieldSize + j + 1;
    } //Номер клітинки за координатами

    public int CellY(int n) {
        return (n - 1) / FieldSize;
    } //Номер рядка

    public int CellX(int n) {
        return (n - 1) % FieldSize;
    } //Номер стовпця

    public AnchorPane getPane () {
        return pane;
    } //Отримати панель

    public int getDirection (int i, int j) {
        return directions.get(i).get(j);
    } //Отримати кут напрямку

    public Button getCell(int i, int j) {
        return cells.get(i).get(j);
    } //Отримати клітинку
}
