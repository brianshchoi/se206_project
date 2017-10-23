package views;

import commons.ScoreTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class ScoreBoard implements Initializable {

    // Define ScoreTable with parameters
    @FXML
    TableView<ScoreTable> tableID;
    @FXML
    TableColumn<ScoreTable, Integer> iQuestion, iNumber;
    @FXML
    TableColumn<ScoreTable, String> iMaori, iUserRecording, iCorrect;

    final ObservableList<ScoreTable> data = FXCollections.observableArrayList();

    public void setData(ObservableList<ScoreTable> scores) {
        data.addAll(scores);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iQuestion.setCellValueFactory(new PropertyValueFactory<ScoreTable, Integer>("Question"));
        iNumber.setCellValueFactory(new PropertyValueFactory<ScoreTable, Integer>("Number"));
        iCorrect.setCellValueFactory(new PropertyValueFactory<ScoreTable, String>("Correct"));
        iUserRecording.setCellValueFactory(new PropertyValueFactory<ScoreTable, String>("UserRecording"));
        iMaori.setCellValueFactory(new PropertyValueFactory<ScoreTable, String>("Maori"));
        tableID.setItems(data);
    }
}
