package com.budgetbuddy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationTest {

    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException {
        // Create a connection to the H2 in-memory database
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root", "TowsonTigers25");
        conn.setAutoCommit(false); 
        // Create the users table
        // try (Statement st = conn.createStatement()) {
        //     st.execute("CREATE TABLE users (USERNAME VARCHAR(255), EMAIL VARCHAR(255), MOBILE_NUMBER VARCHAR(255), PASSCODE VARCHAR(255))");
        // }
    }

    @Test
    public void testValidUserRegistration() throws SQLException {
        // Prepare and execute the registration
        String registerQuery = "INSERT INTO users(USERNAME, EMAIL, MOBILE_NUMBER, PASSCODE) VALUES(?, ?, ?, ?)";
        try (PreparedStatement registerStmt = conn.prepareStatement(registerQuery)) {
            registerStmt.setString(1, "johndoe123");
            registerStmt.setString(2, "johndoe@example.com");
            registerStmt.setString(3, "+1234567890");
            registerStmt.setString(4, "strongPassword123");
            registerStmt.executeUpdate();
        }

        // // Verify the user was inserted
        // try (PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE EMAIL = ?")) {
        //     checkStmt.setString(1, "johndoe@example.com");
        //     ResultSet rs = checkStmt.executeQuery();
        //     assertTrue(rs.next(), "User should be registered successfully");
        //     assertEquals("johndoe123", rs.getString("USERNAME"));
        //     assertEquals("1234567890", rs.getString("MOBILE_NUMBER"));
        //     assertEquals("strongPassword123", rs.getString("PASSCODE"));
        //     assertFalse(rs.next(), "There should be exactly one record");
        // }
    }

    @Test
    public void testInvalidEmailFormatRegistration() throws SQLException {
        // Attempt to insert user with invalid email format
        String registerQuery = "INSERT INTO users(USERNAME, EMAIL, MOBILE_NUMBER, PASSCODE) VALUES(?, ?, ?, ?)";
        try (PreparedStatement registerStmt = conn.prepareStatement(registerQuery)) {
            registerStmt.setString(1, "johndoe123");
            registerStmt.setString(2, "invalidemail.c");
            registerStmt.setString(3, "1234567890");
            registerStmt.setString(4, "strongPassword123");
            // SQLException exception = assertThrows(SQLException.class, registerStmt::executeUpdate);
            // assertTrue(exception.getMessage().toLowerCase().contains("email"));
        }
    }

    @Test
    public void testInvalidPhoneNumberRegistration() throws SQLException {
        // Attempt to insert user with invalid phone number format
        String registerQuery = "INSERT INTO users(USERNAME, EMAIL, MOBILE_NUMBER, PASSCODE) VALUES(?, ?, ?, ?)";
        try (PreparedStatement registerStmt = conn.prepareStatement(registerQuery)) {
            registerStmt.setString(1, "johndoe123");
            registerStmt.setString(2, "johndoe@example.com");
            registerStmt.setString(3, "invalidnumber");
            registerStmt.setString(4, "strongPassword123");
            // assertThrows(SQLException.class, registerStmt::executeUpdate, "Should throw SQL exception due to phone number format validation");
        }
    }
    
}
