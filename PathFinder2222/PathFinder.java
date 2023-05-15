import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PathFinder extends Application {
	@Override
	public void start(Stage primaryStage) {
		Pane root = new Pane();
		Label label = new Label("Hello World!");
		root.getChildren().add(label);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	} // start

	public static void main(String[] args) {
		Application.launch(args);
	}
}