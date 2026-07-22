import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 *  Main.java - Console UI Entry Point
 * ============================================================
 *  Student Management System
 *  Language  : Java
 *  Type      : Console-Based Application
 *  Concepts  : OOP, ArrayList, HashMap, Exception Handling,
 *              Loops, Methods, Scanner, Modular Design
 *
 *  HOW TO COMPILE & RUN:
 *    javac Student.java StudentService.java Main.java
 *    java -cp . Main
 * ============================================================
 */
public class Main {

    static Scanner       sc      = new Scanner(System.in);
    static StudentService service = new StudentService();

    public static void main(String[] args) {
        printBanner();
        int choice;
        do {
            printMenu();
            System.out.print("  Enter your choice: ");
            choice = readInt();
            System.out.println();

            switch (choice) {
                case 1: addStudent();    break;
                case 2: viewStudents();  break;
                case 3: searchStudent(); break;
                case 4: updateStudent(); break;
                case 5: deleteStudent(); break;
                case 6: totalStudents(); break;
                case 7:
                    System.out.println("  ==========================================");
                    System.out.println("   Thank you! Exiting the system. Goodbye!");
                    System.out.println("  ==========================================");
                    break;
                default:
                    System.out.println("  [!] Invalid choice. Please enter 1 to 7.\n");
            }
        } while (choice != 7);

        sc.close();
    }

    // =====================================================
    //  BANNER
    // =====================================================
    static void printBanner() {
        System.out.println();
        System.out.println("  ==========================================");
        System.out.println("       STUDENT MANAGEMENT SYSTEM");
        System.out.println("       Console Application | Java");
        System.out.println("  ==========================================");
        System.out.println("   OOP | ArrayList | HashMap | Modular Design");
        System.out.println("  ==========================================");
    }

    // =====================================================
    //  MENU
    // =====================================================
    static void printMenu() {
        System.out.println();
        System.out.println("  ------------------------------------------");
        System.out.println("                  MAIN MENU");
        System.out.println("  ------------------------------------------");
        System.out.println("    1.  Add Student");
        System.out.println("    2.  View All Students");
        System.out.println("    3.  Search Student by ID");
        System.out.println("    4.  Update Student Details");
        System.out.println("    5.  Delete Student Record");
        System.out.println("    6.  Total Number of Students");
        System.out.println("    7.  Exit");
        System.out.println("  ------------------------------------------");
    }

    // =====================================================
    //  1. ADD STUDENT
    // =====================================================
    static void addStudent() {
        System.out.println("  --- [ Add New Student ] ---");
        try {
            System.out.print("  Student ID   : "); int    id     = readInt();
            System.out.print("  Full Name    : "); String name   = sc.nextLine().trim();
            System.out.print("  Age          : "); int    age    = readInt();
            System.out.print("  Gender       : "); String gender = sc.nextLine().trim();
            System.out.print("  Department   : "); String dept   = sc.nextLine().trim();
            System.out.print("  Year (1-4)   : "); int    year   = readInt();
            System.out.print("  CGPA (0-10)  : "); double cgpa   = readDouble();

            Student s = new Student(id, name, age, gender, dept, year, cgpa);
            if (service.addStudent(s))
                System.out.println("\n  [SUCCESS] Student added successfully!");
            else
                System.out.println("\n  [ERROR]   Student ID " + id + " already exists!");
        } catch (Exception e) {
            System.out.println("\n  [ERROR] Invalid input. Please try again.");
            sc.nextLine();
        }
    }

    // =====================================================
    //  2. VIEW ALL STUDENTS
    // =====================================================
    static void viewStudents() {
        System.out.println("  --- [ All Student Records ] ---\n");
        List<Student> list = service.viewAllStudents();

        if (list.isEmpty()) {
            System.out.println("  No student records found.");
            return;
        }

        System.out.println("  " + "=".repeat(95));
        System.out.printf("  %-6s %-18s %-4s %-8s %-17s %-5s %s%n",
                "ID", "Name", "Age", "Gender", "Department", "Year", "CGPA");
        System.out.println("  " + "-".repeat(95));
        for (Student s : list) {
            System.out.printf("  %-6d %-18s %-4d %-8s %-17s %-5d %.2f%n",
                s.getStudentId(), s.getName(), s.getAge(),
                s.getGender(), s.getDepartment(), s.getYear(), s.getCgpa());
        }
        System.out.println("  " + "=".repeat(95));
        System.out.println("  Total Records: " + list.size());
    }

    // =====================================================
    //  3. SEARCH STUDENT
    // =====================================================
    static void searchStudent() {
        System.out.println("  --- [ Search Student by ID ] ---");
        System.out.print("  Enter Student ID : ");
        int id = readInt();

        Student s = service.searchStudent(id);
        if (s != null) {
            System.out.println();
            System.out.println("  [FOUND] Student Details:");
            System.out.println("  " + "-".repeat(45));
            System.out.println("  Student ID   : " + s.getStudentId());
            System.out.println("  Name         : " + s.getName());
            System.out.println("  Age          : " + s.getAge());
            System.out.println("  Gender       : " + s.getGender());
            System.out.println("  Department   : " + s.getDepartment());
            System.out.println("  Year         : " + s.getYear());
            System.out.println("  CGPA         : " + s.getCgpa());
            System.out.println("  " + "-".repeat(45));
        } else {
            System.out.println("\n  [NOT FOUND] No student with ID: " + id);
        }
    }

    // =====================================================
    //  4. UPDATE STUDENT
    // =====================================================
    static void updateStudent() {
        System.out.println("  --- [ Update Student Details ] ---");
        System.out.print("  Enter Student ID to update : ");
        int id = readInt();

        if (service.searchStudent(id) == null) {
            System.out.println("\n  [ERROR] Student ID " + id + " not found.");
            return;
        }

        System.out.println("  Enter new details:");
        System.out.print("  New Name       : "); String name   = sc.nextLine().trim();
        System.out.print("  New Age        : "); int    age    = readInt();
        System.out.print("  New Gender     : "); String gender = sc.nextLine().trim();
        System.out.print("  New Department : "); String dept   = sc.nextLine().trim();
        System.out.print("  New Year (1-4) : "); int    year   = readInt();
        System.out.print("  New CGPA       : "); double cgpa   = readDouble();

        service.updateStudent(id, name, age, gender, dept, year, cgpa);
        System.out.println("\n  [SUCCESS] Student details updated successfully!");
    }

    // =====================================================
    //  5. DELETE STUDENT
    // =====================================================
    static void deleteStudent() {
        System.out.println("  --- [ Delete Student Record ] ---");
        System.out.print("  Enter Student ID to delete : ");
        int id = readInt();

        if (service.deleteStudent(id))
            System.out.println("\n  [SUCCESS] Student ID " + id + " deleted successfully!");
        else
            System.out.println("\n  [ERROR]   Student ID " + id + " not found.");
    }

    // =====================================================
    //  6. TOTAL STUDENTS
    // =====================================================
    static void totalStudents() {
        System.out.println("  --- [ Total Students ] ---");
        System.out.println("  Total students registered: " + service.getTotalStudents());
    }

    // =====================================================
    //  HELPER METHODS (Input Reading with Validation)
    // =====================================================
    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  [!] Enter a valid whole number: ");
            }
        }
    }

    static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  [!] Enter a valid decimal number: ");
            }
        }
    }
}
