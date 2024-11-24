import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class TxtProcessor {

    // Метод для чтения файла
    public static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    // Метод для обработки текста (вычисление арифметических выражений)
    public static String processContent(String content) {
        String regex = "\\(([^()]+)\\)"; // Находим выражения внутри скобок
        Pattern pattern = Pattern.compile(regex);

        // Обработка выражений внутри скобок
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

        // Обработка оставшегося выражения
        return evaluateExpression(content);
    }

    // Метод для вычисления выражения без скобок
    private static String evaluateExpression(String expression) {
        String original = expression; // Сохраняем оригинальное выражение

        // Регулярные выражения для операций
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\^\\s*\\d+(\\.\\d+)?", "^");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\*\\s*\\d+(\\.\\d+)?", "*");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*/\\s*\\d+(\\.\\d+)?", "/");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\+\\s*\\d+(\\.\\d+)?", "+");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*-\\s*\\d+(\\.\\d+)?", "-");

        // Если выражение не изменилось, возвращаем его без изменений
        return expression.equals(original) ? expression : expression.trim();
    }

    // Метод для вычисления операций по регулярному выражению
    private static String evaluateByRegex(String expression, String regex, String operator) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        while (matcher.find()) {
            String match = matcher.group();
            String cleaned = match.replaceAll("\\s+", ""); // Убираем пробелы только из текущей операции
            double result = calculate(cleaned, operator);
            expression = expression.replace(match, String.valueOf(result));
            matcher = pattern.matcher(expression);
        }
        return expression;
    }

    // Метод для вычисления конкретной операции
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

    // Метод для записи текста в файл
    public static void writeFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content);
    }
}
