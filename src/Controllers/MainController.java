package Controllers;

import Models.ChordModel;
import Models.MainModel;
import Objects.Accord;
import Objects.Player;
import Objects.CustomReceiver;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController extends Controller
{

    public HBox state_bar;

    public MenuBar menu_bar;
    public MenuItem menu_file_new;
    public MenuItem menu_file_save;
    public MenuItem menu_file_saveas;
    public MenuItem menu_file_quit;
    public MenuItem menu_edit_undo;
    public MenuItem menu_edit_redo;

    public Slider master_volume_slider;
    public TextField sequencer_tempo;

    public AnchorPane secondContainer;
    public SplitPane split_workspace;

    public Button chordMakerView;
    public Button chordSorterView;
    public Button tracksView;


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

        try {
            this.model.player = new Player();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }

        /* On initialise les models secondaires */
        this.model.chordModel = new ChordModel();


        /* Assignation des actions de la barre des menus */
        menu_file_new.setOnAction(e -> System.out.println("New File"));
        menu_file_save.setOnAction(e -> System.out.println("Save"));
        menu_file_saveas.setOnAction(e -> System.out.println("Save as"));
        menu_file_quit.setOnAction(e -> System.out.println("Quit"));
        menu_edit_undo.setOnAction(e -> System.out.println("Undo"));
        menu_edit_redo.setOnAction(e -> System.out.println("Redo"));

        /* On charge la vue par defaut (piste_layout) */
        try {
            Pane piste_layout = loadView("piste_layout.fxml");
            mainContainer.getChildren().setAll(piste_layout);
        } catch (IOException e) {
            e.printStackTrace();
        }


        sequencer_tempo.setText("120");
        model.player.sequencer.setTempoInBPM(120);

        /* ---- < Main Event Listener >  ---- */

        sequencer_tempo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Only numeric
            if ( newValue.isEmpty() || Objects.equals(newValue, "")) return;
            if ( !newValue.matches("\\d") )
            {
                String value = newValue.replaceAll("[^\\d]", "");
                sequencer_tempo.setText(value);
                model.player.sequencer.setTempoInBPM(Float.parseFloat(value));
            }
        });

        this.model.player.master_volume.bind(this.master_volume_slider.valueProperty());

        master_volume_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Et ça marche paaas !
            double gain = ((Double) newValue / 100);
            CustomReceiver receiver = (CustomReceiver) model.player.receiver;
            receiver.channel.controlChange(7, (int) (gain * 127.0));

            //model.player.midiChannel.controlChange(7, (Integer) newValue);
            System.out.println("New value : " + newValue + " (" + newValue.getClass().toString() + ")");
            System.out.println("Model.player.master_volume : " + gain);
        });

        /* ---- </ Main Event Listener > ---- */

        views.put(tracksView, "piste_layout.fxml");
        views.put(chordMakerView, "ChordMaker.fxml");
        views.put(chordSorterView, "ChordSorter.fxml");
    /* On ajoute les différentes pages qu'on lie à chacun des buttons */
        for (Map.Entry<Button, String> entry: views.entrySet())
        {
            Button fxmlID = entry.getKey();
            String viewName = entry.getValue();

            /* Pour chaque button, on créé un event qui va permettre de charger la page correspondante */
            fxmlID.setOnMouseClicked(event -> {
                try {
                    Pane container = ( fxmlID == tracksView || fxmlID == chordSorterView ) ? this.mainContainer : this.secondContainer;
                    loadView(viewName, container);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            loadView("ChordMaker.fxml", secondContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadView(String viewName, Pane container) throws IOException {
        /* On récupère la vue dans un pane */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + viewName));
        Node newPane = fxmlLoader.load();


        /* On charge le controller et on lui passse le model */
        Controller ctrl = fxmlLoader.getController();

        System.out.println("Ok");
        ctrl.setModel(model);

        /* On l'affiche dans le container */
        container.getChildren().setAll(newPane);
    }

    public void exit() {
        model.player.synthesizer.close();
        model.player.receiver.close();
        model.player.sequencer.close();
    }
}