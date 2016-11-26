import java.io.InputStream;
import java.util.*;

/**
 * Utility prompter that wraps scanner
 * @author Ronny Chan
 * File Name: Prompter.java
 * Package: grade11review
 */
public class Prompter {

    /**
     * The wrapped scanner object
     */
    public Scanner scanner;

    public Prompter(InputStream input) {
        this.scanner = new Scanner(input);
    }

    /**
     * Prompts for an integer value
     * Will prompt on the same line with colon.
     * @param prompt The prompt to display.
     * @return The integer the user input.
     */
    public int promptInt(String prompt) {
        System.out.print(prompt + ": ");
        return Integer.parseInt(this.scanner.nextLine());
    }
    /**
     * Prompts for a double value
     * Will prompt on the same line with colon.
     * @param prompt The prompt to display.
     * @return The double the user input.
     */
    public double promptDouble(String prompt) {
        System.out.print(prompt + ": ");
        return Double.parseDouble(this.scanner.nextLine());
    }
    /**
     * Prompts for a String line.
     * Will prompt on the same line with colon.
     * @param prompt The prompt to display.
     * @return The line the user input.
     */
    public String promptLine(String prompt) {
        System.out.print(prompt + ": ");
        return this.scanner.nextLine();
    }

    /**
     * Prompts for a character
     * @param prompt
     * Will prompt on the same line with colon.
     * @param prompt The prompt to display.
     * @return The first character the user input.
     */
    public char promptChar(String prompt)
    {
        return this.promptLine(prompt).charAt(0);
    }

}
