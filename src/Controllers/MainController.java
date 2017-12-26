package Controllers;

import Models.MainModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private MainModel model;

    /* Elements qui permettent de changer de page */
    @FXML
    private Button chordsPage, page2;
    private HashMap<Button, String> views = new HashMap<Button, String>();

    /* Container */
    @FXML
    private Pane mainContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /* On initialise le model "principal" */
        this.model = new MainModel();

        /* On ajoute les différentes pages qu'on lie à chacun des buttons */
        views.put(chordsPage, "chords.fxml");
        views.put(page2, "page.fxml");

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

    private void loadView(String viewName) throws IOException {
        /* On récupère la vue dans un pane */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + viewName));
        Pane newPane = fxmlLoader.load();

        /* On charge le controller et on lui passse le model */
        Chords chordsController = fxmlLoader.getController();
        chordsController.setModel(model);

        /* On l'affiche dans le container */
        mainContainer.getChildren().setAll(newPane);
    }

}


