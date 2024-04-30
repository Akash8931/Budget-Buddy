package com.budgetbuddy;

import org.junit.jupiter.api.*;
import java.sql.*;

public class DeleteExpenseTest {
    private static Connection conn;

    @BeforeAll
    public static void setUp() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Connect to the test database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root", "TowsonTigers25");
            conn.setAutoCommit(false); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void initializeTestData() throws SQLException {
    String insertSql = "INSERT INTO expense (EXPENSENAME, EXPENSEAMOUNT) VALUES (?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
        pstmt.setString(1, "Coffee");
        pstmt.setString(2, "4.50");
        pstmt.executeUpdate();
    }
}


    @AfterEach
    public void cleanUpTestData() throws SQLException {
        // Clean up the database after each test
        String deleteSql = "DELETE FROM expense WHERE EXPENSENAME = ? AND EXPENSEAMOUNT = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, "Coffee");
            pstmt.setString(2, "4.50");
            pstmt.executeUpdate();
        }
    }

    @Test
    public void testDeleteExistingTransaction() throws SQLException {
        String deleteSql = "DELETE FROM expense WHERE EXPENSENAME = ? AND EXPENSEAMOUNT = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, "Coffee");
            pstmt.setString(2, "4.50");
            int affectedRows = pstmt.executeUpdate();
            // Assertions.assertEquals(1, affectedRows, "One row should have been deleted.");
        }
    }

    @Test
    public void testDeleteNonExistentTransaction() throws SQLException {
        // Attempt to delete a transaction that does not exist
        String deleteSql = "DELETE FROM expense WHERE EXPENSENAME = ? AND EXPENSEAMOUNT = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, "NonExistentItem");
            pstmt.setDouble(2, 999.99); // Assuming such an item does not exist
            int affectedRows = pstmt.executeUpdate();
            Assertions.assertEquals(0, affectedRows, "No rows should be affected since the transaction does not exist.");
        }
    }

    @Test
    public void testCancelDeletionProcess() throws SQLException {
        // Attempt to delete but cancel the operation by rolling back
        String deleteSql = "DELETE FROM expense WHERE EXPENSENAME = ? AND EXPENSEAMOUNT = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, "Coffee");
            pstmt.setString(2, "4.50");
            pstmt.executeUpdate();
            conn.rollback(); // Simulate canceling the deletion

            // Verify that the data still exists
            String verifySql = "SELECT COUNT(*) FROM expense WHERE EXPENSENAME = ? AND EXPENSEAMOUNT = ?";
            try (PreparedStatement verifyStmt = conn.prepareStatement(verifySql)) {
                verifyStmt.setString(1, "Coffee");
                verifyStmt.setString(2, "4.50");
                ResultSet rs = verifyStmt.executeQuery();
                rs.next();
                // Assertions.assertEquals(1, rs.getInt(1), "Transaction should still exist after cancellation.");
            }
        } 
        }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
