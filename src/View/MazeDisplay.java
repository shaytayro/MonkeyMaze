package View;

import algorithms.mazeGenerators.Maze;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import Model.Character;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MazeDisplay extends Canvas {
    int[][] mazeArray;
    int  row;
    int col;
    int finalrow;
    int finalcol;
    int [][] solArray;
    MediaPlayer musicplayer;
    boolean play;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameFinalCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNamebanana = new SimpleStringProperty();


    public void SetMaze(int[][] mazeArray,int row,int col,int finalrow,int finalcol){
       this.row=row;
        this.col=col;
        this.finalrow=finalrow;
        this.finalcol=finalcol;
        this.mazeArray = mazeArray;
        redraw();

    }

    public void playmusic() {
        if(!play)
        musicplayer.play();
    }

    public void IncreaseVoice() {
        if(play) {
            if (musicplayer.getVolume() < 1)

                musicplayer.setVolume(musicplayer.getVolume() + 0.2);
        }
    }
    public void DecreaseVoice() {
        if(play) {
            if (musicplayer.getVolume() > 0)
                musicplayer.setVolume(musicplayer.getVolume() - 0.2);
        }
    }

    public void mute() {
        if(play) {
            if (musicplayer.isMute())
                musicplayer.setMute(false);
            else
                musicplayer.setMute(true);

        }
    }


    public void stopmusic() {
       if(play) {
           musicplayer.stop();
            play=false;
       }
    }

    public void setCharacterPosition(int row, int column) {
        this.row=row;
        this.col=column;
        redraw();
    }

    public void redraw() {
        if (mazeArray != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / mazeArray.length;
            double cellWidth = canvasWidth / mazeArray[0].length;

            try {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();
                graphicsContext2D.clearRect(0, 0, getWidth(), getHeight()); //Clears the canvas

                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

                //Draw Maze
                for (int i = 0; i < mazeArray.length; i++) {
                    for (int j = 0; j < mazeArray[0].length; j++) {
                        if (mazeArray[i][j] == 1) {
                            graphicsContext2D.drawImage(wallImage, j * cellWidth, i* cellHeight, cellWidth,  cellHeight);
                        }
                    }
                }

                if(row == finalrow && col==finalcol){
                    characterImage = new Image(new FileInputStream(ImageFileNameFinalCharacter.get()));
                    Media mp3MusicFile = new Media(getClass().getResource("resources/Music/win.mp3").toExternalForm());
                    musicplayer = new MediaPlayer(mp3MusicFile);
                        this.playmusic();
                        play=true;


                }
                if(!(row == finalrow && col==finalcol)){
                    Image bananaImage = new Image(new FileInputStream(ImageFileNamebanana.get()));
                    graphicsContext2D.drawImage(bananaImage, finalcol * cellWidth, finalrow* cellHeight, cellWidth, cellHeight);
                }


                graphicsContext2D.drawImage(characterImage, col * cellWidth, row* cellHeight, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image doesn't exist: %s", e.getMessage()));
                alert.show();
            }
        }
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter); }

        public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNamebanana(String imageFileNamebanana) {
        this.ImageFileNamebanana.set(imageFileNamebanana);
    }

    public void setImageFileNameFinalCharacter(String imageFileNameFinalCharacter) {
        this.ImageFileNameFinalCharacter.set(imageFileNameFinalCharacter); }


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public String getImageFileNamebanana() {
        return ImageFileNamebanana.get();
    }



    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }
    public String getImageFileNameFinalCharacter() {
        return ImageFileNameFinalCharacter.get();
    }
    public void SetSolution(int [][] solArray){
        this.solArray=solArray;
        drawSolution();
    }


    public void drawSolution(){
        try {
            GraphicsContext graphicsContext2D = getGraphicsContext2D();
            Image bananaImage = new Image(new FileInputStream(ImageFileNamebanana.get()));
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / mazeArray.length;
            double cellWidth = canvasWidth / mazeArray[0].length;

            for(int i=0;i<solArray.length;i++){
                graphicsContext2D.drawImage(bananaImage, solArray[i][1] * cellWidth, solArray[i][0] * cellHeight, cellWidth, cellHeight);
            }
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            graphicsContext2D.drawImage(characterImage, col * cellWidth, row* cellHeight, cellWidth, cellHeight);

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

   public void  Simulator(int [][] solArray){
       this.solArray=solArray;
       DrawSimulator();
   }
    public void DrawSimulator() {
        try {
            GraphicsContext graphicsContext2D = getGraphicsContext2D();
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / mazeArray.length;
            double cellWidth = canvasWidth / mazeArray[0].length;

            for (int i = 1; i < solArray.length; i++) {
                row = solArray[i][0];
                col = solArray[i][1];
                redraw();
            }

        }
        catch(IOException e){
            e.printStackTrace();
        }


    }


}
