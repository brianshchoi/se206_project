package views;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelpController {

    @FXML
    private Label _title, easyLabel, hardLabel;

    public void setText(boolean mathAid) {
        if (mathAid == false){
            _title.setText("Tātai Practice Help");
            easyLabel.setText("Record yourself saying the Māori number between 1 ~ 9 for the digit shown on the screen.");
            hardLabel.setText("Record yourself saying the Māori number between 1 ~ 99 for the digit shown on the screen.");
        }
    }


}
