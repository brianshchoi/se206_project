package views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {
    //Singleton instance
    private static Main instance = null;

    private static Stage _primaryStage;
    private static Scene _mainScene;

    //Only creates if instance is null - Singleton
    public static Main getInstance() {
        if (instance == null){
            instance = new Main();
        }
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Create the stage responsible for all scenes
        _primaryStage = primaryStage;
        _primaryStage.setTitle("Tātai Maths Aid");
        _primaryStage.setResizable(false);
        _primaryStage.setOnCloseRequest(e -> windowClose());
        createMainScene();
        setMainScene();
        _primaryStage.show();
    }

    //Public method to set the primary stage's scene to main menu
    public void setMainScene(){
        _primaryStage.setScene(_mainScene);
    }

    //Creates the main menu scene which is only created once so that it can be accessed from any other scene.
    private void createMainScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        loader.setController(new MainController());
        Parent root = loader.load();
        _mainScene = new Scene(root, 600, 550);
    }

    //Window prompt when X button is pressed
    private void windowClose(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Tātai Practise Module - Quit");
        alert.setHeaderText("WARNING - Quit");
        alert.setContentText("Are you sure you want to quit?");

        Optional<ButtonType> result = alert.showAndWait();


        if (result.get() == ButtonType.OK) {
            Platform.exit();
        } else {
            alert.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

//TODO TAB VIEW so that results and scoreboard both show up
//TODO Results in the main menu