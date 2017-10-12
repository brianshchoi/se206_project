package views;

import commons.Table;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RecordMenuController {
	@FXML
	private Button mainMenuButton, recordButton, scoreButton, check, play;
	@FXML
	private Label number, round, info;

	private int playingNumber;
	private int roundNumber;
	private int incorrect;
	private int score;
	private Map<Integer, String> dictionary = new HashMap<Integer,String>();
	private String maori;
	private String userRecording;
	private String formula;
	private String nickname;
	private boolean correctness;
	private boolean hardLevel;
	private boolean mathAid;

	private static Main instance = Main.getInstance();
	private ObservableList<Table> data;

	private final static int EASYLIMIT = 9;
	private final static int HARDLIMIT = 99;

	//Load the dictionary with maori words.
	public void initData(int roundNumber, int score, boolean hardLevel, ObservableList<Table> data, boolean mathAid, String nickname) {
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

	public void setData(){
		recordButton.setDisable(false);				// record button enabled
		play.setDisable(true);						// play button disabled
		check.setDisable(true);						// check button disabled
		
		if (mathAid) {								// if playing math aid module, random formula is generated
			playingNumber = generateQuestion();	
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
			if (digit == 0) {
				maori = dictionary.get(tenth)+"tekau";
			} else {
				maori = dictionary.get(tenth)+"tekau"+"maa"+dictionary.get(digit);
			}
		}
		userRecording = "";
		
		round.setText("Question " + Integer.toString(roundNumber));
	}

	//	private int getRandomNumber(int min, int max) {
	//		if (min >= max) {
	//			throw new IllegalArgumentException("max must be greater than min");
	//		}
	//		return (int)(Math.random() * (max-min) + 1);
	//	}
	//
	
	/*
	 * Method generating a random number between given range
	 */
	private int getRandomNumber(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		return min + (int)(Math.random() * ((max-min) + 1));
	}


	@FXML
	private void mainMenuPressed(ActionEvent event) throws IOException {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Tātai Practise Module - Quit");
		alert.setHeaderText("WARNING - You will lose all current progress.");
		alert.setContentText("Are you sure you want to quit?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			instance.setMainScene();
		} else {
			alert.close();
		}
	}

	@FXML
	private void scorePressed(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("scoreBoard.fxml"));
		loader.setController(new ScoreBoard());
		Parent view = loader.load();

		// Access the check view controller and call initData method
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
			check.setDisable(false);
			play.setDisable(false);
		});
		pause.play();
	}

	/*
	 * Method that starts the bash process to record user voice for the given number.
	 * It uses HTK framework to record for 3 seconds.
	 */
	private void recordThread() {
		//Might need to change so that the recording can be played back with a button, then confirmed with a 'check' button
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
	 * Method to play back the user recording
	 */
	public void playPressed(ActionEvent event) {
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
	 * Method to check for correctness of the user recording
	 */
	public void checkPressed(ActionEvent event) {
		if (!correctness && incorrect == 1) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Tatai - Incorrect");
			alert.setHeaderText("Incorrect!");
			alert.setContentText("You said " + userRecording + ". Press 'OK' to try again.");

			alert.showAndWait();

			info.setText("Please try again. Make sure to say the number clearly.");
			recordButton.setDisable(false);
			check.setDisable(true);
			play.setDisable(true);
		} else if (correctness || incorrect != 1) {
			try {
				String correct;
				if (correctness){
					correct = "Correct";
				} else {
					correct = "Incorrect";
				}
				data.add(new Table(roundNumber,playingNumber,correct, userRecording, maori));

				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("correctness.fxml"));
				loader.setController(new CorrectnessController());
				Parent view = loader.load();

				// Access the check view controller and call initData method
				CorrectnessController controller = loader.getController();
				controller.initData(correctness, maori, userRecording, roundNumber, score, hardLevel, data, mathAid, nickname);
				controller.setData();
				Scene viewScene = new Scene(view);

				// Gets the stage information
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

				window.setScene(viewScene);
				window.show();
			} catch (IOException e) {
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
		case 1:
			operation = 1;
			answer = leftSide + rightSide;
			formula = formula + " + " + Integer.toString(rightSide);
			break;
		case 2:
			operation = 2;
			answer = leftSide - rightSide;
			formula = formula + " - " + Integer.toString(rightSide);
			break;
		case 3:
			operation = 3;
			answer = leftSide * rightSide;
			formula = formula + " x " + Integer.toString(rightSide);
			break;
		}

		return answer;
	}

}
