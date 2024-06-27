import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JDialog {
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelBtn;
    private JPanel loginPanel;
    private JComboBox<String> choiceBox;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Login Here");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(500, 424));   // JFrame minimum size
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // Terminate dialog when clicking on close button

        // Clear and populate the role combo box
        choiceBox.removeAllItems();
        choiceBox.addItem("Student");
        choiceBox.addItem("Teacher");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) choiceBox.getSelectedItem();

                if (email.isEmpty() || password.isEmpty() || role == null) {
                    JOptionPane.showMessageDialog(Login.this, "Please enter all fields", "Try Again", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String userID = authenticateUser(email, password, role);
                if (userID != null) {
                    dispose();  // Close the login dialog
                    new MainScreen(userID, role);  // Open the main screen
                } else {
                    JOptionPane.showMessageDialog(Login.this, "Invalid credentials", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private String authenticateUser(String email, String password, String role) {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String sql = "SELECT id FROM " + (role.equals("Student") ? "users" : "teachers") + " WHERE email=? AND password=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("id"); // Return user ID
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Login login = new Login(null);
    }
}
