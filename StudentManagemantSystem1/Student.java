/**
 * ============================================================
 *  Student Management System - Java Project
 * ============================================================
 *  Concepts Used:
 *    - Object-Oriented Programming (OOP)
 *    - Encapsulation (private fields + getters/setters)
 * ============================================================
 */
public class Student {

    // Private fields (Encapsulation)
    private int    studentId;
    private String name;
    private int    age;
    private String gender;
    private String department;
    private int    year;
    private double cgpa;

    // ---- Constructor ----
    public Student(int studentId, String name, int age,
                   String gender, String department, int year, double cgpa) {
        this.studentId  = studentId;
        this.name       = name;
        this.age        = age;
        this.gender     = gender;
        this.department = department;
        this.year       = year;
        this.cgpa       = cgpa;
    }

    // ---- Getters ----
    public int    getStudentId()  { return studentId;  }
    public String getName()       { return name;        }
    public int    getAge()        { return age;         }
    public String getGender()     { return gender;      }
    public String getDepartment() { return department;  }
    public int    getYear()       { return year;        }
    public double getCgpa()       { return cgpa;        }

    // ---- Setters ----
    public void setName(String name)         { this.name       = name;   }
    public void setAge(int age)              { this.age        = age;    }
    public void setGender(String gender)     { this.gender     = gender; }
    public void setDepartment(String dept)   { this.department = dept;   }
    public void setYear(int year)            { this.year       = year;   }
    public void setCgpa(double cgpa)         { this.cgpa       = cgpa;   }

    // ---- Display Format ----
    @Override
    public String toString() {
        return String.format(
            "  ID: %-5d | Name: %-18s | Age: %-3d | Gender: %-7s | Dept: %-16s | Year: %d | CGPA: %.2f",
            studentId, name, age, gender, department, year, cgpa
        );
    }
}
