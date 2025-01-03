import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class TxtProcessor {

    public static String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    public static String processContent(String content) {
        content = content.replaceAll("\\(-\\)", "(-1)*");

        content = content.replaceAll("\\)\\(", ")*(");

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

        content = evaluateFactorials(content);

        content = evaluateTrigonometricFunctions(content);

        content = simplifyConsecutiveSigns(content);

        return evaluateExpression(content);
    }

    private static String evaluateExpression(String expression) {
        String original = expression;

        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\^\\s*\\d+(\\.\\d+)?", "^");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\*\\s*\\d+(\\.\\d+)?", "*");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*/\\s*\\d+(\\.\\d+)?", "/");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*\\+\\s*\\d+(\\.\\d+)?", "+");
        expression = evaluateByRegex(expression, "\\d+(\\.\\d+)?\\s*-\\s*\\d+(\\.\\d+)?", "-");
        expression = evaluateFactorials(expression);
        expression = evaluateTrigonometricFunctions(expression);

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

    private static String evaluateFactorials(String expression) {
        Pattern pattern = Pattern.compile("([-+]?\\d+(\\.\\d+)?)!");
        Matcher matcher = pattern.matcher(expression);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String numberStr = matcher.group(1);
            double number = Double.parseDouble(numberStr);

            if (number < 0 || number % 1 != 0) {
                throw new IllegalArgumentException("Факториал определён только для неотрицательных целых чисел: " + numberStr);
            }

            long result = factorial((int) number);
            matcher.appendReplacement(buffer, String.valueOf(result));
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    private static long factorial(int n) {
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }

    private static String evaluateTrigonometricFunctions(String expression) {
        Pattern pattern = Pattern.compile("(sin|cos|tan)\\(?([-+]?\\d+(\\.\\d+)?([eE][-+]?\\d+)?)\\)?");
        Matcher matcher = pattern.matcher(expression);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String function = matcher.group(1);
            String argument = matcher.group(2);

            double value = Double.parseDouble(argument);

            double result = switch (function) {
                case "sin" -> Math.sin(Math.toRadians(value));
                case "cos" -> Math.cos(Math.toRadians(value));
                case "tan" -> Math.tan(Math.toRadians(value));
                default -> throw new IllegalArgumentException("Неизвестная функция: " + function);
            };

            matcher.appendReplacement(buffer, String.valueOf(result));
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    private static String simplifyConsecutiveSigns(String expression) {
        Pattern pattern = Pattern.compile("([+-](\\s*[+-]){1,})");
        Matcher matcher = pattern.matcher(expression);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String sequence = matcher.group(1).replaceAll("\\s+", "");

            long minusCount = sequence.chars().filter(ch -> ch == '-').count();

            String replacement = (minusCount % 2 == 0) ? "+" : "-";
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    public static void writeFile(String filePath, String content) throws IOException {
        Files.writeString(Path.of(filePath), content);
    }
}
