package edu.umg;

import edu.umg.PrincipalFrame;
import java.sql.PreparedStatement;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Inicio de Sesión");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Usuario:");
        JLabel passwordLabel = new JLabel("Contraseña:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Iniciar Sesión");

        // ... (código de configuración de colores)

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if (autenticar(username, password)) {
                abrirVentanaPrincipal();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }

            Arrays.fill(passwordChars, ' '); // Borra la contraseña en memoria
            passwordField.setText(""); // Borra la contraseña visible en el campo
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean autenticar(String username, String password) {
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String usuarioDB = "postgres";
            String contrasenaDB = "Yami2001";

            Connection conexion = DriverManager.getConnection(url, usuarioDB, contrasenaDB);

            // Consulta SQL para verificar las credenciales
            String sql = "SELECT * FROM login WHERE username = ? AND password = ?";

            try (PreparedStatement statement = (PreparedStatement) conexion.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Las credenciales son correctas
                    resultSet.close();
                    return true;
                }

                resultSet.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Manejo de errores

            // Imprime el mensaje de error en la consola y en un mensaje emergente
            System.out.println("Error en la consulta SQL: " + ex.getMessage());
            JOptionPane.showMessageDialog(LoginFrame.this, "Error en la consulta SQL: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false; // Autenticación fallida por defecto
    }

    private void abrirVentanaPrincipal() {
        SwingUtilities.invokeLater(() -> {
            PrincipalFrame principalFrame = new PrincipalFrame();
            principalFrame.setVisible(true);
        });
        dispose(); // Cierra la ventana de inicio de sesión
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}