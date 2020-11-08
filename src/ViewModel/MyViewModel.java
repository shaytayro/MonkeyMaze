package ViewModel;

import Model.IModel;
import Model.MyModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{
IModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty(""); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty(""); //For Binding

public MyViewModel(IModel model){
    this.model=model;
}
    @Override
    public void update(Observable o, Object arg) {

        characterPositionRowIndex = model.getCharacterRow();
        characterPositionRow.set(characterPositionRowIndex + "");
        characterPositionColumnIndex = model.getCharacterCol();
        characterPositionColumn.set(characterPositionColumnIndex + "");

        setChanged();
        notifyObservers();

    }
    public void generateMaze(int row,int col){
    model.generateMaze(row, col);
    }
    public void moveCharacter(KeyCode keycode){
    model.moveCharacter(keycode);
    }
    public int [][] getMaze(){
    return model.getMaze();
    }
    public int getCharacterRow(){
    return model.getCharacterRow();
    }
    public int getCharacterCol(){
    return model.getCharacterCol();
    }

    public int GoalRow(){
    return model.GoalRow();
    }
    public int GoalCol(){
        return model.Goalcol();
    }
    public int [][] getsolution(){
    return model.getsolution();
    }

    public boolean Save(File file){
        return model.Save(file);
    }
    public boolean Load(File file){
        return model.Load(file);
    }
    public void Exit(){model.Exit();}
}


