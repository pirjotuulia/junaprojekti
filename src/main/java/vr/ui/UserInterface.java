package vr.ui;

import vr.data.train.BackgroundData;
import vr.data.train.JsonReadData;
import java.util.Scanner;

/**
 * by Pirjo, ASCII art by Kaarina
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

    public void start() {//welcome screen
        System.out.println ("              _                            _                       ");
        System.out.println ("__      _____| | ___ ___  _ __ ___   ___  | |_ ___         ");
        System.out.println ("\\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\  ");
        System.out.println (" \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) |   ");
        System.out.println ("  \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/    ");

        System.out.println ("                            _   ");
        System.out.println ("               __      _____( )_ __ ");
        System.out.println ("               \\ \\ /\\ / / _ \\/| '__|");
        System.out.println ("                \\ V  V /  __/ | |   ");
        System.out.println ("                \\_/\\_/ \\___| |_| ");
        System.out.println (" _                       _    ");
        System.out.println ("| |__   __ _ _ __  _ __ (_)_ __   ___  ___ ___ ");
        System.out.println ("| '_ \\ / _` | '_ \\| '_ \\| | '_ \\ / _ \\/ __/ __|");
        System.out.println ("| | | | (_| | |_) | |_) | | | | |  __/\\__ \\__ \\");
        System.out.println ("|_| |_|\\__,_| .__/| .__/|_|_| |_|\\___||___/___/");
        System.out.println ("            |_|   |_|                          ");
        System.out.println ("");
        System.out.println("=============================================");
        System.out.println("      WE'R HAPPINESS AT YOUR SERVICE!  ");
        System.out.println("=============================================");
        while (true) {
            System.out.println("");
            System.out.println("=============================================");
            System.out.println(" 1) Timetable search - passenger trains");
            System.out.println(" 2) Call Santa.");
            System.out.println(" 3) Find nearest Sauna.");
            System.out.println(" 4) Exit");
            System.out.println("=============================================");
            System.out.println("");
            System.out.print("Your choice: ");
            String answer = reader.nextLine();
            if (answer.equals("1")) {
                Search search = new Search(reader, bgrdata, trainData);
                search.timetableSearch();
            } else if (answer.equals("2")) {
                System.out.println ("");
                System.out.println("Call Santa at Santa Claus Village amusement park, Rovaniemi: +358 16 3562096");
            } else if (answer.equals("3")) {
                System.out.println ("");
                System.out.println("Nearest sauna: Löyly at Hernesaarenranta 4, 00150 Helsinki");
            } else if (answer.equals("4")) {
                break;
            }
        }
        System.out.println ("");
        System.out.println (" _____ _                 _                        _ ");
        System.out.println ("|_   _| |__   __ _ _ __ | | __  _   _  ___  _   _| |");
        System.out.println ("  | | | '_ \\ / _` | '_ \\| |/ / | | | |/ _ \\| | | | |");
        System.out.println ("  | | | | | | (_| | | | |   <  | |_| | (_) | |_| |_|");
        System.out.println ("  |_| |_| |_|\\__,_|_| |_|_|\\_\\  \\__, |\\___/ \\__,_(_)");
        System.out.println ("                                |___/               ");
        System.out.println("Thank you for using our happiness search!");
        System.out.println("Hope we satisfied your needs.");

        credits();
    }

    private void credits() {//who and what
        System.out.println("\nTeam: Johannes, Kaarina, Pirjo");
        System.out.println("Data:");
        System.out.println("\tTrains&stations: https://rata.digitraffic.fi/");
        System.out.println("\tWeather: https://openweathermap.org/");
    }
}