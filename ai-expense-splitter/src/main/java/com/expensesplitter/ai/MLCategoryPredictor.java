package com.expensesplitter.ai;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * AI Module: Automatic expense category prediction using Weka Naive Bayes.
 *
 * How it works:
 * ─────────────────────────────────────────────────────────────────────
 * 1. A training ARFF file (categories.arff) contains labeled examples:
 *    "pizza" -> Food, "uber" -> Travel, "electricity" -> Utilities, etc.
 * 2. The Naive Bayes classifier trains on this data at startup.
 * 3. When user types an expense title, predictCategory() is called.
 * 4. The model predicts the most likely category automatically.
 * 5. User can override the prediction if needed.
 *
 * Categories: Food, Travel, Utilities, Entertainment, Shopping, Other
 *
 * Method:
 *   loadModel()        — trains Naive Bayes on ARFF training data
 *   predictCategory()  — predicts category from expense title
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.ai
 */
public class MLCategoryPredictor {

    // Weka Naive Bayes classifier instance
    private NaiveBayes classifier;

    // Path to training data file
    private static final String TRAINING_DATA_PATH =
        "src/main/resources/com/expensesplitter/data/categories.arff";

    /**
     * Loads and trains the Naive Bayes classifier from the ARFF file.
     * Must be called once at application startup before any predictions.
     */
    public void loadModel() {
        // TODO: Implement on Day 9
        // 1. Load categories.arff using ConverterUtils.DataSource
        // 2. Set class index to last attribute (category label)
        // 3. Build classifier: classifier.buildClassifier(trainingData)
        System.out.println("MLCategoryPredictor: model loading (implement on Day 9)");
    }

    /**
     * Predicts the expense category based on the title text.
     * Called in real-time as user types in the Add Expense screen.
     *
     * @param expenseTitle  the title entered by the user (e.g. "dinner at KFC")
     * @return predicted category string (e.g. "Food", "Travel", "Utilities")
     */
    public String predictCategory(String expenseTitle) {
        // TODO: Implement on Day 9
        // 1. Convert expenseTitle to Weka Instance
        // 2. Run classifier.classifyInstance(instance)
        // 3. Convert numeric result back to category string
        return "Other"; // Default until implemented
    }
}
