import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*; 
import java.util.Collections;
import java.lang.Character;

public class Parser {
    public static class variable {
        public final String name;
        public String value;
        public String type;
        float numValue = 0;

        variable(String n, String v) {
            name = n;
            value = v;
        }

        void setValue(String v) {
            value = v;
        }
        void setType(String t) {
            type = t;
        }
        float getNumValue() {
            boolean minus = false;
            boolean dot = false;
            String vv = "";
            int j =0;
            int g = 0;
            if(String.valueOf(value.charAt(0)).equals("-")) g = 1;
            for(int i = g; i < value.length(); i++) vv += String.valueOf(value.charAt(i));
            numValue = Float.parseFloat(vv);
            if(g == 1) numValue *= -1;
            return numValue;
        }
    }


    public static List<Lexer.Token> tokens;
    public static List<String> nameList = new ArrayList<String>();
    public static List<variable> variables = new ArrayList<variable>();
    public static void sParse(List<Lexer.Token> result) {
        tokens = result;
        for(int i = 0; i < tokens.size(); i++) {
            if(isToken(i, "Dim")) {
                if(isToken(i + 1, "Unknown")) {
                    variables.add(new variable(tokens.get(i + 1).name, null));
                    nameList.add(tokens.get(i + 1).name);
                    
                }
                if((i + 1) <= tokens.size() && !isToken(i + 1, "Unknown") || (i + 1) == tokens.size()) {
                    System.out.println("After Dim you must create variable!");
                }
            }
            else if(Collections.frequency(nameList, tokens.get(i).name) > 0) {
                if(isToken(i + 1, "Semicolon")) {
                    continue;
                }
                if(isToken(i + 1, "Pmark")) {
                    continue;
                }
                else if(isToken(i + 1, "Equaly")) {
                    variables.get(searchIdByName(variables, tokens.get(i).name)).setValue(getEqualResult(i));
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Semicolon") || isToken(i, "Pmark")) break;
                    } 
                }
            }
            else if(isToken(i, "While")) {
                i += 2;
                int j = 0;
                int gi = i;
                i++;
                while(i < tokens.size()) {
                    i++;
                    if(isToken(i, "Rparen") && j == 0) break;
                    else if(isToken(i, "Lparen")) j++;
                    else if(isToken(i, "Rparen") && j > 0) j--;
                }
                i++;
                int zi = i;
                while(i < tokens.size()) {
                    i++;
                    if(isToken(i, "RCB") && j == 0) break;
                    else if(isToken(i, "LCB")) j++;
                    else if(isToken(i, "RCB") && j > 0) j--;
                }
                while(checkTruth(gi)) {
                    realise(zi);
                }
            }
            else if(isToken(i, "If")) {
                i += 2;
                if(checkTruth(i)) {
                    int j = 0;
                    i++;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Rparen") && j == 0) break;
                        else if(isToken(i, "Lparen")) j++;
                        else if(isToken(i, "Rparen") && j > 0) j--;
                    }
                    i++;
                    realise(i);
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "RCB") && j == 0) break;
                        else if(isToken(i, "LCB")) j++;
                        else if(isToken(i, "RCB") && j > 0) j--;
                    }
                    if(isToken(i + 1, "Else")) {
                        i++;
                        if(isToken(i + 1, "LCB")) {
                            i++;
                            j = 0;
                            i += 1;
                            while(i < tokens.size()) {
                                i++;
                                if(isToken(i, "RCB") && j == 0) break;
                                else if(isToken(i, "LCB")) j++;
                                else if(isToken(i, "RCB") && j > 0) j--;
                            }
                        }
                        else {
                            if(checkTruth(i + 2)) {
                                j = 0;
                                i++;
                                while(i < tokens.size()) {
                                    i++;
                                    if(isToken(i, "Rparen") && j == 0) break;
                                    else if(isToken(i, "Lparen")) j++;
                                    else if(isToken(i, "Rparen") && j > 0) j--;
                                }
                                i++;
                                realise(i);
                                while(i < tokens.size()) {
                                    i++;
                                    if(isToken(i, "RCB") && j == 0) break;
                                    else if(isToken(i, "LCB")) j++;
                                    else if(isToken(i, "RCB") && j > 0) j--;
                                }
                            }
                        }
                    }
                }
                else if(isToken(i + 1, "Else")) {
                    if(isToken(i + 2, "If")) realise(i + 2);
                    else realise(i + 3);
                }
                else {
                    int j = 0;
                    i++;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Rparen") && j == 0) break;
                        else if(isToken(i, "Lparen")) j++;
                        else if(isToken(i, "Rparen") && j > 0) j--;
                    }
                    i++;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "RCB") && j == 0) break;
                        else if(isToken(i, "LCB")) j++;
                        else if(isToken(i, "RCB") && j > 0) j--;
                    }
                }
            }
            else if(isToken(i, "Print")) {
                String cout = "";
                if(isToken(i + 1, "Lparen")) {
                    i++;
                    cout = getParentsResult(i);
                    int j = 0;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Rparen") && j == 0) break;
                        else if(isToken(i, "Lparen")) j++;
                        else if(isToken(i, "Rparen") && j > 0) j--;
                    }
                }
                else if(Collections.frequency(nameList, tokens.get(i + 1).name) > 0) {
                    cout = variables.get(searchIdByName(variables, tokens.get(i + 1).name)).value;
                }
                prent(cout);
            }
            else if(isToken(i, "Input")) {
                if(Collections.frequency(nameList, tokens.get(i + 1).name) > 0) {
                    Scanner in = new Scanner(System.in);
                    String v = in.nextLine();
                    variables.get(searchIdByName(variables, tokens.get(i + 1).name)).setValue(v);
                }
            }
        }
    }

    public static void realise(int g) {
        int j = 0;
        for(int i = g; i < tokens.size(); i++) {
            if(isToken(i, "RCB")) {
                break;
            }
            else if(isToken(i, "Dim")) {
                if(isToken(i + 1, "Unknown")) {
                    variables.add(new variable(tokens.get(i + 1).name, null));
                    nameList.add(tokens.get(i + 1).name);
                    
                }
                if((i + 1) <= tokens.size() && !isToken(i + 1, "Unknown") || (i + 1) == tokens.size()) {
                    System.out.println("After Dim you must create variable!");
                }
            }
            else if(Collections.frequency(nameList, tokens.get(i).name) > 0) {
                if(isToken(i + 1, "Semicolon")) {
                    continue;
                }
                if(isToken(i + 1, "Pmark")) {
                    continue;
                }
                else if(isToken(i + 1, "Equaly")) {
                    variables.get(searchIdByName(variables, tokens.get(i).name)).setValue(getEqualResult(i));
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Semicolon") || isToken(i, "Pmark")) break;
                    } 
                }
            }
            else if(isToken(i, "While")) {
                i += 2;
                int e = 0;
                int gi = i;
                i++;
                while(i < tokens.size()) {
                    i++;
                    if(isToken(i, "Rparen") && e == 0) break;
                    else if(isToken(i, "Lparen")) e++;
                    else if(isToken(i, "Rparen") && e > 0) e--;
                }
                i++;
                int zi = i;
                while(i < tokens.size()) {
                    i++;
                    if(isToken(i, "RCB") && e == 0) break;
                    else if(isToken(i, "LCB")) e++;
                    else if(isToken(i, "RCB") && e > 0) e--;
                }
                while(checkTruth(gi)) {
                    realise(zi);
                }
            }
            if(isToken(i, "If")) {
                boolean trueOrFalse = false;
                i += 2;
                if(checkTruth(i)) {
                    trueOrFalse = true;
                    int e = 0;
                    i++;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Rparen") && e == 0) break;
                        else if(isToken(i, "Lparen")) e++;
                        else if(isToken(i, "Rparen") && e > 0) e--;
                    }
                    i++;
                    realise(i);
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "RCB") && e == 0) break;
                        else if(isToken(i, "LCB")) e++;
                        else if(isToken(i, "RCB") && e > 0) e--;
                        if(isToken(i + 1, "Else")) {
                            i++;
                            if(isToken(i + 1, "LCB")) {
                                i++;
                                e = 0;
                                i += 1;
                                while(i < tokens.size()) {
                                    i++;
                                    if(isToken(i, "RCB") && e == 0) break;
                                    else if(isToken(i, "LCB")) e++;
                                    else if(isToken(i, "RCB") && e > 0) e--;
                                }
                            }
                            else {
                                if(checkTruth(i + 2)) {
                                    e = 0;
                                    i++;
                                    while(i < tokens.size()) {
                                        i++;
                                        if(isToken(i, "Rparen") && e == 0) break;
                                        else if(isToken(i, "Lparen")) e++;
                                        else if(isToken(i, "Rparen") && e > 0) e--;
                                    }
                                    i++;
                                    realise(i);
                                    while(i < tokens.size()) {
                                        i++;
                                        if(isToken(i, "RCB") && e == 0) break;
                                        else if(isToken(i, "LCB")) e++;
                                        else if(isToken(i, "RCB") && e > 0) e--;
                                    }
                                }
                            }
                        }
                    }
                }
                else if(isToken(i + 1, "Else")) {
                    if(isToken(i + 2, "If")) realise(i + 2);
                    else realise(i + 3);
                }
                else if(isToken(i + 1, "Else") && !trueOrFalse) {
                    i++;
                    if(isToken(i + 1, "LCB")) {
                        i++;
                        realise(i + 1);
                        int e = 0;
                        i += 1;
                        while(i < tokens.size()) {
                            i++;
                            if(isToken(i, "RCB") && e == 0) break;
                            else if(isToken(i, "LCB")) e++;
                            else if(isToken(i, "RCB") && e > 0) e--;
                        }
                    }
                    else {
                        if(checkTruth(i + 2)) {
                            int e = 0;
                            i++;
                            while(i < tokens.size()) {
                                i++;
                                if(isToken(i, "Rparen") && e == 0) break;
                                else if(isToken(i, "Lparen")) e++;
                                else if(isToken(i, "Rparen") && e > 0) e--;
                            }
                            i++;
                            realise(i);
                            while(i < tokens.size()) {
                                i++;
                                if(isToken(i, "RCB") && e == 0) break;
                                else if(isToken(i, "LCB")) e++;
                                else if(isToken(i, "RCB") && e > 0) e--;
                            }
                        }
                    }
                }
                else {
                    int e = 0;
                    i++;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Rparen") && e == 0) break;
                        else if(isToken(i, "Lparen")) e++;
                        else if(isToken(i, "Rparen") && e > 0) e--;
                    }
                    i++;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "RCB") && e == 0) break;
                        else if(isToken(i, "LCB")) e++;
                        else if(isToken(i, "RCB") && e > 0) e--;
                    }
                }
            }
            else if(isToken(i, "Input")) {
                if(Collections.frequency(nameList, tokens.get(i + 1).name) > 0) {
                    Scanner in = new Scanner(System.in);
                    String v = in.nextLine();
                    variables.get(searchIdByName(variables, tokens.get(i + 1).name)).setValue(v);
                }
            }
            else if(isToken(i, "Print")) {
                String cout = "";
                if(isToken(i + 1, "Lparen")) {
                    i++;
                    cout = getParentsResult(i);
                    int e = 0;
                    while(i < tokens.size()) {
                        i++;
                        if(isToken(i, "Rparen") && e == 0) break;
                        else if(isToken(i, "Lparen")) e++;
                        else if(isToken(i, "Rparen") && e > 0) e--;
                    }
                }
                else if(Collections.frequency(nameList, tokens.get(i + 1).name) > 0) {
                    cout = variables.get(searchIdByName(variables, tokens.get(i + 1).name)).value;
                }
                prent(cout);
            }
        }
    }
    

    public static boolean checkTruth(int i) {
        boolean a = false;
        List<String> arguments = new ArrayList<String>();
        List<String> operands = new ArrayList<String>();
        String result = "";
        String current = "";
        String currentMark = "none";
        boolean type = false;
        boolean check = true;
        while(i < tokens.size()) {
            if(isToken(i, "Rparen")) break;
            while(i < tokens.size()) {
                if(isToken(i, "Rparen")) {
                    i--;
                    break;
                }
                if(isToken(i, "Equaly")) {
                    operands.add("equaly");
                    break;
                }
                else if(isToken(i, "More")) {
                    operands.add("more");
                    break;
                }
                else if(isToken(i, "Less")) {
                    operands.add("less");
                    break;
                }
                if(isToken(i, "Lparen")) getParentsResult(i);
                else if(isToken(i, "Plus") || isToken(i, "Minus") || isToken(i, "Multiplication") || isToken(i, "Degree")) {
                    if(isToken(i, "Plus")) currentMark = "plus";
                    else if(isToken(i, "Minus")) currentMark = "minus";
                    else if(isToken(i, "Multiplication")) currentMark = "multiplication";
                    else currentMark = "degree";
                }
                if(isToken(i, "Qmark")) {
                    if(check) {
                        check = false;
                        type = true;
                        current = tokens.get(i + 1).name;
                    }
                    else {
                        check = true;
                    }
                }
                else if(Collections.frequency(nameList, tokens.get(i).name) > 0) {
                    
                    if (Pattern.matches("[a-zA-Z]+", variables.get(searchIdByName(variables, tokens.get(i).name)).value) == true) {
                        type = true;
                    } 
                    
                    current = variables.get(searchIdByName(variables, tokens.get(i).name)).value;
                }
                else if(!tokens.get(i).value.equals("")) {
                    current = tokens.get(i).value;
                }
                if(type) {
                    result += current;
                }
                else {
                    if(result.equals("")) {
                        if(currentMark == "none") {
                            result = current;
                        }
                        else if(currentMark == "minus") {
                            result = Float.toString(-1 * Float.parseFloat(tokens.get(i + 1).value));
                            i++;
                        }
                        else {
                            //!!!!!!!!!!!!!!!!!!!!!
                        }
                    }
                    else {
                        if(!result.equals("") && !current.equals("")) {
                            if(currentMark == "plus") result = Float.toString(Float.parseFloat(result) + Float.parseFloat(current));
                            else if(currentMark == "minus") result = Float.toString(Float.parseFloat(result) - Float.parseFloat(current));
                            else if(currentMark == "degree") result = Float.toString(Float.parseFloat(result) / Float.parseFloat(current));
                            else result = Float.toString(Float.parseFloat(result) * Float.parseFloat(current));
                        }   
                    }
                }
                current = "";
                i++;
            }
            arguments.add(result);
            result = "";
            currentMark = "none";
            type = false;
            check = true;
            i++;
        }
        for(int j = 0; j + 1 < arguments.size(); j++) {
            if(operands.get(j).equals("equaly")) {
                if(arguments.get(j).equals(arguments.get(j + 1))) a = true;
            }
            else if(operands.get(j).equals("more")) {
                if(Float.parseFloat(arguments.get(j)) > Float.parseFloat(arguments.get(j + 1))) a = true;
            }
            else if(operands.get(j).equals("less")) {
                if(Float.parseFloat(arguments.get(j)) < Float.parseFloat(arguments.get(j + 1))) a = true;
            }
        }
        return a;
    }

    public static String getParentsResult(int i) {
        String result = "";
        String current = "";
        String currentMark = "none";
        boolean type = false;
        boolean check = true;
        while(i < tokens.size()) {
            if(isToken(i, "Lparen")) getParentsResult(i + 1);
            if(isToken(i, "Rparen")) break;
            else if(isToken(i, "Plus") || isToken(i, "Minus") || isToken(i, "Multiplication") || isToken(i, "Degree")) {
                if(isToken(i, "Plus")) currentMark = "plus";
                else if(isToken(i, "Minus")) currentMark = "minus";
                else if(isToken(i, "Multiplication")) currentMark = "multiplication";
                else currentMark = "degree";
            }
            if(isToken(i, "Qmark")) {
                if(check) {
                    check = false;
                    type = true;
                    current = tokens.get(i + 1).name;
                }
                else {
                    check = true;
                }
            }
            else if(Collections.frequency(nameList, tokens.get(i).name) > 0) {
                
                if (Pattern.matches("[a-zA-Z]+", variables.get(searchIdByName(variables, tokens.get(i).name)).value) == true) {
                    type = true;
                } 
                
                current = variables.get(searchIdByName(variables, tokens.get(i).name)).value;
            }
            else if(!tokens.get(i).value.equals("")) {
                current = tokens.get(i).value;
            }
            if(type) {
                result += current;
            }
            else {
                if(result.equals("")) {
                    if(currentMark == "none") {
                        result = current;
                    }
                    else if(currentMark == "minus") {
                        result = Float.toString(-1 * Float.parseFloat(tokens.get(i + 1).value));
                        i++;
                    }
                    else {
                        //!!!!!!!!!!!!!!!!!!!!!
                    }
                }
                else {
                    if(!result.equals("") && !current.equals("")) {
                        if(currentMark == "plus") result = Float.toString(Float.parseFloat(result) + Float.parseFloat(current));
                        else if(currentMark == "minus") result = Float.toString(Float.parseFloat(result) - Float.parseFloat(current));
                        else if(currentMark == "degree") result = Float.toString(Float.parseFloat(result) / Float.parseFloat(current));
                        else result = Float.toString(Float.parseFloat(result) * Float.parseFloat(current));
                    }   
                }
            }
            current = "";
            i++;
        }
        return result;
    }

    public static String getEqualResult(int i) {
        String result = "";
        String current = "";
        String currentMark = "none";
        boolean type = false;
        boolean check = true;
        while(i < tokens.size()) {
            i++;
            if(isToken(i, "Semicolon") || isToken(i, "Pmark")) break;
            if(isToken(i, "Plus") || isToken(i, "Minus") || isToken(i, "Multiplication") || isToken(i, "Degree")) {
                if(isToken(i, "Plus")) currentMark = "plus";
                else if(isToken(i, "Minus")) currentMark = "minus";
                else if(isToken(i, "Multiplication")) currentMark = "multiplication";
                else currentMark = "degree";
            }
            if(isToken(i, "Qmark")) {
                if(check) {
                    check = false;
                    type = true;
                    current = tokens.get(i + 1).name;
                }
                else {
                    check = true;
                }
            }
            else if(isToken(i, "Lparen")) {
                current = getParentsResult(i + 1);
                while(i < tokens.size()) {
                    i++;
                    int j = 0;
                    if(isToken(i, "Rparen") && j == 0) break;
                    else if(isToken(i, "Lparen")) j++;
                    else if(isToken(i, "Rparen") && j > 0) j--;
                }
            }
            else if(Collections.frequency(nameList, tokens.get(i).name) > 0) {
                if (Pattern.matches("[a-zA-Z]+", variables.get(searchIdByName(variables, tokens.get(i).name)).value) == true) {
                    type = true;
                } 
                
                current = variables.get(searchIdByName(variables, tokens.get(i).name)).value;
            }
            else if(!tokens.get(i).value.equals("")) {
                current = tokens.get(i).value;
            }
            if(type) {
                result += current;
            }
            else {
                if(result.equals("")) {
                    if(currentMark == "none") {
                        result = current;
                    }
                    else if(currentMark == "minus") {
                        result = Float.toString(-1 * Float.parseFloat(tokens.get(i + 1).value));
                        i++;
                    }
                    else {
                        //!!!!!!!!!!!!!!!!!!!!!
                    }
                }
                else {
                    if(!result.equals("") && !current.equals("")) {
                        if(currentMark == "plus") result = Float.toString(Float.parseFloat(result) + Float.parseFloat(current));
                        else if(currentMark == "minus") result = Float.toString(Float.parseFloat(result) - Float.parseFloat(current));
                        else if(currentMark == "degree") result = Float.toString(Float.parseFloat(result) / Float.parseFloat(current));
                        else result = Float.toString(Float.parseFloat(result) * Float.parseFloat(current));
                    }   
                }
            }
            current = "";
        }
        return result;
    }
    public static String getQmarkResult(int i) {
        i++;
        int j = 0;
        String result = "";
        while((i + 1) < tokens.size() && !isToken(i, "Qmark")) {
            if(j == 0) {
                result = tokens.get(i).name + tokens.get(i).value;
            }
            else {
                result += tokens.get(i).name + tokens.get(i).value;
            }
            i++;
            j++;
        }
        return result;
    }


    public static boolean isToken(int id, String t) {
        if(id < tokens.size() && tokens.get(id).type == Lexer.Type.valueOf(t)) return true;
        else return false;
    }
    public static int searchIdByName(List<variable> v, String name) {
        int lastId = 0;
        for(int i = 0; i < v.size(); i++) {
            if(v.get(i).name.equals(name)) lastId = i;
        }
        return lastId;
    }

    public static void prent(String a) {
        System.out.println(a);
    }
    public static void drawVariable(int id) {
        System.out.println(" \n" + variables.get(id).name + " \n" + variables.get(id).value + " \n");
    }
}