package views;

import commons.ScoreTable;
import commons.UserTable;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RecordMenuController extends ParentController {
	@FXML
	private Button mainMenuButton, recordButton, scoreButton, checkButton, playRecordButton, skipButton;
	@FXML
	private Label number, round, info;

	private int playingNumber, roundNumber, incorrect, score;
	private Map<Integer, String> dictionary = new HashMap<Integer,String>();
	private String maori, userRecording, formula, nickname;
	private boolean correctness, hardLevel, mathAid, custom;

	private static Main instance = Main.getInstance();
	private ObservableList<ScoreTable> data;

	private final static int EASYLIMIT = 9, HARDLIMIT = 99;
	private final static String FILENAME = "customList.csv";

	//Change to Constructor
	//Load the dictionary with maori words.
	public void initData(int roundNumber, int score, boolean hardLevel, ObservableList<ScoreTable> data, boolean mathAid, String nickname, boolean custom) {
		this.custom = custom;
		this.roundNumber = roundNumber;
		this.score = score;
		this.hardLevel = hardLevel;
		this.data = data;
		this.mathAid = mathAid;
		this.nickname = nickname;
		incorrect = 0;
		dictionary.put(1, "tahi");
		dictionary.put(2, "rua");
		dictionary.put(3, "toru");
		dictionary.put(4, "whaa");
		dictionary.put(5, "rima");
		dictionary.put(6, "ono");
		dictionary.put(7, "whitu");
		dictionary.put(8, "waru");
		dictionary.put(9, "iwa");
		dictionary.put(10, "tekau");
	}

	//
	public void setData(){
		recordButton.setDisable(false);				// record button enabled
		playRecordButton.setDisable(true);			// playRecordButton button disabled
		checkButton.setDisable(true);				// checkButton button disabled

		if (mathAid && !custom) {								// if playing math aid module, random formula is generated
			playingNumber = generateQuestion();	
			number.setText(formula);
		} else if(custom){
			loadDataFromFile();
			number.setText(formula);
		} else {									// else if playing practice module, random number is generated
			if (hardLevel){
				playingNumber = getRandomNumber(1, HARDLIMIT);
			} else {
				playingNumber = getRandomNumber(1, EASYLIMIT);
			}
			number.setText(Integer.toString(playingNumber));
		}

		//If the number is between 0 and 10, return from dictionary
		if (playingNumber > 0 && playingNumber < 11) {
			maori = dictionary.get(playingNumber);
		} else {
			// if number is greater than 10, divide it into tenth and digit parts
			int tenth = playingNumber / 10;
			int digit = playingNumber - tenth * 10;

			if (tenth == 1){ //If between 11~19
				maori = "tekau"+"maa"+dictionary.get(digit);
			} else if (digit == 0) { //Else if ends with 0
				maori = dictionary.get(tenth)+"tekau";
			} else {
				maori = dictionary.get(tenth)+"tekau"+"maa"+dictionary.get(digit);
			}
		}
		userRecording = "";

		round.setText("Question " + Integer.toString(roundNumber));
	}

	/*
	 * Method generating a random number between given range
	 */
	private int getRandomNumber(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		return min + (int)(Math.random() * ((max-min) + 1));
	}


	//Button handler for the scoreboard
	@FXML
	private void scorePressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/scoreBoard.fxml"));
		loader.setController(new ScoreBoard());
		Parent view = loader.load();

		// Access the checkButton view controller and call initData method
		ScoreBoard controller = loader.getController();
		controller.setData(data);
		Scene scoreBoardScene = new Scene(view);

		// Gets the stage information
		Stage scoreBoardStage = new Stage();
		scoreBoardStage.setTitle("Tātai Practise Module - Scoreboard");
		scoreBoardStage.initModality(Modality.APPLICATION_MODAL);
		scoreBoardStage.setResizable(false);
		scoreBoardStage.setScene(scoreBoardScene);
		scoreBoardStage.showAndWait();
	}

	//Button handler for the microphone record button
	@FXML
	private void recordButtonPressed(ActionEvent ev) throws IOException {
		System.out.println(maori);
		recordButton.setDisable(true);
		info.setText("Recording ...");
		recordThread();
		outputThread();
		PauseTransition pause = new PauseTransition(Duration.seconds(4));
		pause.setOnFinished(event -> {
			info.setText("Number recorded!");
			checkButton.setDisable(false);
			playRecordButton.setDisable(false);
		});
		pause.play();
	}

	/*
	 * Method that starts the bash process to record user voice for the given number.
	 * It uses HTK framework to record for 3 seconds.
	 */
	private void recordThread() {
		//Might need to change so that the recording can be played back with a button, then confirmed with a 'checkButton' button
		String command = "cd $MYDIR; " + "rm foo.wav; arecord -d 3 -r 22050 -c 1 -i -t wav -f s16_LE foo.wav; HVite -H HMMs/hmm15/macros -H HMMs/hmm15/hmmdefs -C user/configLR  -w user/wordNetworkNum -o SWT -l '*' -i recout.mlf -p 0.0 -s 5.0  user/dictionaryD user/tiedList foo.wav;";
		//                "aplay foo.wav;" +
		//                "rm foo.wav;" +
		//                "more recout.mlf";
		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		Map<String, String> environment = processBuilder.environment();
		String home = environment.get("HOME")+"/Documents/HTK/MaoriNumbers/";	// reading $HOME
		environment.put("MYDIR", home);											// setting $MYDIR

		Thread thread = new Thread(() -> {
			try {
				processBuilder.redirectErrorStream(true);
				Process process = processBuilder.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				System.out.println(processBuilder.command().toString());
				e.printStackTrace();
			}
		});
		thread.start();
	}

	/*
	 * Second thread that performs maori string extraction of the user recording and compares the two
	 */
	private void outputThread(){
		String command2 = "sleep 4; cd $MYDIR; sed -n '/sil/,/sil/{/sil/b;/sil/b;p}' recout.mlf";
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", command2);
		Map<String, String> environment = pb.environment();
		String home = environment.get("HOME")+"/Documents/HTK/MaoriNumbers/";	// reading $HOME
		environment.put("MYDIR", home);										    // setting $MYDIR
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String output="";
				try {
					pb.redirectErrorStream(true);
					Process process = pb.start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line;
					while ((line = reader.readLine()) != null) {
						output = output + line;
					}
					process.waitFor();
					userRecording = output;
					//Checks if user said nothing
					if (userRecording.equals("")) {
						userRecording = "nothing";
					}
					System.out.println(output);

					if (userRecording.equals(maori)) {
						System.out.println("Matches!");
						correctness = true;
						score++;
					} else {
						System.out.println("Fail");
						correctness = false;
						incorrect++;
					}
				} catch (IOException | InterruptedException e) {
					System.out.println(pb.command().toString());
					e.printStackTrace();
				}
			}

		});
		thread.start();
	}

	/*
	 * Button handler for replaying recorded audio.
	 */
	public void playRecordingPressed(ActionEvent event) {
		String command = "cd $MYDIR; aplay foo.wav;";
		ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
		Map<String, String> environment = processBuilder.environment();
		String home = environment.get("HOME")+"/Documents/HTK/MaoriNumbers/";	// reading $HOME
		environment.put("MYDIR", home);											// setting $MYDIR

		Thread thread = new Thread(() -> {
			try {
				processBuilder.redirectErrorStream(true);
				Process process = processBuilder.start();
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				System.out.println(processBuilder.command().toString());
				e.printStackTrace();
			}
		});
		thread.start();
	}

	/*
	 * Method for check button for correctness of the user recording and skip button to skip to the next view
	 */
	public void checkButtonPressed(ActionEvent event) {
		if(event.getSource() == checkButton) {
			if (!correctness && incorrect == 1) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Tatai - Incorrect");
				alert.setHeaderText("Incorrect!");
				alert.setContentText("You said " + userRecording + ". Press 'OK' to try again.");
				alert.showAndWait();
				info.setText("Please try again. Make sure to say the number clearly.");
				recordButton.setDisable(false);
				checkButton.setDisable(true);
				playRecordButton.setDisable(true);
			}
			else {
				System.out.println("condition correct");
				try {
					String correct;
					if (correctness){
						correct = "Correct";
					} else {
						correct = "Incorrect";
					}
					data.add(new ScoreTable(roundNumber,playingNumber,correct, userRecording, maori));
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("fxml/correctness.fxml"));
					CorrectnessController controller = new CorrectnessController();
					loader.setController(controller);
					Parent view = loader.load();
					controller.initData(correctness, maori, userRecording, roundNumber, score, hardLevel, data, mathAid, nickname, custom);
					controller.setData();
					Scene viewScene = new Scene(view);
					// Gets the stage information
					Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

					window.setScene(viewScene);
					window.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				data.add(new ScoreTable(roundNumber,playingNumber, "skipped", "N/A", maori));
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("fxml/correctness.fxml"));
				CorrectnessController controller = new CorrectnessController();
				loader.setController(controller);
				Parent view = loader.load();
				controller.initData(false, maori, "N/A", roundNumber, score, hardLevel, data, mathAid, nickname, custom);
				controller.setData();
				Scene viewScene = new Scene(view);
				// Gets the stage information
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

				window.setScene(viewScene);
				window.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Method that generates a random formula
	 */
	public int generateQuestion() {
		int middle;						// w
		int left;						// x
		int right;						// y
		int result;						// z
		formula = "";

		if (hardLevel) {
			while (true) {										// Hard Level
				left = getRandomNumber(1,99);					// generate a random number x
				formula = formula + Integer.toString(left);
				int middleOperator = getRandomNumber(1,3);		// generate a random operator (+/-/x)

				if ((left != 1) && (middleOperator == 3)) {		// generate a random number w
					middle = getRandomNumber(1,49);				// if the operator is multiplier and x is not 1, w is generated between 1 and 49
				} else {
					middle = getRandomNumber(1,99);
				}
				int temp = randomFormula(left, middle, middleOperator);		// calculate (x operator w), and only proceed if the calculated value
				if (temp > 0 && temp < 100) {								// lies within the valid range, else go back to get new numbers and operator

					int operator = getRandomNumber(1,3);					// generate a second random operator (+/-/x)
					if ((temp != 1) && (operator == 3)) {					// generate a random number z
						right = getRandomNumber(1,49);
					} else {
						right = getRandomNumber(1,99);
					}
					result = randomFormula(temp, right, operator);			// calculate the total result, and only proceed if the result lies
					if (result > 0 && result < 100) {						// within the valid range, else go back to the beginning
						break;
					} else {
						formula = "";
					}
				} else {
					formula = "";
				}
			}
		} else {
			while (true) {													// Easy Level
				left = getRandomNumber(1,99);								// generate a random number x
				formula = formula + Integer.toString(left);
				int operator = getRandomNumber(1,3);						// generate a random operator (+/-/x)
				if ((left != 1) && (operator == 3)) {						// generate a random number y
					right = getRandomNumber(1,49);							// if the operator is multiplier and x is not 1, y is generated between 1 and 49
				} else {
					right = getRandomNumber(1,99);
				}
				result = randomFormula(left, right, operator);				// calculate the result, and only proceed if it lies within the valid range (1-99)
				if (result > 0 && result < 100) {							// else, go back to get new values and operator
					break;
				} else {
					formula = "";
				}
			}
		}
		return result;
	}

	/*
	 * Method that takes two integers (x and y), and a integer representing an operator (+/-/x),
	 * and calculates (x operator y) and returns the result integer z
	 */
	public int randomFormula(int leftSide, int rightSide, int operation) {
		int answer = 0;
		switch(operation) {
		case 1: //Case for addition
			operation = 1;
			answer = leftSide + rightSide;
			formula = formula + " + " + Integer.toString(rightSide);
			break;
		case 2: //Case for subtraction
			operation = 2;
			answer = leftSide - rightSide;
			formula = formula + " - " + Integer.toString(rightSide);
			break;
		case 3: //Case for multiplication
			operation = 3;
			answer = leftSide * rightSide;
			formula = formula + " x " + Integer.toString(rightSide);
			break;
		}

		return answer;
	}

	/**
	 * Loads the saved custom question and answer lists.
	 */
	public void loadDataFromFile() {
		try {
			File file = new File(FILENAME);
			file.createNewFile();				// Check for result data file existence and create one if it doesn't exist
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			String[] array;
			int count = 0;
			while ((line = br.readLine()) != null){
				count++;
				if (count == roundNumber) {
					array = line.split(",");		// read the file, split it by ',' and put them into columns of the tableview
					formula = array[0];
					playingNumber = Integer.parseInt(array[1]);
				}
			}
			br.close();

		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
