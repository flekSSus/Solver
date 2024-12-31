import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TextToJsonConverterTest {

    @Test
    void testTextToJsonConversion() throws IOException {

        String inputText = "{\"name\":\"John\",\"details\":{\"age\":30,\"skills\":[\"Java\",\"Python\"]}}";
        Path inputPath = Files.createTempFile("input", ".txt");
        Files.writeString(inputPath, inputText);


        Path outputPath = Files.createTempFile("output", ".json");

        SingleLineTextToJsonConverter converter = new SingleLineTextToJsonConverter();
        converter.setInputTextFile(inputPath.toString());
        converter.setOutputJsonFile(outputPath.toString());
        converter.processTextToJson();


        String result = Files.readString(outputPath);
        Gson gson = new Gson();
        JsonObject actualJson = gson.fromJson(result, JsonObject.class);
        JsonObject expectedJson = gson.fromJson("""
        {
          "name": "John",
          "details": {
            "age": 30,
            "skills": [
              "Java",
              "Python"
            ]
          }
        }
    """, JsonObject.class);

        assertEquals(expectedJson, actualJson);


        Files.deleteIfExists(inputPath);
        Files.deleteIfExists(outputPath);
    }

}
