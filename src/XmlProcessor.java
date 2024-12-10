import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;

class XmlProcessor
{

    public void compute(String inputFilePath) throws Exception
    {

        File newFile = new File(inputFilePath.substring(0,inputFilePath.lastIndexOf('.'))+"-computed-.xml");
        newFile.createNewFile();
        var buffWr = new BufferedWriter(new FileWriter(newFile));
        buffWr.write(TxtProcessor.processContent(TxtProcessor.readFile(inputFilePath)));

        buffWr.close();
    }

    public void toTxt(String inputFilePath, String outputFilePath)throws Exception
    {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(inputFilePath));
        doc.getDocumentElement().normalize();

        StringBuilder txtContent = new StringBuilder();
        var buffWr = new BufferedWriter(new FileWriter(outputFilePath));
        
        nodeToTxt(doc.getDocumentElement(), txtContent, 0);
        buffWr.write(txtContent.toString());

        buffWr.close();
    }


    private void nodeToTxt(Node node, StringBuilder writer, int depth) 
    {
        String indent = "   ".repeat(depth);

        if (node.getNodeType() == Node.ELEMENT_NODE) 
        {
            writer.append(indent).append(node.getNodeName()).append(":\n");
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) 
        {
            nodeToTxt(children.item(i), writer, depth + (node.getNodeType() == Node.ELEMENT_NODE ? 1 : 0));
        }

        if (node.getNodeType() == Node.TEXT_NODE) 
        {
            String textContent = node.getNodeValue().trim();
            if (!textContent.isEmpty()) 
            {
                writer.append(indent).append(textContent).append("\n");
            }
        }

    }
}
