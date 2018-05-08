package com.example.android.gamesbola;

public class Value {
    private String order;
    private String value;

    public Value(String order, String value) {
        this.order = order;
        this.value = value;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
