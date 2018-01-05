package Controllers;

import Models.ChordModel;
import Models.MainModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public HBox state_bar;

    public MenuBar menu_bar;
    public MenuItem menu_file_new;
    public MenuItem menu_file_save;
    public MenuItem menu_file_saveas;
    public MenuItem menu_file_quit;
    public MenuItem menu_edit_undo;
    public MenuItem menu_edit_redo;

    public Slider master_volume_slider;

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


        /* Assignation des actions de la barre des menus */
        menu_file_new.setOnAction(e -> System.out.println("New File"));
        menu_file_save.setOnAction(e -> System.out.println("Save"));
        menu_file_saveas.setOnAction(e -> System.out.println("Save as"));
        menu_file_quit.setOnAction(e -> System.out.println("Quit"));
        menu_edit_undo.setOnAction(e -> System.out.println("Undo"));
        menu_edit_redo.setOnAction(e -> System.out.println("Redo"));

        /* On charge la vue par default (track) */
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

    }

    private void initAll()
    {
        this.model.player.master_volume.bind(this.master_volume_slider.valueProperty());
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