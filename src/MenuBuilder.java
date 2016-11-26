import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.*;

/**
 * Created by chyyran on 11/25/16.
 */
public class MenuBuilder {

    private List<AbstractMap.SimpleEntry<String,Runnable>> runnables;
    private Runnable exit = () -> System.out.print("Goodbye");
    private Consumer<Exception> error = (e) -> System.out.print("Error: " + e.getMessage());

    public MenuBuilder() {
        this.runnables = new ArrayList<>();
    }

    public MenuBuilder option(String menuOptionName, Runnable r){
        this.runnables.add(new AbstractMap.SimpleEntry<>(menuOptionName, r));
        return this;
    }

    public MenuBuilder exit(Runnable r) {
        this.exit = r;
        return this;
    }

    public MenuBuilder error(Consumer<Exception> r) {
        this.error = r;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < this.runnables.size(); i++) {
            sb.append(String.format("[%1$d] %2$s", (i + 1), this.runnables.get(i).getKey()) + "\n");
        }
        return sb.toString();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        do {
            System.out.println("Select from the choices below. Enter 0 or less than to exit.");
            System.out.print(this);
            selection = scanner.nextInt();
            scanner.nextLine();
            if(selection > this.runnables.size()) {
                System.out.println("Invalid choice! Please select from the menu options.");
                continue;
            }
            try {
                if (selection > 0) this.runnables.get(selection - 1).getValue().run();
            }catch (Exception e) {
                this.error.accept(e);
            }
            System.out.print("Press enter to continue.");
            scanner.nextLine();
            System.out.println();
        } while(selection > 0);
        this.exit.run();
    }


}
