package Controllers;

import Models.ChordModel;
import Models.MainModel;
import Objects.Player;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController extends Controller
{

    public BorderPane main_pane;
    public HBox state_bar;

    public MenuBar menu_bar;
    public MenuItem menu_file_new;
    public MenuItem menu_file_open;
    public MenuItem menu_file_save;
    public MenuItem menu_file_saveas;
    public MenuItem menu_file_quit;
    public MenuItem menu_edit_undo;
    public MenuItem menu_edit_redo;
    public MenuItem menu_view_set_light_theme;
    public MenuItem menu_view_set_dark_theme;

    public Slider master_volume_slider;
    public TextField sequencer_tempo;

    public AnchorPane secondContainer;
    public AnchorPane sideContainer;

    public SplitPane splitPaneHorizontal;
    public SplitPane splitPaneVertical;
    public MenuItem menu_help_about;
    public MenuItem menu_edit_options;

    private Stage popup;
    private Pane popup_container;

    private Scene scene;

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
        menu_file_new.setOnAction(e -> System.out.println("New"));
        menu_file_open.setOnAction(e -> loadFile());
        menu_file_save.setOnAction(e -> System.out.println("Save"));
        menu_file_saveas.setOnAction(e -> saveFile());
        menu_file_quit.setOnAction(e -> exit());
        menu_edit_undo.setOnAction(e -> System.out.println("Undo"));
        menu_edit_redo.setOnAction(e -> System.out.println("Redo"));
        menu_edit_options.setOnAction(e -> setPopup("options.fxml", 600, 400));
        menu_view_set_dark_theme.setOnAction(e -> {this.setDarkTheme();});
        menu_view_set_light_theme.setOnAction(e -> {this.setLightTheme();});
        menu_help_about.setOnAction(e -> setPopup("about.fxml", 360, 250));



        /* On charge la vue par defaut (piste_layout) */
        try {
            Pane piste_layout = loadView("piste_layout.fxml");
            mainContainer.getChildren().setAll(piste_layout);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.popup = new Stage();
        this.popup.initModality(Modality.APPLICATION_MODAL);
        this.popup.initOwner(Main.getPrimaryStage());
        this.popup_container = new Pane();

        Scene popup_scene = new Scene(popup_container);
        this.popup.setScene(popup_scene);
        this.popup.setResizable(false);

        sequencer_tempo.setText("120");
        model.player.sequencer.setTempoInBPM(120);

        /* ---- < Main Event Listener >  ---- */

        sequencer_tempo.textProperty().addListener((observable, oldValue, newValue) -> {
            // Only numeric
            boolean wasRunning = model.player.sequencer.isRunning();

            if ( wasRunning ) model.player.sequencer.stop();

            if ( newValue.isEmpty() || Objects.equals(newValue, "")) return;
            if ( !newValue.matches("\\d") )
            {
                String value = newValue.replaceAll("[^\\d]", "");
                sequencer_tempo.setText(value);
                model.player.setTempo(Integer.parseInt(value));
            }

            if ( wasRunning ) model.player.sequencer.start();
        });

        master_volume_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            model.player.setVolume(newValue.intValue());
        });

        this.master_volume_slider.setValue(75);


        /* ---- </ Main Event Listener > ---- */

        try {
            loadView("ChordSorter.fxml", secondContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            loadView("ChordMaker.fxml", sideContainer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPaneVertical.setDividerPosition(0,0.63);
        splitPaneHorizontal.setDividerPosition(0, 0.59);

    }

    private void loadView(String viewName, Pane container) throws IOException {
        /* On récupère la vue dans un pane */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + viewName));
        Node newPane = fxmlLoader.load();

        /* On charge le controller et on lui passe le model */
        Controller ctrl = fxmlLoader.getController();

        if(ctrl != null)
        {
            if ( ctrl.getClass() == ChordMakerController.class ) model.chordMakerController = (ChordMakerController) ctrl;
            if ( ctrl.getClass() == ChordSorterController.class ) model.chordSorterController = (ChordSorterController) ctrl;
            if ( ctrl.getClass() == PisteLayoutController.class ) model.pisteLayoutController = (PisteLayoutController) ctrl;
            ctrl.setModel(model);
        }

        /* On l'affiche dans le container */
        container.getChildren().setAll(newPane);
    }

    public void exit() {
        model.player.synthesizer.close();
        model.player.receiver.close();
        model.player.sequencer.close();
        System.exit(0);
    }

    private void loadFile(){
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Open midi File");
        File file = dialog.showOpenDialog(Main.getPrimaryStage());
    }

    private void saveFile(){
        FileChooser dialog = new FileChooser();
        dialog.setTitle("Save midi File");
        File file = dialog.showSaveDialog(Main.getPrimaryStage());
    }

    public void setDarkTheme()
    {
        //Besoin de retirer la classe dark si elle est déjà présente pour ne pas la dupliquer
        this.main_pane.getStyleClass().remove("light");
        this.main_pane.getStyleClass().remove("dark");
        this.main_pane.getStyleClass().add("dark");
        this.popup_container.getStyleClass().remove("light");
        this.popup_container.getStyleClass().remove("dark");
        this.popup_container.getStyleClass().add("dark");
    }

    public void setLightTheme()
    {
        this.main_pane.getStyleClass().remove("dark");
        this.main_pane.getStyleClass().remove("light");
        this.main_pane.getStyleClass().add("light");
        this.popup_container.getStyleClass().remove("dark");
        this.popup_container.getStyleClass().remove("light");
        this.popup_container.getStyleClass().add("light");
    }

    public void setPopup(String viewName, int width, int height)
    {
        String title = viewName.substring(0, 1).toUpperCase() + viewName.substring(1).replace(".fxml", "");
        this.popup.setTitle(title);

        try {
            loadView(viewName, popup_container);
        } catch (IOException e) {
            e.printStackTrace();
        }

        popup.setHeight(height+30);
        popup.setWidth(width);
        popup.show();
    }

    public void setKeyPressed(KeyCode code) {
        switch ( code )
        {
            case DELETE:
                model.chordSorterController.deleteSelected();
                break;
            case ADD:
                model.chordSorterController.isRandomTile = true;
                model.chordSorterController.createTile();
                break;
            case PAGE_DOWN:
                model.chordSorterController.previousTile();
                break;
            case PAGE_UP:
                model.chordSorterController.nextTile();
                break;
        }
    }
}