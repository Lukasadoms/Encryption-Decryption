package encryptdecrypt;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static String mode = "enc";
    public static int key = 0;
    public static String data = "";
    public static String filePath = "";
    public static String alg = "";
    public static String result= "";


    public static void main(String[] args) {

        extractArgs(args);
        Finder finder = null;

        switch (alg) {
            case "unicode":
                finder = new Finder(new Unicode());
                break;
            case "shift":
                finder = new Finder(new Shift());
                break;
            default:
                break;
        }
        switch (mode) {
            case "enc":
                result = finder.encode(data, key);
                break;
            case "dec":
                result = finder.decode(data, key);
        }
        System.out.println(result);
        writeDataToFile(result, filePath);
    }


    private static void extractArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-in":
                    data = readDataFromFile(args[i + 1]);
                    break;
                case "-mode":
                    mode = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-out":
                    filePath = args[i + 1];
                    break;
                case "-data":
                    data = args[i + 1];
                case "-alg":
                    alg = args[i + 1];

                default:
                    break;
            }
        }

    }


    private static void writeDataToFile(String result, String filePath) {
        if (filePath != null) {
            try {
                FileWriter writer = new FileWriter(filePath);
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(result);
        }
    }

    private static String readDataFromFile(String arg) {
        try {
            Scanner scanner = new Scanner(new File(arg));
            data = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
}


class Finder {

        private FindingStrategy strategy;

        public Finder(FindingStrategy strategy) {
            this.strategy = strategy;
        }

        /**
         * It performs the search algorithm according to the given strategy
         */
        public String encode(String data, int key) {
            return this.strategy.encode(data, key);
        }
        public String decode(String data, int key) {
            return this.strategy.decode(data, key);
        }
}

interface FindingStrategy {

        /**
         * Returns search result
         */
        String encode(String data, int key);
        String decode(String data, int key);

}

class Unicode implements FindingStrategy {

        @Override
        public String encode(String data, int key) {
            StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < data.length(); i++) {
                        int shiftByKey = data.charAt(i) + key;
                       // int shift = shiftByKey > 127 ?
                         //       shiftByKey - 128                  Commented out loop from stage 5
                           //     : shiftByKey;
                        sb.append((char) shiftByKey);
                    }
                    return sb.toString();

        }
        @Override
        public String decode(String data, int key){
            StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < data.length(); i++) {
                        int shiftByKey = data.charAt(i) - key;
                       // int shift = shiftByKey < 0 ?
                       //         shiftByKey + 128          Commented out loop from stage 5
                       //         : shiftByKey;
                        sb.append((char) shiftByKey);
                    }
                    return sb.toString();

        }


}
class Shift implements FindingStrategy {

        @Override
        public String encode(String data, int key) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) != ' ') {
                    int shiftByKey = data.charAt(i) + key;
                    int shift = shiftByKey > 122 ?
                            shiftByKey - 26
                            : shiftByKey;
                    sb.append((char) shift);
                }
                else sb.append(data.charAt(i));
            }
             return sb.toString();

        }
        @Override
        public String decode(String data, int key) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length(); i++) {
                if (data.charAt(i) != ' ') {
                    int shiftByKey = data.charAt(i) - key;
                    int shift = shiftByKey < 97 ?
                            shiftByKey + 26
                            : shiftByKey;
                    sb.append((char) shift);
                }
                else sb.append(data.charAt(i));
            }
            return sb.toString();
        }
}





