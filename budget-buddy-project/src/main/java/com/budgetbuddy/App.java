package com.budgetbuddy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App extends JFrame {

    public JTextField usernameField;
    public JPasswordField passwordField;

    public App() {
        setTitle("User Registration / Sign-In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new BorderLayout());
 
 
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);
 
 
        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);
 
 
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
 
 
        JButton signInButton = new JButton("Sign In");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signInUser();
            }
        });
            
 
        inputPanel.add(registerButton);
        inputPanel.add(signInButton);
 
 
        add(inputPanel, BorderLayout.CENTER);
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy","root", "yourpassword");
    }

    public void setUsernameField(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.contains(" ")) {
            JOptionPane.showMessageDialog(this, "Username cannot contain spaces!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check username length
        if (username.length() < 4 || username.length() > 20) {
            JOptionPane.showMessageDialog(this, "Username must be between 4 and 20 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check allowed characters in username
        if (!username.matches("^[a-zA-Z0-9_.-]*$")) {
            JOptionPane.showMessageDialog(this, "Username contains invalid characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO users(USERNAME, PASSCODE) VALUES(?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User registered successfully!");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error registering user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void signInUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM users WHERE USERNAME = ? AND PASSCODE = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Sign in successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error signing in: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isValidUsernameAndPassword(String username, String password) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // returns true if there's at least one match
            }
        }
        return false;
    }


    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
    
    public void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
