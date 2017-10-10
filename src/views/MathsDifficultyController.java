package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class MathsDifficultyController extends PracticeDifficultyController implements Initializable{

    private String _name ;

    public MathsDifficultyController(String name){
        _name = name;
    }

    @FXML
    private void playbuttonPressed(ActionEvent event){
        if (event.getSource() == easyButton){
            System.out.println("Easy pressed");
        } else if (event.getSource() == hardButton){
            System.out.println("Hard pressed");
        }
//        titleLabel.setText(_name);
//        System.out.println(_name);
        //change to recording menu screen

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleLabel.setText("Welcome " + _name + ", to the Maths Learning Aid");
        titleLabel.setFont(new Font("System", 25));
    }
}
