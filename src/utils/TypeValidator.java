package utils;

public class TypeValidator {

    public static Object typeValidator (String value, String type){
        switch(type){
            case "int":
                return Integer.parseInt(value);
            case "double":
                return Double.parseDouble(value);
            default:
                return value;
        }
    }
}
