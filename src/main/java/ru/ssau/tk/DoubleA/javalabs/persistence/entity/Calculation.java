package ru.ssau.tk.DoubleA.javalabs.persistence.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table (name = "calculation")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "calculation_id")
    private long id;

    @Column (name = "applied_value", nullable = false)
    private double appliedX;

    @Column (name = "result_value", nullable = false)
    private double resultY;

    @Column (name = "calculation_hash", nullable = false)
    private long hash;

    @OneToMany (mappedBy = "calculation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Calculation> appliedFunctions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAppliedX() {
        return appliedX;
    }

    public void setAppliedX(double appliedX) {
        this.appliedX = appliedX;
    }

    public double getResultY() {
        return resultY;
    }

    public void setResultY(double resultY) {
        this.resultY = resultY;
    }

    public long getHash() {
        return hash;
    }

    public void setHash(long hash) {
        this.hash = hash;
    }

    public Set<Calculation> getAppliedFunctions() {
        return appliedFunctions;
    }

    public void setAppliedFunctions(Set<Calculation> appliedFunctions) {
        this.appliedFunctions = appliedFunctions;
    }
}
