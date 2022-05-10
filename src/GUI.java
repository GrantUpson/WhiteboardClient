import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;


public class GUI extends JFrame implements Runnable {
    private final Map<String, Color> colours;
    private final IWhiteboard server;

    private JPanel guiPanel;
    private JTextField sendMessageField;
    private JButton sendButton;
    private JScrollPane chatScrollPane;
    private JTextPane chatTextArea;
    private JPanel whiteboardContainer;
    private JComboBox<String> shapeSelector;
    private JComboBox<String> colourSelector;
    private JList currentUsers;
    private JTextField textToDraw;
    private final WhiteboardCanvas canvas;

    public GUI(IWhiteboard server) {
        this.server = server;

        colours = new HashMap<>();
        canvas = new WhiteboardCanvas();

        createColourSelector();
        createShapeSelector();

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    if(shapeSelector.getSelectedItem() == "Text") {
                        server.sendDrawable(new Drawable(null, colours.get(colourSelector.getSelectedItem()),
                                new WhiteboardText(textToDraw.getText(), e.getX(), e.getY())));
                        /*canvas.addDrawable(new Drawable(null, colours.get(colourSelector.getSelectedItem()),
                                new WhiteboardText(textToDraw.getText(), e.getX(), e.getY())));*/
                    } else {
                        server.sendDrawable(new Drawable(getSelectedShape(e.getX() - 40, e.getY() - 30), colours.get(colourSelector.getSelectedItem()),
                                null));
                        /*canvas.addDrawable(new Drawable(getSelectedShape(e.getX() - 40, e.getY() - 30), colours.get(colourSelector.getSelectedItem()),
                                null)); */
                    }
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        whiteboardContainer.add(canvas);
    }

    private void createColourSelector() {
        colours.put("Black", new Color(0, 0, 0));
        colourSelector.addItem("Black");
        colours.put("Dark Grey", new Color(64, 64, 64));
        colourSelector.addItem("Dark Grey");
        colours.put("Grey", new Color(128, 128, 128));
        colourSelector.addItem("Grey");
        colours.put("Silver", new Color(192, 192, 192));
        colourSelector.addItem("Silver");
        colours.put("Aqua", new Color(0, 255, 255));
        colourSelector.addItem("Aqua");
        colours.put("Navy", new Color(0, 0, 128));
        colourSelector.addItem("Navy");
        colours.put("Blue", new Color(0, 0, 255));
        colourSelector.addItem("Blue");
        colours.put("Lime", new Color(0, 255, 0));
        colourSelector.addItem("Lime");
        colours.put("Green", new Color(0, 128, 0));
        colourSelector.addItem("Green");
        colours.put("Olive", new Color(128, 128, 0));
        colourSelector.addItem("Olive");
        colours.put("Teal", new Color(0, 128, 128));
        colourSelector.addItem("Teal");
        colours.put("Fuchsia", new Color(255, 0, 255));
        colourSelector.addItem("Fuchsia");
        colours.put("Purple", new Color(128, 0, 128));
        colourSelector.addItem("Purple");
        colours.put("Maroon", new Color(128, 0, 0));
        colourSelector.addItem("Maroon");
        colours.put("Red", new Color(255, 0, 0));
        colourSelector.addItem("Red");
        colours.put("Yellow", new Color(255, 255, 0));
        colourSelector.addItem("Yellow");
    }

    private void createShapeSelector() {
        shapeSelector.addItem("Rectangle");
        shapeSelector.addItem("Triangle");
        shapeSelector.addItem("Circle");
        shapeSelector.addItem("Line");
        shapeSelector.addItem("Text");
    }

    private Shape getSelectedShape(int x, int y) {
        return switch((String) Objects.requireNonNull(shapeSelector.getSelectedItem())) {
            case "Rectangle" -> new Rectangle(x, y, 100, 50);
            case "Circle" -> new Ellipse2D.Double(x, y, 75, 75);
            case "Triangle" -> new Triangle(x, y, 75, 75);
            case "Line" -> new Rectangle(x, y, 200, 4);
            default -> null;
        };
    }


    @Override
    public void run() {
        setTitle("Whiteboard");
        setContentPane(guiPanel);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void addDrawableFromServer(IDrawable drawable) {
        canvas.addDrawable(drawable);
    }

    public void syncDrawablesOnConnect(List<IDrawable> drawables) {
        canvas.syncDrawables(drawables);
    }
}
