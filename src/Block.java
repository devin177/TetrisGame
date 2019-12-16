import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block {

    private int row, col;
    private Rectangle r;
    private boolean hasBlock;

    public Block(int row, int col){
        this.row = row;
        this.col = col;
        hasBlock = false;
        r = new Rectangle(20,20, Color.WHITE);
        r.setStroke(Color.BLACK);
        r.setStrokeWidth(2.0);
    }

    public boolean isBlocked(){
        return(hasBlock);
    }

    public void toggleOn(){
        hasBlock = true;
    }

    public void toggleOff(){
        hasBlock = false;
    }

    public Rectangle getR(){
        return r;
    }

    public void changeColor(Color color){
        r.setFill(color);
    }
}
