import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BudgetSettingPage extends JFrame {
    private JComboBox<String> categoryComboBox;
    private static final String[] SPENDING_CATEGORIES = {"Groceries", "Entertainment", "Bills", "Food", "Transportation", "Other"};
    private JTextField TotalField;
    private JTextField LimitField;
    private String username;
    
    public BudgetSettingPage(String username) {
        setTitle("Set Budget Limits");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
       // Category selection
        inputPanel.add(new JLabel("Select Spending Category:"));
        categoryComboBox = new JComboBox<>(SPENDING_CATEGORIES);
        categoryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFieldsBasedOnCategory();
            }
        });
        inputPanel.add(categoryComboBox);

        // Total Amount
        inputPanel.add(new JLabel("Total Amount:"));
        TotalField = new JTextField();
        TotalField.setEnabled(false); // Initially disabled until a category is selected
        inputPanel.add(TotalField);

        // Limit Amount
        inputPanel.add(new JLabel("Limit Amount:"));
        LimitField = new JTextField();
        LimitField.setEnabled(false); // Initially disabled until a category is selected
        inputPanel.add(LimitField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAndSaveBudgetLimits();
            }
        });
        inputPanel.add(saveButton);

        add(inputPanel, BorderLayout.CENTER);

         this.username = username;
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }



    


     private String getUsername(){
        return username;
    }
    
     private void updateFieldsBasedOnCategory() {
        String selectedCategory = (String) categoryComboBox.getSelectedItem();
        if (selectedCategory != null) {
            TotalField.setEnabled(true);
            LimitField.setEnabled(true);
        }
    }


   

private void updateAndSaveBudgetLimits() {
    String selectedCategory = (String) categoryComboBox.getSelectedItem();
    if (selectedCategory == null) {
        JOptionPane.showMessageDialog(this, "Please select a spending category.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String totalText = TotalField.getText().trim();
    String limitText = LimitField.getText().trim();

    // Check if the fields are empty
    if (totalText.isEmpty() || limitText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Total amount and limit amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        double totalAmount = Double.parseDouble(totalText);
        double limitAmount = Double.parseDouble(limitText);

        // Get user ID
        String username = getUsername();
        int userId = getUserId(username);

        // Check if user ID retrieval was successful
        if (userId == -1) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Define SQL queries
        String checkExistsQuery = "SELECT COUNT(*) FROM budget WHERE BUDGET_USERID=? AND BudgetName=?";
        String insertQuery = "INSERT INTO budget (BUDGET_USERID, BudgetName, TotalAmount, limit_amount) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE budget SET TotalAmount=?, limit_amount=? WHERE BUDGET_USERID=? AND BudgetName=?";

        // Prepare statements
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget-buddy", "root", "Mcneill6!");
             PreparedStatement checkExistsStmt = conn.prepareStatement(checkExistsQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            // Process each spending category
            processBudgetCategory(conn, checkExistsStmt, insertStmt, updateStmt, userId, selectedCategory, totalAmount, limitAmount);

            // Check if total amount exceeds the limit and send a warning
            if (totalAmount > limitAmount) {
                JOptionPane.showMessageDialog(this, "Warning: Total amount exceeds the limit for this category!", "Warning", JOptionPane.WARNING_MESSAGE);
                return; // Return here to prevent displaying the success message
            }

            // Get threshold values based on the category
            double thresholdPercentage = 0.8; // Example: 80% threshold

            // Calculate threshold values
            double thresholdLimit = limitAmount * thresholdPercentage;

            // Check if the total amount nears the limit and send an alert
            if (totalAmount >= thresholdLimit) {
                JOptionPane.showMessageDialog(this, "Alert: You are nearing your budget limit for this category!", "Alert", JOptionPane.WARNING_MESSAGE);
                return; // Return here to prevent displaying the success message
            }

            // Show success message only if there are no warnings or alerts
            JOptionPane.showMessageDialog(this, "Budget limits updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating budget limits: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException | SQLException ex) {
        JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for total amount and limit amount.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}






private void processBudgetCategory(Connection conn, PreparedStatement checkExistsStmt, PreparedStatement insertStmt, PreparedStatement updateStmt, int userId, String budgetName, double totalAmount, double limitAmount) throws SQLException {
    // Check if records exist for the specified conditions
    checkExistsStmt.setInt(1, userId);
    checkExistsStmt.setString(2, budgetName);
    ResultSet existsResult = checkExistsStmt.executeQuery();
    existsResult.next();
    int count = existsResult.getInt(1);

    // If no records exist, insert a new record; otherwise, update existing record
    if (count == 0) {
        insertStmt.setInt(1, userId);
        insertStmt.setString(2, budgetName);
        insertStmt.setDouble(3, totalAmount);
        insertStmt.setDouble(4, limitAmount);
        insertStmt.executeUpdate();
    } else {
        updateStmt.setDouble(1, totalAmount);
        updateStmt.setDouble(2, limitAmount);
        updateStmt.setInt(3, userId);
        updateStmt.setString(4, budgetName);
        updateStmt.executeUpdate();
    }
        // Add similar update logic for other spending categories

}


    @SuppressWarnings("unused")
    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Implement getUserId() method to retrieve the user's ID from the database
   private int getUserId(String username) throws SQLException {
    int userId = -1; // Initialize userId to a default value indicating failure

    // Load the MySQL JDBC driver
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!", "Error", JOptionPane.ERROR_MESSAGE);
        return userId;
    }

    // Query the database to retrieve the user's ID
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget-buddy", "root", "Mcneill6!")) {
        String getUserIdQuery = "SELECT UserID FROM users WHERE username = ?";
        try (PreparedStatement getUserIdStmt = conn.prepareStatement(getUserIdQuery)) {
            getUserIdStmt.setString(1, username);
            ResultSet userIdResult = getUserIdStmt.executeQuery();
            if (userIdResult.next()) {
                userId = userIdResult.getInt("UserID");
            }
        }
    }

    return userId;
}

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BudgetSettingPage("").setVisible(true);
            }
        });
    }
}

