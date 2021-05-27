package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Controller {
    private static final int MatrixSize = 5;
    public AnchorPane Pane;

    @FXML
    private Button Cancel;

    @FXML
    private Button Restart;

    @FXML
    private Button hint;

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
        InputDirections ();
        create_buttons ();
        HidhlightPosible ();
        Cancel.setOnAction(event->ClickCancel());
        Restart.setOnAction(event->ClickRestart());
        hint.setOnAction(event->ClickHint());
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

        while (currentNum != 25) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        for (int y = currentY, x = currentX; x < 5 && x >= 0 && y >= 0 && y < 5; x += i, y += j) {
                            if (matrix.get(y).get(x) == 0 && !HaveWay(stack, y * 5 + x + 1, ways)) {
                                able.add(new Point(y, x));
                            } else if (matrix.get(y).get(x) == 25 && stack.size() == 24) {
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
                //but.setText(Integer.toString(matrix.get(i).get(j)));
                but.setId("but" + k);
                int finalI = i;
                int finalJ = j;
                but.setOnAction(event-> ClickArrow (finalI, finalJ));

                if (i != 4 || j != 4) {
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


                Pane.getChildren().add(but);

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
        possible = FindPossible(solution);

        Cancel.setStyle("-fx-background-color: white");
        MakeAllWhite ();

        for (Point point : possible) {
            arrows.get(point.getY()).get(point.getX()).setStyle("-fx-background-color: green");
        }

        arrows.get((solution.peek() - 1) / 5).get((solution.peek() - 1) % 5).setStyle("-fx-background-color: yellow");
    }

    ArrayList<Point> FindPossible (Stack<Integer> stack) {
        ArrayList<Point> res = new ArrayList<>();
        int i = 0, j = 0;

        int currentY = (stack.peek() - 1) / 5;
        int currentX = (stack.peek() - 1) % 5;

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
            case -1:
                return res;

        }

        for (int y = currentY, x = currentX; x < 5 && x >= 0 && y >= 0 && y < 5; x += i, y += j) {
            if ((((y != 0 || x != 0) && (y != 4 || x != 4)) || stack.size() == 24) && (y != currentY || x != currentX) && !WasHere(y * 5 + x + 1, stack)) {
                res.add(new Point(y, x));
            }
        }

        return res;
    }

    void ClickArrow (int y, int x) {
        if ((y != 4 || x != 4) && IsPossible(y, x, possible)) {
            arrows.get((solution.peek() - 1) / 5).get((solution.peek() - 1) % 5).setStyle("-fx-background-color: red");
            solution.push(y * 5 + x + 1);
            possible.clear();
            HidhlightPosible ();
        } else if (y == 4 && x == 4 && IsPossible(y, x, possible)) {
            
        }
    }

    boolean IsPossible (int y, int x, ArrayList<Point> poss) {

        boolean res = false;

        for (Point point : poss) {
            if (point.getY() == y && point.getX() == x) {
                res = true;
                break;
            }
        }

        return res;
    }

    void MakeAllWhite () {
        for (int i = 0; i < MatrixSize; i++) {
            for (int j = 0; j < MatrixSize; j++) {
                if (!WasHere (i * 5 + j + 1, solution))
                    arrows.get(i).get(j).setStyle("-fx-background-color: white");
            }
        }
    }

    boolean WasHere (int n, Stack<Integer> closed) {
        boolean res = false;
        Stack<Integer> way = (Stack<Integer>) closed.clone();

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

    //To Do
    void ClickRestart () {
        Restart.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        /*Pane.getChildren().clear();
        solution = new Stack<>();
        possible = new ArrayList<>();
        solution.push(1);
        InputMatrix ();
        create_buttons ();
        InputDirections ();
        HidhlightPosible ();
        Cancel.setOnAction(event->ClickCancel());
        Restart.setOnAction(event->ClickRestart());*/
    }

    private int k = 0;

    void go (Stack<Integer> closed, int a) {
        closed.push(a);
        ArrayList<Point> poss = FindPossible(closed);

        if (poss.size() > 0) {
            for (Point point : poss) {
                go (closed, point.getY() * 5 + point.getX() + 1);
            }
        }

        if (closed.size() == 25) {
            return;
        } else {
            closed.pop();
            return;
        }
    }

    ArrayList<Integer> Answer (Stack<Integer> stack) {
        Stack<Integer> closed = (Stack<Integer>) stack.clone();
        ArrayList<Point> poss = FindPossible(closed);

        if (poss != null) {
            for (Point point : poss) {
                go (closed, point.getY() * 5 + point.getX() + 1);
            }
        }

        if (closed.size() == 25) {
            return new ArrayList<>(closed);
        } else {
            return null;
        }
    }

    void ClickHint () {
        ArrayList<Integer> answer = Answer(solution);

        if (answer != null) {
            arrows.get((answer.get(solution.size()) - 1) / 5).get((answer.get(solution.size()) - 1) % 5).setStyle("-fx-background-color: blue");
        } else {
            Cancel.setStyle("-fx-background-color: blue");
        }
    }
}
