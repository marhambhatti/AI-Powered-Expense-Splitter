package com.expensesplitter.utils;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import java.util.Map;

/**
 * Utility class for generating JavaFX charts from expense data.
 * Used by the Analytics & Reports screen.
 *
 * Methods:
 *   generatePieChart()  — category-wise spending breakdown
 *   generateBarChart()  — month-wise spending comparison
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.utils
 */
public class ChartHelper {

    /**
     * Builds a JavaFX PieChart showing spending by category for a group/month.
     * Each slice = one expense category (Food, Travel, Utilities, etc.)
     *
     * @param groupId  the group whose expenses to chart
     * @param month    the month number (1-12)
     * @return configured PieChart ready to display
     */
    public static PieChart generatePieChart(int groupId, int month) {
        // TODO: Implement on Day 11
        // 1. Query ExpenseDAO for category totals
        // 2. Create PieChart.Data for each category
        // 3. Build and return PieChart
        PieChart chart = new PieChart();
        chart.setTitle("Spending by Category");
        return chart;
    }

    /**
     * Builds a JavaFX BarChart showing month-wise total spending for a user.
     * X-axis = months, Y-axis = total amount spent.
     *
     * @param userId  the user whose spending history to chart
     * @return configured BarChart ready to display
     */
    public static BarChart<String, Number> generateBarChart(int userId) {
        // TODO: Implement on Day 11
        // 1. Query ExpenseDAO for monthly totals
        // 2. Build XYChart.Series with month data
        // 3. Build and return BarChart
        return null;
    }
}
