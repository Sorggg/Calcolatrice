package form;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {
    private JButton submit;
    private JPanel loginPanel;
    private JTextField username;
    private JLabel userNameLabel;
    private JTextField password;
    private JLabel passwordLabel;
    static Connection conn1 = null;

    public Login() {
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strSelect = "select * from Users";
                Statement stmt = null;
                try {
                    stmt = conn1.createStatement();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    ResultSet rset = stmt.executeQuery(strSelect);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if(1==2){

                }
                else{
                    JFrame jFrame = new JFrame();
                    JOptionPane.showMessageDialog(jFrame, "Username o password sbagliati");
                }
            }
        });
    }

    public static void main(String[] args) {

        try {
             conn1 = DriverManager.getConnection("jdbc:mysql://127.0.0.1/Calcolatrice", "root", "");
            if (conn1.isValid(5)) {
                System.out.println("Connection is successful.");
                JFrame frame = new JFrame("Login");
                frame.setContentPane(new Login().loginPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            } else {
                System.out.println("Connection is not valid.");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed. Error message: " + e.getMessage());
        }
    }
}
