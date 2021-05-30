package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class GameWindow {
    public AnchorPane Pane;

    @FXML
    private Button UndoButton;

    @FXML
    private Button NewFieldButton;

    @FXML
    private Button HintButton;

    @FXML
    private Button ClearButton;

    private GameProcess gp;

    @FXML
    void initialize() {
        UndoButton.setOnAction(event-> ClickUndo());
        NewFieldButton.setOnAction(event-> ClickNewField());
        HintButton.setOnAction(event->ClickHint());
        ClearButton.setOnAction(event->ClickClear());
        Field field = new Field(Pane);
        gp = new GameProcess(field, UndoButton);
    }

    void ClickUndo() {
        gp.Undo();
    }

    void ClickNewField () {
        gp.NewField();
    }

    void ClickHint () {
        gp.hint();
    }

    void ClickClear () {
        gp.ClearPlayersWay();
    }
}
