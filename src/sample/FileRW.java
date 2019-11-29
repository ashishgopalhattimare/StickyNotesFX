package sample;

import java.io.*;

public class FileRW
{
    private static String filePath = "account.txt";

    public static String[] readFile()
    {
        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            br.close();

            if(line != null) return line.split("[ ]");
        }
        catch (Exception e) {}

        return null;
    }

    public static void writeFile(String str)
    {
        try {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new FileWriter(filePath, false));
                writer.print(str);
            } catch (IOException e) {}
            finally {
                if(writer != null) writer.close();
            }
        }
        catch (Exception e) {}
    }
}
