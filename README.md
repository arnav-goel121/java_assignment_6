# Java File Handling Assignments

Open a terminal in this folder and compile with JDK 11 or later.

```sh
javac *.java
```

## 1. Student records and binary file copying

```sh
java StudentFileManager add 101 "Anaya Sharma" "Computer Science"
java StudentFileManager list
java StudentFileManager copy StudentFileManager.class ClassCopy.bin
```

Student records are appended to `students.txt` as tab-separated ID, name and course fields. Quote names and courses that contain spaces. The copy example uses the compiled class as the sample binary file.

## 2. File-based log analyzer

```sh
java LogAnalyzer sample.log filtered.log
```

The program counts nonblank lines as entries and matches uppercase `ERROR` and `WARNING` as whole words. A line with both keywords contributes to both counts and is written once. The sample produces four entries, one error and one warning. The output file contains the two matching lines in their original order, and the counts appear in the terminal.

## 3. Employee serialization

```sh
java EmployeeFileManager add 201 "Mira Singh" Engineering 75000
java EmployeeFileManager add 202 "Dev Patel" Finance 68000
java EmployeeFileManager list
```

Each `add` loads the existing records, adds one employee, and writes the updated records to `employees.ser`. The `list` command retrieves the saved ID, name, department and salary for each employee.
