import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SingleLineTextToJsonConverter {
    private String inputTextFile = "output.txt";
    private String outputJsonFile = "output.json";

    public void setInputTextFile(String inputTextFile) {
        this.inputTextFile = inputTextFile;
    }

    public void setOutputJsonFile(String outputJsonFile) {
        this.outputJsonFile = outputJsonFile;
    }

    public void processTextToJson() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputTextFile))) {

            String jsonString = reader.readLine();


            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter writer = new FileWriter(outputJsonFile)) {
                writer.write(gson.toJson(jsonObject));
                System.out.println("Строка успешно преобразована в форматированный JSON!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
