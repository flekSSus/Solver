import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;

class XmlProcessor
{
    private String fileInputName;
    private String fileOutputName;

    public void setInputName(String fileNameParam)
    {
        fileInputName=fileNameParam;
    }
    public void setOutputName(String fileNameParam)
    {
        fileOutputName=fileNameParam;
    }

    public XmlProcessor()
    {
        fileInputName="no-info";
        fileOutputName="no-info";
    }
    public XmlProcessor(String fileInputNameParam, String fileOutputNameParam)
    {
        fileInputName=fileInputNameParam;
        fileOutputName=fileOutputNameParam;
    }

    public void compute() throws Exception
    {

        TxtProcessor  romaSosal= new TxtProcessor();

        try(var outWriter = new BufferedWriter(new FileWriter(fileOutputName)))
        {
            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            DocumentBuilder builder=factory.newDocumentBuilder();
            Document doc=builder.parse(new File(fileInputName));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("*");

           //translate in txt 
            var bWr= new BufferedWriter(new FileWriter("textInput.txt"));
            bWr.write(list.item(0).getTextContent());
            bWr.close();
            
            //compute and write in output
            var bR= new BufferedReader(new FileReader("textInput.txt"));
            String tmpStr=null;
            for(int i=0;i<list.getLength();++i)
            {
                tmpStr=bR.readLine();
                if(tmpStr!=null)
                {
                    outWriter.write(list.item(i).getNodeName()+": "+romaSosal.processContent(tmpStr));
                    outWriter.write('\n');
                }
            }
            bR.close();

            

        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("pizdez");
        }

    }

}
