import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BudgetSettingsButtonFrame extends JFrame {
    private JButton budgetSettingsButton;
    private JButton DeleteExpenseButton;
    private JButton AddExpenseButton;
    private JButton FinancialButton;
    private String username;

    public BudgetSettingsButtonFrame(String username) {
        this.username = username;
        setTitle("Budget Buddy");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose frame only
        setSize(800, 600);

        budgetSettingsButton = new JButton("Budget Limit Settings");
        budgetSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openBudgetSettingPage();
            }
        });

        DeleteExpenseButton = new JButton(" Delete Expense Settings");
        DeleteExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDeleteExpense();
            }
        });

        AddExpenseButton = new JButton("Add Expense Settings");
        AddExpenseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddExpense();
            }
        });

        FinancialButton = new JButton("Financial report");
        FinancialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFinancial();
            }
        });

        JPanel panel = new JPanel();
        panel.add(budgetSettingsButton);
        panel.add(DeleteExpenseButton);
        panel.add(AddExpenseButton);
        panel.add(FinancialButton);
        add(panel);
    }

    private void openBudgetSettingPage() {
        // Pass the username to the constructor of BudgetSettingPage
        BudgetSettingPage budgetSettingPage = new BudgetSettingPage(username);
        budgetSettingPage.setVisible(true);
        // Add a window listener to handle when the budget setting page is closed
        budgetSettingPage.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setVisible(true); // Show the button frame again when the budget setting page is closed
            }
        });
    }

    private void openDeleteExpense() {
        // Pass the username to the constructor of DeleteExpense
        DeleteExpense deleteExpense = new DeleteExpense(username);
        deleteExpense.setVisible(true);
        // Add a window listener to handle when the delete expense page is closed
        deleteExpense.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setVisible(true); // Show the button frame again when the delete expense page is closed
        }
    });
}   
    
    private void openAddExpense() {
        // Pass the username to the constructor of DeleteExpense
        AddExpense addExpense = new AddExpense(username);
        addExpense.setVisible(true);
        // Add a window listener to handle when the delete expense page is closed
        addExpense.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setVisible(true); // Show the button frame again when the delete expense page is closed
        }
    });
}
     private void openFinancial() {
        // Pass the username to the constructor of DeleteExpense
        Financial financial = new Financial(username);
        financial.setVisible(true);
        // Add a window listener to handle when the delete expense page is closed
        financial.addWindowListener(new java.awt.event.WindowAdapter() {
             @Override
             public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                setVisible(true); // Show the button frame again when the delete expense page is closed
        }
    });
}

}

