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

    private File toZipFile;
    private File toUnZipFile;

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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files","*.zip"));
        toUnZipFile= fileChooser.showOpenDialog(null);
        
        zipName2.setText(toUnZipFile.getName());
        zipName2.setVisible(true);
        
    }
    
    @FXML
    private void handleLoadFile() throws Exception
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File To Zip");
        toZipFile= fileChooser.showOpenDialog(null);
        
        zipName1.setText(toZipFile.getName());
        zipName1.setVisible(true);
        
    }

    @FXML
    private void handleZip() throws Exception
    {
        var zip= new ZipArchiver();
        zip.zipSingleFile(toZipFile.toString(),toZipFile.toString()+".zip");
        zipOut1.setText(toZipFile.getName()+".zip");
        zipOut1.setVisible(true);
        
    }

    @FXML
    private void handleUnzip() throws Exception
    {
        var zip= new ZipExtractor();
        zip.unzip(zipName2.getText(),toUnZipFile.toString().substring(0,toUnZipFile.toString().length()-toUnZipFile.getName().length()));
        zipOut2.setText(toUnZipFile.getName());
        zipOut2.setVisible(true);
    }
}
