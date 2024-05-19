package validators;

public class TypeValidator {
    public boolean isValueOfType(String value, String type) {
        switch (type) {
            case "int":
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "float":
                try {
                    Float.parseFloat(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "string":
                try {
                    Integer.parseInt(value);
                    Float.parseFloat(value);
                    return false;
                } catch (NumberFormatException e) {
                    return true;
                }
            default:
                return false;
        }
    }
}