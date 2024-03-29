package views;

import commons.ScoreTable;
import commons.UserTable;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class GradeController extends ParentController implements Initializable {

	@FXML
	private Label totalScore;
	@FXML
	private ImageView feedback;
	@FXML
	private Button playAgainEasy, playAgainHard;

	//Define table for grade
	@FXML
	TableView<UserTable> gradeTable;
	@FXML
	TableColumn<UserTable, Integer> iScore;
	@FXML
	TableColumn<UserTable, String> iNickname, iDate;

	private int score;
	private String nickname, FILENAME;
	private boolean hardLevel, mathAid, custom;
	private static Main instance = Main.getInstance();
	private final static int easyToHardBoundary = 8, numQuestions = 10;

	private static final String MATH_FILE = "math_results.csv", PRACTICE_FILE = "practice_results.csv";
	final ObservableList<UserTable> data = FXCollections.observableArrayList();

	//Change to constructor
	public void initData(int score, boolean hardLevel, boolean mathAid, String nickname, boolean custom) {
		this.custom = custom;
		this.score = score;
		this.hardLevel = hardLevel;
		this.mathAid = mathAid;
		this.nickname = nickname;
		if (mathAid || custom) {
			FILENAME = MATH_FILE;
		} else {
			FILENAME = PRACTICE_FILE;
		}
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("hh:mm dd/mm/yy ");
		String strDate = dateFormat.format(date);
		data.add(new UserTable(score, nickname, strDate));	// adds the current play data to the tableview
		loadDataFromFile();									// loads data from previous plays to the tableview
		try {
			saveDataToFile();								// saves the current play data to the file
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setData(){
		totalScore.setText("Total: " + score + "/" + numQuestions);
		if (score == 10) {
			Image perfect = new Image("/resources/perfect.png");
			feedback.setImage(perfect);
			feedback.setEffect(new Glow());
		} else if (score == 8 || score == 9) {
			Image good = new Image("/resources/welldone.jpg");
			feedback.setImage(good);
			feedback.setEffect(new Glow());
		} else if (score > 4 && score < 8) {
			Image average = new Image("/resources/keepup.jpg");
			feedback.setImage(average);
		} else {
			Image tryBetter = new Image("/resources/try.png");
			feedback.setImage(tryBetter);
		}
		if (custom) {
			playAgainEasy.setDisable(true);
			playAgainHard.setDisable(true);
		} else if ((score >= easyToHardBoundary && !hardLevel)||(hardLevel)){
			playAgainHard.setDisable(false);
		} else {
			playAgainHard.setDisable(true);
		}
	}

	@FXML
	private void playAgainPressed(ActionEvent event) throws IOException {
		ObservableList<ScoreTable> newData = FXCollections.observableArrayList();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/recordMenu.fxml"));
		RecordMenuController controller = new RecordMenuController();
		loader.setController(controller);
		Parent view = loader.load();

		Scene viewScene = new Scene(view);

		// Access the play view controller and call initData method
		if (event.getSource().equals(playAgainEasy)){
			controller.initData(1,0,false, newData, mathAid, nickname, custom);
		} else if (event.getSource().equals(playAgainHard)){
			controller.initData(1,0,true, newData, mathAid, nickname, custom);
		}
		controller.setData();

		// Gets the stage information
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(viewScene);
		window.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		iNickname.setCellValueFactory(new PropertyValueFactory<UserTable, String>("Nickname"));
		iDate.setCellValueFactory(new PropertyValueFactory<UserTable, String>("Date"));
		iScore.setCellValueFactory(new PropertyValueFactory<UserTable, Integer>("Score"));
		gradeTable.setItems(data);
	}

	/**
	 * Loads the saved data from previous plays.
	 */
	public void loadDataFromFile() {
		try {
			File file = new File(FILENAME);
			file.createNewFile();				// Check for result data file existence and create one if it doesn't exist
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] array;

			while ((line = br.readLine()) != null){
				array = line.split(",");		// read the file, split it by ',' and put them into columns of the tableview
				gradeTable.getItems().add(new UserTable(Integer.parseInt(array[0]), array[1], array[2]));
			}
			br.close();

		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Saves the current user results to the file.
	 */
	public void saveDataToFile() throws IOException {
		Writer writer = null;
		try {
			File file = new File(FILENAME);
			writer = new BufferedWriter(new FileWriter(file, true));
			String text = data.get(0).getScore().toString() + "," + data.get(0).getNickname() + "," + data.get(0).getDate() + "\n";
			writer.write(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			writer.flush();
			writer.close();
		}
	}
}
