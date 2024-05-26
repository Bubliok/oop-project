package utils;

public class TypeValidator {

    public static Object typeValidator (String value, String type) throws IllegalArgumentException {
        switch(type){
            case "int":
                if ("NULL".equals(value)) {
                    return null;
                }
                return Integer.parseInt(value);
            case "double":
                if ("NULL".equals(value)) {
                    return null;
                }
                return Double.parseDouble(value);
            case "string":
                if ("NULL".equals(value)) {
                    return null;
                }
                return value;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}