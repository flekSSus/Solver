import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JsonToTextConverterTest {

    @Test
    void testJsonToTextConversion() throws IOException {

        String inputJson = """
            {
                "name": "John",
                "details": {
                    "age": 30,
                    "skills": ["Java", "Python"]
                }
            }
        """;
        Path inputPath = Files.createTempFile("input", ".json");
        Files.writeString(inputPath, inputJson);


        Path outputPath = Files.createTempFile("output", ".txt");


        JsonToSingleLineTextConverter converter = new JsonToSingleLineTextConverter();
        converter.setInputJsonFile(inputPath.toString());
        converter.setOutputTextFile(outputPath.toString());
        converter.processJsonToText();


        String result = Files.readString(outputPath);
        String expected = "{\"name\":\"John\",\"details\":{\"age\":30,\"skills\":[\"Java\",\"Python\"]}}";
        assertEquals(expected, result);


        Files.deleteIfExists(inputPath);
        Files.deleteIfExists(outputPath);
    }
}
