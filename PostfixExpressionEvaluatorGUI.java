import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class PostfixExpressionEvaluatorGUI extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JButton evaluateButton;

    public PostfixExpressionEvaluatorGUI() {
        setTitle("Dynamic Expression Evaluator (Postfix Based)");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JLabel titleLabel = new JLabel("Evaluate Arithmetic Expressions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        inputField.setBorder(BorderFactory.createTitledBorder("Enter Infix Expression"));
        centerPanel.add(inputField);

        evaluateButton = new JButton("Evaluate");
        evaluateButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        evaluateButton.setBackground(new Color(100, 149, 237));
        evaluateButton.setForeground(Color.WHITE);
        centerPanel.add(evaluateButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        outputArea.setBorder(BorderFactory.createTitledBorder("Result"));
        centerPanel.add(new JScrollPane(outputArea));

        add(centerPanel, BorderLayout.CENTER);

        evaluateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String infix = inputField.getText();
                try {
                    String postfix = infixToPostfix(infix);
                    int result = evaluatePostfix(postfix);
                    outputArea.setText("Postfix: " + postfix + "\nResult: " + result);
                } catch (Exception ex) {
                    outputArea.setText("Error: Invalid Expression\n" + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }

    private String infixToPostfix(String infix) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        String numberBuffer = "";

        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            if (Character.isWhitespace(ch)) {
                continue;
            }
            if (Character.isDigit(ch)) {
                numberBuffer += ch;  // collect number
            } else {
                if (!numberBuffer.isEmpty()) {
                    output.append(numberBuffer).append(' ');
                    numberBuffer = "";
                }
                if (ch == '(') {
                    stack.push(ch);
                } else if (ch == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        output.append(stack.pop()).append(' ');
                    }
                    if (!stack.isEmpty() && stack.peek() == '(') {
                        stack.pop();
                    }
                } else if (isOperator(ch)) {
                    while (!stack.isEmpty() && precedence(ch) <= precedence(stack.peek())) {
                        output.append(stack.pop()).append(' ');
                    }
                    stack.push(ch);
                }
            }
        }

        if (!numberBuffer.isEmpty()) {
            output.append(numberBuffer).append(' ');
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(' ');
        }

        return output.toString().trim();
    }

    private int evaluatePostfix(String postfix) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.matches("\\d+")) {
                stack.push(Integer.parseInt(token));
            } else if (token.length() == 1 && isOperator(token.charAt(0))) {
                int b = stack.pop();
                int a = stack.pop();
                stack.push(applyOperator(a, b, token.charAt(0)));
            }
        }

        return stack.pop();
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private int precedence(char ch) {
        if (ch == '+' || ch == '-') return 1;
        if (ch == '*' || ch == '/') return 2;
        return 0;
    }

    private int applyOperator(int a, int b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;  // Integer division
            default: throw new IllegalArgumentException("Invalid operator: " + op);
        }
    }

    public static void main(String[] args) {
        new PostfixExpressionEvaluatorGUI();
    }
}
