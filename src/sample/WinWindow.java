package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WinWindow {
    @FXML
    private Button PlayAgain;

    @FXML
    void initialize() {
        PlayAgain.setOnAction(event->again());
    }

    void again () {
        PlayAgain.getScene().getWindow().hide();
    }
}
