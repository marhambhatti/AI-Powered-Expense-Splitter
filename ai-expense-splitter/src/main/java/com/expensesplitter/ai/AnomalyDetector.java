package com.expensesplitter.ai;

import com.expensesplitter.dao.ExpenseDAO;

/**
 * AI Module: Detects abnormal spending patterns using statistical analysis.
 *
 * How it works:
 * ─────────────────────────────────────────────────────────────────────
 * 1. Calculates average monthly spending over the past N months
 * 2. Compares current month's spending to the average
 * 3. If current > average * threshold (e.g. 1.5x), an anomaly is flagged
 * 4. An alert is generated and shown on the dashboard
 *
 * Default threshold: 50% above average triggers an anomaly alert.
 *
 * Method:
 *   detectAnomaly() — returns true if spending is abnormally high
 *   getAnomalyMessage() — returns human-readable alert message
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.ai
 */
public class AnomalyDetector {

    // Spending must be this many times above average to trigger alert
    private static final double ANOMALY_THRESHOLD = 1.5;

    // How many past months to use for average calculation
    private static final int HISTORY_MONTHS = 6;

    /**
     * Checks if the given month's spending is abnormally high.
     * Compares against the average of the past HISTORY_MONTHS months.
     *
     * @param userId  the user to check
     * @param month   the month to evaluate (1-12)
     * @param year    the year
     * @return true if spending exceeds average * ANOMALY_THRESHOLD
     */
    public boolean detectAnomaly(int userId, int month, int year) {
        // TODO: Implement on Day 10
        // 1. Get current month total from ExpenseDAO
        // 2. Get past HISTORY_MONTHS totals and calculate average
        // 3. Return currentTotal > (average * ANOMALY_THRESHOLD)
        return false;
    }

    /**
     * Returns an alert message if an anomaly was detected.
     *
     * @param userId  the user
     * @param month   the month being checked
     * @param year    the year
     * @return alert string, or null if no anomaly
     */
    public String getAnomalyMessage(int userId, int month, int year) {
        // TODO: Implement on Day 10
        if (detectAnomaly(userId, month, year)) {
            return "⚠ Alert: Your spending this month is significantly higher than your average!";
        }
        return null;
    }
}
