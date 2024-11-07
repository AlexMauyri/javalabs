package ru.ssau.tk.DoubleA.javalabs.persistence;

public enum Operations {
    greaterOrEqual("greaterOrEqual"),
    greaterThen("greaterThen"),
    lessOrEqual("lessOrEqual"),
    lessThen("lessThen"),
    equal("equal"),
    notEqual("notEqual");

    private final String text;
    Operations(String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return "Operations{" + "text='" + text + "'}";
    }
}
