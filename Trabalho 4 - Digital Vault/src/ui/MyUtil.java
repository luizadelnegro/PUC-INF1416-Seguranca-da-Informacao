package ui;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Console;

public class MyUtil {

    private static final Scanner sc = new Scanner(System.in);
    private static final Pattern MYPATTERN = Pattern.compile("[^ :A-Za-z@0-9\\-/\\.]", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    public static boolean hasInvalidChar(String s) {
        Matcher matcher = MYPATTERN.matcher(s);
        return matcher.find();
    }

    public static String sanitizeString(String s) {
        s = s.replace("\\", "/");
        Matcher matcher = MYPATTERN.matcher(s);
        String r = matcher.replaceAll("");
        if(hasInvalidChar(s)) {
            System.out.println("Input changed to: " + r);
        }
        return r;
    }

    public static int safeGetIntInput(String userQuestion){
        int r = -1;
        System.out.println(userQuestion);
        try {
            r = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            r = -1;
        }
        return r;
    }

    public static String safeGetString(String userQuestion){
        String r = "";
        System.out.println(userQuestion);
        try {
            r = sc.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sanitizeString(r);
    }

    public static String safeGetPassword(String userQuestion) {
        String r;
        System.out.print(userQuestion);
        try {
            r = String.valueOf(System.console().readPassword());
        } catch (Exception e) {
            e.printStackTrace();
            r = "";
        }
        if (hasInvalidChar(r)) {
            r = "";
        }
        return r;
    }
}

