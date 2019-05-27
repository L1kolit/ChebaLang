import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.Character;


public class Lexer {
    public static FileRead lets;

    public static Parser p;

    public static enum Type {
        Lparen, Rparen, Dim, Equaly, Qmark, Pmark, Semicolon, Unknown, Plus, Minus, Multiplication
        , Degree, Print, Input, If, Else, More, Less, Un, LCB, RCB, While, Dot;
    }

    public static class Token {
        public final Type type;
        public final String name;
        public final String value;

        public Token(Type t, String c, String v) {
            type = t;
            name = c;
            value = v;
        }

        public String toString() {
            return "<" + type + " " + name + " " + value + ">";
        }
    }

    public static String getName(String s, int i) {
        Scanner in = new Scanner(System.in);
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isLetter(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }

    public static String getValue(String s, int i) {
        Scanner in = new Scanner(System.in);
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isDigit(s.charAt(j)) || String.valueOf(s.charAt(j)).equals(".")) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }

    public static String skipNulls(String s, int i) {
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isWhitespace(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }


    public static List<Token> reader(String stroke) {
        ArrayList<Token> result = new ArrayList<Token>();
        for(int i = 0; i < stroke.length(); i++) {
            String res = String.valueOf(stroke.charAt(i));

            if(res.equals(" ")) {continue;}
            else if(res.equals("{")) result.add(new Token(Type.LCB, "", ""));
            else if(res.equals("}")) result.add(new Token(Type.RCB, "", ""));
            else if(res.equals("(")) result.add(new Token(Type.Lparen, "", ""));
            else if(res.equals(")")) result.add(new Token(Type.Rparen, "", ""));
            else if(res.equals(">")) result.add(new Token(Type.More, "", ""));
            else if(res.equals("<")) result.add(new Token(Type.Less, "", ""));
            else if(res.equals("!")) result.add(new Token(Type.Un, "", ""));
            else if(res.equals(".")) result.add(new Token(Type.Dot, "", ""));
            else if(res.equals("'")) {
                result.add(new Token(Type.Qmark, "", ""));
                String qmres = "";
                while(true) {
                    i++;
                    if(String.valueOf(stroke.charAt(i)).equals("'")) break;
                    else {
                        qmres += String.valueOf(stroke.charAt(i));
                    }
                }
                result.add(new Token(Type.Unknown, qmres, ""));
                result.add(new Token(Type.Qmark, "", ""));
            }
            else if(res.equals(",")) result.add(new Token(Type.Pmark, "", ""));
            else if(res.equals(";")) result.add(new Token(Type.Semicolon, "", ""));
            else if(res.equals("=")) result.add(new Token(Type.Equaly, "", ""));
            else if(res.equals("+")) result.add(new Token(Type.Plus, "", ""));
            else if(res.equals("-")) result.add(new Token(Type.Minus, "", ""));
            else if(res.equals("*")) result.add(new Token(Type.Multiplication, "", ""));
            else if(res.equals("~")) break;
            else {
                if(Character.isDigit(stroke.charAt(i))) {
                    String name = getValue(stroke, i);
                    i += name.length() - 1;
                    boolean exists = true; 
                    try {Type.valueOf(name);} catch (IllegalArgumentException e) {exists = false;}
                    if(exists) result.add(new Token(Type.valueOf(name), "", name));
                    else result.add(new Token(Type.Unknown, "", name));
                }
                else {
                    String name = getName(stroke, i);
                    i += name.length() - 1;
                    boolean exists = true; 
                    try {Type.valueOf(name);} catch (IllegalArgumentException e) {exists = false;}
                    if(exists) result.add(new Token(Type.valueOf(name), name, ""));
                    else result.add(new Token(Type.Unknown, name, ""));
                }
            }
        }
        return result;
    }

    public static void writer(String stroke) {
        List<Token> tokens = reader(stroke);
        for(Token t : tokens) {
            System.out.println(t);
        }
    }

    public static void main(String args[]) {

        //List<Token> tokens = reader(stroke);
        //for(Token t : tokens) System.out.println(t);

        int a = 0;

        if(a == 0) {
            p.sParse(reader(lets.fRead("codepole.txt")));
        }
        else if(a == 1) {
            List<Token> tokens = reader(lets.fRead("codepole.txt"));
            for(Token t : tokens) System.out.println(t);
        }
    }
}