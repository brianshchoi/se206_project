package views;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GradeController implements Initializable{

    @FXML
    private Label totalScore;
    @FXML
    private Button playAgainEasy;
    @FXML
    private Button playAgainHard;

    //Define table for grade
    @FXML
    TableView<Table> gradeTable;
    @FXML
    TableColumn<Table, Integer> iQuestion;
    @FXML
    TableColumn<Table, Integer> iNumber;
    @FXML
    TableColumn<Table, String> iCorrect;
    @FXML
    TableColumn<Table, String> iUserRecording;
    @FXML
    TableColumn<Table, String> iMaori;

    private int score;
    private boolean hardLevel;
    private boolean mathAid;
    private static Main instance = Main.getInstance();
    private final static int easyToHardBoundary = 8;
    private final static int numQuestions = 10;

    final ObservableList<Table> data = FXCollections.observableArrayList();

    public void initData(int score, boolean hardLevel, ObservableList<Table> table, boolean mathAid){
        this.score = score;
        this.hardLevel = hardLevel;
        this.mathAid = mathAid;
        data.addAll(table);
    }

    public void setData(){
        totalScore.setText("Total: " + score + "/" + numQuestions);
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
        loader.setLocation(getClass().getResource("recordMenu.fxml"));
        loader.setController(new RecordMenuController());
        Parent view = loader.load();

        Scene viewScene = new Scene(view);

        // Access the play view controller and call initData method
        RecordMenuController controller = loader.getController();

        if (event.getSource().equals(playAgainEasy)){
            controller.initData(1,0,false, newData, mathAid);
        } else if (event.getSource().equals(playAgainHard)){
            controller.initData(1,0,true, newData, mathAid);
        }
        controller.setData();

        // Gets the stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(viewScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iQuestion.setCellValueFactory(new PropertyValueFactory<Table, Integer>("Question"));
        iNumber.setCellValueFactory(new PropertyValueFactory<Table, Integer>("Number"));
        iCorrect.setCellValueFactory(new PropertyValueFactory<Table, String>("Correct"));
        iUserRecording.setCellValueFactory(new PropertyValueFactory<Table, String>("UserRecording"));
        iMaori.setCellValueFactory(new PropertyValueFactory<Table, String>("Maori"));
        gradeTable.setItems(data);
    }
}
