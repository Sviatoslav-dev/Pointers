package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Stack;

public class Controller {
    private static final int MatrixSize = 5;
    public AnchorPane Pane;

    @FXML
    private Button Cancel;

    @FXML
    private Button Restar;

    private ArrayList<ArrayList<Integer>> matrix;
    private ArrayList<Stack<Integer>> ways;
    private ArrayList<ArrayList<Integer>> directions;
    private ArrayList<ArrayList<Button>> arrows;
    private Stack<Integer> solution;
    private ArrayList<Point> possible;

    @FXML
    void initialize() {
        solution = new Stack<>();
        possible = new ArrayList<>();
        solution.push(1);
        InputMatrix ();
        create_buttons ();
        InputDirections ();
        HidhlightPosible ();
        Cancel.setOnAction(event->ClickCancel());
    }

    void InputMatrix () {
        ways = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        ArrayList<Point> able = new ArrayList<>();
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
        matrix.get(4).set(4, 25);

        stack.push(1);

        while (currentNum != 24) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        for (int y = currentY, x = currentX; x < 5 && x >= 0 && y >= 0 && y < 5; x += i, y += j) {
                            if (matrix.get(y).get(x) == 0 && !HaveWay(stack, y * 5 + x + 1)) {
                                able.add(new Point(y, x));
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
                stack.push(currentY * 5 + currentX + 1);

                System.out.println(stack);

                matrix.get(currentY).set(currentX, currentNum);

                able.clear();

            } else {
                ways.add((Stack<Integer>) stack.clone());
                stack.pop();
                System.out.println(stack);

                matrix.get(currentY).set(currentX, 0);

                currentY = (stack.peek() - 1) / 5;
                currentX = (stack.peek() - 1) % 5;
                currentNum--;
            }
        }


    }

    boolean HaveWay (Stack<Integer> s, int a) {
        boolean res = false;
        Stack<Integer> newWay = (Stack<Integer>) s.clone();
        newWay.push(a);

        for (Stack<Integer> way : ways) {
            if (way.equals(newWay)) {
                res = true;
                break;
            }
        }

        return res;
    }

    void create_buttons () {
        arrows = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            arrows.add(new ArrayList<>());
        }

        int X, Y;
        int k;

        for (int i = 0; i < 5; i ++) {
            for (int j = 0; j < 5; j++) {
                X = 100 + j * 40;
                Y = 200 + i * 40;

                Button but = new Button();
                but.setLayoutX(X);
                but.setLayoutY(Y);
                but.setPrefHeight(40);
                but.setPrefWidth(40);
                k = i*5+j + 1;
                but.setText(Integer.toString(matrix.get(i).get(j)));
                but.setId("but" + k);
                int finalI = i;
                int finalJ = j;
                but.setOnAction(event-> ClickArrow (finalI, finalJ));

                Pane.getChildren().add(but);

                arrows.get(i).add(but);
            }
        }
    }

    void InputDirections () {
        int y1 = 0, y2 = 0, x1 = 0, x2 = 0;
        double angle;

        directions = new ArrayList<>();
        for (int i = 0; i < MatrixSize; i++) {
            directions.add(new ArrayList<>());
            for (int j = 0; j < MatrixSize; j++) {
                directions.get(i).add(-1);
            }
        }

        for (int i = 1; i < 25; i++) {
            for (int n = 0; n < 5; n++) {
                for (int k = 0; k < 5; k++) {
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

            /*System.out.println("cos " + (-(y2 - y1))/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
            angle = Math.acos((-(y2 - y1))/(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1))));
            System.out.println(angle);

            directions.get(y1).set(x1, (int)((angle / (2 * Math.PI)) * 7 + 0.1));*/

            if (y2 < y1 && x2 < x1) {
                directions.get(y1).set(x1, 7);
            } else if (y2 < y1 && x2 == x1) {
                directions.get(y1).set(x1, 0);
            } else if (y2 < y1 && x2 > x1) {
                directions.get(y1).set(x1, 1);
            } else if (y2 == y1 && x2 < x1) {
                directions.get(y1).set(x1, 6);
            } else if (y2 == y1 && x2 > x1) {
                directions.get(y1).set(x1, 2);
            } else if (y2 > y1 && x2 < x1) {
                directions.get(y1).set(x1, 5);
            } else if (y2 > y1 && x2 == x1) {
                directions.get(y1).set(x1, 4);
            } else if (y2 > y1 && x2 > x1) {
                directions.get(y1).set(x1, 3);
            }
        }
        PrintMatrix(directions);
    }

    void PrintMatrix (ArrayList<ArrayList<Integer>> M) {
        for (ArrayList<Integer> integers : M) {
            System.out.println(integers);
        }
    }

    void HidhlightPosible () {
        int i = 0, j = 0;

        int currentY = (solution.peek() - 1) / 5;
        int currentX = (solution.peek() - 1) % 5;

        switch (directions.get(currentY).get(currentX)) {
            case 0:
                i = 0;
                j = -1;
                break;
            case 1:
                i = 1;
                j = -1;
                break;
            case 2:
                i = 1;
                j = 0;
                break;
            case 3:
                i = 1;
                j = 1;
                break;
            case 4:
                i = 0;
                j = 1;
                break;
            case 5:
                i = -1;
                j = 1;
                break;
            case 6:
                i = -1;
                j = 0;
                break;
            case 7:
                i = -1;
                j = -1;
                break;
        }

        MakeAllWhite ();

        for (int y = currentY, x = currentX; x < 5 && x >= 0 && y >= 0 && y < 5; x += i, y += j) {
            if ((y != 0 || x != 0) && (y != 4 || x != 4) && (y != currentY || x != currentX) && !WasHere(y * 5 + x + 1)) {
                arrows.get(y).get(x).setStyle("-fx-background-color: green");
                possible.add(new Point(y, x));
            } else if (y == currentY && x == currentX) {
                arrows.get(y).get(x).setStyle("-fx-background-color: yellow");
            }
        }
    }

    void ClickArrow (int y, int x) {
        if ((y != 4 || x != 4) && IsPossible(y, x)) {
            arrows.get((solution.peek() - 1) / 5).get((solution.peek() - 1) % 5).setStyle("-fx-background-color: red");
            solution.push(y * 5 + x + 1);
            possible.clear();
            HidhlightPosible ();
        }
    }

    boolean IsPossible (int y, int x) {

        boolean res = false;

        for (int i = 0; i < possible.size(); i++) {
            if (possible.get(i).getY() == y && possible.get(i).getX() == x) {
                res = true;
            }
        }

        return res;
    }

    void MakeAllWhite () {
        for (int i = 0; i < MatrixSize; i++) {
            for (int j = 0; j < MatrixSize; j++) {
                if (!WasHere (i * 5 + j + 1))
                    arrows.get(i).get(j).setStyle("-fx-background-color: white");
            }
        }
    }

    boolean WasHere (int n) {
        boolean res = false;
        Stack<Integer> way = (Stack<Integer>) solution.clone();

        while (way.size() > 0) {
            if (way.peek() == n)
                res = true;
            way.pop();
        }
        return res;
    }

    void ClickCancel () {
        if (solution.size() > 1) {
            solution.pop();
            possible.clear();
            HidhlightPosible();
        }
    }
}
