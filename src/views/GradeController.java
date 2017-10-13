package views;

import commons.userTable;
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


public class GradeController implements Initializable{

	@FXML
	private Label totalScore;
	@FXML
	private ImageView feedback;
	@FXML
	private Button playAgainEasy;
	@FXML
	private Button playAgainHard;

	//Define table for grade
	@FXML
	TableView<userTable> gradeTable;
	@FXML
	TableColumn<userTable, Integer> iTotal;
	@FXML
	TableColumn<userTable, String> iNickname;
	@FXML
	TableColumn<userTable, String> iDate;

	private int score;
	private String nickname;
	private boolean hardLevel;
	private boolean mathAid;
	private static Main instance = Main.getInstance();
	private final static int easyToHardBoundary = 8;
	private final static int numQuestions = 10;

	private static final String MATH_FILE = "math_results.csv";
	private static final String PRACTICE_FILE = "practice_results.csv";
	private String FILENAME;


	final ObservableList<userTable> data = FXCollections.observableArrayList();

	public void initData(int score, boolean hardLevel, boolean mathAid, String nickname){
		this.score = score;
		this.hardLevel = hardLevel;
		this.mathAid = mathAid;
		this.nickname = nickname;
		if (mathAid) {
			FILENAME = MATH_FILE;
		} else {
			FILENAME = PRACTICE_FILE;
		}
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yy-mm-dd hh:mm");
		String strDate = dateFormat.format(date);
		data.add(new userTable(score, nickname, strDate));	// adds the current play data to the tableview
		loadDataFromFile();									// loads data from previous plays to the tableview
		try {
			saveDataToFile();								// saves the current play data to the file
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setData(){
		totalScore.setText("Total: " + score + "/" + numQuestions);
		Image perfect = new Image("/resources/perfect.png");
		Image good = new Image("/resources/welldone.jpg");
		Image average = new Image("/resources/keepup.jpg");
		Image tryBetter = new Image("/resources/try.png");
		if (score == 10) {
			feedback.setImage(perfect);
			feedback.setEffect(new Glow());
		} else if (score == 8 || score == 9) {
			feedback.setImage(good);
			feedback.setEffect(new Glow());
		} else if (score > 4 && score < 8) {
			feedback.setImage(average);
		} else {
			feedback.setImage(tryBetter);
		}
		if ((score >= easyToHardBoundary && !hardLevel)||(hardLevel)){
			playAgainHard.setDisable(false);
		} else {
			playAgainHard.setDisable(true);
		}
	}

	@FXML
	private void mainMenuPressed(ActionEvent event) throws IOException {
		instance.setMainScene();
	}

	@FXML
	private void playAgainPressed(ActionEvent event) throws IOException {
		ObservableList<Table> newData = FXCollections.observableArrayList();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/recordMenu.fxml"));
		loader.setController(new RecordMenuController());
		Parent view = loader.load();

		Scene viewScene = new Scene(view);

		// Access the play view controller and call initData method
		RecordMenuController controller = loader.getController();

		if (event.getSource().equals(playAgainEasy)){
			controller.initData(1,0,false, newData, mathAid, nickname);
		} else if (event.getSource().equals(playAgainHard)){
			controller.initData(1,0,true, newData, mathAid, nickname);
		}
		controller.setData();

		// Gets the stage information
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(viewScene);
		window.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		iTotal.setCellValueFactory(new PropertyValueFactory<userTable, Integer>("Total"));
		iNickname.setCellValueFactory(new PropertyValueFactory<userTable, String>("Nickname"));
		iDate.setCellValueFactory(new PropertyValueFactory<userTable, String>("Date"));
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
				gradeTable.getItems().add(new userTable(Integer.parseInt(array[0]), array[1], array[2]));
			}
			br.close();

		}catch (Exception ex){
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
			int size = data.size();					// only save last 20 results to the file
			if (size < 21) {
				for (int i = size; i > 0 ; i--) {
					String text = data.get(i-1).getTotal().toString() + "," + data.get(i-1).getNickname() + "," + data.get(i-1).getDate() + "\n";
					writer.write(text);
				}
			} else {
				for (int i = size; i > size - 20 ; i--) {
					String text = data.get(i-1).getTotal().toString() + "," + data.get(i-1).getNickname() + "," + data.get(i-1).getDate() + "\n";
					writer.write(text);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			writer.flush();
			writer.close();
		}
	}
}
