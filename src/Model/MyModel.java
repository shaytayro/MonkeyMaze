package Model;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

import Client.*;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.scene.input.KeyCode;
import Server.*;
public class MyModel extends Observable implements IModel {
    Maze maze;
    Character character;
    Server mazeGeneratingServer;
    Server mazeSolutionServer;
    int [][] solArray;

    public MyModel(){
        StartServerGenerate();
        StartServerSolution();

    }
    public void StartServerGenerate(){
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        mazeGeneratingServer.start();
    }
    public void StartServerSolution(){
        mazeSolutionServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeSolutionServer.start();
    }
    @Override
    public void generateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(row*col)+50 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        character=new Character(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case UP :
                if((character.getI()-1)==-1 || maze.getValue(character.getI()-1,character.getJ())==1 )
                    break;
                else
                character.setI(character.getI()-1);
                break;
            case NUMPAD8:
                if((character.getI()-1)==-1 || maze.getValue(character.getI()-1,character.getJ())==1 )
                    break;
                else
                    character.setI(character.getI()-1);
                break;

            case DOWN:
                if((character.getI()+1)==maze.getRow() ||maze.getValue(character.getI()+1,character.getJ())==1 )
                    break;
                else
                    character.setI(character.getI()+1);
                break;
            case NUMPAD2:
                if((character.getI()+1)==maze.getRow() ||maze.getValue(character.getI()+1,character.getJ())==1 )
                    break;
                else
                    character.setI(character.getI()+1);
                break;

            case RIGHT:
                if((character.getJ()+1)==maze.getColumn()||maze.getValue(character.getI(),character.getJ()+1)==1 )
                    break;
                else
                    character.setJ(character.getJ()+1);
                break;
            case NUMPAD6:
                if((character.getJ()+1)==maze.getColumn()||maze.getValue(character.getI(),character.getJ()+1)==1 )
                    break;
                else
                    character.setJ(character.getJ()+1);
                break;

            case LEFT:
                if((character.getJ()-1)==-1|| maze.getValue(character.getI(),character.getJ()-1)==1)
                    break;
                else
                    character.setJ(character.getJ()-1);
                break;
            case NUMPAD4:
                if((character.getJ()-1)==-1|| maze.getValue(character.getI(),character.getJ()-1)==1)
                    break;
                else
                    character.setJ(character.getJ()-1);
                break;

            case NUMPAD3:
                if((character.getJ()+1)==maze.getColumn() || (character.getI()+1)==maze.getRow() || maze.getValue(character.getI()+1,character.getJ()+1)==1)
                    break;
                else {
                    character.setJ(character.getJ() + 1);
                    character.setI(character.getI() + 1);
                    break;
                }

            case NUMPAD1:
                if((character.getJ()-1)==maze.getColumn() || (character.getI()+1)==maze.getRow() || maze.getValue(character.getI()+1,character.getJ()-1)==1)
                    break;
                else {
                    character.setJ(character.getJ() - 1);
                    character.setI(character.getI() + 1);
                    break;
                }

            case NUMPAD9:
                if((character.getJ()+1)==maze.getColumn() || (character.getI()-1)==maze.getRow() || maze.getValue(character.getI()-1,character.getJ()+1)==1)
                    break;
                else {
                    character.setJ(character.getJ() + 1);
                    character.setI(character.getI() - 1);
                    break;
                }

            case NUMPAD7:
                if((character.getJ()-1)==maze.getColumn() || (character.getI()-1)==maze.getRow() || maze.getValue(character.getI()-1,character.getJ()-1)==1)
                    break;
                else {
                    character.setJ(character.getJ() - 1);
                    character.setI(character.getI() - 1);
                    break;
                }






        }
        setChanged();
        notifyObservers();
    }

    @Override
    public int[][] getMaze() {
        int [][] mazeArray=new int[maze.getRow()][maze.getColumn()];
        for(int i=0;i<maze.getRow();i++){
            for(int j=0;j<maze.getColumn();j++){
                mazeArray[i][j]=maze.getValue(i, j);
            }
        }
        return mazeArray;
    }

    @Override
    public int getCharacterRow() {
        return character.getI();
    }

    @Override
    public int getCharacterCol() {
        return character.getJ();
    }

        public int GoalRow() {
        return maze.getGoalPosition().getRowIndex();
    }
    public int Goalcol() {
        return maze.getGoalPosition().getColumnIndex();
    }
    public void createSolution(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {

                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server


                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        solArray=new int[mazeSolutionSteps.size()][2];
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            MazeState state=(MazeState)mazeSolutionSteps.get(i);
                                    solArray[i][0]=state.getPosition().getRowIndex();
                                    solArray[i][1]=state.getPosition().getColumnIndex();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public int [][] getsolution(){

        createSolution();
        return solArray;
    }

    public boolean Save(File file){
try {
         FileOutputStream mazeFileName = new FileOutputStream(file.getAbsoluteFile());
         ObjectOutputStream mazetofile = new ObjectOutputStream(mazeFileName);
         if(maze!=null){
        mazetofile.writeObject(maze);
        mazetofile.flush();
        mazetofile.close();
        return true;
    }
}
            catch(IOException e){
return false;
}
        return false;

    }

    public boolean Load(File file){
        String path = file.getAbsolutePath();
        try {
            FileInputStream filemaze = new FileInputStream(path);
            ObjectInputStream themaze = new ObjectInputStream(filemaze);
         //   if (!(themaze.readObject() instanceof Maze)){
             //   return false;
  //      }
            maze= (Maze) themaze.readObject();
            character=new Character(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
            filemaze.close();

        }
        catch(IOException e){
        return false;
        }
        catch(ClassNotFoundException e){
            return false;
        }

        return true;

    }
    public void Exit(){
        mazeGeneratingServer.stop();
        mazeSolutionServer.stop();
    }
}
