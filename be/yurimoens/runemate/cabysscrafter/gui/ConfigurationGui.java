package be.yurimoens.runemate.cabysscrafter.gui;

import be.yurimoens.runemate.cabysscrafter.CAbyssCrafter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ConfigurationGui extends Stage {

    public ConfigurationGui(final CAbyssCrafter script) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setController(new ConfigurationGuiController(script, this));
            Parent root = loader.load(script.getClass().getResourceAsStream("/be/yurimoens/runemate/cabysscrafter/resources/Configuration.fxml"));
            Scene scene = new Scene(root);
            setTitle("CAbyssCrafter Configuration");
            setScene(scene);
        } catch (Exception e ) {
            script.guiReturnCode = 1;
        }

        setOnCloseRequest((event) -> script.guiReturnCode = 2);

        show();
    }

}
