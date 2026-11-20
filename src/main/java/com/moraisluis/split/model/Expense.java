package com.moraisluis.split.model;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paid_by", nullable = false)
    private User paidBy;

    @Column(nullable = false)
    private long value;

    @Column(name = "descr", nullable = false)
    private String description;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt; // preenchido pelo DEFAULT now() da base de dados

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseSplit> splits;

    protected Expense() {}

    public Expense(Group group, User paidBy, long value, String description, List<ExpenseSplit> splits) {
        this.group = group;
        this.paidBy = paidBy;
        this.value = value;
        this.description = description;
        this.splits = splits;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public long getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<ExpenseSplit> getSplits() {
        return Collections.unmodifiableList(splits);
    }

    public boolean addSplit(ExpenseSplit split) {
        split.setExpense(this);
        return splits.add(split);
    }

    public boolean removeSplit(ExpenseSplit split) {
        return splits.remove(split);
    }
    
}
