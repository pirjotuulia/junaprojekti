package vr;

import vr.data.BackgroundData;
import vr.data.JsonReadData;
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
        JsonReadData trainData = new JsonReadData();
        UserInterface ui = new UserInterface(reader, bgrdata, trainData);
        ui.start();
    }
}
