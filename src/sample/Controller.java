package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Stack;

public class Controller {
    private static final int MatrixSize = 5;
    public AnchorPane Pane;

    private ArrayList<ArrayList<Integer>> matrix;
    private ArrayList<Stack<Integer>> ways;
    private ArrayList<ArrayList<Button>> arrows;

    @FXML
    void initialize() {
        InputMatrix ();
        create_buttons ();
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

                Pane.getChildren().add(but);

                arrows.get(i).add(but);
            }
        }
    }
}
