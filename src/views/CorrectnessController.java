package views;

import commons.Table;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class CorrectnessController {

    @FXML
    private Button mainMenuButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label questionNumberLabel;
    @FXML
    private Label recordedNumber;
    @FXML
    private Label correctness;
    @FXML
    private ImageView correctIncorrectImage;

    private boolean correct;
    private boolean hardLevel;
    private boolean mathAid;
    private String maoriNumber;
    private String userRecording;
    private int nextQuestionNumber;
    private int score;

    private static final int numQuestions = 10;
    private static Main instance = Main.getInstance();
    private ObservableList<Table> data;

    public void initData(boolean correct, String maori, String userRecording, int questionNumber, int score, boolean hardLevel, ObservableList<Table> data, boolean mathAid){
        this.correct = correct;
        this.maoriNumber = maori;
        this.userRecording = userRecording;
        this.nextQuestionNumber = questionNumber + 1;
        this.score = score;
        this.hardLevel = hardLevel;
        this.data = data;
        this.mathAid = mathAid;
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
            nextButton.setText("Finish");
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
            loader.setLocation(getClass().getResource("recordMenu.fxml"));
            loader.setController(new RecordMenuController());
            Parent view = loader.load();

            Scene viewScene = new Scene(view);
//            viewScene.getStylesheets().add(getClass().getResource("../resources/mainStyle.css").toExternalForm());
            // Access the play view controller and call initData method
            RecordMenuController controller = loader.getController();
            controller.initData(nextQuestionNumber, score, hardLevel, data, mathAid);
            controller.setData();

            // Gets the stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(viewScene);
            window.show();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("grade.fxml"));
            loader.setController(new GradeController());
            Parent view = loader.load();

            Scene viewScene = new Scene(view);
//            viewScene.getStylesheets().add(getClass().getResource("../resources/mainStyle.css").toExternalForm());
            // Access the play view controller and call initData method
            GradeController controller = loader.getController();
            controller.initData(score, hardLevel, data, mathAid);// data
            controller.setData();

            // Gets the stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(viewScene);
            window.show();
        }
    }

    public void mainMenuPressed(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Tātai Practise Module - Quit");
        alert.setHeaderText("WARNING - You will lose all current progress.");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            instance.setMainScene();
        }
    }

}
