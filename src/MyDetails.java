import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyDetails extends JFrame {
    private JPanel detailsPanel;
    private JTextField nameField;
    private JTextField studentNumberField;
    private JTextField emailField;
    private JTextField phoneField;
    private JButton logoutButton;  // Add this line

    public MyDetails(String userID) {
        setTitle("My Details");
        setContentPane(detailsPanel);
        setMinimumSize(new Dimension(500, 400));  // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame

        // Make fields non-editable
        nameField.setEditable(false);
        studentNumberField.setEditable(false);
        emailField.setEditable(false);
        phoneField.setEditable(false);

        // Logout button action
        logoutButton.addActionListener(e -> {
            dispose();  // Close the details screen
            new Login(null);  // Show login screen again
        });

        // Load user details from the database
        loadUserDetails(userID);

        setVisible(true);
    }

    private void loadUserDetails(String userID) {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT name, id, email, phone FROM users WHERE id=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nameField.setText(resultSet.getString("name"));
                studentNumberField.setText(resultSet.getString("id"));
                emailField.setText(resultSet.getString("email"));
                phoneField.setText(resultSet.getString("phone"));
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        SwingUtilities.invokeLater(() -> new MyDetails("12345"));  // Replace "12345" with an actual userID
    }
}
