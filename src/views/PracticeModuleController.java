package views;

import commons.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class PracticeModuleController implements Initializable{

    private static Main instance = Main.getInstance();

    @FXML
    private Button easyButton, hardButton;
    private ObservableList<Table> data;

    /*
    Button handler for when "easy" or "hard" is pressed
     */
    @FXML
    private void playButtonPressed(ActionEvent event) throws IOException {
        //Create new scoreboard
        data = FXCollections.observableArrayList();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/recordMenu.fxml"));
        loader.setController(new RecordMenuController());
        Parent view = loader.load();
        Scene playScene = new Scene(view);

        // Access the play view controller and call initData method
        RecordMenuController controller = loader.getController();
        if (event.getSource().equals(easyButton)) {
            controller.initData(1, 0, false, data, false, "");
        } else if (event.getSource().equals(hardButton)) {
            controller.initData(1, 0, true, data, false, "");
        }

        controller.setData();
        // Gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(playScene);
        window.show();
    }

    /*
    Button handler for when main menu button is pressed. Switches back to main menu
     */
    @FXML
    private void mainMenuButtonPressed(ActionEvent event){
        instance.setMainScene();
    }

    /*
    Button handler for when help button is pressed. Shows the
     */
    @FXML
    private void helpButtonPressed(ActionEvent event){
        //TODO show
        System.out.println("test");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
