package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WinWindowController {

    @FXML
    private Button PlayAgainButton; //Почати знову

    @FXML
    private void initialize() {
        PlayAgainButton.setOnAction(event->PlayAgainButton.getScene().getWindow().hide());
    }
}
