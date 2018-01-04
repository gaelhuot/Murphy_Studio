package Controllers;

import Models.ChordModel;
import Models.MainModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public MenuBar menu_bar;
    private MainModel model;

    private Scene scene;

    /* Elements qui permettent de changer de page */
    @FXML
    private Button chordGridView, changeVue;
    private HashMap<Button, String> views = new HashMap<Button, String>();

    /* Container */
    @FXML
    private BorderPane workspace_pane;
    @FXML
    private Pane mainContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* On initialise le model "principal" */
        this.model = new MainModel();

        /* On initialise les models secondaires */
        this.model.chordModel = new ChordModel();

        Menu menu_file = new Menu("File");
        MenuItem menu_file_new = new MenuItem("New");
        MenuItem menu_file_save = new MenuItem("Save");
        MenuItem menu_file_quit = new MenuItem("Quit");

        menu_file_new.setOnAction(e -> System.out.println("New File"));
        menu_file_quit.setOnAction(e -> System.out.println("Quit"));

        menu_file.getItems().addAll(menu_file_new, menu_file_save, new SeparatorMenuItem(), menu_file_quit);
        menu_bar.getMenus().addAll(menu_file);

        try {
            loadView("track.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }





        /* On ajoute les différentes pages qu'on lie à chacun des buttons */
        views.put(chordGridView, "chords.fxml");

        for (Map.Entry<Button, String> entry: views.entrySet())
        {
            Button fxmlID = entry.getKey();
            String viewName = entry.getValue();

            /* Pour chaque button, on créé un event qui va permettre de charger la page correspondante */
            fxmlID.setOnMouseClicked(event -> {
                try {
                    loadView(viewName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

//        Slider slider_master_volume = (Slider) mainContainer.lookup("#slider_master_volume");
//        model.player.master_volume.bind(slider_master_volume.valueProperty());
    }

    private void loadView(String viewName) throws IOException {
        /* On récupère la vue dans un pane */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + viewName));
        Pane newPane = fxmlLoader.load();

        /* On charge le controller et on lui passse le model */
        Controller ctrl = fxmlLoader.getController();
        ctrl.setModel(model);

        /* On l'affiche dans le container */
        mainContainer.getChildren().setAll(newPane);
    }
}


