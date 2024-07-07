import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ExamSubmission {
    private String userID;
    private String examName;
    private int score;
    private LocalDate writtenDate;

    public ExamSubmission(String userID, String examName, int score) {
        this.userID = userID;
        this.examName = examName;
        this.score = score;
        this.writtenDate = LocalDate.now(); // Use the current date as the written date

    }

    public void saveResultsToDatabase() {
        final String DB_URL = "jdbc:mysql://localhost:3306/onlineexam";
        final String USERNAME = "root";
        final String PASSWORD = "SlyferRose16th!!";

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO results (student_id, exam_name, score, written_date) VALUES (?, ?, ?, ?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, examName);
            preparedStatement.setInt(3, score);
            preparedStatement.setDate(4, java.sql.Date.valueOf(writtenDate));

            preparedStatement.executeUpdate();

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
    }
}
