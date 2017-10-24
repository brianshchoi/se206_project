package views;

import commons.Dictionary;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.AudioClip;

public class LearnController extends ParentController implements Initializable {
	
	// Buttons for available recordings
	@FXML
	private Button _14, _15, _38, _39, _54, _60;
	
	@FXML
	private Button iwa, ono, onoII, rima, rimaII, rua, tahi, tekau, toru, waru, waruII, wha, whitu, whituII;
	
	@FXML
	private Button backButton;
	private static Main instance = Main.getInstance();
	
	// Tableview for a simple dictionary view
	@FXML
	private TableView<Dictionary> dictionary;
	@FXML
    private TableColumn<Dictionary, String> iNumber;
    @FXML
    private TableColumn<Dictionary, String> iMaori;
    
    private final ObservableList<Dictionary> data = FXCollections.observableArrayList(
    		new Dictionary("1","Tahi"),
    		new Dictionary("2", "Rua"),
    		new Dictionary("3", "Toru"),
    		new Dictionary("4","Whā"),
    		new Dictionary("5","Rima"),
    		new Dictionary("6","Ono"),
    		new Dictionary("7","Whitu"),
    		new Dictionary("8","Waru"),
    		new Dictionary("9","Iwa"),
    		new Dictionary("10","Tekau"),
    		new Dictionary("11 ~ 19","Tekau + mā + ones digit"),
			new Dictionary("20, ..., 90(Ends with 0)", "Tens digit + Tekau"),
			new Dictionary("21, ..., 99(Ends with 1~9)", "Tens digit + Tekau + mā + ones digit")
    		);
    
	@FXML
	private void play(ActionEvent event) {
		if (event.getSource().equals(_14)) {
			playNumber("14");
		} else if (event.getSource().equals(_15)) {
			playNumber("15");
		} else if (event.getSource().equals(_38)) {
			playNumber("38");
		} else if (event.getSource().equals(_39)) {
			playNumber("39");
		} else if (event.getSource().equals(_54)) {
			playNumber("54");
		} else if (event.getSource().equals(_60)) {
			playNumber("60");
		} else {
			Button button = (Button) event.getSource();
			String file = button.getId();			// get the button ID as string
			playNumber(file);
		}
	}
	
	/*
	 * Method that takes a number selection from the user and plays corresponding recording.
	 */
	private void playNumber(String number) {
		URL resource = getClass().getResource("/NumberRecordings/"+number+".wav");
		AudioClip clip = new AudioClip(resource.toString());
		clip.play();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		iNumber.setCellValueFactory(new PropertyValueFactory<Dictionary, String>("Number"));
		iMaori.setCellValueFactory(new PropertyValueFactory<Dictionary, String>("Maori"));
		dictionary.setItems(data);
	}

}
