package views;

import commons.UserTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultsController extends ParentController implements Initializable {

    @FXML
    private Label title, selectLabel;
    @FXML
    private Button mathsAidButton, practiceButton, mainMenuButton;

    //Define table for grade
    @FXML
    TableView<UserTable> gradeTable;
    @FXML
    TableColumn<UserTable, Integer> iTotal;
    @FXML
    TableColumn<UserTable, String> iNickname, iDate;

    private static final String MATHS_FILE = "math_results.csv", PRACTICE_FILE = "practice_results.csv";
    final ObservableList<UserTable> data = FXCollections.observableArrayList();

    /**
     * Loads the saved data from previous plays.
     */
    public void loadDataFromFile(String FILE) {
        try {
            File file = new File(FILE);
            file.createNewFile();				// Check for result data file existence and create one if it doesn't exist
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String[] array;
            resetTable();
            while ((line = br.readLine()) != null){
                array = line.split(",");		// read the file, split it by ',' and put them into columns of the tableview
                gradeTable.getItems().add(new UserTable(Integer.parseInt(array[0]), array[1], array[2]));
            }
            br.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void resetTable(){
        gradeTable.getItems().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iTotal.setCellValueFactory(new PropertyValueFactory<UserTable, Integer>("Total"));
        iNickname.setCellValueFactory(new PropertyValueFactory<UserTable, String>("Nickname"));
        iDate.setCellValueFactory(new PropertyValueFactory<UserTable, String>("Date"));
        gradeTable.setItems(data);
    }

    /*
    Button handler for picking which result the user wants to see
     */
    @FXML
    private void resultButtonPressed(ActionEvent event){
        selectLabel.setVisible(false);
        gradeTable.setVisible(true);
        if (event.getSource() == mathsAidButton){
            title.setText("Maths Aid Results");
            loadDataFromFile(MATHS_FILE);
        } else if (event.getSource() == practiceButton){
            title.setText("Practice Results");
            loadDataFromFile(PRACTICE_FILE);
        }
    }

}
