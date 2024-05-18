package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row {
    private List<Object> values;

    public Row() {
        this.values = new ArrayList<>();
    }

    public List<Object> getValues() {
        return values;
    }

    public void addValue(Object value) {
        values.add(Objects.requireNonNullElse(value, "NULL"));
    }
}
