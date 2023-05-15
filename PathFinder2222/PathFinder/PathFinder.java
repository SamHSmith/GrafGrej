import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PathFinder extends JFrame {
    private boolean unsavedChanges = false;

    private JPanel mapPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMapItem, openItem, saveItem, saveImageItem, exitItem;

    private JButton findPathButton, showConnectionButton, newPlaceButton, newConnectionButton, changeConnectionButton;

    public PathFinder() {
        setTitle("PathFinder");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        mapPanel = new JPanel();
        getContentPane().add(mapPanel, BorderLayout.CENTER);


        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newMapItem = new JMenuItem("New Map");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        saveImageItem = new JMenuItem("Save Image");
        exitItem = new JMenuItem("Exit");

        fileMenu.add(newMapItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveImageItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Knappar
        findPathButton = new JButton("Find Path");
        showConnectionButton = new JButton("Show Connection");
        newPlaceButton = new JButton("New Place");
        newConnectionButton = new JButton("New Connection");
        changeConnectionButton = new JButton("Change Connection");

        // Ordning på knapparna
        mapPanel.add(findPathButton);
        mapPanel.add(showConnectionButton);
        mapPanel.add(newPlaceButton);
        mapPanel.add(newConnectionButton);
        mapPanel.add(changeConnectionButton);

        
        newMapItem.addActionListener(e -> newMap());
        openItem.addActionListener(e -> open());
        saveItem.addActionListener(e -> save());
        saveImageItem.addActionListener(e -> saveImage());
        exitItem.addActionListener(e -> exit());
        findPathButton.addActionListener(e -> findPath());
        showConnectionButton.addActionListener(e -> showConnection());
        newPlaceButton.addActionListener(e -> newPlace());
        newConnectionButton.addActionListener(e -> newConnection());
        changeConnectionButton.addActionListener(e -> changeConnection());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    private void newMap() {
        try {
            BufferedImage image = ImageIO.read(new File("src/europa.gif"));
            JLabel label = new JLabel(new ImageIcon(image));
            mapPanel.removeAll();
            mapPanel.add(label);
            this.setSize(image.getWidth(), image.getHeight());
            this.validate();
            this.repaint();
            unsavedChanges = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void open() {
        //
    }

    private void save() {
        //
    }

    private void saveImage() {
        //
    }
    private void exit() {
            if (unsavedChanges) {
                int result = JOptionPane.showConfirmDialog(this,
                        "Det finns osparade ändringar. Är du säker på att du vill avsluta?",
                        "Bekräftelse av avslut",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            System.exit(0);
        }

    private void findPath() {
        //
    }

    private void showConnection() {
        //
    }

    private void newPlace() {
        //
    }

    private void newConnection() {
        //
    }

    private void changeConnection() {
        //
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PathFinder app = new PathFinder();
            app.setVisible(true);
        });
    }
}

