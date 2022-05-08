import java.awt.*;
import java.rmi.RemoteException;


public class Drawable implements IDrawable { //possibly extend unicast object?
    private final Shape shape;
    private final Color colour;
    private final WhiteboardText text;
    private boolean isShape = false;

    public Drawable(Shape shape, Color colour, WhiteboardText text) {
        this.shape = shape;
        this.colour = colour;
        this.text = text;

        if(text == null) {
            isShape = true;
        }
    }

    @Override
    public Shape getShape() throws RemoteException {
        return shape;
    }

    @Override
    public Color getColour() throws RemoteException {
        return colour;
    }

    @Override
    public WhiteboardText getWhiteboardText() throws RemoteException {
        return text;
    }

    @Override
    public boolean isShape() throws RemoteException {
        return isShape;
    }
}
