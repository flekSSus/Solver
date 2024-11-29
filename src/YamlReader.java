import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.util.Map;

class YamlReader
{
    
    private String fileInputName="no-info";
    private String fileOutputName="no-info";

    YamlReader(){}

    public void setInputName(String fileInputNameParam)
    {
        fileInputName=fileInputNameParam;
    }

    public void setOutputName(String fileOutputNameParam)
    {
        fileOutputName=fileOutputNameParam;
    }

    public void toText()
    {
        Yaml yamlParser=new Yaml();    

        try (var fis = new FileInputStream(fileInputName);var reader = new BufferedReader(new InputStreamReader(fis));var buffWriter=new BufferedWriter(new FileWriter(fileOutputName)))
        {
            Map<String, Object> data = yamlParser.load(reader); 
            MapToText(data, buffWriter);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    private void MapToText(Map<String,Object> map, BufferedWriter buffWr)throws IOException
    {
        for(var value: map.entrySet())
        {
            if(value.getValue()==null)
                continue;
            if(value.getValue() instanceof Map)
            {
                MapToText((Map<String,Object>) value.getValue(),buffWr);
            }
            buffWr.write(value.getValue().toString());
        }
    }


}



