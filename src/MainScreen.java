import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainScreen extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> examBox;
    private JTextField studentNumber;
    private JEditorPane rulesPanel;
    private JCheckBox acceptBox;
    private JButton takeExamButton;
    private JButton logoutButton;

    public MainScreen(String userID, String role) {
        setTitle("Main Screen");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(550, 420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame

        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the main screen
                new Login(null);  // Show login screen again
            }
        });

        // Load exams from database
        loadExamsFromDatabase();

        setVisible(true);
    }

    private void loadExamsFromDatabase() {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT exam_name FROM exams";  // Assuming you have an exams table with an exam_name column
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                examBox.addItem(resultSet.getString("exam_name"));
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainScreen("12345", "Student");
            }
        });
    }
}
