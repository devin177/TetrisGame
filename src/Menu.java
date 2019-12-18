import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Menu {

    //Popup menu that opens when you first launch the game
    //Has two options, play and quit
    public static void display(){

        //Create a new Stage that won't allow you to interact with other windows from this application while its open
        //Sets title and width
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Tetris Game Menu");
        window.setWidth(200);

        //Title of Game
        Label label = new Label("Tetris");

        //Options represented as buttons
        Button b1 = new Button("Play");
        Button b2 = new Button("Quit");

        //Handling for buttons and close button using lambda expressions
        window.setOnCloseRequest(e->{
            System.exit(0);
        });

        b1.setOnAction(e ->{
            window.close();
        });

        b2.setOnAction(e ->{
            window.close();
            System.exit(0);
        });

        //Creating a layout and place the buttons and label on it as nodes
        //Also position it
        VBox vb = new VBox(10);
        vb.getChildren().addAll(label, b1, b2);
        vb.setAlignment(Pos.CENTER);

        //Create a scene and add our layout
        //And then show the screen and wait for a response
        Scene scene = new Scene(vb);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void replayMenu(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Tetris Game Menu");
        window.setWidth(200);

        //Title of Game
        Label label = new Label("Tetris");

        //Options represented as buttons
        Button b1 = new Button("Replay?");
        Button b2 = new Button("Quit");

        //Handling for buttons and close button using lambda expressions
        window.setOnCloseRequest(e->{
            System.exit(0);
        });

        b1.setOnAction(e ->{
            window.close();
        });

        b2.setOnAction(e ->{
            window.close();
            System.exit(0);
        });

        //Creating a layout and place the buttons and label on it as nodes
        //Also position it
        VBox vb = new VBox(10);
        vb.getChildren().addAll(label, b1, b2);
        vb.setAlignment(Pos.CENTER);

        //Create a scene and add our layout
        //And then show the screen and wait for a response
        Scene scene = new Scene(vb);
        window.setScene(scene);
    }
}
