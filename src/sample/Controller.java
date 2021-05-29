package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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

    private Field field;
    private GameProcess gp;

    @FXML
    void initialize() {
        Cancel.setOnAction(event->ClickCancel());
        Restart.setOnAction(event->ClickRestart());
        hint.setOnAction(event->ClickHint());
        Clear.setOnAction(event->ClickClear());
        field = new Field(Pane, Cancel);
        gp = new GameProcess(field);
    }

    void ClickCancel () {
        if (gp.solution.size() > 1) {
            gp.solution.pop();
            gp.possible.clear();
            gp.HidhlightPosible();
        }
    }

    //To Do
    void ClickRestart () {
        Restart.getScene().getWindow().hide();

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

        /*Pane.getChildren().clear();
        solution = new Stack<>();
        possible = new ArrayList<>();
        solution.push(1);
        InputMatrix ();
        create_buttons ();
        InputDirections ();
        HidhlightPosible ();
        Cancel.setOnAction(event->ClickCancel());
        Restart.setOnAction(event->ClickRestart());*/
    }

    void ClickHint () {
        if (gp.solution.size() != Field.MatrixSizeSquared) {
            ArrayList<Integer> answer = gp.Answer(gp.solution);

            if (answer != null) {
                field.arrows.get(field.ArrowY(answer.get(gp.solution.size()))).get(field.ArrowX(answer.get(gp.solution.size()))).setStyle("-fx-background-color: blue; -fx-border-width: 1; -fx-border-color: black");
            } else {
                Cancel.setStyle("-fx-background-color: blue");
            }
        }
    }

    void ClickClear () {
        gp.ClearSolution();
    }
}
