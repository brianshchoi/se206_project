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
    private Button mainMenuButton, recordButton, scoreButton;
    @FXML
    private Label number, round, info;

    private int playingNumber;
    private int roundNumber;
    private int incorrect;
    private int score;
    private Map<Integer, String> dictionary = new HashMap<Integer,String>();
    private String maori;
    private String userRecording;
    private boolean correctness;
    private boolean hardLevel;

    private static Main instance = Main.getInstance();
    private ObservableList<Table> data;

    private final static int EASYLIMIT = 9;
    private final static int HARDLIMIT = 99;

    //Load the dictionary with maori words.
    public void initData(int roundNumber, int score, boolean hardLevel, ObservableList<Table> data){
        this.roundNumber = roundNumber;
        this.score = score;
        this.hardLevel = hardLevel;
        this.data = data;
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
        recordButton.setDisable(false);
        if (hardLevel){
            playingNumber = getRandomNumber(1, HARDLIMIT);
            //If the number is between 0 and 10, return from dictionary
            if (playingNumber > 0 && playingNumber < 11) {
                maori = dictionary.get(playingNumber);
            }
            else {
            // if number is greater than 10, divide it into tenth and digit parts
                int tenth = playingNumber / 10;
                int digit = playingNumber - tenth * 10;
                if (digit == 0) {
                    maori = dictionary.get(tenth)+"tekau";
                } else {
                    maori = dictionary.get(tenth)+"tekau"+"maa"+dictionary.get(digit);
                }
            }
        } else {
            playingNumber = getRandomNumber(1, EASYLIMIT);
            maori = dictionary.get(playingNumber);
        }
        userRecording = "";
        number.setText(Integer.toString(playingNumber));
        round.setText("Question " + Integer.toString(roundNumber));
    }

    //Returns a random number between the minimum and maximum number provided
    private int getRandomNumber(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        return (int)(Math.random() * (max-min) + 1);
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
        loader.setLocation(getClass().getResource("fxml/scoreBoard.fxml"));
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
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            if (correctness || incorrect != 1) {
                try {
                    String correct;
                    if (correctness){
                        correct = "Correct";
                    } else {
                        correct = "Incorrect";
                    }
                    data.add(new Table(roundNumber,playingNumber,correct, userRecording, maori));


                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("fxml/correctness.fxml"));
                    loader.setController(new CorrectnessController());
                    Parent view = loader.load();


                    // Access the check view controller and call initData method
                    CorrectnessController controller = loader.getController();
                    controller.initData(correctness, maori, userRecording, roundNumber, score, hardLevel, data);
                    controller.setData();
                    Scene viewScene = new Scene(view);

                    // Gets the stage information
                    Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();

                    window.setScene(viewScene);
                    window.show();
                } catch (IOException e) {
                }
            }
        });
        pause.play();
    }

    /*
     * Uses HTK framework to record 3 seconds of
     */
    private void recordThread() {
        //Might need to change so that the recording can be played back with a button, then confirmed with a 'check' button
        String command = "cd $MYDIR; " + "./GoSpeech";
//                "arecord -d 3 -r 22050 -c 1 -i -t wav -f s16_LE foo.wav; " +
//                "HVite -H HMMs/hmm15/macros -H HMMs/hmm15/hmmdefs -C user/configLR  -w user/wordNetworkNum -o SWT -l '*' -i recout.mlf -p 0.0 -s 5.0  user/dictionaryD user/tiedList foo.wav;" +
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
        String command2 = "sleep 3.2; cd $MYDIR; sed -n '/sil/,/sil/{/sil/b;/sil/b;p}' recout.mlf";
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
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!correctness && incorrect == 1) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Tatai - Incorrect");
                                alert.setHeaderText("Incorrect!");
                                alert.setContentText("You said " + userRecording + ". Press 'OK' to try again.");

                                alert.showAndWait();

                                info.setText("Please try again. Make sure to say the number clearly.");
                                recordButton.setDisable(false);
                            }
                        }
                    });

                } catch (IOException | InterruptedException e) {
                    System.out.println(pb.command().toString());
                    e.printStackTrace();
                }
            }

    });
    thread.start();
}


}
