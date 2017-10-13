package views;

import commons.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DifficultyChooseController extends AbstractController{

    @FXML
    private Button easyButton, hardButton;

    private ObservableList<Table> _data;
    private String _name;

    public DifficultyChooseController(String name, boolean mathAid){
        _name = name;
        _mathAid = mathAid;
    }

    @FXML
    private void buttonPressed(ActionEvent event) throws IOException {
        _data = FXCollections.observableArrayList();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/recordMenu.fxml"));
        loader.setController(new RecordMenuController());
        Parent view = loader.load();
        Scene recordScene = new Scene(view);

        // Access the play view controller and call initData method
        RecordMenuController controller = loader.getController();

        //Easy Math Aid
        if (event.getSource() == easyButton && _mathAid == true){
            controller.initData(1, 0, false, _data, true, _name);
        }
        //Easy Practice Module
        else if (event.getSource() == easyButton && _mathAid == false){
            controller.initData(1, 0, false, _data, false, _name);
        }
        //Hard Math Aid
        else if (event.getSource() == hardButton && _mathAid == true){
            controller.initData(1, 0, true, _data, true, _name);
        }
        //Hard Practice Module
        else if (event.getSource() == hardButton && _mathAid == false){
            controller.initData(1, 0, true, _data, false, _name);
        }

        controller.setData();
        // Gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setWidth(600);
        window.setHeight(550);
        window.setResizable(false);
        window.setScene(recordScene);
        window.show();
    }


}
