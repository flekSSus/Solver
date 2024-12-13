import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonToSingleLineTextConverter {
    private String inputJsonFile = "input.json";  
    private String outputTextFile = "output.txt"; 

    public void setInputJsonFile(String inputJsonFile) {
        this.inputJsonFile = inputJsonFile;
    }

    public void setOutputTextFile(String outputTextFile) {
        this.outputTextFile = outputTextFile;
    }

    public void processJsonToText() {
        try (FileReader reader = new FileReader(inputJsonFile)) {
            
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String jsonString = jsonObject.toString();

            
            try (FileWriter writer = new FileWriter(outputTextFile)) {
                writer.write(jsonString);
                System.out.println("JSON успешно записан в строку в текстовый файл!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
