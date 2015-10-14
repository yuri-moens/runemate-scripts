package be.yurimoens.runemate.cabysscrafter.gui;

import be.yurimoens.runemate.cabysscrafter.CAbyssCrafter;
import be.yurimoens.runemate.cabysscrafter.RuneType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

class ConfigurationGuiController implements Initializable {

    @FXML
    private ChoiceBox<RuneType> cbxRuneType;

    @FXML
    private ChoiceBox<String> cbxBankPreset;

    @FXML
    private ToggleGroup finishConditions;

    @FXML
    private RadioButton btnRunes;

    @FXML
    private RadioButton btnTime;

    @FXML
    private RadioButton btnResources;

    @FXML
    private TextField txtRunes;

    @FXML
    private TextField txtHours;

    @FXML
    private TextField txtMinutes;

    @FXML
    private ListView<String> lstIgnoreObstacles;

    @FXML
    private Button btnStart;

    private final CAbyssCrafter script;
    private final Stage stage;

    public ConfigurationGuiController(CAbyssCrafter script, Stage stage) {
        this.script = script;
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbxRuneType.getItems().addAll(RuneType.values());
        cbxRuneType.getSelectionModel().select(RuneType.BLOOD);

        cbxBankPreset.getItems().add("1");
        cbxBankPreset.getItems().add("2");
        cbxBankPreset.getSelectionModel().selectFirst();

        txtRunes.setDisable(true);
        txtHours.setDisable(true);
        txtMinutes.setDisable(true);

        btnRunes.setOnAction((event) -> {
            txtRunes.setDisable(false);
            txtHours.setDisable(true);
            txtMinutes.setDisable(true);
        });

        btnTime.setOnAction((event) -> {
            txtRunes.setDisable(true);
            txtHours.setDisable(false);
            txtMinutes.setDisable(false);
        });

        btnResources.setOnAction((event) -> {
            txtRunes.setDisable(true);
            txtHours.setDisable(true);
            txtMinutes.setDisable(true);
        });

        btnResources.setSelected(true);

        lstIgnoreObstacles.getItems().add("Gap");
        lstIgnoreObstacles.getItems().add("Rock");
        lstIgnoreObstacles.getItems().add("Tendrils");
        lstIgnoreObstacles.getItems().add("Eyes");
        lstIgnoreObstacles.getItems().add("Boil");
        lstIgnoreObstacles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        btnStart.setOnAction(btnStartAction());
    }

    private EventHandler<ActionEvent> btnStartAction() {
        return event -> {
            script.runeType = cbxRuneType.getSelectionModel().getSelectedItem();

            script.bankPreset = Integer.parseInt(cbxBankPreset.getSelectionModel().getSelectedItem());

            if (finishConditions.getSelectedToggle() == btnRunes) {
                try {
                    script.runesToCraft = Integer.parseInt(txtRunes.getText());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, ignoring stop condition.");
                }
            } else if (finishConditions.getSelectedToggle() == btnTime) {
                try {
                    int hours = Integer.parseInt(txtHours.getText());
                    int minutes = Integer.parseInt(txtMinutes.getText());

                    script.timeToRun = hours * 3600000 + minutes * 60000;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number, ignoring stop condition.");
                }
            }

            List<String> ignoreObstacles = lstIgnoreObstacles.getSelectionModel().getSelectedItems();
            script.ignoreObstacles = new String[ignoreObstacles.size()];
            ignoreObstacles.toArray(script.ignoreObstacles);

            stage.hide();
            script.guiReturnCode = 0;
        };
    }
}
