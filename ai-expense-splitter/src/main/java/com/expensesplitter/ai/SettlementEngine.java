package com.expensesplitter.ai;

import com.expensesplitter.models.Group;
import com.expensesplitter.models.Settlement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Core AI module that calculates the minimum number of transactions
 * needed to settle all debts within a group.
 *
 * Algorithm: Minimize Cash Flow (Graph-based Greedy Approach)
 * ─────────────────────────────────────────────────────────────
 * 1. Calculate net balance for each member (paid - owed)
 * 2. Separate into creditors (positive balance) and debtors (negative)
 * 3. Greedily match the largest creditor with the largest debtor
 * 4. Repeat until all balances are zero
 *
 * This produces the MINIMUM number of transactions to clear all debts.
 *
 * Method:
 *   calculateSettlements(Group g) — runs the algorithm, returns Settlement list
 *
 * Student : Muhammad Arham | L1F23BSSE0372
 * Package : com.expensesplitter.ai
 */
public class SettlementEngine {

    /**
     * Calculates minimum transactions to settle all debts in a group.
     *
     * @param group          the expense group
     * @param netBalances    Map of userId -> net balance (positive = owed TO them, negative = they OWE)
     * @return list of Settlement objects representing required transactions
     */
    public List<Settlement> calculateSettlements(Group group, Map<Integer, Double> netBalances) {
        // TODO: Implement on Day 8
        // Step 1: Split into creditors and debtors lists
        // Step 2: While both lists are non-empty:
        //           a. Pick max creditor and max debtor
        //           b. Transfer min(creditor amount, debtor amount)
        //           c. Create Settlement object
        //           d. Remove settled entries
        // Step 3: Return settlements list
        return new ArrayList<>();
    }

    /**
     * Calculates the net balance for each group member.
     * Net balance = total paid by member - total owed by member
     *
     * @param groupId  the group to calculate balances for
     * @return Map of userId -> net balance
     */
    public Map<Integer, Double> calculateNetBalances(int groupId) {
        // TODO: Implement on Day 8
        // Query ExpenseDAO for all expenses in group
        // For each expense: credit paidBy, debit all split members
        return new HashMap<>();
    }
}
