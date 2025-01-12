import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TxtProcessorTest {

    @Test
    void testProcessContent_simpleAddition() {
        String input = "2 + 3";
        String expected = "5.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_simpleSubtraction() {
        String input = "10 - 7";
        String expected = "3.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_withBrackets() {
        String input = "2 * (3 + 4)";
        String expected = "14.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_nestedBrackets() {
        String input = "(2 + (3 * (4 - 1)))";
        String expected = "11.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_factorial() {
        String input = "5!";
        String expected = "120";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_powerOperation() {
        String input = "2 ^ 3";
        String expected = "8.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_combinedOperations() {
        String input = "3 + 4 * 2 - (1 + 2)";
        String expected = "8.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_trigonometricFunction() {
        String input = "sin(0) + cos(0)";
        String expected = "1.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_complexExpression() {
        String input = "2 * (3 + 4!) - sin(0) + 3 ^ 2";
        String expected = "63.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_invalidFactorial() {
        String input = "2.5!";
        assertThrows(IllegalArgumentException.class, () -> TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_negativeFactorial() {
        String input = "(-5)!";
        assertThrows(IllegalArgumentException.class, () -> TxtProcessor.processContent(input));
    }

    @Test
    void testProcessContent_minusFactorial() {
        String input = "-5!";
        String expected = "-120";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void invalidPower() {
        String input = "(-2) ^ 0.5";
        assertThrows(IllegalArgumentException.class, () -> TxtProcessor.processContent(input));
    }

    @Test
    void validPower() {
        String input = "-4 ^ 2";
        String expected = "-16.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void consecutiveSigns() {
        String input = "2 -- 3";
        String expected = "5.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void multipleBrackets() {
        String input = "((2 + 3) * (1 - 4))";
        String expected = "-15.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void emptyBrackets() {
        String input = "2 * () + 3";
        String expected = "5.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void bigFactorial() {
        String input = "10!";
        String expected = "3628800";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void edgeCaseBrackets() {
        String input = "(1) + (2) * (3)";
        String expected = "7.0";
        assertEquals(expected, TxtProcessor.processContent(input));
    }

    @Test
    void trigonometric() {
        String input = "cos(0) + sin(3.14159265358979 / 2)";
        Double expected = 2.0;
        assertEquals(expected, Double.parseDouble(TxtProcessor.processContent(input)), 0.00001);
    }

    @Test
    void doubleTrigonometric() {
        String input = "sin50";
        Double expected = Math.sin(50);
        assertEquals(expected, Double.parseDouble(TxtProcessor.processContent(input)), 0.00001);
    }

    @Test
    void complexTrigonometric() {
        String input = "tan(47-34)";
        Double expected = Math.tan(13);
        assertEquals(expected, Double.parseDouble(TxtProcessor.processContent(input)), 0.00001);
    }

    @Test
    void zeroFactorial() {
        String input = "0!";
        String expected = "1";
        assertEquals(expected, TxtProcessor.processContent(input));
    }
}
