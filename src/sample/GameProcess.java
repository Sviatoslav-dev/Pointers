package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GameProcess {
    private Stack<Integer> PlayersWay;
    private ArrayList<Pair> AllowedSteps;

    private Field field;

    public GameProcess (Field field) {
        setField(field);
    }

    public void setField (Field field) {
        this.field = field;
        PlayersWay = new Stack<>();
        AllowedSteps = new ArrayList<>();
        PlayersWay.push(1);
        HidhlightPosible ();

        for (int i = 0; i < Field.FieldSize; i++) {
            for (int j = 0; j < Field.FieldSize; j++) {
                int finalI = i;
                int finalJ = j;
                field.getArrow(i, j).setOnAction(event->ClickArrow(finalI, finalJ));
            }
        }
    }

    public void HidhlightPosible () {
        AllowedSteps = FindPossible(PlayersWay);

        field.getCancelButton().setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-color: black");
        MakeAllWhite ();

        for (Pair pair : AllowedSteps) {
            field.getArrow(pair.getY(), pair.getX()).setStyle("-fx-background-color: green; -fx-border-width: 1; -fx-border-color: black");
        }

        field.getArrow(field.ArrowY(PlayersWay.peek()), field.ArrowX(PlayersWay.peek())).setStyle("-fx-background-color: yellow; -fx-border-width: 1; -fx-border-color: black");
    }

    void MakeAllWhite () {
        for (int i = 0; i < Field.FieldSize; i++) {
            for (int j = 0; j < Field.FieldSize; j++) {
                if (WasHere(i * Field.FieldSize + j + 1, PlayersWay))
                    field.getArrow(i, j).setStyle("-fx-background-color: white; -fx-border-width: 1; -fx-border-color: black");
            }
        }
    }

    void ClickArrow (int i, int j) {
        if ((i != Field.FieldSize - 1 || j != Field.FieldSize - 1) && IsPossible(i, j, AllowedSteps)) {
            field.getArrow(field.ArrowY(PlayersWay.peek()), field.ArrowX(PlayersWay.peek())).setStyle("-fx-background-color: red; -fx-border-width: 1; -fx-border-color: black");
            PlayersWay.push(field.ArrowNum(i, j));
            AllowedSteps.clear();
            HidhlightPosible ();
        } else if (i == Field.FieldSize - 1 && j == Field.FieldSize - 1 && IsPossible(i, j, AllowedSteps)) {
            field.getArrow(field.ArrowY(PlayersWay.peek()), field.ArrowX(PlayersWay.peek())).setStyle("-fx-background-color: red; -fx-border-width: 1; -fx-border-color: black");
            PlayersWay.push(field.ArrowNum(i, j));
            AllowedSteps.clear();
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
            stage.initOwner(field.getPane().getScene().getWindow());
            stage.setOnHidden(event->Restart());
            stage.show();
        }
    }

    boolean IsPossible (int i, int j, ArrayList<Pair> allowed) {
        boolean res = false;

        for (Pair pair : allowed) {
            if (pair.getY() == i && pair.getX() == j) {
                res = true;
                break;
            }
        }

        return res;
    }

    void ClearSolution () {
        PlayersWay.clear();
        PlayersWay.push(1);
        HidhlightPosible();
    }

    ArrayList<Pair> FindPossible (Stack<Integer> way) {
        ArrayList<Pair> res = new ArrayList<>();
        int i = 0, j = 0;

        int currentY = field.ArrowY(way.peek());
        int currentX = field.ArrowX(way.peek());

        switch (field.getDirection(currentY, currentX)) {
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

        for (int y = currentY, x = currentX; x < Field.FieldSize && x >= 0 && y >= 0 && y < Field.FieldSize; x += i, y += j) {
            if ((((y != 0 || x != 0) && (y != Field.FieldSize - 1 || x != Field.FieldSize - 1)) || way.size() == Field.ArrowsNum - 1) && (y != currentY || x != currentX) && WasHere(field.ArrowNum(y, x), way)) {
                res.add(new Pair(y, x));
            }
        }

        return res;
    }

    boolean WasHere (int step, Stack<Integer> way) {
        boolean res = false;
        Stack<Integer> WayCopy = (Stack<Integer>) way.clone();

        while (WayCopy.size() > 0) {
            if (WayCopy.peek() == step)
                res = true;
            WayCopy.pop();
        }
        return !res;
    }

    void go (Stack<Integer> AnswerWay, int step) {
        AnswerWay.push(step);
        ArrayList<Pair> poss = FindPossible(AnswerWay);

        if (poss.size() > 0) {
            for (Pair pair : poss) {
                go (AnswerWay, field.ArrowNum(pair.getY(), pair.getX()));
            }
        }

        if (AnswerWay.size() != Field.ArrowsNum) {
            AnswerWay.pop();
        }
    }

    ArrayList<Integer> Answer (Stack<Integer> way) {
        Stack<Integer> AnswerWay = (Stack<Integer>) way.clone();
        ArrayList<Pair> allowed = FindPossible(AnswerWay);

        if (allowed != null) {
            for (Pair pair : allowed) {
                go (AnswerWay, field.ArrowNum(pair.getY(), pair.getX()));
            }
        }

        if (AnswerWay.size() == Field.ArrowsNum) {
            return new ArrayList<>(AnswerWay);
        } else {
            return null;
        }
    }

    ArrayList<Integer> Answer () {
        return Answer(PlayersWay);
    }

    void Restart () {
        setField(new Field(field.getPane(), field.getCancelButton()));
    }

    void CancelStep () {
        if (PlayersWay.size() > 1) {
            PlayersWay.pop();
            AllowedSteps.clear();
            HidhlightPosible();
        }
    }

    void hint () {
        if (PlayersWay.size() != Field.ArrowsNum) {
            ArrayList<Integer> answer = Answer();
            if (answer != null) {
                field.getArrow(field.ArrowY(answer.get(PlayersWay.size())), field.ArrowX(answer.get(PlayersWay.size()))).setStyle("-fx-background-color: blue; -fx-border-width: 1; -fx-border-color: black");
            } else {
                field.getCancelButton().setStyle("-fx-background-color: blue");
            }
        }
    }
}