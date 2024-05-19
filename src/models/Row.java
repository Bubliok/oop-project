package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Row {
    private List<Object> values;

    public Row() {
        this.values = new ArrayList<>();
    }

    public Row(List<Object> values) {
        this.values = new ArrayList<>(values);
    }
    public List<Object> getValues() {
        return values;
    }
    public void addValue(Object value) {
        values.add(Objects.requireNonNullElse(value, "NULL"));
    }

    public void removeValue(Object value) {
        values.remove(value);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i < values.size() - 1) {
                sb.append(", ");
            }
        }
      return sb.toString();
    }
}
