import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

public class Financial extends JFrame {

    private JTextField startDateField;
    private JTextField endDateField;

    public Financial(String username) {
        setTitle("Budget-Buddy");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 400);

        // Create text fields for entering start and end dates
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);

        // Create a button to trigger data update
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChart();
            }
        });

        // Create pie dataset for default date range (null)
        DefaultPieDataset dataset = createDataset(null, null);

        // Create pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Expense Figures",  // Chart title
                dataset,                // Dataset
                true,                   // Include legend
                true,                   // Include tooltips
                false                   // Exclude URLs
        );

        // Customize pie plot to show percentage labels
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: ${1} ({2})"));

        // Create and set up chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        controlPanel.add(startDateField);
        controlPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        controlPanel.add(endDateField);
        controlPanel.add(updateButton);
        getContentPane().add(controlPanel, "North");
        getContentPane().add(chartPanel);

        // Display the frame
        setVisible(true);
    }

    private DefaultPieDataset createDataset(String startDate, String endDate) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Connect to SQL database and retrieve data for the specified date range
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/budget-buddy", "root", "Mcneill6!")) {
            Statement statement = connection.createStatement();
            // Modify SQL query to fetch data for the specified date range
            String query = "SELECT EXPENSENAME, EXPENSEAMOUNT FROM expense WHERE 1=1";
            if (startDate != null) {
                query += " AND EXPENSEDATE >= '" + startDate + "'";
            }
            if (endDate != null) {
                query += " AND EXPENSEDATE <= '" + endDate + "'";
            }
            ResultSet resultSet = statement.executeQuery(query);

            // Add data to dataset
            while (resultSet.next()) {
                String category = resultSet.getString("EXPENSENAME");
                double value = resultSet.getDouble("EXPENSEAMOUNT");
                dataset.setValue(category, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private void updateChart() {
        // Get start and end dates from text fields
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        // Update chart dataset for the specified date range
        DefaultPieDataset dataset = createDataset(startDate, endDate);
        // Get the chart plot and update the dataset
        JFreeChart chart = ((ChartPanel) getContentPane().getComponent(1)).getChart();
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setDataset(dataset);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Financial("").setVisible(true);
            }
        });
    }
}
