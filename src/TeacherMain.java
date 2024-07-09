import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeacherMain extends JFrame {
    private String userID;
    private JPanel mainPanel;
    private JTextField examCodeField;
    private JTextField durationField;
    private JButton addExamButton;
    private JComboBox<String> deleteExamBox;
    private JButton deleteExamButton;
    private JButton logoutButton;

    public TeacherMain(String userID) {
        this.userID = userID;
        setTitle("Teacher Main Screen");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        loadExamsIntoComboBox();
        setVisible(true);
        initializeListeners();
    }

    private void initializeListeners() {
        addExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String examCode = examCodeField.getText();
                String durationStr = durationField.getText();
                if (examCode.isEmpty() || durationStr.isEmpty()) {
                    JOptionPane.showMessageDialog(TeacherMain.this, "Please enter both exam code and duration", "Try Again", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int duration = Integer.parseInt(durationStr);
                    addExamToDatabase(examCode, duration);
                    loadExamsIntoComboBox();  // Refresh the combo box with updated exams
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TeacherMain.this, "Please enter a valid number for duration", "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedExam = (String) deleteExamBox.getSelectedItem();
                if (selectedExam != null) {
                    deleteExamFromDatabase(selectedExam);
                    loadExamsIntoComboBox();  // Refresh the combo box with updated exams
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the main screen
                new Login(null);  // Show login screen again
            }
        });
    }

    private void loadExamsIntoComboBox() {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT exam_name FROM exams";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            deleteExamBox.removeAllItems();
            while (resultSet.next()) {
                deleteExamBox.addItem(resultSet.getString("exam_name"));
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

    private void addExamToDatabase(String examCode, int duration) {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO exams (exam_name, duration) VALUES (?, ?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, examCode);
            preparedStatement.setInt(2, duration);

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Exam added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding exam", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteExamFromDatabase(String examName) {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "DELETE FROM exams WHERE exam_name=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, examName);

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Exam deleted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting exam", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherMain("12345"));
    }
}
