package ru.ssau.tk.DoubleA.javalabs.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "applied_function")
public class AppliedFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "calculation_id", nullable = false)
    private Calculation calculationId;

    @Column(name = "function_order", nullable = false)
    private int functionOrder;

    @Column(name = "function_serialized", nullable = false)
    private byte[] functionSerialized;

    @Column(name = "mod_unmodifiable")
    private Boolean modUnmodifiable;

    @Column(name = "mod_strict")
    private Boolean modStrict;

    public Boolean getModStrict() {
        return modStrict;
    }

    public void setModStrict(Boolean modStrict) {
        this.modStrict = modStrict;
    }

    public Boolean getModUnmodifiable() {
        return modUnmodifiable;
    }

    public void setModUnmodifiable(Boolean modUnmodifiable) {
        this.modUnmodifiable = modUnmodifiable;
    }

    public byte[] getFunctionSerialized() {
        return functionSerialized;
    }

    public void setFunctionSerialized(byte[] functionSerialized) {
        this.functionSerialized = functionSerialized;
    }

    public int getFunctionOrder() {
        return functionOrder;
    }

    public void setFunctionOrder(int functionOrder) {
        this.functionOrder = functionOrder;
    }

    public Calculation getCalculationId() {
        return calculationId;
    }

    public void setCalculationId(Calculation calculationId) {
        this.calculationId = calculationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
