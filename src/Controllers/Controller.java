package Controllers;

import Models.MainModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private MainModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void setModel(MainModel model) { this.model = model; }

    protected Pane loadView(String viewName) throws IOException {
        /* On récupère la vue dans un pane */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + viewName));
        Pane newPane = fxmlLoader.load();

        /* On charge le controller et on lui passse le model */
        Controller ctrl = fxmlLoader.getController();
        ctrl.setModel(model);

        return newPane;
    }

}
