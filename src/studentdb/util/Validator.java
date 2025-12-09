package studentdb.util;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern SIMPLE_EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    // Check non-empty
    public static boolean isNonEmpty(String s){
        boolean result = s != null && !s.trim().isEmpty();
        if(!result) System.out.println("Validation failed: empty field -> '" + s + "'");
        return result;
    }

    // Check valid email
    public static boolean isValidEmail(String email){
        boolean result = isNonEmpty(email) && SIMPLE_EMAIL.matcher(email).matches();
        if(!result) System.out.println("Validation failed: invalid email -> '" + email + "'");
        return result;
    }

    // Check valid age
    public static boolean isValidAge(String ageStr){
        if(!isNonEmpty(ageStr)) return false;
        try{
            int age = Integer.parseInt(ageStr.trim());
            boolean result = age > 0 && age <= 120;
            if(!result) System.out.println("Validation failed: invalid age -> " + age);
            return result;
        } catch(Exception e){
            System.out.println("Validation failed: age not a number -> '" + ageStr + "'");
            return false;
        }
    }
}
