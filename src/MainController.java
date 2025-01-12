import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    private ComboBox<String> outExtComboBox;

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

        fileSavedLabel.setVisible(false);
        outExtComboBox.setValue("txt");
        outExtComboBox.getItems().addAll("txt","xml","json","yaml");
        outExtComboBox.setOnAction(event ->{
            outputTextArea.clear();
            });

        extComboBox.setOnAction(event -> {
            String selectedItem = extComboBox.getValue();
            if (selectedItem != null) {
                updateComboBox2(selectedItem);
            }
        });
    }

    private void updateComboBox2(String selectedItem) 
    {
        outExtComboBox.getItems().clear();

        outputTextArea.clear();
        switch (selectedItem) 
        {
            case "txt":
            {
                outExtComboBox.setValue("txt");
                outExtComboBox.getItems().addAll("txt","json", "xml","yaml");
                break;
            }
            case "json":
            {
                outExtComboBox.setValue("json");
                outExtComboBox.getItems().addAll("txt","json");
                break;
            }
            case "xml":
            {
                outExtComboBox.setValue("xml");
                outExtComboBox.getItems().addAll("txt","xml");
                break;
            }
            case "yaml":
            {
                outExtComboBox.setValue("yaml");
                outExtComboBox.getItems().addAll("txt","yaml");
                break;
            }
            default:
                break;
        }

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
        String outChoice = outExtComboBox.getValue();

        if (inputText == null || inputText.trim().isEmpty()) 
        {
            outputTextArea.setText("-- !Please enter some input text to solve.");
            return;
        }

        switch (extChoice) 
        {
            case "txt":
            {
                result = TxtProcessor.processContent(inputText);
                switch (outChoice)
                {
                    case "txt":
                    {
                        outputTextArea.setText(result);
                        break;
                    }
                    case "json":
                    {
                        
                        File tempFileI = File.createTempFile("inputText", ".txt");
                        File tempFileO = File.createTempFile("outputText", ".tmp");
                        var jsonParser = new SingleLineTextToJsonConverter();

                        Files.writeString(tempFileI.toPath(), result);
                        jsonParser.setInputTextFile(tempFileI.toString());
                        jsonParser.setOutputJsonFile(tempFileO.toString());
                        jsonParser.processTextToJson();
                        
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
                    }
                    case "xml":
                    {
                        File tempFileI = File.createTempFile("inputText", ".tmp");
                        File tempFileO = File.createTempFile("outputText", ".tmp");
                        XmlProcessor xmlProc= new XmlProcessor();
    
                        Files.writeString(tempFileI.toPath(), result);
                        xmlProc.txtToXml(tempFileI.toString(),tempFileO.toString());
    
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
                    }
                    case "yaml":
                    {
                        File tempFileI = File.createTempFile("inputText", ".tmp");
                        File tempFileO = File.createTempFile("outputText", ".tmp");
                        var yamlParser= new YamlProcessor();
    
                        Files.writeString(tempFileI.toPath(), result);
                        yamlParser.txtToYaml(tempFileI.toString());
    
                        if (tempFileO!= null) 
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(tempFileI.toString().substring(0,tempFileI.toString().lastIndexOf('.'))+".yaml"))) 
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
                    }
                    default:   
                        break;
                }
                break;
            }
            case "xml":
            {
                switch (outChoice)
                {
                    case "txt":
                    {

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
                    }
                    case "xml":
                    {
                        File tempFileI = File.createTempFile("inputText", ".tmp");
                        XmlProcessor xmlProc= new XmlProcessor();
    
                        String tempFileO= tempFileI.toString().substring(0,tempFileI.toString().lastIndexOf('.'))+"-computed-.xml";
                        Files.writeString(tempFileI.toPath(), inputText);
                        xmlProc.compute(tempFileI.toPath().toString());
    
                        if (tempFileO!= null) 
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(tempFileI.toString().substring(0,tempFileI.toString().lastIndexOf('.'))+"-computed-.xml"))) 
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
                        break;
                    }
                        
                }
                break;
            }
            case "json":
            {

                File tempFileI = File.createTempFile("inputText", ".json");
                File tempFileP = File.createTempFile("parsedText", ".tmp");
                File tempFileO = File.createTempFile("outputText", ".tmp");
                var jsonParser = new JsonToSingleLineTextConverter();

                Files.writeString(tempFileI.toPath(), inputText);
                jsonParser.setInputJsonFile(tempFileI.toString());
                jsonParser.setOutputTextFile(tempFileP.toString());
                jsonParser.processJsonToText();

                var buffWriter = new BufferedWriter(new FileWriter(tempFileO.toString()));
                buffWriter.write(TxtProcessor.processContent(TxtProcessor.readFile(tempFileP.toString())));
                buffWriter.close();

                switch (outChoice)
                {
                    case "txt":
                    {

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
                        tempFileP.deleteOnExit();
                        break;
                    }
                    case "json":
                    {

                        var jsonParser2 = new SingleLineTextToJsonConverter();
                        jsonParser2.setInputTextFile(tempFileO.toString());
                        jsonParser2.setOutputJsonFile(tempFileI.toString());
                        jsonParser2.processTextToJson();
                        
                        if (tempFileI!= null) 
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(tempFileI))) 
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
                        tempFileP.deleteOnExit();
                        break;
                    }
                }

                break;
            }
            case "yaml":
            {
                switch (outChoice)                
                {
                    case "txt":
                    {
                        File tempFileI = File.createTempFile("inputText", ".tmp");
                        File tempFileO = File.createTempFile("outputText", ".tmp");
                        var yamlParser= new YamlProcessor();
    
                        Files.writeString(tempFileI.toPath(), inputText);
                        yamlParser.compute(tempFileI.toString());
                        yamlParser.toTxt(tempFileI.toString().substring(0,tempFileI.toString().lastIndexOf('.'))+"-computed-.yaml",tempFileO.toString()); 
                        
    
                        if (tempFileO!= null) 
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(tempFileO.toString()))) 
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
                    }
                    case "yaml":
                    {
                        
                        File tempFileI = File.createTempFile("inputText", ".tmp");
                        File tempFileO = File.createTempFile("outputText", ".tmp");
                        var yamlParser= new YamlProcessor();
    
                        Files.writeString(tempFileI.toPath(),inputText);
                        yamlParser.compute(tempFileI.toString());
    
                        if (tempFileO!= null) 
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(tempFileI.toString().substring(0,tempFileI.toString().lastIndexOf('.'))+"-computed-.yaml"))) 
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
                    }
                }
            }
                break;
        }

    }

    @FXML
    private void handleSaveResult() 
    {
        fileSavedLabel.setVisible(true);
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
    
    @FXML
    private void openArchivatorWindow() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Archivator.fxml"));
            Parent root = loader.load();

            Stage archivatorStage = new Stage();
            archivatorStage.setTitle("Archivator");
            archivatorStage.setScene(new Scene(root, 600, 1000));

            archivatorStage.setWidth(800); // Set explicit width
            archivatorStage.setHeight(600); // Set explicit height

            archivatorStage.show();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    @FXML
    private void openCryptatorWindow()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cryptator.fxml"));
            Parent root = loader.load();

            Stage cryptatorStage = new Stage();
            cryptatorStage.setTitle("Cryptator");
            cryptatorStage.setScene(new Scene(root, 600, 1000));
                
            cryptatorStage.show();
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }

    }
}

