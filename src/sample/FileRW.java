package sample;

import java.io.*;

public class FileRW
{
    // Where the current user's details are stored (username, password)
    private static String filePath = "account.txt";

    /**
     * Method Name : readFile
     * @return - Username and Password as tuple, else null for no data found
     */
    public static String[] readFile()
    {
        try {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            br.close();
            if(line != null) return line.split("[ ]");
        }
        catch (Exception ignored) {} // end try/catch
        return null;

    }// end readFile()

    /**
     * Method Name : writeFile
     * Purpose : Write username and password on the account for current login details next time
     * @param str - String to store in the file
     */
    public static void writeFile(String str)
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            writer.print(str);
        } catch (IOException ignored) { }

    }// end writeFile(String)
}
