import org.yaml.snakeyaml.Yaml;
import java.io.*;

class YamlProcessor
{
    public void compute(String fileInputPath) throws Exception
    {
        File newFile = new File(fileInputPath.substring(0,fileInputPath.lastIndexOf('.'))+"-computed-.yaml");
        newFile.createNewFile();
        var buffWr = new BufferedWriter(new FileWriter(newFile));
        buffWr.write(TxtProcessor.processContent(TxtProcessor.readFile(fileInputPath)));

        buffWr.close();
    }
    
    public void toTxt(String fileInputPath, String fileOutputPath) throws Exception
    {
        File newFile = new File(fileInputPath.substring(0,fileInputPath.lastIndexOf('.'))+"-computed-.yaml");
        var buffR = new BufferedReader(new FileReader(fileInputPath));
        var buffW = new BufferedWriter(new FileWriter(fileOutputPath));

        String line;
        while((line=buffR.readLine()) != null)
        {
            buffW.write(line+'\n');
        }

        buffR.close();
        buffW.close();

    }

    
}
