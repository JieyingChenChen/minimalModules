package no.siriuslabs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application starter class.
 */
public class MinimalModulesApplication extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinimalModulesApplication.class);

    private Stage stage;

    @Override
    public void start(Stage stage) {
        BasicConfigurator.configure();
        LOGGER.info("Starting MinimalModulesApplication");

        this.stage = stage;

        MainPanel rootPane = new MainPanel(this);
        Scene scene = new Scene(rootPane, 800, 600);

        stage.setTitle("Minimal Modules");
        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
