package texteditor;

import java.io.*;

public class TextEditorIO {
    String text;

    public TextEditorIO() {
        text = "";
    }

    public boolean write(String fileName, String input) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
            bw.write(input);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String read(File source) {
        String result = "", temp = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(source));
            while((temp = br.readLine()) != null) {
                result += temp + "\n";
                System.out.println("LINE ADDED SUCCESSFULLY");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
