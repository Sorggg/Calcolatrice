package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class Calcolatrice {
    private boolean RPN = false;
    public JPanel panel1;
    private JButton division;
    private JButton multiplication;
    private JButton minus;
    private JButton plus;
    private JButton equal;
    private JButton zero;
    private JButton mode;
    private JButton one;
    private JButton two;
    private JButton three;
    private JButton four;
    private JButton five;
    private JButton six;
    private JButton seven;
    private JButton eight;
    private JButton nine;
    private JTextField screen;
    private JButton history;
    JFrame jFrame = new JFrame();

    public Calcolatrice() {


        seven.addActionListener(e -> screen.setText(screen.getText() + "7"));
        eight.addActionListener(e -> screen.setText(screen.getText() + "8"));
        nine.addActionListener(e -> screen.setText(screen.getText() + "9"));
        division.addActionListener(e -> screen.setText(screen.getText() + "/"));
        four.addActionListener(e -> screen.setText(screen.getText() + "4"));
        five.addActionListener(e -> screen.setText(screen.getText() + "5"));
        six.addActionListener(e -> screen.setText(screen.getText() + "6"));
        multiplication.addActionListener(e -> screen.setText(screen.getText() + "*"));
        one.addActionListener(e -> screen.setText(screen.getText() + "1"));
        two.addActionListener(e -> screen.setText(screen.getText() + "2"));
        three.addActionListener(e -> screen.setText(screen.getText() + "3"));
        minus.addActionListener(e -> screen.setText(screen.getText() + "-"));
        mode.addActionListener(e -> {
            if(RPN){
                mode.setText("Norm");
                RPN = false;
                screen.setText(toNorm(screen.getText()));
            }
            else{
                mode.setText("RPN");
                RPN = true;
                screen.setText(toRPN(screen.getText()));
            }
        });

        zero.addActionListener(e -> screen.setText(screen.getText() + "0"));
        equal.addActionListener(e -> {
            if (RPN){

                try{
                    String expression = toNorm(screen.getText());
                    String result = String.valueOf(solveRPN(screen.getText()));
                    Statement stmt = Login.conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    if(!Objects.equals(expression, "") && !Objects.equals(result, "")){
                        String query = "insert into History (Id,Expression,Result) values ('" + Login.logged + "','" + expression + "','" + result +"')";

                        int rs = stmt.executeUpdate(query);
                    }
                    else {
                        JOptionPane.showMessageDialog(jFrame, "Uno dei campi è vuoto");
                    }

                } catch (Exception exception){
                    exception.printStackTrace();
                }
                screen.setText(String.valueOf(solveRPN(screen.getText())));
            }
            else {

                try{
                    String expression = screen.getText();
                    String result = String.valueOf(solveNorm(screen.getText()));
                    Statement stmt = Login.conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    if(!Objects.equals(expression, "") && !Objects.equals(result, "")){
                        String query = "insert into History (Id,Expression,Result) values ('" + Login.logged + "','" + expression + "','" + result +"')";

                        int rs = stmt.executeUpdate(query);
                    }
                    else {
                        JOptionPane.showMessageDialog(jFrame, "Uno dei campi è vuoto");
                    }

                } catch (Exception exception){
                    exception.printStackTrace();
                }
                screen.setText(String.valueOf(solveNorm(screen.getText())));

            }

        });
        plus.addActionListener(e -> screen.setText(screen.getText() + "+"));
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //Statement stmt = Login.conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    //ResultSet rs = stmt.executeQuery("SELECT * FROM History WHERE here Id = '" + Login.logged + "')");
                    String query = "SELECT Expression, Result FROM history WHERE Id = ?";
                    PreparedStatement stmt = Login.conn1.prepareStatement(query);
                    stmt.setString(1, String.valueOf(Login.logged));
                    ResultSet rs = stmt.executeQuery();

                    StringBuilder message = new StringBuilder("History:\n");
                    while (rs.next()) {
                        String expression = rs.getString("Expression");
                        float result = rs.getInt("Result");
                        message.append(expression).append(" = ").append(result).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, message.toString());
                } catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
    }
    public String toNorm(String espressioneRPN) {
        Stack<String> stack = new Stack<>();

        for (String token : espressioneRPN.split(" ")) {
            if (isOperator(token.charAt(0))) {
                String operand2 = stack.pop();
                String operand1 = stack.pop();
                String risultato = " " + operand1 + token + operand2 + " ";
                stack.push(risultato);
            } else {
                stack.push(token);
            }
        }

        if (stack.size() == 1) {
            return stack.pop();
        } else {
            return "Errore: espressione non valida";
        }
    }



    public  String toRPN(String espressioneInfix) {
        StringBuilder espressioneRPN = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();
        boolean inNumero = false;  // Utilizzato per tenere traccia se si sta analizzando un numero.

        for (char carattere : espressioneInfix.toCharArray()) {
            if (Character.isDigit(carattere)) {
                if (inNumero) {
                    // Concatena il carattere al numero corrente.
                    espressioneRPN.append(carattere);
                } else {
                    inNumero = true;
                    espressioneRPN.append(carattere); // Inizia un nuovo numero
                }
            } else {
                if (inNumero) {
                    espressioneRPN.append(" "); // Aggiungi uno spazio dopo il numero
                    inNumero = false;
                }

                if (carattere == '(') {
                    operatorStack.push(carattere);
                } else if (carattere == ')') {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                        espressioneRPN.append(operatorStack.pop());
                        espressioneRPN.append(" ");
                    }
                    if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                        operatorStack.pop();
                    }
                } else if (isOperator(carattere)) {
                    while (!operatorStack.isEmpty() && precedence(carattere) <= precedence(operatorStack.peek())) {
                        espressioneRPN.append(operatorStack.pop());
                        espressioneRPN.append(" ");
                    }
                    operatorStack.push(carattere);
                }
            }
        }

        // Aggiungi spazi tra operatori se non ci sono spazi
        if (inNumero) {
            espressioneRPN.append(" ");
        }

        while (!operatorStack.isEmpty()) {
            espressioneRPN.append(operatorStack.pop());
            espressioneRPN.append(" ");
        }

        return espressioneRPN.toString().trim();
    }



    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }


    private static int precedence(char operatore) {
        return switch (operatore) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }
    int solveRPN(String exrpession){
        Stack<Integer> calcStack = new Stack<>();

        for (String token : exrpession.split("\\s+")) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                calcStack.push(Integer.parseInt(token));
            } else if (isOperator(token.charAt(0))) {
                int op2 = calcStack.pop();
                int op1 = calcStack.pop();
                int ris = applyOperator(op1, token.charAt(0), op2);
                calcStack.push(ris);
            }
        }

        return calcStack.pop();
    }
    int solveNorm(String expression){
        return solveRPN(toRPN(expression));
    }
    private static int applyOperator(int op1, char operatore, int op2) {
        return switch (operatore) {
            case '+' -> op1 + op2;
            case '-' -> op1 - op2;
            case '*' -> op1 * op2;
            case '/' -> {
                if (op2 == 0) {
                    throw new ArithmeticException("Divisione per zero");
                }
                yield op1 / op2;
            }
            default -> throw new IllegalArgumentException("Operatore sconosciuto: " + operatore);
        };
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Calcolatrice");
        frame.setContentPane(new Calcolatrice().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        System.out.println("Connection failed. Error message: ");
    }
}
