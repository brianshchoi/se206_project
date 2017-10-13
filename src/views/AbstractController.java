package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AbstractController {

    protected boolean _mathAid;

    @FXML
    private Label _title, easyLabel, hardLabel;

    protected static Main instance = Main.getInstance();

    @FXML
    protected void helpButtonPressed(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/Help.fxml"));
            loader.setController(new HelpController());
            Parent view = loader.load();

            // Access the check view controller and call initData method
            HelpController controller = loader.getController();
            controller.setText(_mathAid);
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

    }

    @FXML
    protected void mainMenuPromptPressed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Tātai Practise Module - Quit");
        alert.setHeaderText("WARNING - You will lose all current progress.");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            instance.setMainScene();
        } else {
            alert.close();
        }
    }

    @FXML
    protected void mainMenuPressed(ActionEvent event){
        instance.setMainScene();
    }
}
