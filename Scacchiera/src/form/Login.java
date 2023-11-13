package form;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public class Login {
    private JButton login;
    private JPanel loginPanel;
    private JTextField usernameText;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JButton register;
    private JPasswordField passwordText;
    public static Connection conn1 = null;
    public static int logged;
    JFrame jFrame = new JFrame();

    public Login() {
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String strSelect = "select * from Users";

                ArrayList<Integer> id = new ArrayList<>();
                ArrayList<String> username = new ArrayList<>();
                ArrayList<String> password = new ArrayList<>();
                int ris = -1;
                try{
                    Statement stmt = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stmt.executeQuery("select * from Users");

                    boolean trovato = false;

                    while(rs.next() && trovato == false){

                        if(Objects.equals(rs.getString("Username"), usernameText.getText())&&Objects.equals(passwordText.getText(), rs.getString("Password"))){
                            trovato = true;
                            ris = rs.getInt("Id");
                        }

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(ris);
                if(ris > -1){
                    logged = ris;
                    JOptionPane.showMessageDialog(null, "Login effettuato con successo");
                    JFrame frame = new JFrame("Calcolatrice");
                    frame.setContentPane(new Calcolatrice().panel1);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }
                else{
                    JOptionPane.showMessageDialog(jFrame, "Username o password sbagliati");
                }
            }
        });
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String username = usernameText.getText();
                    String password = passwordText.getText();
                    Statement stmt = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    if(!Objects.equals(password, "") && !Objects.equals(username, "")){
                        String query = "insert into Users (Id,Username,Password) values (null,'"+username+"','"+password+"')";

                        int rs = stmt.executeUpdate(query);
                        JOptionPane.showMessageDialog(jFrame, "Nuovo utente inserito");
                    }
                    else {
                        JOptionPane.showMessageDialog(jFrame, "Uno dei campi è vuoto");
                    }

                } catch (Exception exception){
                    exception.printStackTrace();
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
