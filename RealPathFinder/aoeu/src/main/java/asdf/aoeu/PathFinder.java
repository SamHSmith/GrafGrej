package asdf.aoeu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PathFinder extends Application {
    private boolean unsavedChanges = false;

    private BorderPane layout;
    private MenuBar menuBar;
    private Menu fileMenu;
    private MenuItem newMapItem, openItem, saveItem, saveImageItem, exitItem;

    private Button findPathButton, showConnectionButton, newPlaceButton, newConnectionButton, changeConnectionButton;

    @Override
    public void start(Stage primaryStage) {
        layout = new BorderPane();

        // Create the menu
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        newMapItem = new MenuItem("New Map");
        openItem = new MenuItem("Open");
        saveItem = new MenuItem("Save");
        saveImageItem = new MenuItem("Save Image");
        exitItem = new MenuItem("Exit");

        fileMenu.getItems().addAll(newMapItem, openItem, saveItem, saveImageItem, exitItem);
        menuBar.getMenus().add(fileMenu);

        layout.setTop(menuBar);

        // Create the buttons
        findPathButton = new Button("Find Path");
        showConnectionButton = new Button("Show Connection");
        newPlaceButton = new Button("New Place");
        newConnectionButton = new Button("New Connection");
        changeConnectionButton = new Button("Change Connection");

        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(15));
        buttonBox.getChildren().addAll(findPathButton, showConnectionButton, newPlaceButton, newConnectionButton, changeConnectionButton);

        layout.setCenter(buttonBox);

        // Event Handlers
        newMapItem.setOnAction(e -> newMap());
        openItem.setOnAction(e -> open());
        saveItem.setOnAction(e -> save());
        saveImageItem.setOnAction(e -> saveImage());
        exitItem.setOnAction(e -> exit());
        findPathButton.setOnAction(e -> findPath());
        showConnectionButton.setOnAction(e -> showConnection());
        newPlaceButton.setOnAction(e -> newPlace());
        newConnectionButton.setOnAction(e -> newConnection());
        changeConnectionButton.setOnAction(e -> changeConnection());

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setTitle("PathFinder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void newMap() {
        try {
            Image image = new Image(new FileInputStream("src/europa.gif"));
            ImageView imageView = new ImageView(image);
            layout.setCenter(imageView);
            unsavedChanges = true;
        } catch (FileNotFoundException e) {
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Bekräftelse av avslut");
            alert.setHeaderText("Det finns osparade ändringar. Är du säker på att du vill avsluta?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
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
        launch(args);
    }
}

