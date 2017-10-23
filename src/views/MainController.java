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
    private Button mathsAidButton, practiceButton, learnButton;

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

        	// learn interface for new users
        if (event.getSource() == learnButton) {
        	loader.setLocation(getClass().getResource("fxml/learn.fxml"));
            loader.setController(new LearnController());
            Parent view = loader.load();
            playScene = new Scene(view);
            window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(playScene);
            window.show();
        } else {
        	//Changes to nickname screen for those who wants to play
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
}
