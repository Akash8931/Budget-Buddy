import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddExpense extends JFrame {
    private JTextField expenseNameField;
    private JTextField expenseDescriptionField;
    private JTextField expenseDateField;
    private JTextField expenseAmountField;
    private JButton addButton;

    public AddExpense(String username) {
        setTitle("Add Expense");
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

        addButton = new JButton("Add Expense");

        panel.add(nameLabel);
        panel.add(expenseNameField);

        panel.add(dateLabel);
        panel.add(expenseDateField);

        panel.add(descriptionLabel);
        panel.add(expenseDescriptionField);

        panel.add(amountLabel);
        panel.add(expenseAmountField);

        panel.add(new JLabel()); // Placeholder for better alignment
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = expenseNameField.getText();
                String date = expenseDateField.getText();
                String description = expenseDescriptionField.getText();
                String amount = expenseAmountField.getText();

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget-buddy", "root", "Mcneill6!");

                    String sql = "INSERT INTO expense (EXPENSENAME, EXPENSEAMOUNT, EXPENSEDESCRIPTION, EXPENSEDATE) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, amount);
                    pstmt.setString(3, description);
                    pstmt.setString(4, date);

                    int insertedRows = pstmt.executeUpdate();
                    if (insertedRows > 0) {
                        JOptionPane.showMessageDialog(null, "Expense added successfully!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add expense.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    conn.close();
                } catch (ClassNotFoundException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error adding expense: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
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
                new AddExpense("").setVisible(true);
            }
        });
    }
}
