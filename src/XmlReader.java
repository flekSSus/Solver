import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;                                                                    
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*; 

class XmlReader
{
    private String fileInputName="no-info"; 
    private String fileOutputName="no-info";

    public void setInputName(String fileInputNameParametr)
    {
        fileInputName=fileInputNameParametr;
    }
    public void setOutputName(String fileOutputNameParametr)
    {
        fileOutputName=fileOutputNameParametr;
    }

    XmlReader(String fileInputNameParametr, String fileOutputNameParametr)
    {
        fileInputName=fileInputNameParametr;
        fileOutputName=fileOutputNameParametr;
    }

    public void toText()
    {
        try(var buffWriter=new BufferedWriter(new FileWriter(fileOutputName)))
        {
            
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document doc=builder.parse(new File(fileInputName));
            doc.getDocumentElement().normalize();

            NodeList nodeList=doc.getElementsByTagName("*");

            Element el=(Element)nodeList.item(0);
            buffWriter.write(el.getTextContent().replace(" ","").replace("\n",""));
    
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
}
