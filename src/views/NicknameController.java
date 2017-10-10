package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NicknameController implements Initializable {

    private String _nickname;

    @FXML
    private TextField _nickNameField;
    @FXML
    private Label _errorLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void submitButtonPressed(ActionEvent event){
        String nickname = _nickNameField.getText();

        if (noSpecialCharacters(nickname)){
            _nickname = nickname;
            _errorLabel.setText("correct");
        }
        else{
            _errorLabel.setText("ERROR no special characters allowed");
        }

        System.out.println(_nickname);
    }

    private boolean noSpecialCharacters(String name) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        boolean b = m.find();

        if (b){
            System.out.println("There is a special character in my string");
            return false;
        }
        else {
            return true;
        }


    }
}
