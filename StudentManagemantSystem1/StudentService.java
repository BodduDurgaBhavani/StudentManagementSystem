import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 *  StudentService.java - Business Logic Layer
 * ============================================================
 *  Concepts Used:
 *    - Java Collections: HashMap & ArrayList
 *    - Modular Design (separate class for logic)
 *    - CRUD Operations: Add, View, Search, Update, Delete
 * ============================================================
 */
public class StudentService {

    // HashMap stores students: Key = Student ID, Value = Student object
    private Map<Integer, Student> studentMap = new HashMap<>();

    // -------------------------------------------------------
    // ADD - Add a new student record
    // -------------------------------------------------------
    public boolean addStudent(Student student) {
        if (studentMap.containsKey(student.getStudentId())) {
            return false; // Duplicate ID not allowed
        }
        studentMap.put(student.getStudentId(), student);
        return true;
    }

    // -------------------------------------------------------
    // VIEW - Get all student records as a List (ArrayList)
    // -------------------------------------------------------
    public List<Student> viewAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    // -------------------------------------------------------
    // SEARCH - Find student by ID
    // -------------------------------------------------------
    public Student searchStudent(int studentId) {
        return studentMap.get(studentId); // returns null if not found
    }

    // -------------------------------------------------------
    // UPDATE - Update details of an existing student
    // -------------------------------------------------------
    public boolean updateStudent(int studentId, String name, int age,
                                  String gender, String department,
                                  int year, double cgpa) {
        Student student = studentMap.get(studentId);
        if (student == null) return false;

        student.setName(name);
        student.setAge(age);
        student.setGender(gender);
        student.setDepartment(department);
        student.setYear(year);
        student.setCgpa(cgpa);
        return true;
    }

    // -------------------------------------------------------
    // DELETE - Remove a student record by ID
    // -------------------------------------------------------
    public boolean deleteStudent(int studentId) {
        if (!studentMap.containsKey(studentId)) return false;
        studentMap.remove(studentId);
        return true;
    }

    // -------------------------------------------------------
    // TOTAL - Count of students
    // -------------------------------------------------------
    public int getTotalStudents() {
        return studentMap.size();
    }
}
