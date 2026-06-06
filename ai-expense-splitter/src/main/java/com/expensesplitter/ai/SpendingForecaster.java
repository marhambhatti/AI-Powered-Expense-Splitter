package com.expensesplitter.ai;

import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

/**
 * AI Module: Predicts next month's spending using Weka Linear Regression.
 *
 * How it works:
 * ─────────────────────────────────────────────────────────────────────
 * 1. Collects historical monthly spending data for the user (past 6-12 months)
 * 2. Trains a Linear Regression model: y = mx + c
 *    where x = month number, y = total spending
 * 3. Predicts spending for the next month
 * 4. Result shown on the Analytics dashboard
 *
 * Method:
 *   forecastNextMonth()  — returns predicted spending amount
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.ai
 */
public class SpendingForecaster {

    // Weka Linear Regression model
    private LinearRegression model;

    /**
     * Forecasts the next month's estimated spending for a user.
     * Uses past monthly totals from the database as training data.
     *
     * @param userId  the user whose spending history to use
     * @return estimated spending amount for next month
     */
    public double forecastNextMonth(int userId) {
        // TODO: Implement on Day 10
        // 1. Fetch monthly totals from ExpenseDAO (past 6-12 months)
        // 2. Build Weka Instances with month -> amount pairs
        // 3. Train LinearRegression model
        // 4. Predict for (currentMonth + 1)
        // 5. Return predicted value
        return 0.0; // Placeholder
    }
}
