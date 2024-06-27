import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {
    private JPanel mainPanel;
    private JTextField studentNumber;
    private JEditorPane RulesPanel;
    private JCheckBox acceptBox;
    private JButton TakeExamBtn;
    private JComboBox<String> examBox;
    private JButton logoutButton;

    public MainScreen(String userID, String role) {
        setTitle("Main Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(null);  // Center the frame

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));

        studentNumber = new JTextField();
        mainPanel.add(new JLabel("Student Number:"));
        mainPanel.add(studentNumber);

        RulesPanel = new JEditorPane();
        RulesPanel.setText("Exam Rules:\n1. No cheating\n2. No talking\n...");
        RulesPanel.setEditable(false);
        mainPanel.add(new JLabel("Rules:"));
        mainPanel.add(new JScrollPane(RulesPanel));  // Use JScrollPane to make it scrollable

        acceptBox = new JCheckBox("I accept the rules");
        mainPanel.add(acceptBox);

        examBox = new JComboBox<>();
        examBox.addItem("Math Exam");
        examBox.addItem("Science Exam");
        mainPanel.add(new JLabel("Select Exam:"));
        mainPanel.add(examBox);

        TakeExamBtn = new JButton("Take Exam");
        mainPanel.add(TakeExamBtn);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the main screen
                new Login(null);  // Show login screen again
            }
        });
        mainPanel.add(logoutButton);

        setContentPane(mainPanel);
        pack();  // Adjust the frame size based on the preferred sizes of its components
        setVisible(true);
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
