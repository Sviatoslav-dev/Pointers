package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class Controller {
    public AnchorPane Pane;

    @FXML
    private Button Cancel;

    @FXML
    private Button Restart;

    @FXML
    private Button hint;

    @FXML
    private Button Clear;

    private GameProcess gp;

    @FXML
    void initialize() {
        Cancel.setOnAction(event->ClickCancel());
        Restart.setOnAction(event->ClickRestart());
        hint.setOnAction(event->ClickHint());
        Clear.setOnAction(event->ClickClear());
        Field field = new Field(Pane, Cancel);
        gp = new GameProcess(field);
    }

    void ClickCancel () {
        gp.CancelStep();
    }

    void ClickRestart () {
        gp.Restart();
    }

    void ClickHint () {
        gp.hint();
    }

    void ClickClear () {
        gp.ClearSolution();
    }
}
