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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Game extends Application {

    //Variable declaration block of rules for the game
    private final int rows = 20;
    private final int cols = 10;
    private int dropRate = 1;

    public void start(Stage window) throws Exception{
        window.setTitle("Tetris");
        //window.setWidth(500);

        //Creating and setting up a group and a Canvas node
        Group root = new Group();
        GridPane gridPane = new GridPane();



        //Declare and initialize an array of rectangles that will display the board
        Block[][] grid = new Block[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                grid[i][j] = new Block(i,j);
                gridPane.add(grid[i][j].getR(), j, i, 1, 1);
            }
        }

        //adding group with its nodes to the scene and then stage
        Scene scene = new Scene(gridPane);

        Tetromino tetris = new Tetromino(0,4,dropRate,grid);


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch(e.getCode()){
                    case LEFT: tetris.changeCol(-1); break;
                    case RIGHT: tetris.changeCol(1); break;
                    case DOWN:  if(tetris.getRow()+1 < rows && !grid[tetris.getRow()+1][tetris.getCol()].isBlocked()) {
                                    tetris.changeRow(1);
                                }
                                break;
                    default: break;
                }
            }
        });

        window.setScene(scene);
        window.show();





        //Main Game loop
        AnimationTimer timer = new AnimationTimer()
        {
            public long start = System.nanoTime();
            int secs = 0;
            double checkTime = 1.0;
            public void handle(long currentNanoTime)
            {
                double time = (currentNanoTime - start)/(1000_000_000.0);
                for(int i = 0; i < rows; i++){
                    for(int j = 0; j < cols; j++) {
                        if (grid[i][j].isBlocked()) {
                            grid[i][j].changeColor(Color.BLACK);
                        }else{
                            grid[i][j].changeColor(Color.WHITE);
                        }
                    }
                }
                if(time > checkTime){                                 //if the time elapsed is more than 1 second, naturally add the change the position of the tetris
                    secs +=1;                                   //Also, if the tetris has hit the bottom, or there is a piece under it, stop dropping and move it back
                    start = currentNanoTime;                    //and toggle the last position it was at to have blocked on
                    if(tetris.getRow() < rows-1 && !grid[tetris.getRow() + 1][tetris.getCol()].isBlocked()){
                        tetris.fall();
                    }else{
                        grid[tetris.getRow()][tetris.getCol()].toggleOn();
                        tetris.setRow(0);
                    }
                }

                tetris.draw();
                checkTetris();

            }
        };
        timer.start();

    }



    public void checkTetris(){

    }

    public static void main(String[] args){
        launch(args);
    }

}
