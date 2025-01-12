import javafx.fxml.FXML;                                                                                           
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;


public class CryptatorController
{

    @FXML
    private Button loadEncFile;
    @FXML
    private Button loadFileBut;
    @FXML
    private Button encButton;
    @FXML
    private Button decrButton;
    
    @FXML
    private Button loadPrivateButton;
    @FXML
    private Button loadPrivateButton2;
    
    @FXML
    private Button loadPublicButton;
    @FXML
    private Button loadPublicButton2;


    private File publicKeyFile1;
    private File publicKeyFile2;
    private File privateKeyFile1;
    private File privateKeyFile2;
    private File toEncFile;
    private File toDecrFile;

    

    @FXML
    public void initialize()
    {
        
        loadPrivateButton.setDisable(true);
        loadPublicButton.setDisable(true);
        loadPrivateButton2.setDisable(true);
        loadPublicButton2.setDisable(true);

    
    }

    @FXML
    private void handleEncryption() throws Exception
    {
        Encryptor.generateKeys(publicKeyFile1.toString(),privateKeyFile1.toString());
        Encryptor.encryptFile(toEncFile.toString(),toEncFile.toString()+".enc",publicKeyFile1.toString());
    }
    @FXML
    private void handleDecryption()throws Exception
    {
        Decryptor.decryptFile(toDecrFile.toString(),toDecrFile.toString().substring(0,toDecrFile.toString().length()-4),privateKeyFile2.toString());
    }
    @FXML  
    private void handleLoadEnc()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File To Decrypt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files","*.enc"));
        toDecrFile= fileChooser.showOpenDialog(null);

        loadPrivateButton2.setDisable(false);
        loadPublicButton2.setDisable(false);
    }

    @FXML
    private void handleLoadFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File to Encrypt");
        toEncFile= fileChooser.showOpenDialog(null);
        loadPrivateButton.setDisable(false);
        loadPublicButton.setDisable(false);
    }
    @FXML
    private void handleLoadPrK()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PrivateKeyFile");
        privateKeyFile1 = fileChooser.showOpenDialog(null);
    }

    @FXML
    private void handleLoadPrK2()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PrivateKeyFile");
        privateKeyFile2 = fileChooser.showOpenDialog(null);
    }
    @FXML
    private void handleLoadPuK()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PublicKeyFile");
        publicKeyFile1= fileChooser.showOpenDialog(null);
    }
    @FXML
    private void handleLoadPuK2()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PublicKeyFile");
        publicKeyFile2= fileChooser.showOpenDialog(null);
    }
}
