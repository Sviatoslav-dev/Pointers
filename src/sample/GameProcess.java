package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GameProcess {
    public Stack<Integer> solution;
    public ArrayList<Pair> possible;

    public Field field;

    public GameProcess (Field field) {
        this.field = field;
        solution = new Stack<>();
        possible = new ArrayList<>();
        solution.push(1);
        HidhlightPosible ();

        for (int i = 0; i < Field.MatrixSize; i++) {
            for (int j = 0; j < Field.MatrixSize; j++) {
                int finalI = i;
                int finalJ = j;
                field.arrows.get(i).get(j).setOnAction(event->ClickArrow(finalI, finalJ));
            }
        }
    }

    public void HidhlightPosible () {
        possible = FindPossible(solution);

        field.Cancel.setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-color: black");
        MakeAllWhite ();

        for (Pair pair : possible) {
            field.arrows.get(pair.getY()).get(pair.getX()).setStyle("-fx-background-color: green; -fx-border-width: 1; -fx-border-color: black");
        }

        field.arrows.get(field.ArrowY(solution.peek())).get(field.ArrowX(solution.peek())).setStyle("-fx-background-color: yellow; -fx-border-width: 1; -fx-border-color: black");
    }

    void MakeAllWhite () {
        for (int i = 0; i < Field.MatrixSize; i++) {
            for (int j = 0; j < Field.MatrixSize; j++) {
                if (WasHere(i * Field.MatrixSize + j + 1, solution))
                    field.arrows.get(i).get(j).setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-color: black");
            }
        }
    }

    void ClickArrow (int y, int x) {
        if ((y != Field.MatrixSize - 1 || x != Field.MatrixSize - 1) && IsPossible(y, x, possible)) {
            field.arrows.get(field.ArrowY(solution.peek())).get(field.ArrowX(solution.peek())).setStyle("-fx-background-color: red; -fx-border-width: 1; -fx-border-color: black");
            solution.push(field.ArrowNum(y, x));
            possible.clear();
            HidhlightPosible ();
        } else if (y == Field.MatrixSize - 1 && x == Field.MatrixSize - 1 && IsPossible(y, x, possible)) {
            field.arrows.get(field.ArrowY(solution.peek())).get(field.ArrowX(solution.peek())).setStyle("-fx-background-color: red; -fx-border-width: 1; -fx-border-color: black");
            solution.push(field.ArrowNum(y, x));
            possible.clear();
            HidhlightPosible ();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("win_window.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            Window primaryStage = field.pane.getScene().getWindow();
            stage.initOwner(primaryStage);
            stage.show();
        }
    }

    boolean IsPossible (int y, int x, ArrayList<Pair> poss) {
        boolean res = false;

        for (Pair pair : poss) {
            if (pair.getY() == y && pair.getX() == x) {
                res = true;
                break;
            }
        }

        return res;
    }

    void ClearSolution () {
        solution.clear();
        solution.push(1);
        HidhlightPosible();
    }

    ArrayList<Pair> FindPossible (Stack<Integer> stack) {
        ArrayList<Pair> res = new ArrayList<>();
        int i = 0, j = 0;

        int currentY = field.ArrowY(stack.peek());
        int currentX = field.ArrowX(stack.peek());

        switch (field.directions.get(currentY).get(currentX)) {
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

        for (int y = currentY, x = currentX; x < Field.MatrixSize && x >= 0 && y >= 0 && y < Field.MatrixSize; x += i, y += j) {
            if ((((y != 0 || x != 0) && (y != Field.MatrixSize - 1 || x != Field.MatrixSize - 1)) || stack.size() == Field.MatrixSizeSquared - 1) && (y != currentY || x != currentX) && WasHere(field.ArrowNum(y, x), stack)) {
                res.add(new Pair(y, x));
            }
        }

        return res;
    }

    boolean WasHere (int n, Stack<Integer> closed) {
        boolean res = false;
        Stack<Integer> way = (Stack<Integer>) closed.clone();

        while (way.size() > 0) {
            if (way.peek() == n)
                res = true;
            way.pop();
        }
        return !res;
    }

    void go (Stack<Integer> closed, int a) {
        closed.push(a);
        ArrayList<Pair> poss = FindPossible(closed);

        if (poss.size() > 0) {
            for (Pair pair : poss) {
                go (closed, field.ArrowNum(pair.getY(), pair.getX()));
            }
        }

        if (closed.size() != Field.MatrixSizeSquared) {
            closed.pop();
        }
    }

    ArrayList<Integer> Answer (Stack<Integer> stack) {
        Stack<Integer> closed = (Stack<Integer>) stack.clone();
        ArrayList<Pair> poss = FindPossible(closed);

        if (poss != null) {
            for (Pair pair : poss) {
                go (closed, field.ArrowNum(pair.getY(), pair.getX()));
            }
        }

        if (closed.size() == Field.MatrixSizeSquared) {
            return new ArrayList<>(closed);
        } else {
            return null;
        }
    }
}
