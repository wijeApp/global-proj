package com.tas.global.globalven;

public enum TransactionType {
    SALARY("Salary"),
    BONUS("Bonus"),
    OVERTIME("Overtime"),
    ALLOWANCE("Allowance"),
    DEDUCTION("Deduction"),
    REIMBURSEMENT("Reimbursement"),
    ADVANCE("Advance"),
    OTHER("Other");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
