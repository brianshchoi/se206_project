package views;

import commons.ScoreTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DifficultyChooseController extends ParentController {

	@FXML
	private Button easyButton, hardButton, custom;

	private ObservableList<ScoreTable> _data;
	private String _name;
	private boolean _custom;

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

		//Change initData to constructor
		//Easy Math Aid
		if (event.getSource() == easyButton && _mathAid == true){
			controller.initData(1, 0, false, _data, true, _name, false);
		}
		//Easy Practice Module
		else if (event.getSource() == easyButton && _mathAid == false){
			controller.initData(1, 0, false, _data, false, _name, false);
		}
		//Hard Math Aid
		else if (event.getSource() == hardButton && _mathAid == true){
			controller.initData(1, 0, true, _data, true, _name, false);
		}
		//Hard Practice Module
		else if (event.getSource() == hardButton && _mathAid == false){
			controller.initData(1, 0, true, _data, false, _name, false);
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

	@FXML
	private void customPressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/custom.fxml"));
		loader.setController(new CustomLevelController(_name));
		
		Parent view = loader.load();
		Scene recordScene = new Scene(view);
		
		CustomLevelController controller = loader.getController();
		controller.setUp();
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setWidth(600);
		window.setHeight(550);
		window.setResizable(false);
		window.setScene(recordScene);
		window.show();
	}


}
