import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class TxtProcessorTest {

    @Test
    void testReadFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "Test content");

        String content = TxtProcessor.readFile(tempFile.toString());
        assertEquals("Test content", content);

        Files.delete(tempFile);
    }

    @Test
    void testWriteFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");

        TxtProcessor.writeFile(tempFile.toString(), "Test content");
        String content = Files.readString(tempFile);

        assertEquals("Test content", content);

        Files.delete(tempFile);
    }

    @Test
    void testProcessContent() {
        String input = "(2+3)*(4-1)";
        String result = TxtProcessor.processContent(input);
        assertEquals("15.0", result);
    }

    @Test
    void testEvaluateExpression() {
        String input = "2 + 3 * 4 - 5";
        String result = TxtProcessor.processContent(input);
        assertEquals("9.0", result);
    }

    @Test
    void testEvaluateFactorials() {
        String input = "5! + 3!";
        String result = TxtProcessor.processContent(input);
        assertEquals("126.0", result);
    }

    @Test
    void testEvaluateTrigonometricFunctions() {
        String input = "sin(0.78539816) + cos(0.78539816) + tan(0.78539816)";
        String result = TxtProcessor.processContent(input);
        assertEquals(2.41421356237, Double.parseDouble(result), 0.00001);
    }

    @Test
    void testSimplifyConsecutiveSigns() {
        String input = "5 + --3";
        String result = TxtProcessor.processContent(input);
        assertEquals("8.0", result);
    }

    @Test
    void testNestedParentheses() {
        String input = "((2 + 3) * (4 + 1))";
        String result = TxtProcessor.processContent(input);
        assertEquals("25.0", result);
    }

    @Test
    void testComplexExpression() {
        String input = "(2 + 3) * (4 - 1) + sin(0) + cos(0) - 5!";
        String result = TxtProcessor.processContent(input);
        assertEquals("-104.0", result);
    }

    @Test
    void testEvaluateByRegex() {
        String input = "3 ^ 2";
        String result = TxtProcessor.processContent(input);
        assertEquals("9.0", result);
    }

    @Test
    void testInvalidFactorial() {
        String input = "(-5)!";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TxtProcessor.processContent(input));
        assertTrue(exception.getMessage().contains("Факториал определён только для неотрицательных целых чисел"));
    }

    @Test
    void testEmptyInput() {
        String input = "";
        String result = TxtProcessor.processContent(input);
        assertEquals("", result);
    }
}
