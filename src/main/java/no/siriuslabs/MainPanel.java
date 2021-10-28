package no.siriuslabs;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import no.siriuslabs.computationapi.implementation.util.ShellHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbee.javafx.scene.layout.MigPane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX UI class.
 */
public class MainPanel extends BorderPane {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainPanel.class);

    private final MinimalModulesApplication application;

    /**
     * Selected ontology file
     */
    private File ontologyFile = null;
    /**
     * Selected signature file
     */
    private File signatureFile = null;
    /**
     * Selected output directory
     */
    private File outputDirectory = null;

    private Text ontologyFileLabel;
    private TextField ontologyFileTextField;
    private Button ontologyFileChooseButton;

    private Text signatureFileLabel;
    private TextField signatureFileTextField;
    private Button signatureFileChooseButton;

    private Text outputDirLabel;
    private TextField outputDirTextField;
    private Button outputDirChooseButton;

    private Button action1Button;
    private Button action2Button;

    private Text outputLabel;
    private TextArea outputTextArea;

    private Button clearButton;

    public MainPanel(MinimalModulesApplication application) {
        this.application = application;
        initialize();
    }

    private void initialize() {
        initComponents();
        initLayout();
    }

    private void initComponents() {
        LOGGER.info("Building UI components");
        ontologyFileLabel = new Text("Ontology File:");

        ontologyFileTextField = new TextField();
        ontologyFileTextField.setEditable(false);
        ontologyFileTextField.setDisable(true);

        ontologyFileChooseButton = new Button("...");
        ontologyFileChooseButton.setOnAction(this::handleOntologyFileSelection);

        signatureFileLabel = new Text("Signature File:");

        signatureFileTextField = new TextField();
        signatureFileTextField.setEditable(false);
        signatureFileTextField.setDisable(true);

        signatureFileChooseButton = new Button("...");
        signatureFileChooseButton.setOnAction(this::handleSignatureFileSelection);

        outputDirLabel = new Text("Output Directory (optional):");

        outputDirTextField = new TextField();
        outputDirTextField.setEditable(false);
        outputDirTextField.setDisable(true);

        outputDirChooseButton = new Button("...");
        outputDirChooseButton.setOnAction(this::handleOutputDirSelection);

        action1Button = new Button("Run Single Module");
        action1Button.setOnAction(this::handleAction1);

        action2Button = new Button("Run All Modules");
        action2Button.setOnAction(this::handleAction2);

        outputLabel = new Text("Output:");

        outputTextArea = new TextArea();
        outputTextArea.setPrefColumnCount(40);
        outputTextArea.setEditable(false);

        clearButton = new Button("Clear");
        clearButton.setOnAction(this::handleClearAction);
    }

    private void initLayout() {
        LOGGER.info("Building layout");
        MigPane migPane = new MigPane("insets 20", "[] [grow] [fill]", "[][][][30px][][30px][][grow][]");

        migPane.add(ontologyFileLabel, "cell 0 0,alignx left,aligny baseline") ;
        migPane.add(ontologyFileTextField, "cell 1 0,growx,aligny baseline");
        migPane.add(ontologyFileChooseButton, "cell 2 0, aligny baseline");

        migPane.add(signatureFileLabel, "cell 0 1,alignx left,aligny baseline") ;
        migPane.add(signatureFileTextField, "cell 1 1,growx,aligny baseline");
        migPane.add(signatureFileChooseButton, "cell 2 1, aligny baseline");

        migPane.add(outputDirLabel, "cell 0 2,alignx left,aligny baseline") ;
        migPane.add(outputDirTextField, "cell 1 2,growx,aligny baseline");
        migPane.add(outputDirChooseButton, "cell 2 2, aligny baseline");

        migPane.add(action1Button, "cell 1 4 2 1, sizegroupx 1,sizegroupy 1,growx,aligny baseline");
        migPane.add(action2Button, "cell 1 4 2 1, sizegroupx 1,sizegroupy 1,growx,aligny baseline");

        migPane.add(outputLabel, "cell 0 6,alignx left,aligny baseline");
        migPane.add(outputTextArea, "cell 0 7 3 1, growx,growy, alignx left, aligny baseline");

        migPane.add(clearButton, "cell 2 8, alignx right, aligny baseline");

        setCenter(migPane);
    }

    /**
     * React on ontology button click
     */
    private void handleOntologyFileSelection(ActionEvent actionEvent) {
        LOGGER.info("handleOntologyFileSelection called");
        File file = handleFileSelection(FileType.ONTOLOGY);
        if(file != null) {
            LOGGER.info("Ontology file chosen: {}", file.getAbsolutePath());
            ontologyFile = file;
            ontologyFileTextField.setText(ontologyFile.getAbsolutePath());
        }
    }

    /**
     * React on signature button click
     */
    private void handleSignatureFileSelection(ActionEvent actionEvent) {
        LOGGER.info("handleSignatureFileSelection called");
        File file = handleFileSelection(FileType.SIGNATURE);
        if(file != null) {
            LOGGER.info("Signature file chosen: {}", file.getAbsolutePath());
            signatureFile = file;
            signatureFileTextField.setText(signatureFile.getAbsolutePath());
        }
    }

    /**
     * Show file chooser in the right configuration
     */
    private File handleFileSelection(FileType fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose " + fileType.getName() + " File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType.getShortName(), fileType.getFileExtension()));
        fileChooser.setInitialDirectory(new File("."));

        return fileChooser.showOpenDialog(application.getStage());
    }

    /**
     * React on output directory button click
     */
    private void handleOutputDirSelection(ActionEvent actionEvent) {
        LOGGER.info("handleOutputDirSelection called");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Output Directory");
        directoryChooser.setInitialDirectory(new File("."));

        File file = directoryChooser.showDialog(application.getStage());
        if(file != null) {
            LOGGER.info("Output Directory chosen: {}", file.getAbsolutePath());
            outputDirectory = file;
            outputDirTextField.setText(file.getAbsolutePath());
        }
    }

    /**
     * React on run button #1 click
     */
    private void handleAction1(ActionEvent actionEvent) {
        LOGGER.info("handleAction1 called");
        String command = "java";
        List<String> parameters = new ArrayList<>(List.of("-jar", "singleMinModule.jar", "--ontology", ontologyFile.getAbsolutePath(), "--sig", signatureFile.getAbsolutePath()));
        if(outputDirectory != null) {
            parameters.add("--output");
            parameters.add(outputDirectory.getAbsolutePath() + File.separator);
        }
        runCommand(command, parameters);
    }

    /**
     * React on run button #2 click
     */
    private void handleAction2(ActionEvent actionEvent) {
        LOGGER.info("handleAction2 called");
        String command = "java";
        List<String> parameters = new ArrayList<>(List.of("-jar", "allMinModules.jar", "--ontology", ontologyFile.getAbsolutePath(), "--sig", signatureFile.getAbsolutePath()));
        if(outputDirectory != null) {
            parameters.add("--output");
            parameters.add(outputDirectory.getAbsolutePath() + File.separator);
        }
        runCommand(command, parameters);
    }

    /**
     * Lock the UI and start the command line call given in the parameters
     */
    private void runCommand(String command, List<String> parameters) {
        disableUi(true);
        clearButton.setDisable(true);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                String result = ShellHelper.runShellCommand(command, parameters);
                outputTextArea.setText(result);
                clearButton.setDisable(false);
                return null;
            }
        };
        new Thread(task).start();
    }

    /**
     * React on clear button click
     */
    private void handleClearAction(ActionEvent actionEvent) {
        ontologyFile = null;
        signatureFile = null;
        outputDirectory = null;
        ontologyFileTextField.setText("");
        signatureFileTextField.setText("");
        outputDirTextField.setText("");
        outputTextArea.setText("");
        disableUi(false);
    }

    /**
     * Change the disabled state of the UI
     */
    private void disableUi(boolean disable) {
        ontologyFileChooseButton.setDisable(disable);
        signatureFileChooseButton.setDisable(disable);
        outputDirChooseButton.setDisable(disable);
        action1Button.setDisable(disable);
        action2Button.setDisable(disable);
    }

}
