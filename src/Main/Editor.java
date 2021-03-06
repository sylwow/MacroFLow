package Main;

import Instructions.Macro;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.*;

public class Editor {
    static Editor editor = new Editor();
    File actualFile = null;
    @FXML
    TextArea messages;
    @FXML
    TextArea editArea;
    @FXML
    Button saver;
    @FXML
    Button loader;
    @FXML
    public Label MacroName;

    private Editor() {
        System.out.println("editor");
    }

    // menu
    public void newMacro() {
        clearMsg();
        if (actualFile != null) {
            saveActual();
        }
        editArea.setText("");
        saveNewFile();
    }

    public void saverSave() {
        clearMsg();
        if (actualFile == null) {
            saveNewFile();
        } else {
            saveActual();
        }
    }

    public void saveAs() {
        clearMsg();
        saveNewFile();
    }

    public void changeToMain() {
        Controller.map.activate(Controller.Scenes.main);
    }

    public void openMacro() {
        clearMsg();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Macro File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MCR files (*.mcr)", "*.mcr");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(Controller.primaryStage);
        openMacro(file);
    }

    public void openMacro(File file) {
        if (file != null) {
            try (FileReader fileStream = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileStream)) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                editArea.setText(sb.toString());
                Main.main.macrosManager.addNewMacro(file);
            } catch (IOException ex) {
                messages.setText(String.format("Could Not Open File %s, Error: %s", file.getName(), ex.getMessage()));
            }
            actualFile = file;
            MacroName.setText(file.getName());
        }
    }

    public void close() {
        clearMsg();
        if (actualFile != null) {
            saveActual();
            editArea.setText("");
            MacroName.setText("");
            actualFile = null;
        }
    }


    public void helpInstructions() {
        Main.main.helpInstructions();
    }

    private void saveNewFile() {
        clearMsg();
        FileChooser fileChooser = new FileChooser();
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MCR files (*.mcr)", "*.mcr");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(Controller.primaryStage);

        if (file != null) {
            saveTextToFile(editArea.getText(), file);
            Main.main.macrosManager.addNewMacro(file);
            // Main.main.
        }
    }

    private void clearMsg() {
        messages.setText("");
    }

    //end menu
    // load button

    public void validate() {
        clearMsg();
        try {
            if (!actualFile.exists())
                throw new Exception(String.format("Actual macro file '%s' does not exist", actualFile.getName()));
            Macro macro = new Macro(actualFile, null, null, null);
            String msg = macro.readMacro(editArea.getText());
            if (msg == null)
                messages.setText("Macro validated");
            else
                messages.setText(msg);
        } catch (Exception e) {
            messages.setText(e.getMessage());
        }
    }

    //end load button
    private void saveTextToFile(String content, File file) {
        clearMsg();
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.print(content);
            writer.close();
        } catch (IOException ex) {
            messages.setText(String.format("Could Not Save File: Error %s", ex.getMessage()));
        }
    }

    private void saveActual() {
        clearMsg();
        saveTextToFile(editArea.getText(), actualFile);
    }
}
