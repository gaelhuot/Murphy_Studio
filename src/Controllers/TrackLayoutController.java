package Controllers;

import Models.MainModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TrackLayoutController extends Controller implements Initializable {

    private MainModel model;

    private ArrayList<TrackController> tracks = new ArrayList<TrackController>();

    public ToolBar track_layout_button_bar;
    public Button addTrackBtn;
    public Button playTrackBtn;
    public VBox trackLayoutVBox;

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void initTracksLayout() {
        addTrackBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/track.fxml"));
                Pane track = fxmlLoader.load();

                TrackController trackController = fxmlLoader.getController();
                trackController.setModel(model);
                tracks.add(trackController);

                trackController.setName("Track #" + tracks.size());
                trackLayoutVBox.getChildren().add(track);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        playTrackBtn.setOnMouseClicked(event-> {
            tracks.get(0).setName("Test");
        });
    }

    public void setModel(MainModel model)
    {
        this.model = model;
        initTracksLayout();
    }


}
