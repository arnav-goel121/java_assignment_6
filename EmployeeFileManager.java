import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;

public class EmployeeFileManager {
    private static final Path EMPLOYEE_FILE = Path.of("employees.ser");

    public static void main(String[] args) {
        try {
            if (args.length == 5 && args[0].equals("add")) {
                Employee employee = new Employee(Integer.parseInt(args[1]), args[2],
                        args[3], Double.parseDouble(args[4]));
                List<Employee> employees = loadEmployees();
                employees.add(employee);
                saveEmployees(employees);
                System.out.println("Employee saved.");
            } else if (args.length == 1 && args[0].equals("list")) {
                List<Employee> employees = loadEmployees();
                if (employees.isEmpty()) {
                    System.out.println("No employee records yet.");
                }
                for (Employee employee : employees) {
                    System.out.println(employee);
                }
            } else {
                System.out.println("Usage: java EmployeeFileManager add <id> <name> <department> <salary>");
                System.out.println("       java EmployeeFileManager list");
            }
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            System.err.println("Employee file operation failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static List<Employee> loadEmployees() throws IOException, ClassNotFoundException {
        List<Employee> employees = new ArrayList<>();
        if (Files.notExists(EMPLOYEE_FILE)) {
            return employees;
        }

        try (InputStream file = Files.newInputStream(EMPLOYEE_FILE);
             ObjectInputStream input = new ObjectInputStream(file)) {
            input.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                    "maxdepth=2;maxrefs=1000000;Employee;!*"));
            int count = input.readInt();
            if (count < 0) {
                throw new StreamCorruptedException("Invalid employee count.");
            }
            for (int i = 0; i < count; i++) {
                Object record = input.readObject();
                if (!(record instanceof Employee)) {
                    throw new StreamCorruptedException("Expected an Employee record.");
                }
                employees.add((Employee) record);
            }
        }
        return employees;
    }

    private static void saveEmployees(List<Employee> employees) throws IOException {
        Path temporaryFile = Files.createTempFile(Path.of("."), "employees-", ".tmp");
        try {
            try (OutputStream file = Files.newOutputStream(temporaryFile);
                 ObjectOutputStream output = new ObjectOutputStream(file)) {
                output.writeInt(employees.size());
                for (Employee employee : employees) {
                    output.writeObject(employee);
                }
            }
            Files.move(temporaryFile, EMPLOYEE_FILE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}
