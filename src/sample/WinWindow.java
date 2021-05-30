package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WinWindow {

    @FXML
    private Button PlayAgainButton;

    @FXML
    void initialize() {
        PlayAgainButton.setOnAction(event->again());
    }

    void again () {
        PlayAgainButton.getScene().getWindow().hide();
    }
}
