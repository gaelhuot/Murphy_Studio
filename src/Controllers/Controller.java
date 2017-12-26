package Controllers;

import Models.MainModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private MainModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void setModel(MainModel model) { this.model = model; }

}
