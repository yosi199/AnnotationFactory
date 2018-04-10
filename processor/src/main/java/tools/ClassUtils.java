package tools;

public class ClassUtils {

    public static Class getClass(String className) {
        switch (className) {
            case "boolean":
                return boolean.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "char":
                return char.class;
            case "void":
                return void.class;
            case "java.lang.String[]":
                return String[].class;
            case "java.lang.int[]":
                return int[].class;
            default:
                String name = className.contains(".") ? className : "java.lang.".concat(className);
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException ex) {
                    throw new IllegalArgumentException("Class not found: " + name);
                }
        }
    }
}
