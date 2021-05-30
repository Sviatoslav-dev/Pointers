package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GameProcess {
    private Stack<Integer> PlayersWay;
    private ArrayList<Pair> AllowedSteps;
    private Button UndoButton;
    private Field field;

    public GameProcess (Field field, Button UndoButton) {
        setField(field, UndoButton);
    }

    public void setField (Field field, Button CancelButton) {
        this.field = field;
        this.UndoButton = CancelButton;
        PlayersWay = new Stack<>();
        AllowedSteps = new ArrayList<>();
        PlayersWay.push(1);
        HighlightAllowed();

        for (int i = 0; i < Field.FieldSize; i++) {
            for (int j = 0; j < Field.FieldSize; j++) {
                int finalI = i;
                int finalJ = j;
                field.getArrow(i, j).setOnAction(event->ClickArrow(finalI, finalJ));
            }
        }
    }

    public void HighlightAllowed() {
        AllowedSteps = FindAllowed(PlayersWay);

        MakeAllWhite ();

        for (Pair pair : AllowedSteps) {
            field.getArrow(pair.getY(), pair.getX()).setStyle("-fx-background-color: #1abc9c");
            field.getArrow(pair.getY(), pair.getX()).setCursor(Cursor.HAND);
        }

        field.getArrow(field.ArrowY(PlayersWay.peek()), field.ArrowX(PlayersWay.peek())).setStyle("-fx-background-color: #e67e22");
    }

    void MakeAllWhite () {
        UndoButton.setStyle("-fx-background-color: #ecf0f1");

        for (int i = 0; i < Field.FieldSize; i++) {
            for (int j = 0; j < Field.FieldSize; j++) {
                if (WasHere(i * Field.FieldSize + j + 1, PlayersWay))
                    field.getArrow(i, j).setStyle("-fx-background-color: #ecf0f1");
                field.getArrow(i, j).setCursor(Cursor.DEFAULT);
            }
        }
    }

    void ClickArrow (int i, int j) {
        if (IsAllowed(i, j, AllowedSteps)) {
            field.getArrow(field.ArrowY(PlayersWay.peek()), field.ArrowX(PlayersWay.peek())).setStyle("-fx-background-color: #f1c40f");
            PlayersWay.push(field.ArrowNum(i, j));
            AllowedSteps.clear();
            HighlightAllowed();

            if (i == Field.FieldSize - 1 && j == Field.FieldSize - 1) {
                LoadWinWindow ();
            }
        }
    }

    void LoadWinWindow () {
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
        stage.setOnHidden(event-> NewField());
        stage.show();
    }

    boolean IsAllowed(int i, int j, ArrayList<Pair> allowed) {
        boolean res = false;

        for (Pair pair : allowed) {
            if (pair.getY() == i && pair.getX() == j) {
                res = true;
                break;
            }
        }

        return res;
    }

    void ClearPlayersWay() {
        PlayersWay.clear();
        PlayersWay.push(1);
        HighlightAllowed();
    }

    ArrayList<Pair> FindAllowed (Stack<Integer> way) {
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

    void Bypass (Stack<Integer> AnswerWay, int step) {
        AnswerWay.push(step);
        ArrayList<Pair> poss = FindAllowed(AnswerWay);

        if (poss.size() > 0) {
            for (Pair pair : poss) {
                Bypass(AnswerWay, field.ArrowNum(pair.getY(), pair.getX()));
            }
        }

        if (AnswerWay.size() != Field.ArrowsNum) {
            AnswerWay.pop();
        }
    }

    ArrayList<Integer> FindAnswer(Stack<Integer> way) {
        Stack<Integer> AnswerWay = (Stack<Integer>) way.clone();
        ArrayList<Pair> allowed = FindAllowed(AnswerWay);

        if (allowed != null) {
            for (Pair pair : allowed) {
                Bypass(AnswerWay, field.ArrowNum(pair.getY(), pair.getX()));
            }
        }

        if (AnswerWay.size() == Field.ArrowsNum) {
            return new ArrayList<>(AnswerWay);
        } else {
            return null;
        }
    }

    ArrayList<Integer> FindAnswer() {
        return FindAnswer(PlayersWay);
    }

    void NewField () {
        setField(new Field(field.getPane()), UndoButton);
    }

    void Undo () {
        if (PlayersWay.size() > 1) {
            PlayersWay.pop();
            AllowedSteps.clear();
            HighlightAllowed();
        }
    }

    void hint () {
        if (PlayersWay.size() != Field.ArrowsNum) {
            ArrayList<Integer> answer = FindAnswer();
            if (answer != null) {
                field.getArrow(field.ArrowY(answer.get(PlayersWay.size())), field.ArrowX(answer.get(PlayersWay.size()))).setStyle("-fx-background-color: #2980b9");
            } else {
                UndoButton.setStyle("-fx-background-color: #2980b9");
            }
        }
    }
}
