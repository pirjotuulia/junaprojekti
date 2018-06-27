package vr.ui;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
import vr.data.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * by Pirjo
 */
public class UserInterface {
    private Scanner reader;
    private JsonReadData trainData;
    private BackgroundData bgrdata;

    public UserInterface(Scanner reader, BackgroundData bgrdata, JsonReadData trainData) {
        this.reader = reader;
        this.bgrdata = bgrdata;
        this.trainData = trainData;
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("            WE'R HAPPINESS!  ");
        System.out.println("========================================");
        while (true) {
            System.out.println("");
            System.out.println(" 1) Timetable search - passenger trains");
            System.out.println(" 2) Call Santa.");
            System.out.println(" 3) Find nearest Sauna.");
            System.out.println(" 4) Exit\n");
            System.out.print("Your choice: ");
            String answer = reader.nextLine();
            if (answer.equals("1")) {
                Search search = new Search(reader, bgrdata, trainData);
                search.timetableSearch();
            } else if (answer.equals("2")) {
                System.out.println("Call Santa at Santa Claus Village amusement park, Rovaniemi: +358 16 3562096");
            } else if (answer.equals("3")) {
                System.out.println("Nearest sauna: Löyly at Hernesaarenranta 4, 00150 Helsinki");
            } else if (answer.equals("4")) {
                break;
            }
        }
        System.out.println("Thank you for using our happiness search!");

    }
}