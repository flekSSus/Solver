import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

public class ArchivatorController 
{

    @FXML
    private Button loadZipBut;
    @FXML
    private Button loadUnzipBut;
    @FXML
    private Button zipButton;
    @FXML
    private Button unzipButton;
    @FXML
    private Label zipName1;
    @FXML
    private Label zipOut1;
    @FXML
    private Label zipOut2;
    @FXML
    private Label zipName2;

    @FXML
    public void initialize()
    {
        zipName1.setVisible(false);
        zipName2.setVisible(false);
        zipOut1.setVisible(false);
        zipOut2.setVisible(false);
    }

    @FXML
    private void handleLoadZip()
    {
        FileChooser fileChooser = new FileChooser();
 
        fileChooser.setTitle("Open Zip File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files","*."+"zip"));

        File selectedFile = fileChooser.showOpenDialog(null);
        
        zipName2.setText(selectedFile.getName());
        zipName2.setVisible(true);
        
    }
    
    @FXML
    private void handleMakeZip()
    {
        FileChooser fileChooser = new FileChooser();
 
        fileChooser.setTitle("Open File To Zip");

        File selectedFile = fileChooser.showOpenDialog(null);
        
        zipName1.setText(selectedFile.getName());
        zipName1.setVisible(true);
        
    }

    @FXML
    private void handleZip() throws Exception
    {
        var zip= new ZipArchiver();
        zip.zipFiles(zipName1.getText(),"zip.txt");
        zipOut1.setText("zip.txt");
        zipOut1.setVisible(true);
        
    }
}
