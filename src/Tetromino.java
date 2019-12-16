import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tetromino {

    int row, col, velocity;
    Block[][] grid;
    public Tetromino(int row, int col, int velocity, Block[][] grid){
        this.row = row;
        this.col = col;
        this.velocity = velocity;
        this.grid = grid;
    }

    public void draw(){
        grid[row][col].changeColor(Color.RED);
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public void setRow(int newRow){
        row = newRow;
    }

    public void changeCol(int x){
        if(col+x >= 0 && col+x < grid[col].length){
            if(!grid[row][col+x].isBlocked())
                col+=x;
        }
    }

    public void changeRow(int y){
        row+=y;
    }

    public void fall(){
        row += velocity;
    }
}
