import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MainScreen extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> examBox;
    private JTextField studentNumber;
    private JEditorPane rulesPanel;
    private JCheckBox acceptBox;
    private JButton takeExamButton;
    private JButton logoutButton;
    private JLabel MyDetailsLabel;
    private JLabel MyResultsLabel;

    public MainScreen(String userID, String role) {
        setTitle("Main Screen");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(520, 420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame

        // Make the studentNumber field non-editable and set the user ID
        studentNumber.setText(userID);
        studentNumber.setEditable(false);

        // Make the JEditorPane non-editable
        rulesPanel.setEditable(false);

        // Logout button action
        logoutButton.addActionListener(e -> {
            dispose();  // Close the main screen
            new Login(null);  // Show login screen again
        });

        // Add action listener to "My Details" label
        MyDetailsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MyDetails(userID);
                dispose();  // Close the main screen
            }
        });

        // Add action listener to "My Results" label
        MyResultsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MyResults(userID);
                dispose();  // Close the main screen
            }
        });

        // Load exams from database
        loadExamsFromDatabase();

        // Disable the "Take Exam" button until the checkbox is ticked
        takeExamButton.setEnabled(false);
        acceptBox.addActionListener(e -> takeExamButton.setEnabled(acceptBox.isSelected()));

        // Take exam button action
        takeExamButton.addActionListener(e -> {
            List<Question> questions = List.of(
                    new Question("int a = 2 * 4; int b = 3; int c = a * b;", List.of("12", "24", "Error", "11"), 1),
                    new Question("String text_1 = \"Hello\"; String text_2 = \"User\"; System.out.println(text_1 + text_2); What's the output:", List.of("HelloUser", "Hello User", "Error", "UserHello"), 0),
                    new Question("What is the output of the following code? int[] arr = {1, 2, 3}; System.out.println(arr[1]);", List.of("1", "2", "3", "Error"), 1),
                    new Question("Which of the following is a correct way to create an object of class Foo?", List.of("Foo obj = new Foo();", "Foo obj;", "Foo() obj = new Foo;", "Foo obj = Foo;"), 0),
                    new Question("What is the output of the following code? String str = \"abcd\"; System.out.println(str.length());", List.of("3", "4", "5", "Error"), 1)
            );
            String selectedExam = (String) examBox.getSelectedItem();
            new ExamQuestion(questions, userID, selectedExam);  // Pass the userID and selected exam name
            dispose();  // Close the main screen
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
        SwingUtilities.invokeLater(() -> new MainScreen("12345", "Student"));
    }
}
