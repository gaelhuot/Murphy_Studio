package Controllers;

import Models.MainModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Chords implements Initializable {

    private MainModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            /!\ ATTENTION /!\
            Ici le model n'est pas encore chargé.
            La méthode initialize est appelée lorsque l'on fait FXMLLoader.load(); (CF application.Controller - @loadView() )
         */
    }

    public void setModel(MainModel model) { this.model = model; }
}