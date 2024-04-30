package com.budgetbuddy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DeleteExpense extends JFrame {
    private JTextField expenseNameField;
    private JTextField expenseDescriptionField;
    private JTextField expenseDateField;
    private JTextField expenseAmountField;
    private JButton deleteButton;


    public DeleteExpense() {
        setTitle("Expense Tracker");
        setSize(350, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));


        JLabel nameLabel = new JLabel("Expense Name To Delete:");
        expenseNameField = new JTextField();


        JLabel amountLabel = new JLabel("Expense Amount:");
        expenseAmountField = new JTextField();


        deleteButton = new JButton("Delete Expense");


        panel.add(nameLabel);
        panel.add(expenseNameField);


        panel.add(amountLabel);
        panel.add(expenseAmountField);


        panel.add(new JLabel()); // Placeholder for better alignment
        panel.add(deleteButton);


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action to add expense here
                String name = expenseNameField.getText();
                String amount = expenseAmountField.getText();

                // Perform whatever operation you want with the expense data
                // System.out.println("Expense Name: " + name);
                // System.out.println("Expense Amount: " + amount);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException error) {
                    JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }


                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget-buddy", "root",
                        "TowsonTigers2")) {
                    String sql = "DELETE FROM expense WHERE (EXPENSENAME = ?) && (EXPENSEAMOUNT = ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, amount);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Expense Successfully Deleted");
                    clearFields();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error Try Again: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

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
                new DeleteExpense();
            }
        });
    }
}
