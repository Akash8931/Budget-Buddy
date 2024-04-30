package com.budgetbuddy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.jfree.data.general.DefaultPieDataset;

public class ReportsTest {

    private Reports reports;

    @BeforeEach
    public void setup() {
        reports = new Reports() {
            @Override
            public Connection getConnection() throws SQLException {
                // Override to use H2 database for testing
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy","root", "TowsonTigers25");
            }
        };

        // Setup database schema and initial data
        try (Connection conn = reports.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE expense (EXPENSENAME VARCHAR(255), EXPENSEAMOUNT DOUBLE, EXPENSEDATE DATE)");
            // Assume the test is run in May 2024 for consistent testing
            stmt.execute("INSERT INTO expense (EXPENSENAME, EXPENSEAMOUNT, EXPENSEDATE) VALUES ('Groceries', 150, '2024-04-01')");
            stmt.execute("INSERT INTO expense (EXPENSENAME, EXPENSEAMOUNT, EXPENSEDATE) VALUES ('Utilities', 120, '2024-04-15')");

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void displayValid() {
        // Setting date range for May 2024
        // Setting date range for May 2024
        // Setting date range for April 2024
        String start = LocalDate.of(2024, 4, 1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String end = LocalDate.of(2024, 4, 30).format(DateTimeFormatter.ISO_LOCAL_DATE);

        DefaultPieDataset dataset = reports.createDataset(start, end);

        assertNotNull(dataset, "Dataset should not be null");
        assertEquals(150.0, dataset.getValue("Groceries"), "Groceries expense should be correctly fetched");
        // assertEquals(120.0, dataset.getValue("Utilities"), "Utilities expense should be correctly fetched");
    }

    @Test
    public void testInsufficientData() {
    // Setting date range for last year (assuming no data available)
    String start = LocalDate.of(2023, 1, 1).format(DateTimeFormatter.ISO_LOCAL_DATE);
    String end = LocalDate.of(2023, 12, 31).format(DateTimeFormatter.ISO_LOCAL_DATE);

    DefaultPieDataset dataset = reports.createDataset(start, end);

    assertNull(dataset, "Dataset should be null when there is insufficient data");
}


@Test
public void testInvalidDateRange() {
    // Setting date range for future quarter (assuming it is unselectable)
    String start = LocalDate.now().plusMonths(3).format(DateTimeFormatter.ISO_LOCAL_DATE);
    String end = LocalDate.now().plusMonths(6).format(DateTimeFormatter.ISO_LOCAL_DATE);

    DefaultPieDataset dataset = reports.createDataset(start, end);

    assertNull(dataset, "Dataset should be null for an invalid date range");
}

}
