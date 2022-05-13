/*
 * Name: Grant Upson
 * ID: 1225133
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;


public class GUI extends JFrame implements Runnable {
    private static final String CLIENT_TITLE = "Whiteboard Client";
    private static final String EMPTY_STRING_MESSAGE = "You cannot send an empty chat message.";
    private static final String PENDING_APPROVAL_MESSAGE = "Connection pending approval..";
    private static final String RECTANGLE_SELECTOR = "Rectangle";
    private static final String CIRCLE_SELECTOR = "Circle";
    private static final String TRIANGLE_SELECTOR = "Triangle";
    private static final String LINE_SELECTOR = "Line";
    private static final String TEXT_SELECTOR = "Text";
    private static final int SHAPE_OFFSET = 35;

    private Map<String, Color> colours;
    private final IWhiteboardServer server;
    private final IClientCallback client;
    private boolean connectionAccepted;

    private JPanel guiPanel;
    private JTextField sendMessageField;
    private JButton sendButton;
    private JScrollPane chatScrollPane;
    private JTextPane chatTextArea;
    private JPanel whiteboardContainer;
    private JComboBox<String> shapeSelector;
    private JComboBox<String> colourSelector;
    private JTextField textToDraw;
    private JButton disconnectButton;
    private JList<Object> connectedUsers;
    private JLabel connectedUsersLabel;
    private JScrollPane connectedUsersScrollbar;
    private final WhiteboardCanvas canvas;

    public GUI(IWhiteboardServer server, IClientCallback client) {
        this.server = server;
        this.client = client;
        canvas = new WhiteboardCanvas();
        connectionAccepted = false;

        initializeColourSelector();
        initializeShapeSelector();
        initializeListeners();

        disableServerCommunication();
        whiteboardContainer.add(canvas);
    }

    @Override
    public void run() {
        setTitle(CLIENT_TITLE);
        setContentPane(guiPanel);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void initializeListeners() {
        EventQueue.invokeLater(() -> canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                try {
                    if(connectionAccepted) {
                        if(shapeSelector.getSelectedItem() == TEXT_SELECTOR) {
                            server.sendDrawable(new Drawable(null, colours.get((String)colourSelector.getSelectedItem()),
                                    new WhiteboardText(textToDraw.getText(), e.getX(), e.getY())));
                        } else {
                            server.sendDrawable(new Drawable(getSelectedShape(e.getX() - SHAPE_OFFSET,
                                    e.getY() - SHAPE_OFFSET), colours.get((String)colourSelector.getSelectedItem()), null));
                        }
                    }
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        EventQueue.invokeLater(() -> disconnectButton.addActionListener(e -> {
            try {
                exitApplication();
            } catch(RemoteException ex) {
                ex.printStackTrace();
            }
        }));

        EventQueue.invokeLater(() -> sendButton.addActionListener(e -> {
            if(!sendMessageField.getText().trim().equalsIgnoreCase("")) {
                try {
                    server.sendChatMessage(client, sendMessageField.getText());
                    sendMessageField.setText("");
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            } else {
                //New thread so server messages aren't blocked.
                new Thread(() -> JOptionPane.showMessageDialog(null, EMPTY_STRING_MESSAGE)) {
                }.start();
            }
        }));

        EventQueue.invokeLater(() -> addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    exitApplication();
                } catch(RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }));
    }

    public void addDrawableFromServer(IDrawable drawable) {
        canvas.addDrawable(drawable);
    }

    public void updateDrawables(List<IDrawable> drawables) {
        canvas.addDrawableList(drawables);
    }

    public void addChatMessage(String message) {
        chatTextArea.setText(chatTextArea.getText() + "\n" + message);
    }

    public void updateConnectedUsers(List<String> users) {
        connectedUsers.setListData(users.toArray());
    }

    public void enableServerCommunication() {
        sendMessageField.setEnabled(true);
        sendButton.setEnabled(true);
        chatTextArea.setText("");
        shapeSelector.setEnabled(true);
        colourSelector.setEnabled(true);
        textToDraw.setEnabled(true);
        connectionAccepted = true;
    }

    private void initializeColourSelector() {
        colours = new HashMap<>();
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

    private void initializeShapeSelector() {
        shapeSelector.addItem(RECTANGLE_SELECTOR);
        shapeSelector.addItem(TRIANGLE_SELECTOR);
        shapeSelector.addItem(CIRCLE_SELECTOR);
        shapeSelector.addItem(LINE_SELECTOR);
        shapeSelector.addItem(TEXT_SELECTOR);
    }

    private Shape getSelectedShape(int x, int y) {
        return switch((String) Objects.requireNonNull(shapeSelector.getSelectedItem())) {
            case RECTANGLE_SELECTOR -> new Rectangle(x, y, 100, 50);
            case CIRCLE_SELECTOR -> new Ellipse2D.Double(x, y, 75, 75);
            case TRIANGLE_SELECTOR -> new Triangle(x, y, 75, 75);
            case LINE_SELECTOR -> new Rectangle(x, y, 200, 4);
            default -> null;
        };
    }

    private void disableServerCommunication() {
        sendMessageField.setEnabled(false);
        sendButton.setEnabled(false);
        chatTextArea.setText(PENDING_APPROVAL_MESSAGE);
        shapeSelector.setEnabled(false);
        colourSelector.setEnabled(false);
        textToDraw.setEnabled(false);
    }

    private void exitApplication() throws RemoteException {
        if(connectionAccepted) {
            server.disconnect(client);
        } else {
            server.terminateRequest(client);
        }
        System.exit(0);
    }
}
