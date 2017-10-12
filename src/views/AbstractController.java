package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by brian on 12/10/2017.
 */
public class AbstractController {

    private boolean _mathAid;

    @FXML
    private Label _title, easyLabel, hardLabel;

    private static Main instance = Main.getInstance();

    @FXML
    private void helpButtonPressed(ActionEvent event){
        //TODO show help
        //If it is Math Aid Module
        if (_mathAid = true){
            // Change module
        }
        //If it is Practice Module
        else {
            //
            _title.setText("Tatai")

        }


        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/Help.fxml"));
            loader.setController(new HelpController());
            Parent view = loader.load();

            // Access the check view controller and call initData method
            HelpController controller = loader.getController();
            Scene helpScene = new Scene(view);

            // Gets the stage information
            Stage helpStage = new Stage();
            helpStage.setTitle("Tātai Practise Module - Scoreboard");
            helpStage.initModality(Modality.APPLICATION_MODAL);
            helpStage.setResizable(false);
            helpStage.setScene(helpScene);
            helpStage.showAndWait();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Help Button Pressed");
    }

}
