package com.budgetbuddy;
import java.time.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Expense extends JFrame {
    public JTextField expenseNameField;
    public JTextField expenseDescriptionField;
    public JTextField expenseDateField;
    public JTextField expenseAmountField;
    public JButton addButton;

    public Expense() {
        setTitle("Expense Tracker");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));

        JLabel nameLabel = new JLabel("Expense Name:");
        expenseNameField = new JTextField();

        JLabel dateField = new JLabel("Date:");
        expenseDateField = new JTextField();

        JLabel descriptionField = new JLabel("Description:");
        expenseDescriptionField = new JTextField();

        JLabel amountLabel = new JLabel("Expense Amount:");
        expenseAmountField = new JTextField();

        addButton = new JButton("Add Expense");

        panel.add(nameLabel);
        panel.add(expenseNameField);

        panel.add(amountLabel);
        panel.add(expenseAmountField);

        panel.add(dateField);
        panel.add(expenseDateField);

        panel.add(descriptionField);
        panel.add(expenseDescriptionField);

        panel.add(new JLabel()); // Placeholder for better alignment
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action to add expense here
                String name = expenseNameField.getText();
                String amount = expenseAmountField.getText();
                String description = expenseDescriptionField.getText();
                String dateSelection = expenseDateField.getText();
                
                // Check if the date is in the future
                LocalDate currentDate = LocalDate.now();
                LocalDate expenseDate = LocalDate.parse(dateSelection);
                if (expenseDate.isAfter(currentDate)) {
                    // Display message to the user
                    showMessage("Expense cannot be made for future dates");
                    return; // Do not proceed with adding the expense to the database
                }
                // Perform whatever operation you want with the expense data
                // System.out.println("Expense Name: " + name);
                // System.out.println("Expense Amount: " + amount);
                double amountValue = Double.parseDouble(amount);
                if (amountValue < 0) {
                    // Display message to the user
                    showMessage("Expense amount cannot be negative");
                    return; // Do not proceed with adding the expense to the database
                }
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException error) {
                    JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetbuddy", "root",
                        "TowsonTigers25")) {
                    String sql = "INSERT INTO expense(EXPENSENAME, EXPENSEAMOUNT, EXPENSEDESCRIPTION, EXPENSEDATE) VALUES(?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, amount);
                    pstmt.setString(3, description);
                    pstmt.setString(4, dateSelection);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Expense Added successfully!");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error adding expense " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            

        //     public void setExpenseName(String name) {
        //         expenseNameField.setText(name);
        //     }
        
        //     public void setExpenseAmount(String amount) {
        //         expenseAmountField.setText(amount);
        //     }
        
        //     public void setExpenseDescription(String description) {
        //         expenseDescriptionField.setText(description);
        //     }
        
        //     public void setExpenseDate(String date) {
        //         expenseDateField.setText(date);
        //     }

            private void clearFields() {
                expenseNameField.setText("");
                expenseAmountField.setText("");
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Expense();
            }
        });
    }

    public Connection conn;
    public void setConnection(Connection con) {
        this.conn = conn;
    }

    public void showMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

