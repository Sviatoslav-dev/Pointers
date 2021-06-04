package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    private GameProcess gp; //Ігровий процес

    @FXML
    private void initialize() {
        UndoButton.setOnAction(event-> gp.Undo());
        NewFieldButton.setOnAction(event-> gp.setField(new Field(Pane)));
        HintButton.setOnAction(event->gp.hint());
        ClearButton.setOnAction(event->gp.ClearPlayersWay());
        gp = new GameProcess(new Field(Pane), UndoButton);
    }
}
