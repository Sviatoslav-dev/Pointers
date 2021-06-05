package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;

public class MainWindowController {
    @FXML
    private AnchorPane Pane; //Панель

    @FXML
    private Button UndoButton; //Кнопка відміни кроку

    @FXML
    private Button NewFieldButton; //Кнопка для створення нового поля

    @FXML
    private Button HintButton; //Кнопка підказки

    @FXML
    private Button ClearButton; //Кнопка очищення відповіді

    @FXML
    private ChoiceBox<Integer> Required;

    private GameProcess gp; //Ігровий процес

    private int a = 5;

    @FXML
    private void initialize() {
        for (int i = 2; i < 25; i++) {
            Required.getItems().add(i);
        }

        Required.setOnAction(event-> choise ());

        UndoButton.setOnAction(event-> gp.Undo());
        NewFieldButton.setOnAction(event-> gp.setField(new Field(Pane, a)));
        HintButton.setOnAction(event->gp.hint());
        ClearButton.setOnAction(event->gp.ClearPlayersWay());
        gp = new GameProcess(new Field(Pane, a), UndoButton);
    }

    void choise () {
        a = Required.getValue();
        gp.setField(new Field(Pane, a));
    }
}
