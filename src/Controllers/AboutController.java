package Controllers;

import javafx.scene.control.Hyperlink;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController extends Controller {

    public Hyperlink link_github;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        link_github.setOnAction(e -> {
            try {
                new ProcessBuilder("x-www-browser", "https://github.com/Yarhi/Murphy_Studio").start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
}
