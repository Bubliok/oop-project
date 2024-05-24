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

    public void removeValue(Object value) {
        values.remove(value);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      for (Object value : values){
          sb.append(values.toString()).append(", ");
      }
      sb.append(sb.length() - 1);
      return sb.toString();
    }
}
