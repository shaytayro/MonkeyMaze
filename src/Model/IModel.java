package Model;

import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    public void generateMaze(int row,int col);
    public void moveCharacter(KeyCode keycode);
    public int [][] getMaze();
    public int getCharacterRow();
    public int getCharacterCol();
    public int Goalcol() ;
    public int GoalRow() ;
    public void createSolution();
    public int [][] getsolution();
    public boolean Save(File file);
    public boolean Load(File file);
    public void Exit();
}
