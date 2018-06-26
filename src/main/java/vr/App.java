package vr;

import vr.data.BackgroundData;
import vr.ui.UserInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * by Pirjo
 */
public class App {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        BackgroundData bgrdata = new BackgroundData();
        Map<String, String> stationShortCodes = bgrdata.generateShortCodes();
        UserInterface ui = new UserInterface(reader, stationShortCodes);
        ui.start();
    }
}
