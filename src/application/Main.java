package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../views/main_layout.fxml"));
        Rectangle2D bounds = Screen.getPrimary().getBounds();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(root.minWidth(-1));
        primaryStage.setMinHeight(root.minHeight(-1));
        primaryStage.setX((bounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((bounds.getHeight() - primaryStage.getHeight()) / 4);
        primaryStage.show();

        BorderPane workspace = (BorderPane) scene.lookup("#workspace_pane");
        SplitPane split_interface = new SplitPane();
        split_interface.setOrientation(Orientation.VERTICAL);
        BorderPane bp1 = new BorderPane();
        BorderPane bp2 = new BorderPane();
        bp1.setTop(new Slider());
        bp2.setBottom(new Slider());
        bp1.setCenter(new Button());
        bp2.setTop(new Button());
        split_interface.getItems().addAll(bp1, bp2);
        workspace.setCenter(split_interface);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
