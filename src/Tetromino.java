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
    private Block[] block;              //Array of blocks that together represent this tetromino
    private Block[] phantom;            //Array of blocks that together represent where this tetromino will fall

    //constructor
    public Tetromino(int row, int col, int velocity, Block[][] grid){
        this.row = row;
        this.col = col;
        this.velocity = velocity;
        this.grid = grid;
        block = new Block[4];
        shapeType = 1;
        phantom = new Block[4];
        initBlock();
        initPhantom();
        updatePhantom();                //Called once at the beginning to get the starting phantom, will be called from outside in the loop after
        //drawPhantom();
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

    //initialize the block array that will be this piece's phantom as the same position as the original block
    public void initPhantom(){
        for(int i = 0; i < 4; ++i){
            phantom[i] = new Block(block[i].getRow(),block[i].getCol());
        }
    }

    //Update the position of the phantom
    public void updatePhantom(){
        int i = 0;
        for(int j = 0; j < 4; ++j){             //Reset phantom so it can be recalculated
            phantom[j].setRow(block[j].getRow());
            phantom[j].setCol(block[j].getCol());
        }
        while(canPhantomDrop()){                //Recalculate phantom
            for(int j = 0; j < 4; ++j){
                phantom[j].fall(1);
            }
            ++i;
        }
    }

    //Draw the phantom
    public void drawPhantom(){
        for(int i = 0; i < 4; ++i){
            grid[phantom[i].getRow()][phantom[i].getCol()].changeColor(Color.GREEN);
        }
    }

    //Checks if the phantom can drop
    public boolean canPhantomDrop(){
        boolean able = true;
        for(int i = 0; i < 4; ++i){
            if(phantom[i].getRow()+1 > grid.length-1 || grid[phantom[i].getRow()+1][phantom[i].getCol()].isBlocked()){
                able = false;
            }
        }
        return(able);
    }

    //Randomly selects a variation of tetromino which is represented by an int 1-7
    public void changeShape(){
        shapeType = (int)(Math.random()*2)+1;
    }

    public void hardDrop(){
        for(int i = 0; i < 4; ++i){
            block[i].setRow(phantom[i].getRow());
            block[i].setCol(phantom[i].getCol());
        }
        solidify();
        changeShape();
        resetRow();
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

    //Brings piece back to top of board
    //THIS IS ALSO WHERE THE SHAPES ACTUALLY CHANGE
    public void resetRow(){
        if(shapeType == 1){                     //square
            if(!(grid[0][4].isBlocked() && grid[0][5].isBlocked() && grid[1][4].isBlocked() && grid[1][5].isBlocked())){
                rotation = 1;
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
        if(shapeType == 2){                     //long
            if(!(grid[0][3].isBlocked() && grid[0][4].isBlocked() && grid[0][5].isBlocked() && grid[0][6].isBlocked())){
                rotation = 1;
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

    public void rotate(){
        //Start with 2 because squares don't need to rotate

        //Long piece
        if(shapeType == 2){
            //original position for line
            int spotR = block[2].getRow();
            int spotC = block[2].getCol();
            if(rotation == 1){
                if(block[2].getRow() == grid.length-1){
                    if((!grid[spotR-3][spotC].isBlocked() && !grid[spotR-2][spotC].isBlocked() && !grid[spotR-1][spotC].isBlocked())){
                        block[0].setRow(spotR-3);
                        block[0].setCol(spotC);
                        block[1].setRow(spotR-2);
                        block[1].setCol(spotC);
                        block[2].setRow(spotR-1);
                        block[3].setRow(spotR);
                        block[3].setCol(spotC);
                        rotation = 2;
                    }
                }else{
                    if(block[2].getRow() == 0 || block[2].getRow() == 1){
                        if((!grid[spotR+1][spotC].isBlocked() && !grid[spotR+2][spotC].isBlocked() && !grid[spotR+3][spotC].isBlocked())){
                            block[0].setRow(spotR);
                            block[0].setCol(spotC);
                            block[1].setRow(spotR+1);
                            block[1].setCol(spotC);
                            block[2].setRow(spotR+2);
                            block[3].setRow(spotR+3);
                            block[3].setCol(spotC);
                            rotation = 2;
                        }
                    }else if((!grid[spotR-2][spotC].isBlocked() && !grid[spotR-1][spotC].isBlocked() && !grid[spotR+1][spotC].isBlocked())){
                        block[0].setRow(spotR-2);
                        block[0].setCol(spotC);
                        block[1].setRow(spotR-1);
                        block[1].setCol(spotC);
                        block[3].setRow(spotR+1);
                        block[3].setCol(spotC);
                        rotation = 2;
                    }
                }
            }else if(rotation == 2){
                if(!(spotC<2) && spotC != grid[0].length-1){
                    if(spotR != grid.length-2){
                        if ((!grid[spotR][spotC-2].isBlocked() && !grid[spotR][spotC - 1].isBlocked() && !grid[spotR][spotC + 1].isBlocked())) {
                            block[0].setRow(spotR);
                            block[0].setCol(spotC - 2);
                            block[1].setRow(spotR);
                            block[1].setCol(spotC - 1);
                            block[3].setRow(spotR);
                            block[3].setCol(spotC + 1);
                            rotation = 1;
                        }
                    }else{
                        if ((!grid[spotR+1][spotC-2].isBlocked() && !grid[spotR+1][spotC - 1].isBlocked() && !grid[spotR+1][spotC + 1].isBlocked())){
                            block[0].setRow(spotR+1);
                            block[0].setCol(spotC-2);
                            block[1].setRow(spotR+1);
                            block[1].setCol(spotC-1);
                            block[2].setRow(spotR+1);
                            block[3].setRow(spotR+1);
                            block[3].setCol(spotC+1);
                            rotation = 1;
                        }
                    }
                }else{
                    if(spotC == grid[0].length-1){
                        if ((!grid[spotR][spotC-3].isBlocked() && !grid[spotR][spotC - 2].isBlocked() && !grid[spotR][spotC -1].isBlocked())) {
                            block[0].setRow(spotR);
                            block[0].setCol(spotC - 3);
                            block[1].setRow(spotR);
                            block[1].setCol(spotC - 2);
                            block[2].setCol(spotC-1);
                            block[3].setRow(spotR);
                            block[3].setCol(spotC);
                            rotation = 1;
                        }
                    }
                    if(spotC == 0){
                        if ((!grid[spotR][spotC+3].isBlocked() && !grid[spotR][spotC+2].isBlocked() && !grid[spotR][spotC+1].isBlocked())) {
                            block[0].setRow(spotR);
                            block[0].setCol(spotC);
                            block[1].setRow(spotR);
                            block[1].setCol(spotC+1);
                            block[2].setCol(spotC+2);
                            block[3].setRow(spotR);
                            block[3].setCol(spotC+3);
                            rotation = 1;
                        }
                    }
                    if(spotC == 1){
                        if ((!grid[spotR][spotC-1].isBlocked() && !grid[spotR][spotC+2].isBlocked() && !grid[spotR][spotC+1].isBlocked())) {
                            block[0].setRow(spotR);
                            block[0].setCol(spotC-1);
                            block[1].setRow(spotR);
                            block[1].setCol(spotC);
                            block[2].setCol(spotC+1);
                            block[3].setRow(spotR);
                            block[3].setCol(spotC+2);
                            rotation = 1;
                        }
                    }
                }
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
