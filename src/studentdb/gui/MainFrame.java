package studentdb.gui;

import studentdb.dao.StudentDAO;
import studentdb.model.Student;
import studentdb.util.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextField txtFirstName, txtLastName, txtAge, txtEmail, txtSearchId;
    private JButton btnAdd, btnView, btnSearch;
    private JTable tableStudents;
    private JTextArea txtStatus;
    private StudentTableModel tableModel;
    private StudentDAO studentDAO;

    public MainFrame() {
        studentDAO = new StudentDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("StudentDB Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ---------------- Top Panel ----------------
        JPanel topPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        topPanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField(); topPanel.add(txtFirstName);

        topPanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField(); topPanel.add(txtLastName);

        topPanel.add(new JLabel("Age:"));
        txtAge = new JTextField(); topPanel.add(txtAge);

        topPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(); topPanel.add(txtEmail);

        topPanel.add(new JLabel("Search by ID:"));
        txtSearchId = new JTextField(); topPanel.add(txtSearchId);

        btnSearch = new JButton("Search"); topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // ---------------- Table ----------------
        tableModel = new StudentTableModel();
        tableStudents = new JTable(tableModel);
        add(new JScrollPane(tableStudents), BorderLayout.CENTER);

        // ---------------- Bottom Panel ----------------
        JPanel bottomPanel = new JPanel(new BorderLayout(5,5));
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Student");
        btnView = new JButton("View All Students");
        buttonPanel.add(btnAdd); buttonPanel.add(btnView);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        txtStatus = new JTextArea(5,50);
        txtStatus.setEditable(false);
        bottomPanel.add(new JScrollPane(txtStatus), BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------------- Event Listeners ----------------
        btnAdd.addActionListener(e -> addStudent());
        btnView.addActionListener(e -> viewAllStudents());
        btnSearch.addActionListener(e -> searchStudent());

        setVisible(true);
    }

    private void appendStatus(String msg){
        txtStatus.append(msg + "\n");
        System.out.println(msg); // also print to console
    }

    private void addStudent() {
        String fName = txtFirstName.getText().trim();
        String lName = txtLastName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String email = txtEmail.getText().trim();

        if(!Validator.isNonEmpty(fName) || !Validator.isNonEmpty(lName) ||
                !Validator.isValidAge(ageStr) || !Validator.isValidEmail(email)){
            appendStatus("Error: Please enter valid student information.");
            return;
        }

        int age = Integer.parseInt(ageStr);
        Student student = new Student(fName, lName, age, email);

        new SwingWorker<Boolean, Void>(){
            @Override
            protected Boolean doInBackground() {
                return studentDAO.addStudent(student);
            }
            @Override
            protected void done(){
                try{
                    boolean success = get();
                    if(success){
                        appendStatus("Student added successfully with ID: " + student.getId());
                        clearInputFields();
                        viewAllStudents();
                    } else {
                        appendStatus("Error: Could not add student. Check console for details.");
                    }
                } catch(Exception ex){
                    ex.printStackTrace(); // Full exception in console
                    appendStatus("Exception: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void viewAllStudents() {
        new SwingWorker<List<Student>, Void>(){
            @Override
            protected List<Student> doInBackground() {
                return studentDAO.getAllStudents();
            }
            @Override
            protected void done(){
                try{
                    List<Student> students = get();
                    tableModel.setStudents(students);
                    appendStatus("Loaded " + students.size() + " students.");
                } catch(Exception ex){
                    ex.printStackTrace();
                    appendStatus("Error loading students: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void searchStudent() {
        String idStr = txtSearchId.getText().trim();
        if(!Validator.isNonEmpty(idStr)){
            appendStatus("Error: Please enter a valid ID to search.");
            return;
        }
        int id;
        try { id = Integer.parseInt(idStr); }
        catch(NumberFormatException e){
            appendStatus("Error: ID must be a number.");
            return;
        }

        new SwingWorker<Student, Void>(){
            @Override
            protected Student doInBackground() {
                return studentDAO.getStudentById(id);
            }
            @Override
            protected void done(){
                try{
                    Student s = get();
                    if(s != null){
                        tableModel.setStudents(List.of(s));
                        appendStatus("Found student: ID " + s.getId());
                    } else {
                        appendStatus("Student with ID " + id + " not found.");
                        tableModel.setStudents(List.of());
                    }
                } catch(Exception ex){
                    ex.printStackTrace();
                    appendStatus("Error: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void clearInputFields(){
        txtFirstName.setText("");
        txtLastName.setText("");
        txtAge.setText("");
        txtEmail.setText("");
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
