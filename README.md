# 🎓 Student Management System

A full-featured, modern **Student Management System** built with **Java (Swing GUI & Console CLI)** and **Web Technologies (HTML, CSS, JavaScript)**.

Designed for managing student records efficiently using Object-Oriented Programming (OOP) principles and Java Collections Framework (`HashMap`, `ArrayList`).

---

## 🌟 Key Features

- **➕ Add Student**: Register new students with details (ID, Name, Age, Gender, Department, Year of Study, CGPA).
- **📋 View All Students**: Interactive tabular layout with real-time record count.
- **🔍 Live Search & Filter**: Search student records instantly by Name, Department, or Student ID.
- **✏️ Update Student Details**: Pre-filled dropdown forms for seamless detail updates.
- **🗑️ Delete Student Record**: Safely delete student records with modal confirmation.
- **📊 Real-time Dashboard**: Displays Total Students, Topper Count (CGPA ≥ 9.0), and Department Statistics.

---

## 🛠️ Tech Stack & Concepts Applied

| Component | Technologies & Concepts Used |
| :--- | :--- |
| **Language** | Java 8+ |
| **GUI Framework** | Java Swing, AWT |
| **Web Interface** | HTML5, Modern CSS3 (Dark Mode), Vanilla JavaScript |
| **Data Structures** | `HashMap` (Key-Value fast lookup), `ArrayList` (Dynamic listing) |
| **OOP Concepts** | Encapsulation, Polymorphism, Abstraction, Modular Design |
| **Error Handling** | Exception Handling (`try-catch`), Input Validation |

---

## 📁 Directory Structure

```text
StudentManagementSystem/
├── Student.java          # Model class representing Student entity (Encapsulation)
├── StudentService.java   # Business logic layer (HashMap & ArrayList CRUD operations)
├── StudentGUI.java       # Desktop GUI application (Swing UI with custom renderers)
├── Main.java             # Console-based interactive CLI application
├── index.html            # Responsive Web UI dashboard
└── README.md             # Project documentation
