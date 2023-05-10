package PathFinder;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PathFinder extends JFrame {
    private static final long serialVersionUID = 1L;
    private boolean unsavedChanges = false;

    // Swing-komponenter här
    private JPanel mapPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMapItem, openItem, saveItem, saveImageItem, exitItem;

    public PathFinder() {
        // Initialize components and add them to the window
        setTitle("PathFinder");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Create and add mapPanel
        mapPanel = new JPanel();
        getContentPane().add(mapPanel, BorderLayout.CENTER);

        // Create File menu and add it to JMenuBar
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

        // Add ActionListener for menu items
        newMapItem.addActionListener(e -> newMap());
        openItem.addActionListener(e -> open());
        saveItem.addActionListener(e -> save());
        saveImageItem.addActionListener(e -> saveImage());
        exitItem.addActionListener(e -> exit());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }

    // Methods for handling menu items
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PathFinder app = new PathFinder();
            app.setVisible(true);
        });
    }
}

