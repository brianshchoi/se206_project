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
    private Button easyButton;
    @FXML
    private Button hardButton;

    private ObservableList<Table> data;

    /*
    When this method is called, it will invoke random number to be generated
    to be displayed in the play view.
     */
    @FXML
    private void buttonPressed(ActionEvent event) throws IOException {
        //Create new scoreboard
        data = FXCollections.observableArrayList();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("recordMenu.fxml"));
        loader.setController(new RecordMenuController());
        Parent view = loader.load();
        Scene playScene = new Scene(view);

        // Access the play view controller and call initData method
        RecordMenuController controller = loader.getController();
        if (event.getSource().equals(easyButton)) {
            controller.initData(1, 0, false, data);
        } else if (event.getSource().equals(hardButton)) {
            controller.initData(1, 0, true, data);
        }

        controller.setData();
        // Gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(playScene);
        window.show();
    }

    @FXML
    private void mainMenuButtonPressed(ActionEvent event){
        instance.setMainScene();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
