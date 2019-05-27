import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.Character;
import java.io.*;

class FileRead {
    public static String fRead(String file) {
        String finalS = "";
        try(FileReader reader = new FileReader(file))
        {
           // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){
                 
                if((char)c == '\n') {
                    finalS += " ";
                }
                else {
                    finalS += (char)c;
                }
            } 
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
        return finalS;
    }
}