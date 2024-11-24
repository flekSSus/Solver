import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class TxtProcessor {

    public static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public static String processContent(String content) {
        String regex = "\\(([^()]+)\\)";
        Pattern pattern = Pattern.compile(regex);

        while (content.contains("(")) {
            Matcher matcher = pattern.matcher(content);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                String innerExpression = matcher.group(1);
                String result = evaluateExpression(innerExpression.trim());
                matcher.appendReplacement(buffer, result);
            }
            matcher.appendTail(buffer);
            content = buffer.toString();
        }

        return evaluateExpression(content);
    }

    private static String evaluateExpression(String expression) {
        String original = expression;

        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\^\\s*\\d+(\\.\\d+)?", "^");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\*\\s*\\d+(\\.\\d+)?", "*");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*/\\s*\\d+(\\.\\d+)?", "/");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\+\\s*\\d+(\\.\\d+)?", "+");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*-\\s*\\d+(\\.\\d+)?", "-");
        return expression.equals(original) ? expression : expression.trim();
    }

    private static String evaluateByRegex(String expression, String regex, String operator) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            String match = matcher.group();
            String cleaned = match.replaceAll("\\s+", "");
            double result = calculate(cleaned, operator);
            expression = expression.replace(match, String.valueOf(result));
            matcher = pattern.matcher(expression);
        }
        return expression;
    }

    private static double calculate(String operation, String operator) {
        String[] parts = operation.split(Pattern.quote(operator));
        double left = Double.parseDouble(parts[0]);
        double right = Double.parseDouble(parts[1]);

        switch (operator) {
            case "^": return Math.pow(left, right);
            case "*": return left * right;
            case "/": return left / right;
            case "+": return left + right;
            case "-": return left - right;
            default: throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
    }

    public static void writeFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content);
    }
}
