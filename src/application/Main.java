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

        /*
        BorderPane workspace = (BorderPane) scene.lookup("#workspace_pane");

        MenuBar menubar = (MenuBar) scene.lookup("#menu_bar");
        Menu menu_file = new Menu("File");
        MenuItem menu_file_new = new MenuItem("New");
        MenuItem menu_file_save = new MenuItem("Save");
        MenuItem menu_file_quit = new MenuItem("Quit");

        menu_file_new.setOnAction(e -> System.out.println("New File"));
        menu_file_quit.setOnAction(e -> System.out.println("Quit"));

        menu_file.getItems().addAll(menu_file_new, menu_file_save, new SeparatorMenuItem(), menu_file_quit);
        menubar.getMenus().addAll(menu_file);

        VBox tracks = new VBox();
        tracks.getChildren().addAll(new Track("Track #1"), new Track("#Track #2"));

        SplitPane split_interface = new SplitPane();
        split_interface.setOrientation(Orientation.VERTICAL);
        split_interface.getItems().addAll(tracks, new BorderPane());
        workspace.setCenter(split_interface);
        */
    }


    public static void main(String[] args) {
        launch(args);
    }
}
