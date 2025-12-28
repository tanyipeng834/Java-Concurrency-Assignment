package logger;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static void log(String message) {
        try {
            FileWriter myWriter = new FileWriter("log.txt", true);
            myWriter.write(message + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return;
    }
}
