package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button mathsAidButton, practiceButton;

    @FXML
    private void buttonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Scene playScene;
        Stage window;
        boolean mathAid = false;

        if (event.getSource() == mathsAidButton){
            mathAid = true;
        } else if (event.getSource() == practiceButton){
            mathAid = false;
        }

        //Changes to nickname screen
        loader.setLocation(getClass().getResource("fxml/NameScreen.fxml"));
        loader.setController(new NameController(mathAid));
        Parent view = loader.load();
        playScene = new Scene(view);
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setWidth(600);
        window.setHeight(300);
        window.setScene(playScene);
        window.show();
    }
}
