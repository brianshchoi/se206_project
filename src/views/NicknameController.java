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

public class NicknameController implements Initializable {

	private String _nickname;
	private ObservableList<Table> data;

	@FXML
	private TextField _nickNameField;
	@FXML
	private Label _errorLabel;
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	private void submitButtonPressed(ActionEvent event) throws IOException{
		String nickname = _nickNameField.getText();

		if (noSpecialCharacters(nickname)){
			_nickname = nickname;
			_errorLabel.setText("correct");
			//Create new scoreboard
			data = FXCollections.observableArrayList();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("recordMenu.fxml"));
			loader.setController(new RecordMenuController());
			Parent view = loader.load();
			Scene playScene = new Scene(view);

			// Access the play view controller and call initData method
			RecordMenuController controller = loader.getController();
			controller.initData(1, 0, true, data, true, nickname);
			controller.setData();
			// Gets the stage information
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
			window.setMinWidth(600);
			window.setMinHeight(550);
			window.setScene(playScene);
			window.show();
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
