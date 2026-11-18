package com.moraisluis.split.model.compositeKeys;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class ExpenseSplitId implements Serializable {

    private UUID expenseId;
    private UUID userId;
    public UUID getExpenseId() {
        return expenseId;
    }
    public void setExpenseId(UUID expenseId) {
        this.expenseId = expenseId;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expenseId == null) ? 0 : expenseId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpenseSplitId other = (ExpenseSplitId) obj;
        if (expenseId == null) {
            if (other.expenseId != null)
                return false;
        } else if (!expenseId.equals(other.expenseId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
}