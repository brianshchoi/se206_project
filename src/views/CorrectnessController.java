package views;

import commons.ScoreTable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class CorrectnessController extends ParentController {

    @FXML
    private Button mainMenuButton, nextButton;
    @FXML
    private Label questionNumberLabel, recordedNumber, correctness;
    @FXML
    private ImageView correctIncorrectImage;
    private boolean correct, hardLevel, mathAid, custom;
    private String maoriNumber, userRecording, nickname;
    private int nextQuestionNumber, score;
    private static final int numQuestions = 10;
    private static Main instance = Main.getInstance();
    private ObservableList<ScoreTable> data;

    //Change to controller
    public void initData(boolean correct, String maori, String userRecording, int questionNumber, int score,
                         boolean hardLevel, ObservableList<ScoreTable> data, boolean mathAid, String nickname, boolean custom) {
		this.custom = custom;
        this.correct = correct;
        this.maoriNumber = maori;
        this.userRecording = userRecording;
        this.nextQuestionNumber = questionNumber + 1;
        this.score = score;
        this.hardLevel = hardLevel;
        this.data = data;
        this.mathAid = mathAid;
        this.nickname = nickname;
    }

    public void setData(){
        questionNumberLabel.setText("Question " + (nextQuestionNumber -1));

        if (nextQuestionNumber <= numQuestions) {
            if (correct) {
                correctness.setText("Correct");
                recordedNumber.setText("You said " + userRecording + " correctly. Press 'Next' to play the next round.");
            } else {
                correctIncorrectImage.setImage(new Image("file:src/resources/incorrect.png/"));
                correctness.setText("Incorrect");
                recordedNumber.setText("You said " + userRecording + " instead of "+ maoriNumber +". Press 'Next' to play the next round.");
            }
        } else {
            // If it is the last question
            nextButton.setText("Finish");               //change button to "Finish"
            if (correct) {
                correctness.setText("Correct");
                recordedNumber.setText("You said " + userRecording + " correctly. Press 'Finish' to see your final score.");
            } else {
                correctIncorrectImage.setImage(new Image("file:src/resources/incorrect.png/"));
                correctness.setText("Incorrect");
                recordedNumber.setText("You said " + userRecording + " instead of " + maoriNumber +". Press 'Finish' to see your final score.");
            }
        }

    }

    public void nextPressed(ActionEvent event) throws IOException {
        if (nextQuestionNumber <= numQuestions) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/recordMenu.fxml"));
            loader.setController(new RecordMenuController());
            Parent view = loader.load();

            Scene viewScene = new Scene(view);
            // Access the play view controller and call initData method
            RecordMenuController controller = loader.getController();
            controller.initData(nextQuestionNumber, score, hardLevel, data, mathAid, nickname, custom);
            controller.setData();

            // Gets the stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(viewScene);
            window.show();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/grade.fxml"));
            GradeController controller = new GradeController();
            loader.setController(controller);
            Parent view = loader.load();

            Scene viewScene = new Scene(view);
            // Access the play view controller and call initData method
            controller.initData(score, hardLevel, mathAid, nickname, custom);
            controller.setData();

            // Gets the stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(viewScene);
            window.show();
        }
    }
}
