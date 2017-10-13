package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import commons.Table;

public class NameController implements Initializable {

	@FXML
	private TextField _nameField;
	@FXML
	private Label _errorLabel;
	private boolean _mathAid;

	public NameController(boolean mathAid){
		_mathAid = mathAid;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {}

	@FXML
	private void submitButtonPressed(ActionEvent event) throws IOException{
		String name = _nameField.getText();

		if (noSpecialCharacters(name)){
			//Create new scoreboard
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("fxml/difficultyChoose.fxml"));
			loader.setController(new DifficultyChooseController(name, _mathAid));
			Parent view = loader.load();
			Scene chooseScene = new Scene(view);

			// Gets the stage information
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setWidth(600);
			window.setHeight(550);
			window.setResizable(false);
			window.setScene(chooseScene);
			window.show();
		}
		else{
			_errorLabel.setText("ERROR: No special characters allowed. Please try again");
		}
	}

	private boolean noSpecialCharacters(String name) {
		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(name);
		boolean hasSpecialCharacter = matcher.find();

		if (hasSpecialCharacter){
			return false;
		}
		else {
			return true;
		}
	}
}
