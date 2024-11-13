package ru.ssau.tk.DoubleA.javalabs.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "calculation")
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calculation_id")
    private int id;

    @Column(name = "applied_value", nullable = false)
    private double appliedX;

    @Column(name = "result_value", nullable = false)
    private double resultY;

    @Column(name = "calculation_hash", nullable = false)
    private long hash;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
