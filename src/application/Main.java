package application;

import Objects.Track;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        //TODO charger les vues suivantes :
        // mainContainer -> split pane
        // split pane top -> track_layout.fxml
        // track_layout_vbox -> track.fxml
        // split pane bottom -> chords.fxml (apres avoir retire le surperflu)

    }


    public static void main(String[] args) {
        launch(args);
    }
}
