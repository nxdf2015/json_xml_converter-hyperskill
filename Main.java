package converter;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        Converter converter = new Converter();
        String fileName = "test.txt";
        String line=null;
        try(BufferedReader reader=new BufferedReader(new FileReader(new File(fileName)))) {
            line = reader.lines().collect(Collectors.joining());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = null;

        if (line.startsWith("{")){
            response = converter.toXML(line);
        }
        else {
            response = converter.toJSON(line);
        }
        System.out.println(response);
    }
}
