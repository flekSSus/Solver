import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;

public class MainController
{

    @FXML
    private ComboBox<String> extComboBox;

    @FXML
    private Label fileNameLabel; 

    @FXML
    private Label fileSavedLabel; 

    @FXML
    private TextArea outputTextArea;
    
    @FXML
    private TextArea inputTextArea;

    @FXML
    public void initialize() 
    {
        extComboBox.getItems().addAll("txt", "xml","json","yaml");
        extComboBox.setValue("txt");
    }

    @FXML
    private void handleComboBoxAction() 
    {
        String selectedItem = extComboBox.getValue();
        System.out.println("Selected Item: " + selectedItem);
    }

    @FXML
    private void handleLoadFile() 
    {
        FileChooser fileChooser = new FileChooser();

        String selectedItem = extComboBox.getValue();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files","*."+selectedItem));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) 
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) 
            {
                StringBuilder content = new StringBuilder();
                String line;
                
                fileNameLabel.setText(selectedFile.getName());
                while ((line = reader.readLine()) != null) 
                {
                    content.append(line).append("\n");
                }
                inputTextArea.setText(content.toString());
            } 
            catch (IOException e) 
            {
                inputTextArea.setText("Error reading file: " + e.getMessage());
            }
        }

        
    }


    @FXML
    private void handleSolve() throws Exception 
    {
        String inputText = inputTextArea.getText();
        String result; 
        String extChoice = extComboBox.getValue();

        if (inputText == null || inputText.trim().isEmpty()) 
        {
            outputTextArea.setText("-- !Please enter some input text to solve.");
            return;
        }

        switch (extChoice) 
        {
            case "txt":

                result = TxtProcessor.processContent(inputText);
                outputTextArea.setText(result);
                break;

            case "xml":

                File tempFileI = File.createTempFile("inputText", ".tmp");
                File tempFileO = File.createTempFile("outputText", ".tmp");
                XmlProcessor xmlProc= new XmlProcessor();

                Files.writeString(tempFileI.toPath(), inputText);
                xmlProc.compute(tempFileI.toPath().toString());
                xmlProc.toTxt(tempFileI.toPath().toString().substring(0,tempFileI.toPath().toString().lastIndexOf('.'))+"-computed-.xml",tempFileO.toPath().toString());

                if (tempFileO!= null) 
                {
                    try (BufferedReader reader = new BufferedReader(new FileReader(tempFileO))) 
                    {
                        StringBuilder content = new StringBuilder();
                        String line;
                        
                        while ((line = reader.readLine()) != null) 
                        {
                            content.append(line).append("\n");
                        }
                        outputTextArea.setText(content.toString());
                    } 
                    catch (IOException e) 
                    {
                        outputTextArea.setText("Error reading file: " + e.getMessage());
                    }
                }

                tempFileI.deleteOnExit();
                tempFileO.deleteOnExit();

                break;
            case "json":
                break;
            case "yaml":
                break;
        }

    }

    @FXML
    private void handleSaveResult() 
    {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Result As");

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) 
        {
            try (FileWriter writer = new FileWriter(selectedFile)) 
            {
                writer.write(outputTextArea.getText());
                fileSavedLabel.setText("Saved!");
            } 
            catch (IOException e) 
            {
                fileSavedLabel.setText("Error");
            }
        }
    }
}

