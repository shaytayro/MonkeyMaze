package View;


import View.MazeDisplay;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3;
import com.sun.corba.se.impl.orbutil.ObjectUtility;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.fxml.Initializable;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;

public class MyViewController implements IView,Initializable,Observer{
    MyViewModel viewModel;
    Number SceneWidth;
    Number SceneHeight;
    Media mp3MusicFile = new Media(getClass().getResource("resources/Music/music.mp3").toExternalForm());
    MediaPlayer musicplayer;
    boolean playmusic;
    boolean ismute;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_colsNum;
    public MazeDisplay mazeDisplay;
    public javafx.scene.control.Button play;
    public javafx.scene.control.Button generate;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Label lbl_RowsNum;
   // public javafx.scene.control.Button simulator;
    public javafx.scene.control.Button Solution;
    public javafx.scene.layout.VBox vbox;
    public javafx.scene.layout.GridPane grid;
    public javafx.scene.layout.BorderPane borderpa;
    public javafx.scene.image.ImageView Title;
    public javafx.scene.image.ImageView kkk;
    public javafx.scene.image.ImageView kofrun;
    public javafx.scene.image.ImageView kofbutton;
    public javafx.scene.layout.Pane pane;

    public DoubleProperty layoutxkkk = new SimpleDoubleProperty(330); //For Binding
    public DoubleProperty layoutykkk = new SimpleDoubleProperty(290); //For Binding


    public DoubleProperty heightprop = new SimpleDoubleProperty(503); //For Binding
    public DoubleProperty widthprop = new SimpleDoubleProperty(210); //For Binding

    public DoubleProperty heightmaze = new SimpleDoubleProperty(600); //For Binding
    public DoubleProperty widthmaze = new SimpleDoubleProperty(600); //For Binding

    public DoubleProperty heightkofrun = new SimpleDoubleProperty(150); //For Binding
    public DoubleProperty widthkofrun = new SimpleDoubleProperty(200); //For Binding

    public DoubleProperty heightTitel = new SimpleDoubleProperty(253); //For Binding
    public DoubleProperty widthTitel = new SimpleDoubleProperty(424); //For Binding

    public DoubleProperty heightkkk = new SimpleDoubleProperty(228); //For Binding
    public DoubleProperty widthkkk = new SimpleDoubleProperty(314); //For Binding



    private void bindProperties(MyViewModel viewModel) {
        lbl_RowsNum.textProperty().bind(viewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
        }

    public void SetViewModel ( MyViewModel viewModel){
        makeZoomable(generate.getScene(), borderpa);
        this.viewModel=viewModel;
        bindProperties(viewModel);
        bindSize();

    }
    public void bindSize(){
        kkk.layoutXProperty().bind(layoutxkkk);
        kkk.layoutYProperty().bind(layoutykkk);

        vbox.prefWidthProperty().bind(widthprop);
        grid.prefHeightProperty().bind(heightprop);
        mazeDisplay.widthProperty().bind(widthmaze);
        mazeDisplay.heightProperty().bind(heightmaze);

        kofrun.fitHeightProperty().bind(heightkofrun);
        kofrun.fitWidthProperty().bind(widthkofrun);

        Title.fitHeightProperty().bind(heightTitel);
        Title.fitWidthProperty().bind(widthTitel);

        kkk.fitHeightProperty().bind(heightkkk);
        kkk.fitWidthProperty().bind(widthkkk);

    }
    public void mute() {
        mazeDisplay.mute();
        if(musicplayer.isMute()) {
            musicplayer.setMute(false);
            ismute=false;
        }
        else {
            musicplayer.setMute(true);
            ismute=true;
        }

    }
    public void IncreaseVoice() {
        if(ismute){
            mute();
        }
        mazeDisplay.IncreaseVoice();
        if(musicplayer.getVolume()<1)

            musicplayer.setVolume(musicplayer.getVolume()+0.2);
    }
    public void DecreaseVoice() {
        mazeDisplay.DecreaseVoice();

        if(musicplayer.getVolume()>0)
        musicplayer.setVolume(musicplayer.getVolume()-0.2);
    }


    public void playmusic() {
            if(!ismute) {
                musicplayer.play();
                playmusic = true;
            }
        if(ismute) {
            musicplayer.play();
            musicplayer.setMute(true);
            playmusic = true;
        }


    }
    public void stopmusic(){
        musicplayer.stop();
        playmusic=false;
    }

    public void generateMaze() throws Exception {
       // simulator.setDisable(false);
        Title.setImage(null);
        kkk.setImage(null);
        Solution.setDisable(false);
        mazeDisplay.stopmusic();
        if(playmusic){
            stopmusic();
        }
        musicplayer = new MediaPlayer(mp3MusicFile);
        (new Thread(() -> {
            this.playmusic();
        })).start();

        musicplayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                musicplayer.seek(Duration.ZERO);
                musicplayer.play();
            }
        });

        int rows=0;
        int columns=0;
        try {
            rows = Integer.valueOf(txtfld_rowsNum.getText());
            columns =Integer.valueOf(txtfld_colsNum.getText());
                viewModel.generateMaze(rows, columns);
                mazeDisplay.SetMaze(viewModel.getMaze(), viewModel.getCharacterRow(), viewModel.getCharacterCol(), viewModel.GoalRow(), viewModel.GoalCol());
        }/*
        catch (NumberFormatException e){
            Stage error = new Stage();
            error.setTitle("Error");
            StackPane layout = new StackPane();
            layout.getChildren().add(new Label("please enter number"));
            error.setScene(new Scene(layout, 200, 300));
            error.show();
            if(playmusic){
                musicplayer.stop();
            }
        }
        */
        catch (NumberFormatException e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("please enter only numbers");
            error.setContentText("pay attention if there are extra characters like space");
            error.show();
            Solution.setDisable(true);

            //if(playmusic){
              //  musicplayer.stop();
           // }
        }

        }

    public void openstartScreen(){
        //Block events to other windows
        try {
            Stage startWindow;
            startWindow = new Stage();
            startWindow.initModality(Modality.APPLICATION_MODAL);
            startWindow.setTitle("Welcome");
           FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("MainScreen.fxml").openStream());
            Scene scene = new Scene(root, 630, 685);
            startWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Exit");
                    alert.setHeaderText("The game will be close");
                    alert.setContentText("Are you ok with this?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        Stage stage= (Stage)generate.getScene().getWindow();
                        stage.close();
                        viewModel.Exit();
                        startWindow.close();
                    } else {
                        windowEvent.consume();
                    }
                }
            });

            startWindow.setMaxHeight(685);
            startWindow.setMinHeight(685);
            startWindow.setMinWidth(630);
            startWindow.setMaxWidth(630);
            scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());

            startWindow.setScene(scene);
            startWindow.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)  {
//        openstartScreen();
    }

    public void close(){
        Stage stage = (Stage) play.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

        public void KeyPressed(KeyEvent keyEvent) {
        try {
            if (!(viewModel.getCharacterRow() == viewModel.GoalRow() && viewModel.getCharacterCol() == viewModel.GoalCol())) {
                viewModel.moveCharacter(keyEvent.getCode());
                keyEvent.consume();
            }
        }
        catch (Exception e){
            //System.out.println();
        }
    }
    public void Dragged(MouseDragEvent mouseEvent) {
        //viewModel.moveCharacterOnMouse(mouseEvent.);
        //openstartScreen();
        stopmusic();
    }

        public void update(Observable o, Object arg) {
        if(o==viewModel) {
            if(viewModel.getCharacterRow()==viewModel.GoalRow() && viewModel.getCharacterCol()==viewModel.GoalCol())
            {
                stopmusic();

            }
            mazeDisplay.setCharacterPosition(viewModel.getCharacterRow(), viewModel.getCharacterCol());

        }
    }

    public void solution(){
        mazeDisplay.SetSolution(viewModel.getsolution());

    }
 //   public void simulator() {
       // mazeDisplay.Simulator(viewModel.getsolution());
 //   }

    public void Load(){
        try{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Maze");
        File selectedFile= fileChooser.showOpenDialog((Stage) generate.getScene().getWindow());
        if(selectedFile!=null) {
            Title.setImage(null);
            kkk.setImage(null);
            boolean ans = viewModel.Load(selectedFile);
            if(!ans){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("This is not Maze");
                error.show();
                if(playmusic){
                    musicplayer.stop();
                }
                    return;
            }
            mazeDisplay.SetMaze(viewModel.getMaze(), viewModel.getCharacterRow(), viewModel.getCharacterCol(), viewModel.GoalRow(), viewModel.GoalCol());
            //  simulator.setDisable(false);
            Solution.setDisable(false);


            mazeDisplay.stopmusic();
            if(playmusic){
                stopmusic();
            }
            musicplayer = new MediaPlayer(mp3MusicFile);
            (new Thread(() -> {
                this.playmusic();
            })).start();

            musicplayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    musicplayer.seek(Duration.ZERO);
                    musicplayer.play();
                }
            });


        }
        }
        catch(Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("This is not Maze");
            error.show();
            if(playmusic){
                musicplayer.stop();
            }
        }

    }

    public void Save(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        File file = fileChooser.showSaveDialog((Stage) generate.getScene().getWindow());
        if(file!=null) {
            boolean ans=viewModel.Save(file);
            if(!ans){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("No maze to save");
                error.show();
            }
        }
    }

    public void Handle(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("The game will be close");
        alert.setContentText("Are you ok with this?");
        Stage stage= (Stage)generate.getScene().getWindow();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            alert.close();
            stage.close();
            viewModel.Exit();
        }
        else {

        }

    }


    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
               layoutxkkk.set((double)newSceneWidth*0.39285);
                widthprop.set((double)newSceneWidth*0.25);
                widthmaze.set((double)newSceneWidth*0.714);
                widthkofrun.set((double)newSceneWidth*0.238);

                widthTitel.set((double)newSceneWidth*0.50476);
                widthkkk.set((double)newSceneWidth*0.3738);
                mazeDisplay.redraw();

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                layoutykkk.set((double)newSceneHeight*0.44615);

                heightprop.set((double)newSceneHeight*0.773);
                heightmaze.set((double)newSceneHeight*0.923);
                heightkofrun.set((double)newSceneHeight*(0.23));

                heightTitel.set((double)newSceneHeight*0.38923);
                heightkkk.set((double)newSceneHeight*0.35);

                mazeDisplay.redraw();
            }
        });
    }




public void returnmaze(){
    txtfld_rowsNum.setDisable(true);
    txtfld_colsNum.setDisable(true);

    }

    public void rollback(){
        txtfld_rowsNum.setDisable(false);
        txtfld_colsNum.setDisable(false);

    }



    private static final int      DEFAULT_DOUBLE_PRECISION = 8;
    private static final double[] POWER_OF_TEN = new double[15];

    static {
        for (int i = 0; i < POWER_OF_TEN.length; i++) {
            POWER_OF_TEN[i] = Math.pow(10, i);
        }
    }

    public static boolean smallerDoublePrecision(double double1, double double2, int precision) {
        // try to save the POWER operation
        double factor = (precision >= 0 && precision < POWER_OF_TEN.length) ? POWER_OF_TEN[precision] : Math.pow(10, precision);
        long result = Math.round((double1 - double2) * factor);
        if (result < 0) {
            return true;
        }
        return false;
    }
    public void About() {
        try {
            Stage stage = new Stage();
            stage.setTitle("AboutController");
            FXMLLoader fxmlLoader = new FXMLLoader();
           // Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(new BorderPane(), 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
            stage.setMaxHeight(740);
            stage.setMinHeight(740);
            stage.setMinWidth(720);
            stage.setMaxWidth(720);
            scene.getStylesheets().add(getClass().getResource("about.css").toExternalForm());
        } catch (Exception e) {

        }

    }

    public void Help() {
        try {
            Stage stage = new Stage();
            stage.setTitle("HelpController");
            FXMLLoader fxmlLoader = new FXMLLoader();
           // Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(new BorderPane(), 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
            stage.setMaxHeight(630);
            stage.setMinHeight(630);
            stage.setMinWidth(780);
            stage.setMaxWidth(780);
            scene.getStylesheets().add(getClass().getResource("help.css").toExternalForm());
        } catch (Exception e) {

        }
    }


    public void Properties(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("PropertiesController");
            FXMLLoader fxmlLoader = new FXMLLoader();
           // Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(new BorderPane(), 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
            stage.setMaxHeight(530);
            stage.setMinHeight(530);
            stage.setMinWidth(850);
            stage.setMaxWidth(850);
            scene.getStylesheets().add(getClass().getResource("prop.css").toExternalForm());
        } catch (Exception e) {

        }
    }





    public void makeZoomable(Scene scene4EventFilter, Node control4Scaling) {
        scene4EventFilter.addEventFilter(ScrollEvent.ANY, (ScrollEvent event) -> {


            if(event.isControlDown()){
            double delta = 1.1;
            double scale = control4Scaling.getScaleX();
            final double MAX_SCALE = 20.0;
            final double MIN_SCALE = 0.1;

            if (smallerDoublePrecision(event.getDeltaY(), 0,DEFAULT_DOUBLE_PRECISION)) {
                scale /= delta;
            } else {
                scale *= delta;
            }

            if (scale < MIN_SCALE || scale > MAX_SCALE) {
                scale = scale < MIN_SCALE ? MIN_SCALE : MAX_SCALE;
            }

            control4Scaling.setScaleX(scale);
            control4Scaling.setScaleY(scale);

            event.consume();
            }
        });
    }}




