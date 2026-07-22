import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * StudentGUI.java - Java Swing GUI
 * Student Management System
 */
public class StudentGUI extends JFrame {

    private StudentService service = new StudentService();

    // ── Colors ──────────────────────────────────────────────
    private static final Color BG         = new Color(18, 24, 38);
    private static final Color SIDEBAR    = new Color(22, 30, 50);
    private static final Color CARD       = new Color(30, 41, 63);
    private static final Color BORDER_CLR = new Color(55, 70, 100);
    private static final Color BLUE       = new Color(59, 130, 246);
    private static final Color GREEN      = new Color(34, 197, 94);
    private static final Color RED        = new Color(239, 68, 68);
    private static final Color ORANGE     = new Color(249, 115, 22);
    private static final Color PURPLE     = new Color(168, 85, 247);
    private static final Color WHITE      = new Color(240, 245, 255);
    private static final Color GRAY       = new Color(140, 160, 200);
    private static final Color ROW_ODD    = new Color(28, 38, 60);
    private static final Color ROW_EVEN   = new Color(35, 47, 73);
    private static final Color ROW_SEL    = new Color(59, 100, 200);
    private static final Color HDR_BG     = new Color(40, 55, 90);

    // ── Dropdown options ────────────────────────────────────
    private static final String[] GENDERS  = {"Select Gender", "Male", "Female", "Other"};
    private static final String[] DEPTS    = {
        "Select Department", "AI & ML", "Computer Science",
        "Data Science", "Electronics", "Mechanical",
        "Civil Engineering", "Information Technology", "Biotechnology"
    };
    private static final String[] YEARS    = {"Select Year", "1st Year", "2nd Year", "3rd Year", "4th Year"};

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JLabel valTotal, valToppers, valDepts;

    public StudentGUI() {
        service.addStudent(new Student(101, "Rahul Kumar",  20, "Male",   "AI & ML",          3, 9.25));
        service.addStudent(new Student(102, "Sneha Reddy",  21, "Female", "Computer Science",  4, 9.50));
        service.addStudent(new Student(103, "Arjun Mehta",  19, "Male",   "Data Science",      2, 8.75));
        service.addStudent(new Student(104, "Priya Sharma", 22, "Female", "AI & ML",           4, 9.80));
        service.addStudent(new Student(105, "Vikram Singh", 20, "Male",   "Electronics",       3, 7.90));
        service.addStudent(new Student(106, "Ananya Das",   21, "Female", "Computer Science",  3, 9.10));

        setTitle("Student Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 700);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildCenter(),    BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        refreshTable();
        updateStats();
        setVisible(true);
    }

    // ════════════════════════════════════════════════════════
    //  TOP BAR  — Title + 3 Stat Cards (no Average CGPA)
    // ════════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(SIDEBAR);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_CLR));

        // ── Title (ONLY heading, no language line) ──
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        left.setOpaque(false);

        JLabel icon = new JLabel("🎓");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JLabel title = new JLabel("STUDENT MANAGEMENT SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(WHITE);

        left.add(icon);
        left.add(title);

        // ── 3 stat cards ──
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(0, 0, 0, 15));

        valTotal   = statCard(right, "Total Students",  "0", BLUE);
        valToppers = statCard(right, "Toppers (CGPA ≥9)", "0", GREEN);
        valDepts   = statCard(right, "Departments",     "0", PURPLE);

        top.add(left,  BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private JLabel statCard(JPanel parent, String label, String val, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 3));
        card.setBackground(BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 1, true),
            new EmptyBorder(7, 18, 7, 18)
        ));
        card.setPreferredSize(new Dimension(160, 58));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(GRAY);

        JLabel num = new JLabel(val, SwingConstants.CENTER);
        num.setFont(new Font("Segoe UI", Font.BOLD, 20));
        num.setForeground(accent);

        card.add(lbl, BorderLayout.NORTH);
        card.add(num, BorderLayout.CENTER);
        parent.add(card);
        return num;
    }

    // ════════════════════════════════════════════════════════
    //  CENTER — Search + Table
    // ════════════════════════════════════════════════════════
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(14, 20, 10, 20));

        // Search row
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchRow.setOpaque(false);
        searchRow.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel sLbl = new JLabel("🔍  Search:");
        sLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sLbl.setForeground(GRAY);

        searchField = new JTextField(28);
        styleField(searchField);
        searchField.setToolTipText("Search by name, department or student ID");
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterTable(); }
        });

        JButton clrBtn = createBtn("✖ Clear", CARD, GRAY);
        clrBtn.setPreferredSize(new Dimension(90, 34));
        clrBtn.addActionListener(e -> { searchField.setText(""); refreshTable(); });

        searchRow.add(sLbl);
        searchRow.add(searchField);
        searchRow.add(clrBtn);

        // Table
        String[] cols = {"  ID", "  Name", "  Age", "  Gender", "  Department", "  Year", "  CGPA"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(ROW_ODD);
        table.setForeground(WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(45, 60, 90));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(ROW_SEL);
        table.setSelectionForeground(WHITE);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFocusable(false);

        // Header renderer — fixes invisible headers
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            { setOpaque(true); setHorizontalAlignment(LEFT); }
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                setText(val == null ? "" : val.toString());
                setBackground(HDR_BG);
                setForeground(WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 1, BLUE),
                    new EmptyBorder(0, 10, 0, 0)
                ));
                return this;
            }
        });
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));
        table.getTableHeader().setReorderingAllowed(false);

        // Cell renderer
        DefaultTableCellRenderer cr = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setFont(new Font("Segoe UI", col == 1 ? Font.BOLD : Font.PLAIN, 13));
                if (!sel) {
                    setBackground(row % 2 == 0 ? ROW_ODD : ROW_EVEN);
                    setForeground(WHITE);
                    if (col == 0) setForeground(BLUE);
                    if (col == 6 && val != null) {
                        try {
                            double c = Double.parseDouble(val.toString().trim());
                            setForeground(c >= 9.0 ? GREEN : c >= 7.5 ? ORANGE : RED);
                        } catch (Exception ignored) {}
                    }
                }
                return this;
            }
        };
        for (int i = 0; i < cols.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(cr);

        int[] widths = {65, 185, 60, 90, 185, 90, 75};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        scroll.getViewport().setBackground(ROW_ODD);

        center.add(searchRow, BorderLayout.NORTH);
        center.add(scroll,    BorderLayout.CENTER);
        return center;
    }

    // ════════════════════════════════════════════════════════
    //  BOTTOM BUTTONS
    // ════════════════════════════════════════════════════════
    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bar.setBackground(SIDEBAR);
        bar.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, BORDER_CLR));

        JButton btnAdd     = createBtn("➕  Add Student",    BLUE,   WHITE);
        JButton btnUpdate  = createBtn("✏  Update Student",  ORANGE, WHITE);
        JButton btnDelete  = createBtn("🗑  Delete Student",  RED,    WHITE);
        JButton btnView    = createBtn("👁  View Details",   GREEN,  WHITE);
        JButton btnRefresh = createBtn("🔄  Refresh",         CARD,   GRAY);

        btnAdd.addActionListener(e    -> openAddDialog());
        btnUpdate.addActionListener(e -> openUpdateDialog());
        btnDelete.addActionListener(e -> deleteStudent());
        btnView.addActionListener(e   -> viewStudent());
        btnRefresh.addActionListener(e -> { searchField.setText(""); refreshTable(); updateStats(); });

        for (JButton b : new JButton[]{btnAdd, btnUpdate, btnDelete, btnView, btnRefresh}) {
            b.setPreferredSize(new Dimension(162, 42));
            bar.add(b);
        }
        return bar;
    }

    private JButton createBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(fg);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        Color bright = bg.brighter();
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bright); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }

    // ════════════════════════════════════════════════════════
    //  ADD DIALOG  — dropdowns for Gender, Dept, Year
    // ════════════════════════════════════════════════════════
    private void openAddDialog() {
        JDialog d = dialog("➕  Add New Student", 490, 520);
        JPanel form = new JPanel(new GridLayout(0, 2, 12, 14));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(25, 30, 10, 30));

        JTextField fId   = addRow(form, "Student ID *");
        JTextField fName = addRow(form, "Full Name *");
        JTextField fAge  = addRow(form, "Age *");

        // Dropdowns
        JComboBox<String> cbGender = addCombo(form, "Gender *",        GENDERS);
        JComboBox<String> cbDept   = addCombo(form, "Department *",     DEPTS);
        JComboBox<String> cbYear   = addCombo(form, "Year of Study *",  YEARS);

        JTextField fCgpa = addRow(form, "CGPA (0 - 10) *");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        btns.setBackground(CARD);
        JButton save   = createBtn("✅  Save", BLUE, WHITE);
        JButton cancel = createBtn("✖  Cancel", RED, WHITE);

        save.addActionListener(e -> {
            try {
                int    id     = Integer.parseInt(fId.getText().trim());
                String name   = fName.getText().trim();
                int    age    = Integer.parseInt(fAge.getText().trim());
                String gender = (String) cbGender.getSelectedItem();
                String dept   = (String) cbDept.getSelectedItem();
                String yearStr= (String) cbYear.getSelectedItem();
                double cgpa   = Double.parseDouble(fCgpa.getText().trim());

                if (name.isEmpty() || gender.startsWith("Select") ||
                    dept.startsWith("Select") || yearStr.startsWith("Select")) {
                    msg("⚠  Please fill all fields and select options.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int year = Integer.parseInt(yearStr.substring(0, 1)); // "3rd Year" → 3

                if (service.addStudent(new Student(id, name, age, gender, dept, year, cgpa))) {
                    msg("✅  Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable(); updateStats(); d.dispose();
                } else {
                    msg("❌  Student ID " + id + " already exists!", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                msg("⚠  Enter valid numbers for ID, Age, and CGPA.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        });
        cancel.addActionListener(e -> d.dispose());
        btns.add(save); btns.add(cancel);
        d.add(form, BorderLayout.CENTER);
        d.add(btns, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    // ════════════════════════════════════════════════════════
    //  UPDATE DIALOG  — pre-fills all fields, dropdowns for Gender/Dept/Year
    // ════════════════════════════════════════════════════════
    private void openUpdateDialog() {
        int row = table.getSelectedRow();
        if (row < 0) { msg("⚠  Select a student row first.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString().trim());
        Student s = service.searchStudent(id);
        if (s == null) return;

        JDialog d = dialog("✏  Update Student — ID: " + id, 490, 490);
        JPanel form = new JPanel(new GridLayout(0, 2, 12, 14));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(25, 30, 10, 30));

        // Pre-fill text fields
        JTextField fName = addRowPre(form, "Full Name",   s.getName());
        JTextField fAge  = addRowPre(form, "Age",         String.valueOf(s.getAge()));

        // Pre-select dropdowns
        JComboBox<String> cbGender = addCombo(form, "Gender",       GENDERS);
        JComboBox<String> cbDept   = addCombo(form, "Department",   DEPTS);
        JComboBox<String> cbYear   = addCombo(form, "Year of Study",YEARS);

        // Pre-select current values
        cbGender.setSelectedItem(s.getGender());
        cbDept.setSelectedItem(s.getDepartment());
        cbYear.setSelectedItem(s.getYear() + (s.getYear()==1?"st":s.getYear()==2?"nd":s.getYear()==3?"rd":"th") + " Year");

        JTextField fCgpa = addRowPre(form, "CGPA (0 - 10)", String.valueOf(s.getCgpa()));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 14));
        btns.setBackground(CARD);
        JButton save   = createBtn("✅  Update", ORANGE, WHITE);
        JButton cancel = createBtn("✖  Cancel",  RED,    WHITE);

        save.addActionListener(e -> {
            try {
                String name    = fName.getText().trim();
                int    age     = Integer.parseInt(fAge.getText().trim());
                String gender  = (String) cbGender.getSelectedItem();
                String dept    = (String) cbDept.getSelectedItem();
                String yearStr = (String) cbYear.getSelectedItem();
                double cgpa    = Double.parseDouble(fCgpa.getText().trim());

                if (name.isEmpty() || gender.startsWith("Select") ||
                    dept.startsWith("Select") || yearStr.startsWith("Select")) {
                    msg("⚠  Please fill all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int year = Integer.parseInt(yearStr.substring(0, 1));
                service.updateStudent(id, name, age, gender, dept, year, cgpa);
                msg("✅  Student updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
                refreshTable(); updateStats(); d.dispose();
            } catch (NumberFormatException ex) {
                msg("⚠  Enter valid numbers for Age and CGPA.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        });
        cancel.addActionListener(e -> d.dispose());
        btns.add(save); btns.add(cancel);
        d.add(form, BorderLayout.CENTER);
        d.add(btns, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    // ════════════════════════════════════════════════════════
    //  DELETE
    // ════════════════════════════════════════════════════════
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) { msg("⚠  Select a student row first.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int id     = Integer.parseInt(tableModel.getValueAt(row, 0).toString().trim());
        String name= tableModel.getValueAt(row, 1).toString().trim();
        int ok = JOptionPane.showConfirmDialog(this,
            "Delete student?\n\nID: " + id + "   Name: " + name,
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            service.deleteStudent(id);
            msg("🗑  Student deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            refreshTable(); updateStats();
        }
    }

    // ════════════════════════════════════════════════════════
    //  VIEW DETAILS
    // ════════════════════════════════════════════════════════
    private void viewStudent() {
        int row = table.getSelectedRow();
        if (row < 0) { msg("⚠  Select a student row first.", "No Selection", JOptionPane.WARNING_MESSAGE); return; }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString().trim());
        Student s = service.searchStudent(id);
        if (s == null) return;

        String info =
            "────────────────────────────────────\n" +
            "  Student ID    :  " + s.getStudentId()  + "\n" +
            "  Full Name     :  " + s.getName()        + "\n" +
            "  Age           :  " + s.getAge()         + "\n" +
            "  Gender        :  " + s.getGender()      + "\n" +
            "  Department    :  " + s.getDepartment()  + "\n" +
            "  Year          :  " + s.getYear()        + "\n" +
            "  CGPA          :  " + String.format("%.2f", s.getCgpa()) + "\n" +
            "────────────────────────────────────";
        JOptionPane.showMessageDialog(this, info,
            "👁  Student Details — " + s.getName(), JOptionPane.INFORMATION_MESSAGE);
    }

    // ════════════════════════════════════════════════════════
    //  TABLE HELPERS
    // ════════════════════════════════════════════════════════
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : service.viewAllStudents()) {
            String yr = s.getYear() + (s.getYear()==1?"st":s.getYear()==2?"nd":s.getYear()==3?"rd":"th") + " Year";
            tableModel.addRow(new Object[]{
                "  " + s.getStudentId(), "  " + s.getName(),
                "  " + s.getAge(),       "  " + s.getGender(),
                "  " + s.getDepartment(),"  " + yr,
                "  " + String.format("%.2f", s.getCgpa())
            });
        }
    }

    private void filterTable() {
        String q = searchField.getText().toLowerCase().trim();
        tableModel.setRowCount(0);
        for (Student s : service.viewAllStudents()) {
            if (s.getName().toLowerCase().contains(q)
             || s.getDepartment().toLowerCase().contains(q)
             || String.valueOf(s.getStudentId()).contains(q)) {
                String yr = s.getYear() + (s.getYear()==1?"st":s.getYear()==2?"nd":s.getYear()==3?"rd":"th") + " Year";
                tableModel.addRow(new Object[]{
                    "  " + s.getStudentId(), "  " + s.getName(),
                    "  " + s.getAge(),       "  " + s.getGender(),
                    "  " + s.getDepartment(),"  " + yr,
                    "  " + String.format("%.2f", s.getCgpa())
                });
            }
        }
    }

    private void updateStats() {
        List<Student> list = service.viewAllStudents();
        valTotal.setText(String.valueOf(list.size()));
        long top   = list.stream().filter(s -> s.getCgpa() >= 9.0).count();
        valToppers.setText(String.valueOf(top));
        long depts = list.stream().map(Student::getDepartment).distinct().count();
        valDepts.setText(String.valueOf(depts));
    }

    // ════════════════════════════════════════════════════════
    //  FORM HELPERS
    // ════════════════════════════════════════════════════════
    private JTextField addRow(JPanel form, String label) {
        return addRowPre(form, label, "");
    }

    private JTextField addRowPre(JPanel form, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(GRAY);

        JTextField tf = new JTextField(value);
        styleField(tf);

        form.add(lbl);
        form.add(tf);
        return tf;
    }

    private JComboBox<String> addCombo(JPanel form, String label, String[] options) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(GRAY);

        JComboBox<String> cb = new JComboBox<>(options);
        cb.setBackground(BG);
        cb.setForeground(WHITE);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setFocusable(false);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                    JList<?> l, Object val, int idx, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, val, idx, sel, foc);
                setBackground(sel ? BLUE : BG);
                setForeground(WHITE);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return this;
            }
        });

        form.add(lbl);
        form.add(cb);
        return cb;
    }

    private void styleField(JTextField tf) {
        tf.setBackground(BG);
        tf.setForeground(WHITE);
        tf.setCaretColor(WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(7, 10, 7, 10)
        ));
    }

    private JDialog dialog(String title, int w, int h) {
        JDialog d = new JDialog(this, title, true);
        d.setSize(w, h);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        d.getContentPane().setBackground(CARD);
        return d;
    }

    private void msg(String text, String title, int type) {
        JOptionPane.showMessageDialog(this, text, title, type);
    }

    // ════════════════════════════════════════════════════════
    //  MAIN
    // ════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(StudentGUI::new);
    }
}
