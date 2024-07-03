import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ExamQuestion extends JFrame {
    private JPanel questionPanel;
    private JLabel questionLabel;
    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;
    private JButton nextButton;
    private JButton submitButton;
    private JLabel timerLabel;
    private ButtonGroup optionsGroup;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int timeRemaining = 20 * 60;  // 20 minutes in seconds
    private List<Integer> userAnswers;  // Store user answers

    public ExamQuestion(List<Question> questions) {
        this.questions = questions;
        this.userAnswers = new ArrayList<>(questions.size());  // Initialize user answers list
        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add(Integer.valueOf(-1));  // Initialize all answers to -1 (no answer)
        }

        setTitle("Exam Question");
        setContentPane(questionPanel);
        setMaximumSize(new Dimension(700, 520));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        optionsGroup = new ButtonGroup();
        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);

        // Initialize with the first question
        loadQuestion(0);

        // Timer setup
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                updateTimerLabel();
                if (timeRemaining <= 0) {
                    ((Timer) e.getSource()).stop();
                    submitExam();
                }
            }
        });
        timer.start();

        nextButton.addActionListener(e -> nextQuestion());
        //previousButton.addActionListener(e -> previousQuestion());
        submitButton.addActionListener(e -> submitExam());

        setVisible(true);
    }

    private void loadQuestion(int questionIndex) {
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            return;
        }

        Question question = questions.get(questionIndex);
        questionLabel.setText(question.getQuestionText());
        option1.setText(question.getOptions().get(0));
        option2.setText(question.getOptions().get(1));
        option3.setText(question.getOptions().get(2));
        option4.setText(question.getOptions().get(3));

        optionsGroup.clearSelection();
        int userAnswer = userAnswers.get(questionIndex);
        if (userAnswer != -1) {
            switch (userAnswer) {
                case 0 -> option1.setSelected(true);
                case 1 -> option2.setSelected(true);
                case 2 -> option3.setSelected(true);
                case 3 -> option4.setSelected(true);
            }
        }

        // Enable/disable navigation buttons
        // previousButton.setEnabled(questionIndex > 0);
        nextButton.setEnabled(questionIndex < questions.size() - 1);
        submitButton.setVisible(questionIndex == questions.size() - 1);
    }

    private void nextQuestion() {
        saveUserAnswer();
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }

    private void previousQuestion() {
        saveUserAnswer();
        currentQuestionIndex--;
        loadQuestion(currentQuestionIndex);
    }

    private void saveUserAnswer() {
        int selectedOption = -1;
        if (option1.isSelected()) selectedOption = 0;
        else if (option2.isSelected()) selectedOption = 1;
        else if (option3.isSelected()) selectedOption = 2;
        else if (option4.isSelected()) selectedOption = 3;

        userAnswers.set(currentQuestionIndex, Integer.valueOf(selectedOption));
    }

    private void submitExam() {
        saveUserAnswer();  // Save the answer of the last question
        int score = calculateScore();
        JOptionPane.showMessageDialog(this, "Exam submitted! Your score: " + score);
        dispose();
        new MainScreen("12345", "Student");  // Navigate back to the main screen
    }

    private int calculateScore() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int correctAnswer = question.getCorrectAnswer();
            int userAnswer = userAnswers.get(i);
            if (userAnswer == correctAnswer) {
                score++;
            }
        }
        return score;
    }

    private void updateTimerLabel() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;

        // Manually format the string
        String timeString = "Time remaining: " +
                (minutes < 10 ? "0" : "") + minutes + ":" +
                (seconds < 10 ? "0" : "") + seconds;

        timerLabel.setText(timeString);
    }

    public static void main(String[] args) {
        // Example usage
        List<Question> questions = List.of(
                new Question("int a = 2 * 4; int b = 3; int c = a * b;", List.of("12", "24", "Error", "11"), 1),
                new Question("String text_1 = \"Hello\"; String text_2 = \"User\"; System.out.println(text_1 + text_2); What's the output:", List.of("HelloUser", "Hello User", "Error", "UserHello"), 0),
                new Question("What is the output of the following code? int[] arr = {1, 2, 3}; System.out.println(arr[1]);", List.of("1", "2", "3", "Error"), 1),
                new Question("Which of the following is a correct way to create an object of class Foo?", List.of("Foo obj = new Foo();", "Foo obj;", "Foo() obj = new Foo;", "Foo obj = Foo;"), 0),
                new Question("What is the output of the following code? String str = \"abcd\"; System.out.println(str.length());", List.of("3", "4", "5", "Error"), 1)
        );
        SwingUtilities.invokeLater(() -> new ExamQuestion(questions));
    }
}
