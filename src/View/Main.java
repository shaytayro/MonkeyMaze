package View;

//import algorithms.mazeGenerators.Maze;
import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        MyModel model = new MyModel();
       // model.StartServer();
        MyViewModel viewModel = new MyViewModel(model);
        model.addObserver(viewModel);
        primaryStage.setTitle("Monkey Maze!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 840 , 650);
        scene.getStylesheets().add(getClass().getResource("ViewStyleMaze.css").toExternalForm());
        primaryStage.setScene(scene);
        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.SetViewModel(viewModel);

        viewModel.addObserver(view);
        primaryStage.show();
       view.openstartScreen();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText("The game will be close");
                alert.setContentText("Are you ok with this?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    viewModel.Exit();
                } else {
                    windowEvent.consume();
                }
            }
        });
               /*
        Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

*/
    }


    public static void main(String[] args) {
        launch(args);

    }
}
