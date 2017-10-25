package views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import commons.ScoreTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class CustomLevelController {
	
	private ObservableList<String> operators = 
			FXCollections.observableArrayList(
					"+", "-", "x"
					);

	@FXML
	private ComboBox x1, x2, x3, x4, x5, x6, x7, x8, x9, x10;
	@FXML
	private ComboBox y1, y2, y3, y4, y5, y6, y7, y8, y9, y10;
	@FXML
	private ComboBox op1, op2, op3, op4, op5, op6, op7, op8, op9, op10;

	@FXML
	private Button submit;

	private int[] answers = new int[10];
	private String[] questions = new String[10];
	private ObservableList<ScoreTable> _data;
	private String _name;
	private String _fileName = "customList.csv";

	private List<ComboBox> xlist = new ArrayList<>();
	private List<ComboBox> ylist = new ArrayList<>();
	private List<ComboBox> oplist = new ArrayList<>();
	
	public CustomLevelController(String name) {
		_name = name;
	}

	public void setUp() {
		ObservableList<Integer> numbers = FXCollections.observableArrayList();
		for (int i=1; i<100; i++) {
			numbers.add(i);
		}
		xlist.add(x1);
		xlist.add(x2);
		xlist.add(x3);
		xlist.add(x4);
		xlist.add(x5);
		xlist.add(x6);
		xlist.add(x7);
		xlist.add(x8);
		xlist.add(x9);
		xlist.add(x10);
		for (ComboBox combo : xlist) {
			combo.setItems(numbers);
		}
		ylist.add(y1);
		ylist.add(y2);
		ylist.add(y3);
		ylist.add(y4);
		ylist.add(y5);
		ylist.add(y6);
		ylist.add(y7);
		ylist.add(y8);
		ylist.add(y9);
		ylist.add(y10);
		for (ComboBox combo : ylist) {
			combo.setItems(numbers);
		}
		oplist.add(op1);
		oplist.add(op2);
		oplist.add(op3);
		oplist.add(op4);
		oplist.add(op5);
		oplist.add(op6);
		oplist.add(op7);
		oplist.add(op8);
		oplist.add(op9);
		oplist.add(op10);
		for (ComboBox combo : oplist) {
			combo.setItems(operators);
		}
	}

	@FXML
	private void submitPressed(ActionEvent event) throws IOException {
		// loop through the combobox to get the user selection
		int listCount = 0;
		int answerCount = 0;

		for (int i=0; i<10; i++) {
			if (xlist.get(i).getValue() == null || ylist.get(i).getValue() == null || oplist.get(i).getValue() == null) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("No input selected!");
				alert.setHeaderText("Invalid!");
				alert.setContentText("You must select one input for all variables. Press 'OK' and try again.");
				alert.showAndWait();
				break;
			} else {
				listCount++;
			}
			int x = (int)xlist.get(i).getValue();
			int y = (int)ylist.get(i).getValue();
			String op = (String)oplist.get(i).getValue();
			questions[i] = formulaGenerator(x,y,op);
			answers[i] = formulaAnswer(x,y,op);
			if (answers[i]<1 || answers[i]>99) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Invalid question");
				alert.setHeaderText("Invalid!");
				alert.setContentText("Answer to the questions must lie between 1 and 99. Press 'OK' to re-choose your numbers.");
				alert.showAndWait();
				break;
			} else {
				answerCount++;
			}
		}

		if (listCount == 10 && answerCount == 10){
			start(event);
		}
	}

	/*
	Method to switch to record menu
	 */
	private void start(ActionEvent event) throws IOException {
		saveDataToFile();
		_data = FXCollections.observableArrayList();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("fxml/recordMenu.fxml"));
		RecordMenuController controller = new RecordMenuController();
		loader.setController(controller);
		Parent view = loader.load();
		Scene recordScene = new Scene(view);

		// Access the play view controller and call initData method
		controller.initData(1, 0, false, _data, true, _name, true);

		controller.setData();
		// Gets the stage information
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setWidth(600);
		window.setHeight(550);
		window.setResizable(false);
		window.setScene(recordScene);
		window.show();
	}

	/*
	 * Method to concatenate inputs to form a string representing the question
	 */
	private String formulaGenerator(int x, int y, String op) {
		return Integer.toString(x) + " " + op + " " + Integer.toString(y);
	}
	
	/*
	 * Method that takes user selections and performs arithmetic operation to get the result
	 */
	private int formulaAnswer(int x, int y, String op) {
		int answer = 0;
		switch(op) {
		case "+": //Case for addition
			answer = x + y;
			break;
		case "-": //Case for subtraction
			answer = x - y;
			break;
		case "x": //Case for multiplication
			answer = x * y;
			break;
		}
		return answer;
	}
	
	/**
	 * Saves the custom question and answer list to the file.
	 */
	public void saveDataToFile() throws IOException {
		Writer writer = null;
		String text = "";
		try {
			File file = new File(_fileName);
			writer = new BufferedWriter(new FileWriter(file));
			for (int i=0; i<10; i++) {
				text += questions[i] + "," + answers[i] + "\n";
			}
			writer.write(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			writer.flush();
			writer.close();
		}
	}

}
