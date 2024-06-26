import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Registration extends JDialog {
    private JTextField userName;
    private JTextField userEmail;
    private JTextField userPhone;
    private JPasswordField userPassword;  // JPassword so that it doesn't reveal the user's password while typing
    private JPasswordField userConfirmPassword;
    private JButton registerButton;
    private JButton cancelButton;
    private JPanel RegisterPanel;

    public Registration(JFrame parent) {
        super(parent);
        setTitle("Create A New Account");
        setContentPane(RegisterPanel);
        setMinimumSize(new Dimension(500, 424));   // JFrame minimum size
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = userName.getText();
        String email = userEmail.getText();
        String phone = userPhone.getText();
        String password = new String(userPassword.getPassword());
        String confirmPassword = new String(userConfirmPassword.getPassword());

        // Regular expression patterns for validation
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phonePattern = "\\d+";
        int minimumPhoneNo = 7;

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields", "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.matches(emailPattern)) {
            JOptionPane.showMessageDialog(this, "Invalid Email Format", "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phone.matches(phonePattern) || phone.length() < minimumPhoneNo) {
            JOptionPane.showMessageDialog(this, "Phone number should be above seven digits and only contain digits", "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords Do Not Match", "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUserToDatabase(name, email, phone, password);
        if (user != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed To Register", "Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;

    private User addUserToDatabase(String name, String email, String phone, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam"; // Update with your database name
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!"; // Update with your database password

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String sql = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.password = password;
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return user;
    }

    public static void main(String[] args) {
        Registration regForm = new Registration(null);
        User user = regForm.user;
        if (user != null) {
            System.out.println("Successful Registration Of: " + user.name);
        } else {
            System.out.println("Registration Failed");
        }
    }
}
