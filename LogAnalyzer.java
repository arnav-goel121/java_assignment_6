import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.regex.Pattern;

public class LogAnalyzer {
    private static final Pattern ERROR = Pattern.compile("\\bERROR\\b");
    private static final Pattern WARNING = Pattern.compile("\\bWARNING\\b");

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java LogAnalyzer <input-log> <filtered-log>");
            return;
        }

        try {
            analyze(Path.of(args[0]), Path.of(args[1]));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Log analysis failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void analyze(Path input, Path output) throws IOException {
        if (Files.exists(output) && Files.isSameFile(input, output)) {
            throw new IOException("Choose an output file different from the input log.");
        }

        long total = 0;
        long errors = 0;
        long warnings = 0;

        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                total++;
                boolean hasError = ERROR.matcher(line).find();
                boolean hasWarning = WARNING.matcher(line).find();

                if (hasError) {
                    errors++;
                }
                if (hasWarning) {
                    warnings++;
                }
                if (hasError || hasWarning) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        System.out.println("Total log entries: " + total);
        System.out.println("ERROR entries: " + errors);
        System.out.println("WARNING entries: " + warnings);
        System.out.println("Filtered entries saved to " + output);
    }
}
