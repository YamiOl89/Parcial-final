package edu.umg;
import java.sql.PreparedStatement;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PrincipalFrame extends JFrame {
    private JButton studentDetailsButton;
    private JButton courseDetailsButton;
    private JButton registrationButton;

    public PrincipalFrame() {
        setTitle("Menú Principal");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        studentDetailsButton = new JButton("Detalles del Estudiante");
        courseDetailsButton = new JButton("Detalles del Curso");
        registrationButton = new JButton("Inscripción");

        // Establecer nuevos colores
        Color backgroundColor = new Color(173, 216, 230); // Azul Claro
        Color buttonColor = new Color(50, 205, 50); // Verde Lima

        panel.setBackground(backgroundColor);
        studentDetailsButton.setBackground(buttonColor);
        courseDetailsButton.setBackground(buttonColor);
        registrationButton.setBackground(buttonColor);

        studentDetailsButton.addActionListener(e -> abrirFormulario(new StudentDetailsFrame()));
        courseDetailsButton.addActionListener(e -> abrirFormulario(new CourseDetailsFrame()));
        registrationButton.addActionListener(e -> abrirFormulario(new RegistrationFrame()));

        panel.add(studentDetailsButton);
        panel.add(courseDetailsButton);
        panel.add(registrationButton);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void abrirFormulario(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PrincipalFrame::new);
    }
}
