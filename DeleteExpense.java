import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DeleteExpense extends JFrame {
    private JTextField expenseNameField;
    private JTextField expenseDescriptionField;
    private JTextField expenseDateField;
    private JTextField expenseAmountField;
    private JButton deleteButton;

    public DeleteExpense(String username) {
        setTitle("Delete Expense");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Expense Name:");
        expenseNameField = new JTextField();

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        expenseDateField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        expenseDescriptionField = new JTextField();

        JLabel amountLabel = new JLabel("Expense Amount:");
        expenseAmountField = new JTextField();

        deleteButton = new JButton("Delete");

        panel.add(nameLabel);
        panel.add(expenseNameField);

        panel.add(dateLabel);
        panel.add(expenseDateField);

        panel.add(descriptionLabel);
        panel.add(expenseDescriptionField);

        panel.add(amountLabel);
        panel.add(expenseAmountField);

        panel.add(new JLabel()); // Placeholder for better alignment
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = expenseNameField.getText();
                String date = expenseDateField.getText();
                String description = expenseDescriptionField.getText();
                String amount = expenseAmountField.getText();

                // Check if all fields are filled
                if (name.isEmpty() || date.isEmpty() || description.isEmpty() || amount.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields to delete an expense.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method if any field is empty
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this expense?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget-buddy", "root", "Mcneill6!");

                        String sql = "DELETE FROM expense WHERE EXPENSENAME = ? AND EXPENSEAMOUNT = ? AND EXPENSEDESCRIPTION = ? AND EXPENSEDATE = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, name);
                        pstmt.setString(2, amount);
                        pstmt.setString(3, description);
                        pstmt.setString(4, date);

                        int deletedRows = pstmt.executeUpdate();
                        if (deletedRows > 0) {
                            JOptionPane.showMessageDialog(null, "Expense deleted successfully!");
                            clearFields();
                        } else {
                            JOptionPane.showMessageDialog(null, "Expense not found or already deleted.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        conn.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error deleting expense: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            private void clearFields() {
                expenseNameField.setText("");
                expenseDateField.setText("");
                expenseDescriptionField.setText("");
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
                new DeleteExpense("").setVisible(true);
            }
        });
    }
}
