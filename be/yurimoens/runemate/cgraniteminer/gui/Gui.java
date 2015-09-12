package be.yurimoens.runemate.cgraniteminer.gui;

import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.script.framework.AbstractScript;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Gui extends Stage {

    public int returnCode;

    public Gui(String title, final AbstractScript script) {
        super(StageStyle.UNDECORATED);
        this.setTitle(title);
        JFrame frame = ClientUI.getFrame();
        initStyle(StageStyle.TRANSPARENT);

//        setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                returnCode = 2;
//            }
//        });

        setOnCloseRequest((event) -> returnCode = 2);

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                getIcons().add(SwingFXUtils.toFXImage((BufferedImage) frame.getIconImage(), null));
//                setX(frame.getX() + (frame.getWidth() / 2) - 290);
//                setY(frame.getY() + (frame.getHeight() / 2) - 176 + 40);
//                setScene(createScene());
//            }
//        });

        Platform.runLater(() -> {
            getIcons().add(SwingFXUtils.toFXImage((BufferedImage) frame.getIconImage(), null));
            setX(frame.getX() + (frame.getWidth() / 2) - 290);
            setY(frame.getY() + (frame.getHeight() / 2) - 176 + 40);
            setScene(createScene());
        });
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();

        Scene scene = new Scene(root);
        scene.setFill(null);
        root.setMinWidth(580);
        root.setMinHeight(350);

        FadeTransition ft = new FadeTransition(Duration.millis(500), root);
        ft.setFromValue(0.0f);
        ft.setToValue(1.0f);
        ft.play();

        return scene;
    }

}
