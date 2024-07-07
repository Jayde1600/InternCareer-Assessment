import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyResults extends JFrame {
    private JPanel resultsPanel;
    private JTextField studentNumberField;
    private JComboBox<String> examNameBox;
    private JTextField examResultsField;
    private JTextField writtenDateField;
    private JButton logoutButton;

    private String userID;

    public MyResults(String userID) {
        this.userID = userID;
        setTitle("My Results");
        setContentPane(resultsPanel);
        setMinimumSize(new Dimension(520, 420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame

        // Make the studentNumber field non-editable and set the user ID
        studentNumberField.setText(userID);
        studentNumberField.setEditable(false);

        // Make other fields non-editable
        examResultsField.setEditable(false);
        writtenDateField.setEditable(false);

        // Load exams and results from the database
        loadExamsFromDatabase();

        examNameBox.addActionListener(e -> loadResults());

        // Logout button action
        logoutButton.addActionListener(e -> {
            dispose();  // Close the MyResults screen
            new Login(null);  // Show login screen again
        });

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
            String sql = "SELECT DISTINCT exam_name FROM results WHERE student_id = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                examNameBox.addItem(resultSet.getString("exam_name"));
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

    private void loadResults() {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String selectedExamName = (String) examNameBox.getSelectedItem();

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT score, written_date FROM results WHERE student_id = ? AND exam_name = ?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, selectedExamName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int score = resultSet.getInt("score");
                String writtenDate = resultSet.getString("written_date");
                int percentage = (score * 100) / 5;  // Assuming 5 questions in each exam

                examResultsField.setText(percentage + "%");
                writtenDateField.setText(writtenDate);
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
        SwingUtilities.invokeLater(() -> {
            new MyResults("1").setVisible(true);  // Example userID
        });
    }
}
