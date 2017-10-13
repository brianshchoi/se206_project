package views;

import commons.Table;
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
    // Define Table with parameters
    @FXML
    TableView<Table> tableID;
    @FXML
    TableColumn<Table, Integer> iQuestion, iNumber;
    @FXML
    TableColumn<Table, String> iCorrect, iMaori, iUserRecording;

    final ObservableList<Table> data = FXCollections.observableArrayList();

    public void setData(ObservableList<Table> scores) {
        data.addAll(scores);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iQuestion.setCellValueFactory(new PropertyValueFactory<Table, Integer>("Question"));
        iNumber.setCellValueFactory(new PropertyValueFactory<Table, Integer>("Number"));
        iCorrect.setCellValueFactory(new PropertyValueFactory<Table, String>("Correct"));
        iUserRecording.setCellValueFactory(new PropertyValueFactory<Table, String>("UserRecording"));
        iMaori.setCellValueFactory(new PropertyValueFactory<Table, String>("Maori"));

        tableID.setItems(data);
    }
}
