import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Game extends Application {

    //Variable declaration block of rules for the game
    private final int rows = 20;
    private final int cols = 10;
    private int dropRate = 1;

    public void start(Stage window) throws Exception{
        Menu.display();
        window.setTitle("TetrisGame");
        window.setWidth(700);

        //Creating and setting up a group and the layouts
        BorderPane bp = new BorderPane();
        GridPane gridPane = new GridPane();
        VBox vb = new VBox();
        HBox hb = new HBox();
        VBox right = new VBox();

        //Declare and initialize an array of rectangles that will display the board
        Block[][] grid = new Block[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                grid[i][j] = new Block(i,j);
                gridPane.add(grid[i][j].getR(), j, i, 1, 1);
            }
        }

        //adding group with its nodes to the scene and then stage
        Scene scene = new Scene(bp);
        bp.setCenter(gridPane);
        bp.setLeft(vb);
        bp.setBottom(hb);
        bp.setRight(right);

        Label label = new Label();
        vb.setMinWidth(100);
        vb.getChildren().add(label);

        Label label2 = new Label("TETRIS BUT ONLY SQUARES AND LINES AND YOU CAN'T ROTATE AND THERE'S NO INDICATOR");
        hb.getChildren().add(label2);
        Tetromino tetromino = new Tetromino(0,4,dropRate,grid);

        boolean[] lines = new boolean[rows];

        //Controller handling for moving or rotating the piece
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch(e.getCode()){
                    case LEFT: if(tetromino.canMove(-1)){
                        tetromino.changeCol(-1);
                    }
                        break;
                    case RIGHT: if(tetromino.canMove(1)){
                        tetromino.changeCol(1);
                    }
                        break;
                    case DOWN: if(tetromino.canDrop()){
                        tetromino.changeRow(1);
                    }
                        break;
                    default: break;
                }
            }
        });

        window.setScene(scene);
        window.show();


        Label instructions = new Label("Use arrow keys to move");

        right.getChildren().add(instructions);


        //Main Game loop
        AnimationTimer timer = new AnimationTimer()
        {
            public long start = System.nanoTime();
            int secs = 0;
            double checkTime = 1.0;
            int score = 0;
            public void handle(long currentNanoTime)
            {
                double time = (currentNanoTime - start)/(1000_000_000.0);
                for(int i = 0; i < rows; i++){
                    for(int j = 0; j < cols; j++) {
                        if (grid[i][j].isBlocked()) {
                            grid[i][j].changeColor(Color.BLUE);
                        }else{
                            grid[i][j].changeColor(Color.WHITE);
                        }
                    }
                }
                if(time > checkTime){                                 //if the time elapsed is more than 1 second, naturally add the change the position of the tetromino
                    secs +=1;                                   //Also, if the tetromino has hit the bottom, or there is a piece under it, stop dropping and move it back
                    start = currentNanoTime;                    //and toggle the last position it was at to have blocked on

                    if(tetromino.canDrop()){
                        tetromino.fall();
                    }else{
                        tetromino.solidify();                   //Cements the block onto the board
                        score+= 100 * checkTetris(grid, lines);        //Checks if you can clear any lines
                        tetromino.changeShape();
                        if(!tetromino.getFree()) {
                            System.out.println("Game Over");
                        }
                        tetromino.resetRow();                    //Brings the tetromino back to the top
                    }
                }
                tetromino.draw();
                label.setText("Score: " + score);
            }
        };
        timer.start();

    }

    public int checkTetris(Block[][] grid, boolean[] lines){
        int firstCleared = -1;
        boolean linesCleared = false;
        int amountCleared = 0;
        for(int i = rows-1; i >= 0; --i){
            int count = 0;
            for(int j = 0; j < cols; ++j){
                if(grid[i][j].isBlocked()){
                    ++count;
                }
            }
            if(count == 10){            //if all of the blocks in this row are blocked, set that row to true
                lines[i] = true;
            }
            if(lines[i]){               //if that row is true, reset all the blocks in that row
                for(int j = 0; j < cols; ++j){
                    grid[i][j].toggleOff();
                }
                ++amountCleared;
                lines[i] = false;
                linesCleared = true;
                firstCleared = i;
            }

        }
        if(linesCleared){
            for(int i = 0; i < amountCleared; ++i){
                moveRemaining(grid, firstCleared);
            }
        }
        return(amountCleared);
    }

    public void moveRemaining(Block[][] grid, int firstLine){
        for(int i = firstLine; i >= 0; --i){
            for(int j = 0; j < cols; j++){
                if(grid[i][j].isBlocked() && !grid[i+1][j].isBlocked()){
                    grid[i][j].toggleOff();
                    grid[i+1][j].toggleOn();
                }
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }

}
