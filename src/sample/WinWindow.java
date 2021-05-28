package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class WinWindow {
    @FXML
    private Button PlayAgain;

    @FXML
    void initialize() {
        PlayAgain.setOnAction(event->again());
    }

    void again () {
        Stage st = (Stage) PlayAgain.getScene().getWindow();
        st.getOwner().hide();
        PlayAgain.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
