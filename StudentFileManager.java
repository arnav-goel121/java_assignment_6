import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class StudentFileManager {
    private static final String STUDENT_FILE = "students.txt";

    public static void main(String[] args) {
        try {
            if (args.length == 4 && args[0].equals("add")) {
                addStudent(Integer.parseInt(args[1]), args[2], args[3]);
                System.out.println("Student saved.");
            } else if (args.length == 1 && args[0].equals("list")) {
                listStudents();
            } else if (args.length == 3 && args[0].equals("copy")) {
                copyBinaryFile(Path.of(args[1]), Path.of(args[2]));
                System.out.println("Binary file copied.");
            } else {
                System.out.println("Usage: java StudentFileManager add <id> <name> <course>");
                System.out.println("       java StudentFileManager list");
                System.out.println("       java StudentFileManager copy <source> <destination>");
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Student file operation failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void addStudent(int id, String name, String course) throws IOException {
        if (id <= 0 || name.isBlank() || course.isBlank()
                || name.matches("(?s).*[\\t\\r\\n].*") || course.matches("(?s).*[\\t\\r\\n].*")) {
            throw new IllegalArgumentException("Enter a positive ID and single-line name and course without tabs.");
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(STUDENT_FILE, StandardCharsets.UTF_8, true))) {
            writer.write(id + "\t" + name + "\t" + course);
            writer.newLine();
        }
    }

    private static void listStudents() throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(STUDENT_FILE, StandardCharsets.UTF_8))) {
            System.out.println("ID\tName\tCourse");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            if (!Files.notExists(Path.of(STUDENT_FILE))) {
                throw e;
            }
            System.out.println("No student records yet.");
        }
    }

    private static void copyBinaryFile(Path source, Path destination) throws IOException {
        if (Files.exists(destination) && Files.isSameFile(source, destination)) {
            throw new IOException("Choose a destination different from the source.");
        }

        try (FileInputStream input = new FileInputStream(source.toFile());
             FileOutputStream output = new FileOutputStream(destination.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
}
