import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import static org.junit.Assert.*;

public class XmlProcessorTest 
{

    @Test
    public void testXmlProcessor() throws Exception
    {
        
        var xml= new XmlProcessor();
        String input= "<catalog><book><title>Java (3+3) Programming</title><author><a>uiua</a></author><price>29.99</price></book><book><title>Programming</title><author>Doe</author><price>2.9</price></book></catalog>";

        Path inputPath =Files.createTempFile("11-input",".xml");
        Path outputPath = Files.createTempFile("11-output",".txt");
        Files.writeString(inputPath,input);

        xml.toTxt(inputPath.toString(),outputPath.toString());
        String result= new String("catalog:\n   book:\n      title:\n         Java (3+3) Programming\n      author:\n         a:\n            uiua\n      price:\n         29.99\n   book:\n      title:\n         Programming\n      author:\n         Doe\n      price:\n         2.9\n");

        String content = new String(Files.readAllBytes(outputPath));
        assertEquals(result,content);

        Files.deleteIfExists(inputPath);
        Files.deleteIfExists(outputPath);
    }

}
