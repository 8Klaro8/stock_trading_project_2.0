package com.stockmarket;

import java.util.List;
import java.util.Scanner;

import javax.crypto.spec.OAEPParameterSpec;
import javax.sound.midi.Track;
import javax.sound.sampled.Port;

import com.stockmarket.Manager_classes.StockMarketManager;
import com.stockmarket.Manager_classes.TradersManager;
import com.stockmarket.SubItems.Portfolio;
import com.stockmarket.SubItems.Trader;

public class Motor {
    public static Scanner scan = new Scanner(System.in);

    public static void testLine() {

        do {
            System.out.println("Create user?");
            String ans = scan.nextLine();
            if (!(ans.equalsIgnoreCase("yes") || ans.equalsIgnoreCase("no"))) {
                continue;
            }

            // ask details
            System.out.println("Type username");
            String username = scan.nextLine();
            System.out.println("Type Password");
            String password = scan.nextLine();

            // create user
            Trader trader = new Trader();
            trader.setUsername(username);
            trader.setPassword(HashPassword.hashPassword(password));

            System.out.println("Your Profile:");
            System.out.println(trader.toString());
            System.out.println(trader.getPortfolio().toString());

        } while (true);
    }

    public static void start() {
        String answer;
        do {
            System.out.println("Do you have an account? yes/no");
            answer = scan.nextLine();
        } while (!(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")));

        switch (answer) {
            case "yes":
                login();
                break;
            case "no":
                createAccount();
                break;

            default:
                break;
        }
    }

    public static void createAccount() {
        Trader trader;
        String username;
        do {
            System.out.println("Choose a username:");
            username = scan.nextLine();
            if (TradersManager.traderExists(username)) {
                System.out.println("\nTrader with username: " + username + " already exists!");
                continue;
            } else {
                break;
            }
        } while (true);

        System.out.println("Choose a password");
        String password = HashPassword.hashPassword(scan.nextLine());
        System.out.println("Do you want to deposit funds now? yes/no");

        do {
            String depositFunds = scan.nextLine();
            if (depositFunds.equalsIgnoreCase("yes")) {
                // deposit funds
                double fundsToDeposit = containsLetter("How much would you like to deposit?", "Type only numbers!");
                trader = new Trader();
                trader.setFunds(fundsToDeposit);
                trader.setUsername(username);
                trader.setPassword(password);

                TradersManager.saveTrader(trader);
                break;
            } else if (depositFunds.equalsIgnoreCase("no")) {
                // create trader account
                trader = new Trader(username, password);
                // create portfolio
                Portfolio portfolio = new Portfolio();
                trader.setPortfolio(portfolio);
                // save trader
                TradersManager.saveTraderWithoutFunds(trader);
                break;
            }
        } while (true);

        // show options
        do {
            showOptions(trader);
        } while (true);

    }

    private static double containsLetter(String question, String warning) {
        Boolean isLetter;
        String amount;
        do {
            isLetter = false;
            System.out.println(question);
            amount = scan.nextLine();
            amount = amount.strip();
            if (amount.equalsIgnoreCase("")) {
                System.out.println("Wrong input");
                isLetter = true;
            }
            for (int i = 0; i < amount.length(); i++) {
                char currChar = amount.charAt(i);
                if (Character.isAlphabetic(currChar)) {
                    isLetter = true;
                }
            }
            if (isLetter) {
                System.out.println(warning);
            }
        } while (isLetter);
        return Double.valueOf(amount);
    }

    private static void login() {

        String username;

        do {
            System.out.println("Type username:");
            username = scan.nextLine();
            if (!(TradersManager.traderExists(username))) {
                System.out.println("\nTrader with username: " + username + " doest not exists!");
                continue;
            } else {
                break;
            }
        } while (true);

        do {
            System.out.println("Type password:");
            String password = scan.nextLine();

            if (HashPassword.arePasswordEqual(password, TradersManager.getUsersHashedPassword(username))) {
                break;
            } else if (password.equalsIgnoreCase("q")) {
                System.out.println("Bye <3, Come back!");
                System.exit(0);
            } else {
                System.out.println(
                        "\n---------------------------\nPassword is incorrect. Try again. or press 'Q' to quit.\n");
                continue;
            }
        } while (true);

        // create trader & portfolio

        Trader trader = TradersManager.readTrader(username);

        System.out.println("Welcome " + trader.getUsername() + "!");
        if (trader.getPortfolio().toString().equalsIgnoreCase("")) {
            System.err.println("There is no protfolio yet.");
            System.out.println("Available funds: " + trader.getFunds());
        }

        do {
            showOptions(trader);
        } while (true);
    }

    public static void showOptions(Trader trader) {
        String question = "\nWhat would you like to do?\n\t1.) Check Portfolio\n\t2.) Buy Stock/ Share\n\t3.) Sell Stock/ Share\n\t4.) Deposit funds\n\t5.) Logout\n\n\t6.) Exit";

        String choice;
        choice = String.valueOf(floorNum(containsLetter(question, "Type number only (1-3)")));

        switch (choice) {
            case "1":
                checkPortfolio(trader);
                break;
            case "2":
                buy(trader);
                break;
            case "3":
                sell(trader);
                break;
            case "4":
                addDeposit(trader);
                break;
            case "5":
                logout(trader);
                break;
            case "6":
                System.out.println("\nCome back " + trader.getUsername() + "!");
                System.exit(0);
                break;

            default:
                System.out.println("Wrong");
                break;
        }
    }

    public static void logout(Trader trader) {
        System.out.println("\n--------------------\nBye " + trader.getUsername() + " <3!\n--------------------\n");
        start();
    }

    public static void addDeposit(Trader trader) {
        double deposit = containsLetter("How much would you like to deposit?", "Type number only.");
        trader.updateFunds(deposit, "+");
        TradersManager.saveTrader(trader);
    }

    public static void checkPortfolio(Trader trader) {
        if (trader.getPortfolio().toString().equalsIgnoreCase("")) {
            System.err.println("There is no protfolio yet.");
            System.out.println("Available funds: " + trader.getFunds());
        } else {
            System.out.println(trader.getPortfolio().toString());
            System.out.println("Available funds: " + trader.getFunds());
        }

    }

    public static void sell(Trader trader) {
        System.out.println("\nYour stocks:\n");
        trader.getPortfolio().dispalyStocksForSell();

    }

    public static void buy(Trader trader) {
        // dispaly avaialble stock/ stock market
        StockMarketManager.dispalyStockMarket();
        List<String> stockNames = StockMarketManager.collectStocksNames();
        boolean isInRange;
        int choice;
        do {
            choice = floorNum(containsLetter("Choose a stock (1-" + stockNames.size() + ")", "Type number only"));
            isInRange = true;
            if (choice < 1 || choice > stockNames.size()) {
                System.out.println("Please type number in the given range");
                isInRange = false;
            }
        } while (!(isInRange));

        choice--;
        String stockToBuy = stockNames.get(choice);
        double shareNum = containsLetter("How much would you like to buy?", "Type only number.");
        trader.buyStock(stockToBuy, shareNum);
    }

    public static int floorNum(double num) {
        return (int) Math.floor(num);
    }

}
