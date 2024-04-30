package com.budgetbuddy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BudgetSettingPageTest {

    private BudgetSettingPage budgetSettingPage;
    private Connection connection;

    @BeforeEach
    public void setup() throws Exception {
        // Initialize your frame with a test username
        budgetSettingPage = new BudgetSettingPage("testUser");


        // Set up the in-memory database
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy","root", "TowsonTigers25");
        connection.setAutoCommit(false); // Use transaction to rollback after tests

        // Create your schema in the in-memory database
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE budgetTest3 (BUDGET_USERID INT, BudgetName VARCHAR(255), TotalAmount DOUBLE, limit_amount DOUBLE);");
        // statement.execute("CREATE TABLE usersTest (UserID INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255));");
        statement.execute("INSERT INTO users (username, EMAIL, MOBILE_NUMBER, PASSCODE) VALUES ('testUser', 'test@example.com', '1123456789', 'test123');");
    }

    @Test
    public void testUpdateAndSaveBudgetLimits_ValidInput_Success() throws Exception {
        // Override getConnection to use our in-memory database
        budgetSettingPage = new BudgetSettingPage("testUser")
         {
            @Override
            public Connection getConnection() throws SQLException {
                return connection;
            }
        };

        // Execute the method to test
        budgetSettingPage.categoryComboBox.setSelectedItem("Bills");
        budgetSettingPage.TotalField.setText("10");
        budgetSettingPage.LimitField.setText("100");

        budgetSettingPage.updateAndSaveBudgetLimits();

        // Add assertions here to check database state if necessary
        // Example: Check if the new limits are correctly inserted in the table
    }
}
