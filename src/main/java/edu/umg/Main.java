package edu.umg;

import java.sql.PreparedStatement;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Main extends JFrame {
    private JButton studentDetailsButton;
    private JButton courseDetailsButton;
    private JButton registrationButton;
    private JButton readDataButton;
    private JButton deleteDataButton;

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Yami2001";

    public Main() {
        setTitle("Menú Principal");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        studentDetailsButton = new JButton("Detalles del Estudiante");
        courseDetailsButton = new JButton("Detalles del Curso");
        registrationButton = new JButton("Inscripción");
        readDataButton = new JButton("Leer Datos");
        deleteDataButton = new JButton("Eliminar Datos");

        // Establecer nuevos colores y tipos de letra...

        // Agregar acciones a los botones
        studentDetailsButton.addActionListener(e -> abrirFormulario(new StudentDetailsFrame()));
        courseDetailsButton.addActionListener(e -> abrirFormulario(new CourseDetailsFrame()));
        registrationButton.addActionListener(e -> abrirFormulario(new RegistrationFrame()));
        readDataButton.addActionListener(e -> abrirVentanaLeer());
        deleteDataButton.addActionListener(e -> abrirVentanaEliminar());

        panel.add(studentDetailsButton);
        panel.add(courseDetailsButton);
        panel.add(registrationButton);
        panel.add(readDataButton);
        panel.add(deleteDataButton);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void abrirFormulario(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    private void abrirVentanaLeer() {
        JFrame readFrame = new JFrame("Leer Datos");
        readFrame.setSize(400, 150);
        readFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel readPanel = new JPanel(new GridLayout(3, 2));
        JLabel studentLabel = new JLabel("Nombre del Estudiante:");
        JTextField studentField = new JTextField(20);
        JButton readStudentButton = new JButton("Leer Estudiantes");

        readPanel.add(studentLabel);
        readPanel.add(studentField);
        readPanel.add(readStudentButton);

        readFrame.add(readPanel);
        readFrame.setLocationRelativeTo(this);
        readFrame.setVisible(true);

        readStudentButton.addActionListener(e -> {
            String studentName = studentField.getText();
            // Realizar la consulta y obtener los detalles del estudiante
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String query = "SELECT * FROM estudiantes WHERE nombre = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, studentName);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            // Crear una nueva ventana para mostrar los detalles del estudiante
                            JFrame detailsFrame = new JFrame("Detalles del Estudiante");
                            detailsFrame.setSize(300, 150);

                            JPanel detailsPanel = new JPanel(new GridLayout(4, 2));
                            detailsPanel.add(new JLabel("ID:"));
                            detailsPanel.add(new JLabel(String.valueOf(resultSet.getInt("id_estudiante"))));
                            detailsPanel.add(new JLabel("Nombre:"));
                            detailsPanel.add(new JLabel(resultSet.getString("nombre")));
                            detailsPanel.add(new JLabel("Apellido:"));
                            detailsPanel.add(new JLabel(resultSet.getString("apellido")));
                            detailsPanel.add(new JLabel("Email:"));
                            detailsPanel.add(new JLabel(resultSet.getString("email")));

                            detailsFrame.add(detailsPanel);
                            detailsFrame.setLocationRelativeTo(null);
                            detailsFrame.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(readFrame, "No se encontraron datos para el estudiante: " + studentName, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(readFrame, "Error al leer datos del estudiante", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void abrirVentanaEliminar() {
        JFrame deleteFrame = new JFrame("Eliminar Datos");
        deleteFrame.setSize(400, 200);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel deletePanel = new JPanel(new GridLayout(3, 2));
        JLabel studentLabel = new JLabel("Nombre del Estudiante:");
        JTextField studentField = new JTextField(20);
        JButton deleteStudentButton = new JButton("Eliminar Estudiante");

        deletePanel.add(studentLabel);
        deletePanel.add(studentField);
        deletePanel.add(deleteStudentButton);

        deleteFrame.add(deletePanel);
        deleteFrame.setLocationRelativeTo(this);
        deleteFrame.setVisible(true);

        deleteStudentButton.addActionListener(e -> {
            String studentName = studentField.getText();
            eliminarEstudiante(studentName);
            JOptionPane.showMessageDialog(deleteFrame, "Estudiante eliminado: " + studentName, "Eliminar Estudiante", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void eliminarEstudiante(String studentName) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Primero eliminar inscripciones asociadas al estudiante
            String deleteInscripcionesQuery = "DELETE FROM inscripciones WHERE id_estudiante IN (SELECT id_estudiante FROM estudiantes WHERE nombre = ?)";
            try (PreparedStatement inscripcionesStatement = connection.prepareStatement(deleteInscripcionesQuery)) {
                inscripcionesStatement.setString(1, studentName);
                ((java.sql.PreparedStatement) inscripcionesStatement).executeUpdate();
            }

            // Luego, eliminar al estudiante
            String deleteEstudianteQuery = "DELETE FROM estudiantes WHERE nombre = ?";
            try (PreparedStatement estudianteStatement = connection.prepareStatement(deleteEstudianteQuery)) {
                estudianteStatement.setString(1, studentName);
                ((java.sql.PreparedStatement) estudianteStatement).executeUpdate();
            }

            System.out.println("Estudiante eliminado: " + studentName);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}