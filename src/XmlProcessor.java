import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import java.util.Stack;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

    public static void txtToXml(String fileInputPath, String fileOutputPath) throws Exception 
    {
        File txtFile = new File(fileInputPath);
        BufferedReader reader = new BufferedReader(new FileReader(txtFile));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = null;
        Stack<Element> stack = new Stack<>();
        String line;

        while ((line = reader.readLine()) != null) 
        {

            int count = 0;
            while (count < line.length() && line.charAt(count) == ' ') 
            {
                count++;
            }
            int depth = count/ 3;

            line = line.trim();

            if (line.contains(":")) 
            {
                String[] parts = line.split(":",2);
                String tagName = parts[0].trim();
                String value = parts.length > 1 ? parts[1].trim() : "";
                
                Element element = doc.createElement(tagName);

                if (root == null) 
                {
                    root = element;
                } 
                else 
                {
                    while (stack.size() > depth) 
                    {
                        stack.pop();
                    }
                    stack.peek().appendChild(element);
                }

                stack.push(element);
                if (!value.isEmpty()) 
                {
                    element.appendChild(doc.createTextNode(value));
                }

            } 
            else 
            {
                if (!stack.isEmpty()) 
                {
                    stack.peek().appendChild(doc.createTextNode(line));
                }
            }
        }
        reader.close();

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        transformer.setOutputProperty(OutputKeys.METHOD, "html");

        DOMSource source = new DOMSource(root);
        StreamResult result = new StreamResult(new File(fileOutputPath));
        transformer.transform(source, result);

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
