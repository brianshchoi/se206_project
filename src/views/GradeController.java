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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    final ObservableList<userTable> data = FXCollections.observableArrayList();

    public void initData(int score, boolean hardLevel, boolean mathAid, String nickname){
        this.score = score;
        this.hardLevel = hardLevel;
        this.mathAid = mathAid;
        this.nickname = nickname;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yy-mm-dd hh:mm");
        String strDate = dateFormat.format(date);
        data.add(new userTable(score, nickname, strDate));
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
}
