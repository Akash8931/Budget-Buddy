package com.budgetbuddy;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

public class ExpenseTest {
    private Expense expense;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setup() throws SQLException {
        expense = new Expense();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // Set up the connection and prepared statement mocks
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Initialize the Expense class with mocked connection for testing
        expense.setConnection(mockConnection);
    }

    @Test
    public void testValidExpenseEntry() throws SQLException {
        // Set field values
        expense.expenseNameField.setText("food");
        expense.expenseAmountField.setText("10");
        expense.expenseDateField.setText("2023-10-22");

        // Trigger the action
        expense.addButton.doClick();
    }

    @Test
    public void testNegativeExpenseAmount() throws SQLException {
        // Set field values
        expense.expenseNameField.setText("food");
        expense.expenseAmountField.setText("-10");
        expense.expenseDateField.setText("2023-10-22");

        // Trigger the action
        expense.addButton.doClick();

    }

    @Test
    public void testInvalidDateFormat() throws SQLException {
        // Set field values
        expense.expenseNameField.setText("food");
        expense.expenseAmountField.setText("-10");
        expense.expenseDateField.setText("10th January 2022");

        // Trigger the action
        expense.addButton.doClick();
    }

    @Test
    public void testFutureDate() throws SQLException {
        // Set field values with a future date
        expense.expenseNameField.setText("food");
        expense.expenseAmountField.setText("-10");
        expense.expenseDateField.setText("2025-10-22");

        // Trigger the action
        expense.addButton.doClick();

    }
    @Test
    public void testNonNumericExpenseEntry() throws SQLException {
        // Set field values with a future date
        expense.expenseNameField.setText("food");
        expense.expenseAmountField.setText("bills");
        expense.expenseDateField.setText("2023-10-22");
    
        // Trigger the action
        expense.addButton.doClick();
    }
}
