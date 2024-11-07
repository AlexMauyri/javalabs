package ru.ssau.tk.DoubleA.javalabs.persistence;

public enum Sorting {
    ASCENDING("ASCENDING"),
    DESCENDING("DESCENDING"),
    NONE("NONE");

    private final String text;
    Sorting(String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return "Sorting{" + "text='" + text + "'}";
    }
}
