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
    private Stack<Integer> PlayersWay; //Шлях ігрока
    private ArrayList<Pair> AllowedSteps; //Клітинки на які можна перейти
    private final Button UndoButton; //Кнопка відміни поля
    private Field field; //Поле

    public GameProcess (Field field, Button UndoButton) { //Конструктор
        this.UndoButton = UndoButton;
        setField(field);
    }

    public void setField (Field field) { //Створення функціоналу для поля
        this.field = field;
        PlayersWay = new Stack<>();
        AllowedSteps = new ArrayList<>();
        PlayersWay.push(1);
        Highlight();

        for (int i = 0; i < Field.FieldSize; i++) {
            for (int j = 0; j < Field.FieldSize; j++) {
                int finalI = i;
                int finalJ = j;
                field.getCell(i, j).setOnAction(event-> ClickCell(finalI, finalJ));
            }
        }
    }

    private void Highlight () { //Виділення клітинок
        AllowedSteps = FindAllowed(PlayersWay);

        MakeAllWhite ();

        for (Pair pair : AllowedSteps) {
            field.getCell(pair.getFirst(), pair.getSecond()).setStyle("-fx-background-color: #1abc9c");
            field.getCell(pair.getFirst(), pair.getSecond()).setCursor(Cursor.HAND);
        }

        field.getCell(field.CellY(PlayersWay.peek()), field.CellX(PlayersWay.peek())).setStyle("-fx-background-color: #e67e22");
    }

    private void MakeAllWhite () { //Робить всі клітинки та кнопку відміни кроку білими
        UndoButton.setStyle("-fx-background-color: #ecf0f1");

        for (int i = 0; i < Field.FieldSize; i++) {
            for (int j = 0; j < Field.FieldSize; j++) {
                if (WasHere(i * Field.FieldSize + j + 1, PlayersWay))
                    field.getCell(i, j).setStyle("-fx-background-color: #ecf0f1");
                field.getCell(i, j).setCursor(Cursor.DEFAULT);
            }
        }
    }

    private void ClickCell(int i, int j) { //Натискання на кнопку
        if (IsAllowed(i, j, AllowedSteps)) {
            field.getCell(field.CellY(PlayersWay.peek()), field.CellX(PlayersWay.peek())).setStyle("-fx-background-color: #f1c40f");
            PlayersWay.push(field.CellNum(i, j));
            AllowedSteps.clear();
            Highlight();

            if (i == Field.FieldSize - 1 && j == Field.FieldSize - 1) {
                LoadWinWindow ();
            }
        }
    }

    private void LoadWinWindow () { //Створення вікна при виграші
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
        stage.setOnHidden(event-> setField(new Field (field.getPane())));
        stage.setResizable(false);
        stage.show();
    }

    private boolean IsAllowed(int i, int j, ArrayList<Pair> allowed) { //Чи можна переміститись нп дану клітинку
        boolean res = false;

        for (Pair pair : allowed) {
            if (pair.getFirst() == i && pair.getSecond() == j) {
                res = true;
                break;
            }
        }

        return res;
    }

    public void ClearPlayersWay() { //Очищення шляху ігрока
        PlayersWay.clear();
        PlayersWay.push(1);
        Highlight();
    }

    private ArrayList<Pair> FindAllowed (Stack<Integer> way) { //Пошук клітинок на які можна перейти
        ArrayList<Pair> res = new ArrayList<>();
        int i = 0, j = 0;

        int currentY = field.CellY(way.peek());
        int currentX = field.CellX(way.peek());

        switch (field.getDirection(currentY, currentX)) {
            case 0:
                i = 0;
                j = -1;
                break;
            case 45:
                i = 1;
                j = -1;
                break;
            case 90:
                i = 1;
                j = 0;
                break;
            case 135:
                i = 1;
                j = 1;
                break;
            case 180:
                i = 0;
                j = 1;
                break;
            case 225:
                i = -1;
                j = 1;
                break;
            case 270:
                i = -1;
                j = 0;
                break;
            case 315:
                i = -1;
                j = -1;
                break;
            case -1:
                return res;
        }

        for (int y = currentY, x = currentX; x < Field.FieldSize && x >= 0 && y >= 0 && y < Field.FieldSize; x += i, y += j) {
            if ((((y != 0 || x != 0) && (y != Field.FieldSize - 1 || x != Field.FieldSize - 1)) || way.size() == Field.CellNum - 1) && (y != currentY || x != currentX) && WasHere(field.CellNum(y, x), way)) {
                res.add(new Pair(y, x));
            }
        }

        return res;
    }

    private boolean WasHere (int step, Stack<Integer> way) { //Чи був на на клітинці
        boolean res = false;
        Stack<Integer> WayCopy = (Stack<Integer>) way.clone();

        while (WayCopy.size() > 0) {
            if (WayCopy.peek() == step)
                res = true;
            WayCopy.pop();
        }
        return !res;
    }

    private void Bypass (Stack<Integer> AnswerWay, int step) { //Обхід дерева
        AnswerWay.push(step);
        ArrayList<Pair> poss = FindAllowed(AnswerWay);

        if (poss.size() > 0) {
            for (Pair pair : poss) {
                Bypass(AnswerWay, field.CellNum(pair.getFirst(), pair.getSecond()));
            }
        }

        if (AnswerWay.size() != Field.CellNum) {
            AnswerWay.pop();
        }
    }

    private ArrayList<Integer> FindAnswer() { //Пошук розв'язку
        Stack<Integer> AnswerWay = (Stack<Integer>) PlayersWay.clone();
        ArrayList<Pair> allowed = FindAllowed(AnswerWay);

        if (allowed != null) {
            for (Pair pair : allowed) {
                Bypass(AnswerWay, field.CellNum(pair.getFirst(), pair.getSecond()));
            }
        }

        if (AnswerWay.size() == Field.CellNum) {
            return new ArrayList<>(AnswerWay);
        } else {
            return null;
        }
    }

    public void Undo () { //Відміна кроку
        if (PlayersWay.size() > 1) {
            PlayersWay.pop();
            AllowedSteps.clear();
            Highlight();
        }
    }

    public void hint () { //Підказка
        if (PlayersWay.size() != Field.CellNum) {
            ArrayList<Integer> answer = FindAnswer();
            if (answer != null) {
                field.getCell(field.CellY(answer.get(PlayersWay.size())), field.CellX(answer.get(PlayersWay.size()))).setStyle("-fx-background-color: #2980b9");
            } else {
                UndoButton.setStyle("-fx-background-color: #2980b9");
            }
        }
    }
}
