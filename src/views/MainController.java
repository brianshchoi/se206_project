package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button mathsAidButton;
    @FXML
    private Button practiceButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void buttonPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Scene playScene;
        Stage window;

        if (event.getSource() == mathsAidButton){
            //TODO Change to Maths aid Main Menu, and load controller
            loader.setLocation(getClass().getResource("NicknameScreen.fxml"));
            loader.setController(new NicknameController());
            Parent view = loader.load();
            playScene = new Scene(view);
            window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setWidth(500);
            window.setHeight(300);
            window.setScene(playScene);
            window.show();
        }

        else if (event.getSource() == practiceButton){
            loader.setLocation(getClass().getResource("PracticeModule.fxml"));
            loader.setController(new PracticeModuleController());
            Parent view = loader.load();
            playScene = new Scene(view);
            window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(playScene);
            window.show();
        } else {
        	loader.setLocation(getClass().getResource("learn.fxml"));
            loader.setController(new LearnController());
            Parent view = loader.load();
            playScene = new Scene(view);
            window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(playScene);
            window.show();
        }

    }
}
