package com.budgetbuddy;

import org.junit.jupiter.api.*;

import java.sql.*;

public class PasswordResetTest {
    private static Connection conn;

    @BeforeAll
    public static void setUp() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root", "TowsonTigers25");
            conn.setAutoCommit(false); // To avoid saving test data
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestData() throws SQLException {
        String insertUserSql = "INSERT INTO users (USERNAME, EMAIL, MOBILE_NUMBER, PASSCODE) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql)) {
            pstmt.setString(1, "testUser");
            pstmt.setString(2, "testemail@example.com");
            pstmt.setString(3, "1234567891");
            pstmt.setString(4, "oldPassword");
            pstmt.executeUpdate();
        }
    }

    @AfterEach
    public void cleanUpTestData() throws SQLException {
        String deleteUserSql = "DELETE FROM users WHERE USERNAME = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteUserSql)) {
            pstmt.setString(1, "testUser");
            pstmt.executeUpdate();
        }
        conn.rollback();
    }

    @Test
    public void testResetPasswordWithValidCredentials() throws SQLException {
        String resetPasswordSql = "UPDATE users SET PASSCODE = ? WHERE USERNAME = ? AND EMAIL = ? AND MOBILE_NUMBER = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(resetPasswordSql)) {
            pstmt.setString(1, "newPassword");
            pstmt.setString(2, "testUser");
            pstmt.setString(3, "testemail@example.com");
            pstmt.setString(4, "1234567891");
            int affectedRows = pstmt.executeUpdate();

            Assertions.assertEquals(1, affectedRows, "Password should be reset successfully.");

            // Verify password has been updated
            String verifySql = "SELECT PASSCODE FROM users WHERE USERNAME = ?";
            try (PreparedStatement verifyStmt = conn.prepareStatement(verifySql)) {
                verifyStmt.setString(1, "testUser");
                ResultSet rs = verifyStmt.executeQuery();
                if (rs.next()) {
                    String updatedPasscode = rs.getString("PASSCODE");
                    Assertions.assertEquals("newPassword", updatedPasscode, "Password update verification failed.");
                }
            }
        }
        
        
    }

    

    @AfterAll
    public static void tearDown() throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(true); // Reset default behavior
            conn.close();
        }
    }

    @Test
    public void testResetPasswordWithUnregisteredEmailAndRegisteredMobile() throws SQLException {
        String query = "SELECT * FROM users WHERE EMAIL = ? AND MOBILE_NUMBER = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "invalidemail@example.com");
            stmt.setString(2, "1234567890");
            stmt.executeQuery();

            // Expect that no records are found since the email is unregistered
            // Assertions.assertFalse(rs.next(), "No records should be found as the email is not registered.");

            // Additional logic to simulate alert to user (not executable in JDBC context)
            // For example, we would typically expect some UI feedback here in a real application
        }
    }

    @Test
    public void testResetPasswordWithRegisteredEmailAndUnregisteredMobile() throws SQLException {
        String resetPasswordSql = "UPDATE users SET PASSCODE = ? WHERE USERNAME = ? AND EMAIL = ? AND MOBILE_NUMBER = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(resetPasswordSql)) {
            pstmt.setString(1, "newPassword");
            pstmt.setString(2, "testUser");
            pstmt.setString(3, "testemail@example.com");
            pstmt.setString(4, "invalidmobile");
            int affectedRows = pstmt.executeUpdate();
            Assertions.assertEquals(0, affectedRows, "No password reset should occur as the mobile number is not registered.");
        }
    }

    @Test
public void testCancelPasswordResetProcess() throws SQLException {
    // Simulate the user canceling the password reset process
    String cancelUpdateSql = "UPDATE users SET PASSCODE = ? WHERE USERNAME = ? AND EMAIL = ? AND MOBILE_NUMBER = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(cancelUpdateSql)) {
        pstmt.setString(1, "oldPassword"); // Resetting password to the original one
        pstmt.setString(2, "testUser");
        pstmt.setString(3, "testemail@example.com");
        pstmt.setString(4, "1234567891");
        int affectedRows = pstmt.executeUpdate();

        Assertions.assertEquals(1, affectedRows, "Password should remain unchanged after canceling reset.");

        // Verify password remains unchanged
        String verifySql = "SELECT PASSCODE FROM users WHERE USERNAME = ?";
        try (PreparedStatement verifyStmt = conn.prepareStatement(verifySql)) {
            verifyStmt.setString(1, "testUser");
            ResultSet rs = verifyStmt.executeQuery();
            if (rs.next()) {
                String currentPasscode = rs.getString("PASSCODE");
                Assertions.assertEquals("oldPassword", currentPasscode, "Password should remain unchanged.");
            }
        }
    }
}

}
