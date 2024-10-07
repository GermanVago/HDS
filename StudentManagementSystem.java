import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentManagementSystem extends JFrame {
    private ArrayList<Student> students;
    private JTextArea displayArea;
    private JTextField nameField, idField;
    private JButton addButton, saveButton, loadButton;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        
        // Configuración de la interfaz gráfica
        setTitle("Sistema de Gestión de Estudiantes");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        
        nameField = new JTextField(15);
        idField = new JTextField(10);
        addButton = new JButton("Agregar Estudiante");
        saveButton = new JButton("Guardar a Archivo");
        loadButton = new JButton("Cargar de Archivo");

        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(addButton);
        inputPanel.add(saveButton);
        inputPanel.add(loadButton);

        // Área de visualización
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Agregar componentes al frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Agregar action listeners
        addButton.addActionListener(e -> addStudent());
        saveButton.addActionListener(e -> saveToFile());
        loadButton.addActionListener(e -> loadFromFile());
    }

    private void addStudent() {
        try {
            String name = nameField.getText();
            int id = Integer.parseInt(idField.getText());
            
            Student student = new Student(name, id);
            students.add(student);
            
            updateDisplay();
            nameField.setText("");
            idField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un ID válido");
        }
    }

    private void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Student student : students) {
            sb.append(student.toString()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            for (Student student : students) {
                writer.println(student.getName() + "," + student.getId());
            }
            JOptionPane.showMessageDialog(this, "Datos guardados exitosamente");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo");
        }
    }

    private void loadFromFile() {
        students.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                students.add(new Student(parts[0], Integer.parseInt(parts[1])));
            }
            updateDisplay();
            JOptionPane.showMessageDialog(this, "Datos cargados exitosamente");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el archivo");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentManagementSystem().setVisible(true);
        });
    }
}

class Student {
    private String name;
    private int id;
    private LocalDate enrollmentDate;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.enrollmentDate = LocalDate.now();
    }

    public String getName() { return name; }
    public int getId() { return id; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Estudiante: %s (ID: %d) - Inscrito: %s", 
            name, id, enrollmentDate.format(formatter));
    }
}