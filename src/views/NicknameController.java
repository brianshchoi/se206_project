package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NicknameController implements Initializable {

    private String _name;

    @FXML
    private TextField _nameField;
    @FXML
    private Label _errorLabel;


    @FXML
    private void submitButtonPressed(ActionEvent event){
        String name = _nameField.getText();

        if (noSpecialCharacters(name)){
            _name = name;
            //go to next select difficulty scene
            try {
                goToDifficultyScene(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            _errorLabel.setText("ERROR no special characters allowed. Please try again");
        }

        System.out.println(_name);
    }

    private void goToDifficultyScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Scene playScene;
        Stage window;

        loader.setLocation(getClass().getResource("fxml/PracticeDifficulty.fxml"));
        loader.setController(new MathsDifficultyController(_name));
        Parent view = loader.load();
        playScene = new Scene(view);
        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setWidth(550);
        window.setHeight(600);
        window.setScene(playScene);
        window.show();
    }

    private boolean noSpecialCharacters(String name) {
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        boolean x = matcher.find();

        if (x){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
