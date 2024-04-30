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
    public JTextField emailField;
    public JTextField mobileNumberField;
    public JPasswordField passwordField;
    
    public App() {
        setTitle("Budget buddy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Mobile Number:"));
        mobileNumberField = new JTextField();
        inputPanel.add(mobileNumberField);

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

        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                forgotPassword();
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
        inputPanel.add(forgotPasswordButton);
        inputPanel.add(signInButton);
        add(inputPanel, BorderLayout.CENTER);
    }

    public String getUsernameFromTextField() {
    // Assuming you have a JTextField instance named usernameField where users enter their username
    return usernameField.getText(); // Replace usernameField with the actual name of your JTextField
}

    

public void registerUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String mobileNumber = mobileNumberField.getText();
        String password = new String(passwordField.getPassword());

        // Check if email ends with ".com"
        if (!email.endsWith(".com")) {
            JOptionPane.showMessageDialog(this, "Email must end with '.com'. Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root", "TowsonTigers25")) {
            // Check if email is already in use
            String emailCheckQuery = "SELECT * FROM users WHERE EMAIL = ?";
            PreparedStatement emailCheckStmt = conn.prepareStatement(emailCheckQuery);
            emailCheckStmt.setString(1, email);
            ResultSet emailCheckResult = emailCheckStmt.executeQuery();
            if (emailCheckResult.next()) {
                JOptionPane.showMessageDialog(this, "Email already in use. Please use a different email address.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Register the user
            String registerQuery = "INSERT INTO users(USERNAME, EMAIL, MOBILE_NUMBER, PASSCODE) VALUES(?, ?, ?, ?)";
            PreparedStatement registerStmt = conn.prepareStatement(registerQuery);
            registerStmt.setString(1, username);
            registerStmt.setString(2, email);
            registerStmt.setString(3, mobileNumber);
            registerStmt.setString(4, password);
            registerStmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User registered successfully!");
            // Navigate to home screen or perform any other action here
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error registering user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void forgotPassword() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String mobileNumber = mobileNumberField.getText();

        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root", "TowsonTigers25")) {
            // Check if username exists
            String usernameCheckQuery = "SELECT * FROM users WHERE USERNAME = ?";
            PreparedStatement usernameCheckStmt = conn.prepareStatement(usernameCheckQuery);
            usernameCheckStmt.setString(1, username);
            ResultSet usernameCheckResult = usernameCheckStmt.executeQuery();
            if (!usernameCheckResult.next()) {
                JOptionPane.showMessageDialog(this, "Username not found. Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if email or mobile number matches the user
            String getUserQuery = "SELECT * FROM users WHERE USERNAME = ? AND (EMAIL = ? OR MOBILE_NUMBER = ?)";
            PreparedStatement getUserStmt = conn.prepareStatement(getUserQuery);
            getUserStmt.setString(1, username);
            getUserStmt.setString(2, email);
            getUserStmt.setString(3, mobileNumber);
            ResultSet getUserResult = getUserStmt.executeQuery();
            if (getUserResult.next()) {
                String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
                if (newPassword != null && !newPassword.isEmpty()) {
                    String resetPasswordQuery = "UPDATE users SET PASSCODE = ? WHERE USERNAME = ?";
                    PreparedStatement resetPasswordStmt = conn.prepareStatement(resetPasswordQuery);
                    resetPasswordStmt.setString(1, newPassword);
                    resetPasswordStmt.setString(2, username);
                    resetPasswordStmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Password reset successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or mobile number for the provided username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error resetting password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void signInUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
    
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root", "TowsonTigers25")) {
            // Check if username and password match
            String signInQuery = "SELECT * FROM users WHERE USERNAME = ? AND PASSCODE = ?";
            PreparedStatement signInStmt = conn.prepareStatement(signInQuery);
            signInStmt.setString(1, username);
            signInStmt.setString(2, password);
            ResultSet signInResult = signInStmt.executeQuery();
            if (signInResult.next()) {
                // Open the budget setting page after successful sign-in
                openBudgetSettingPage();
                JOptionPane.showMessageDialog(this, "Sign in successful!");
            } else {
                // If password is incorrect but username is correct, prompt user to reset password
                String forgotPasswordPrompt = "Password is incorrect. Do you want to reset your password?";
                int option = JOptionPane.showConfirmDialog(this, forgotPasswordPrompt, "Forgot Password", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    forgotPassword();
                }
            }
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error signing in: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy","root", "TowsonTigers25");
    }

    public void setUsernameField(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }
    private void openBudgetSettingPage() {
    String username = getUsernameFromTextField(); // Get the username from the usernameField
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            // Pass the username to the constructor of BudgetSettingPage
            new BudgetSettingPage(username).setVisible(true);
        }
    });
}
    
public void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void clearFields() {
        usernameField.setText("");
        emailField.setText("");
        mobileNumberField.setText("");
        passwordField.setText("");
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
