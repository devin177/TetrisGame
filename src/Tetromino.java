import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tetromino {

    private int row, col;               //These will represent the center of rotation for the tetromino
    private int velocity;               //How many rows this Tetromino drops per tick
    private int shapeType;              //What kind of tetromino is this
    private int rotation;               //Represents which of the rotations a block is on. Will be 1 - 4
    private boolean free;               //Is the block able to be reset?
    private Block[][] grid;
    private Block[] block;

    //constructor
    public Tetromino(int row, int col, int velocity, Block[][] grid){
        this.row = row;
        this.col = col;
        this.velocity = velocity;
        this.grid = grid;
        block = new Block[4];
        shapeType = 1;
        initBlock();
        rotation = 1;
        free = true;
    }

    //initialize the block d array that will be this piece
    //for now block will always start as cube
    public void initBlock(){

        //1 = square, 2 = line, 3 = T, 4 = S, 5 = Z, 6 = L, 7 = J
        if(shapeType == 1){    //This is a square
            block[0] = new Block(row,col);
            block[1] = new Block(row,col+1);
            block[2] = new Block(row+1,col);
            block[3] = new Block(row+1,col+1);
        }
    }

    //Randomly selects a variation of tetromino which is represented by an int 1-7
    public void changeShape(){
        shapeType = (int)(Math.random()*2)+1;
        System.out.println(shapeType);
    }

    //Returns whether or not the tetromino can fall
    public boolean canDrop(){
        boolean able = true;
        for(int i = 0; i < 4; ++i){
            if(block[i].getRow()+1 > grid.length-1 || grid[block[i].getRow()+1][block[i].getCol()].isBlocked()){
                able = false;
            }
        }
        return(able);
    }

    //Returns whether or not the tetromino can move horizontally
    public boolean canMove(int x){
        boolean able = true;
        for(int i = 0; i < 4; ++i){
            if(block[i].getCol()+x < 0 || block[i].getCol()+x > grid[0].length-1 || grid[block[i].getRow()][block[i].getCol()+x].isBlocked()){
                able = false;
            }
        }
        return(able);
    }

    //Displays the falling tetromino as red
    public void draw(){
        for(int i = 0; i < 4; i++){
            grid[block[i].getRow()][block[i].getCol()].changeColor(Color.RED);
        }
    }

    //Brings piece back to top of board based on what type of piece it is
    public void resetRow(){
        if(shapeType == 1){                     //square
            if(!(grid[0][4].isBlocked() && grid[0][5].isBlocked() && grid[1][4].isBlocked() && grid[1][5].isBlocked())){
                block[0].setRow(0);
                block[1].setRow(0);
                block[2].setRow(1);
                block[3].setRow(1);
                block[0].setCol(4);
                block[1].setCol(5);
                block[2].setCol(4);
                block[3].setCol(5);
            }else{
                free = false;
            }
        }
        if(shapeType == 2){                     //line
            if(!(grid[0][3].isBlocked() && grid[0][4].isBlocked() && grid[0][5].isBlocked() && grid[0][6].isBlocked())){
                for(int i = 0; i < 4; ++i){
                    block[i].setRow(0);
                }
                block[0].setCol(3);
                block[1].setCol(4);
                block[2].setCol(5);
                block[3].setCol(6);
            }else{
                free = false;
            }
        }
    }

    //Changes the column of the tetromino if able to by x columns
    public void changeCol(int x){
        boolean isBlocked = false;
        for(int i = 0; i < 4; i++){
            if(block[i].getRow() + x >= 0)
                if(grid[block[i].getRow()][block[i].getCol()+x].isBlocked())
                    isBlocked=true;
        }
        if(!isBlocked){
            for(int i = 0; i < 4; i++){
                block[i].changeCol(x);
            }
        }
    }

    //Changes the grid to match tetromino that locked in
    public void solidify(){
        for(int i = 0; i < 4; ++i){
            grid[block[i].getRow()][block[i].getCol()].toggleOn();
        }
    }

    //Change the row of the tetromino
    public void changeRow(int y){
        for(int i = 0; i < 4; ++i){
            block[i].fall(y);
        }
    }

    //Drop the tetromino one row
    public void fall(){
        for(int i = 0; i < 4; i++){
            block[i].fall(velocity);
        }
    }

    //Returns whether or not the game can go on
    public boolean getFree(){
        return free;
    }
}
