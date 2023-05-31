// PROG2 VT2023, Inlämningsuppgift, del 2
// Grupp 100
// Sam Smith sasm7798
// Marcus Berngarn mabe1838

package asdf.aoeu;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

class Place {
	public double x, y;
	public boolean selected;
	public String name;
	public long s_number;

	public Place(double x, double y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		selected = false;
		s_number = 0;
	}
}

class Connection {
	public Place p1, p2;
	public String name;
	public int time;

	public Connection(Place p1, Place p2, String name, int time) {
		this.p1 = p1;
		this.p2 = p2;
		this.name = name;
		this.time = time;
	}
}

public class PathFinder extends Application {
	private static final double CIRCLE_RAD = 15;

	private static final double LINE_WIDTH = 5;

	private boolean unsavedChanges = false;

	private BorderPane layout;
	private MenuBar menuBar;
	private Menu fileMenu;
	private MenuItem newMapItem, openItem, saveItem, saveImageItem, exitItem;

	private Button findPathButton, showConnectionButton, newPlaceButton, newConnectionButton, changeConnectionButton;

	private VBox vert_box;
	private Stage primaryStage;

	private boolean new_place_mode;

	private Group circles;
	private ArrayList<Place> places = new ArrayList<Place>();
	private long s_counter = 0;
	private ArrayList<Connection> connections = new ArrayList<Connection>();

	private String map_url;
	private double yoffset = 0.0;

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
		buttonBox.getChildren().addAll(findPathButton, showConnectionButton, newPlaceButton, newConnectionButton,
				changeConnectionButton);

		vert_box = new VBox(15);
		vert_box.getChildren().add(buttonBox);

		layout.setCenter(vert_box);

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

		this.circles = new Group();
		layout.getChildren().add(circles);

		Scene scene = new Scene(layout);
		primaryStage.setTitle("PathFinder");
		primaryStage.setScene(scene);
		primaryStage.show();
		this.primaryStage = primaryStage;

		yoffset = layout.getHeight() + vert_box.getSpacing();
	}

	private void updatePlaces() {
		this.circles.getChildren().clear();
		for (int i = 0; i < places.size(); i++) {
			Circle c;
			if (places.get(i).selected) {
				c = new Circle(places.get(i).x, places.get(i).y + yoffset, CIRCLE_RAD, Paint.valueOf("RED"));
			} else {
				c = new Circle(places.get(i).x, places.get(i).y + yoffset, CIRCLE_RAD, Paint.valueOf("BLUE"));
			}
			c.setOnMouseClicked(e -> placePress(e.getSource()));
			circles.getChildren().add(c);
		}

		Canvas can = new Canvas(primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight());
		can.mouseTransparentProperty().set(true);
		circles.getChildren().add(can);

		GraphicsContext gc = can.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(LINE_WIDTH);
		for (int i = 0; i < connections.size(); i++) {
			Place p1 = connections.get(i).p1;
			Place p2 = connections.get(i).p2;
			gc.strokeLine(p1.x, p1.y + yoffset, p2.x, p2.y + yoffset);
		}
	}

	private void newMap() {
		map_url = "file:europa.gif";
		setMapImage();
		connections.clear();
		places.clear();
		updatePlaces();
	}

	private void setMapImage() {
		try {
			Image image = new Image(new URL(map_url).openStream());
			ImageView imageView = new ImageView(image);
			imageView.setOnMouseClicked(e -> mouseMapPress(e.getX(), e.getY()));

			if (vert_box.getChildren().size() == 1) {
				vert_box.getChildren().add(imageView);
			} else {
				vert_box.getChildren().set(1, imageView);
			}
			primaryStage.sizeToScene();
			unsavedChanges = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		connections.clear();
		places.clear();
		updatePlaces();
	}

	private void open() {
		try {
			BufferedReader r = new BufferedReader(new FileReader("europa.graph"));
			map_url = r.readLine();
			setMapImage();
			String[] tops = r.readLine().split(";");
			for (int i = 0; i + 2 < tops.length;) {
				String name = tops[i + 0];
				double x = Double.parseDouble(tops[i + 1]);
				double y = Double.parseDouble(tops[i + 2]);
				i += 3;

				places.add(new Place(x, y, name));
			}

			while (true) {
				String line = r.readLine();
				if (line == null || line.length() == 0) {
					break;
				}

				String[] bops = line.split(";");
				String p1n = bops[0];
				String p2n = bops[1];
				String cn = bops[2];
				int time = Integer.parseInt(bops[3]);

				Place p1 = null;
				Place p2 = null;
				for (int i = 0; i < places.size(); i++) {
					if (places.get(i).name.equals(p1n)) {
						p1 = places.get(i);
					}
					if (places.get(i).name.equals(p2n)) {
						p2 = places.get(i);
					}
				}

				connections.add(new Connection(p1, p2, cn, time));
			}
			updatePlaces();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void save() {
		try {
			FileWriter w = new FileWriter("europa.graph");
			w.write(map_url + "\n");
			for (int i = 0; i < places.size(); i++) {
				if (i != 0) {
					w.write(";");
				}
				Place p = places.get(i);
				w.write(p.name);
				w.write(";");
				w.write(p.x + ";" + p.y);
			}
			w.write('\n');

			for (int i = 0; i < connections.size(); i++) {
				Connection c = connections.get(i);
				w.write(c.p1.name);
				w.write(";");
				w.write(c.p2.name);
				w.write(";");
				w.write(c.name);
				w.write(";" + c.time);
				w.write('\n');
			}

			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void saveImage() {
		WritableImage snapshot = this.primaryStage.getScene().snapshot(null);
		try {
			while (snapshot.getProgress() < 1.0) {
			}
			BufferedImage bi = SwingFXUtils.fromFXImage(snapshot, null);
			File f = new File("capture.png");
			ImageIO.write(bi, "png", f);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

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

	private void resetState() {
		this.primaryStage.getScene().setCursor(Cursor.DEFAULT);
		this.new_place_mode = false;
		this.newPlaceButton.setDisable(false);
	}

	private void findPath() {
		resetState();
		
		Place p1 = null;
		Place p2 = null;
		{
		int i = 0;
		while (i < places.size()) {
			if (places.get(i).selected) {
				p1 = places.get(i);
				i++;
				break;
			}
			i++;
		}
		while (i < places.size()) {
			if (places.get(i).selected) {
				p2 = places.get(i);
				i++;
				break;
			}
			i++;
		}
		if (p1 == null || p2 == null) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText(null);
			a.setContentText("Two places must be selected.");
			a.showAndWait();
			return;
		}
		}
		
		ListGraph<Place> graph = new ListGraph<Place>();
		
		for (int i = 0; i < places.size(); i++) {
			graph.add(places.get(i));
		}
		for (int i = 0; i < connections.size(); i++) {
			Connection c = connections.get(i);
			graph.connect(c.p1, c.p2, c.name, c.time);
		}
		
		List<Edge<Place>> path = graph.getPath(p1, p2);
		if(path == null)
		{
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText(null);
			a.setContentText("There is no path.");
			a.showAndWait();
			return;
		}
		
		String str = "";
		int total = 0;
		for (int i = 0; i < path.size(); i++) {
			Edge<Place> e = path.get(i);
			str += "to " + e.getDestination().name + " by " + e.getName() + " takes " + e.getWeight() + "\n";
			total += e.getWeight();
		}
		str += "Total " + total + "\n";
		
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("The path from " + p1.name + " to " + p2.name);

		TextField nameField = new TextField(str);
		nameField.setEditable(false);
		a.setContentText(str);
		a.showAndWait();
	}

	private void showConnection() {
		int connection_index = getSelectedConnectionIndex();
		if (connection_index == -1) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText(null);
			a.setContentText("Two places must be selected and here must be a direct connection.");
			a.showAndWait();
			return;
		}

		Connection con = connections.get(connection_index);

		TextInputDialog dialog = new TextInputDialog("");
		GridPane grid = new GridPane();
		grid.setAlignment(null);
		grid.setPadding(new Insets(10));

		TextField nameField = new TextField(con.name);
		nameField.setEditable(false);
		TextField timeField = new TextField("" + con.time);
		timeField.setEditable(false);

		grid.addRow(1, new Label("Name: "), nameField);
		grid.addRow(2, new Label("Time: "), timeField);

		dialog.getDialogPane().setContent(grid);
		dialog.setHeaderText(null);
		dialog.setTitle("Connection");
		boolean was_confirm = dialog.showAndWait().isPresent();
	}

	private void newPlace() {
		resetState();
		this.primaryStage.getScene().setCursor(Cursor.CROSSHAIR);
		this.new_place_mode = true;
		this.newPlaceButton.setDisable(true);
	}

	private void mouseMapPress(double mx, double my) {
		System.out.println("Press " + mx + " " + my);

		if (this.new_place_mode) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Name");
			dialog.setHeaderText(null);
			dialog.setContentText("Please enter your name:");
			Optional<String> res = dialog.showAndWait();
			if (!res.isPresent()) {
				resetState();
				return;
			}
			places.add(new Place(mx, my, res.get()));
			updatePlaces();
			resetState();
			unsavedChanges = true;
		}
	}

	private void placePress(Object o) {
		int index = circles.getChildren().indexOf(o);
		System.out.println("Pressed index = " + index);

		int count = 0;
		for (int i = 0; i < places.size(); i++) {
			if (places.get(i).selected) {
				count += 1;
			}
		}

		if (count == 2 && !places.get(index).selected) {
			return;
		}

		places.get(index).selected = !places.get(index).selected;
		places.get(index).s_number = ++this.s_counter;
		updatePlaces();
	}

	private int getSelectedConnectionIndex() {
		Place p1 = null;
		Place p2 = null;
		int i = 0;
		while (i < places.size()) {
			if (places.get(i).selected) {
				p1 = places.get(i);
				i++;
				break;
			}
			i++;
		}
		while (i < places.size()) {
			if (places.get(i).selected) {
				p2 = places.get(i);
				i++;
				break;
			}
			i++;
		}
		if (p1 == null || p2 == null) {
			return -1;
		}

		for (int j = 0; j < connections.size(); j++) {
			if (connections.get(j).p1 == p1 && connections.get(j).p2 == p2) {
				return j;
			}
			if (connections.get(j).p1 == p2 && connections.get(j).p2 == p1) {
				return j;
			}
		}
		return -1;
	}

	private void newConnection() {
		if (getSelectedConnectionIndex() != -1) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText(null);
			a.setContentText("Connection already exists, use \"Change Connection\" instead.");
			a.showAndWait();
			return;
		}

		{
			int count = 0;
			for (int i = 0; i < places.size(); i++) {
				if (places.get(i).selected) {
					count += 1;
				}
			}
			if (count != 2) {
				Alert a = new Alert(AlertType.ERROR, "Two places must be selected.");
				a.setTitle("Error!");
				a.setHeaderText(null);
				a.showAndWait();
				return;
			}
		}

		Place p1 = null;
		Place p2 = null;
		int i = 0;
		while (i < places.size()) {
			if (places.get(i).selected) {
				p1 = places.get(i);
				i++;
				break;
			}
			i++;
		}
		while (i < places.size()) {
			if (places.get(i).selected) {
				p2 = places.get(i);
				i++;
				break;
			}
			i++;
		}
		if (p1.s_number > p2.s_number) {
			Place t = p1;
			p1 = p2;
			p2 = t;
		}
		System.out.println("NC " + p1.name + " -> " + p2.name);

		TextInputDialog dialog = new TextInputDialog("");
		GridPane grid = new GridPane();
		grid.setAlignment(null);
		grid.setPadding(new Insets(10));

		TextField nameField = new TextField();
		TextField timeField = new TextField();

		grid.addRow(1, new Label("Name: "), nameField);
		grid.addRow(2, new Label("Time: "), timeField);

		dialog.getDialogPane().setContent(grid);
		dialog.setHeaderText(null);
		dialog.setTitle("Connection");
		boolean was_confirm = dialog.showAndWait().isPresent();
		if (!was_confirm) {
			return;
		}

		String connection_name = nameField.getText();
		int connection_time = -1;
		try {
			connection_time = Integer.parseInt(timeField.getText());
		} catch (Exception e) {
		}

		if (connection_name.length() == 0) {
			Alert a = new Alert(AlertType.ERROR, "Bad connection name. Cannot be empty.");
			a.setTitle("Error!");
			a.setHeaderText(null);
			a.showAndWait();
			return;
		}
		if (connection_time < 0) {
			Alert a = new Alert(AlertType.ERROR, "Bad connection time value. Must be a positive integer.");
			a.setTitle("Error!");
			a.setHeaderText(null);
			a.showAndWait();
			return;
		}

		connections.add(new Connection(p1, p2, connection_name, connection_time));
		updatePlaces();
		unsavedChanges = true;
	}

	private void changeConnection() {
		int connection_index = getSelectedConnectionIndex();
		if (connection_index == -1) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText(null);
			a.setContentText("Connection does not exist, use \"New Connection\" instead.");
			a.showAndWait();
			return;
		}

		Connection con = connections.get(connection_index);

		TextInputDialog dialog = new TextInputDialog("");
		GridPane grid = new GridPane();
		grid.setAlignment(null);
		grid.setPadding(new Insets(10));

		TextField nameField = new TextField(con.name);
		nameField.setEditable(false);
		TextField timeField = new TextField();

		grid.addRow(1, new Label("Name: "), nameField);
		grid.addRow(2, new Label("Time: "), timeField);

		dialog.getDialogPane().setContent(grid);
		dialog.setHeaderText(null);
		dialog.setTitle("Connection");
		boolean was_confirm = dialog.showAndWait().isPresent();
		if (!was_confirm) {
			return;
		}

		String connection_name = nameField.getText();
		int connection_time = -1;
		try {
			connection_time = Integer.parseInt(timeField.getText());
		} catch (Exception e) {
		}

		if (connection_name.length() == 0) {
			Alert a = new Alert(AlertType.ERROR, "Bad connection name. Cannot be empty.");
			a.setTitle("Error!");
			a.setHeaderText(null);
			a.showAndWait();
			return;
		}
		if (connection_time < 0) {
			Alert a = new Alert(AlertType.ERROR, "Bad connection time value. Must be a positive integer.");
			a.setTitle("Error!");
			a.setHeaderText(null);
			a.showAndWait();
			return;
		}

		con.name = connection_name;
		con.time = connection_time;
		unsavedChanges = true;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
