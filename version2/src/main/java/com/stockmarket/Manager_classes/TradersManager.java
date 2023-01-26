package com.stockmarket.Manager_classes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.stockmarket.SubItems.Portfolio;
import com.stockmarket.SubItems.Stock;
import com.stockmarket.SubItems.Trader;

public class TradersManager {
    private static CSVWriter writer;
    private static CSVReader reader;
    private static File file;
    private static final String PATH = "version2/src/main/java/com/stockmarket/Traders/traders.csv";

    public static java.lang.reflect.Type type = new TypeToken<JsonObject>() {
    }.getType();
    public static Gson gson = new Gson();

    public static void saveTrader(Trader trader) {
        reader = createReader();
        String username = trader.getUsername();
        String password = trader.getPassword();
        Double funds = trader.getFunds();
        Portfolio portfolio = new Portfolio(trader.getPortfolio());

        JsonObject myDict = new JsonObject();
        myDict.addProperty("username", username);
        myDict.addProperty("password", password);
        myDict.addProperty("funds", funds);

        JsonObject portfolioObject = new JsonObject();
        JsonArray stocksObject = new JsonArray();
        for (Stock currStock : portfolio.getStocks()) {
            stocksObject.add(currStock.getStockName());
        }
        portfolioObject.add("stock_names", stocksObject);
        myDict.add("portfolio", portfolioObject);

        JsonArray stockShareCount = new JsonArray();
        for (Stock currStock : portfolio.getStocks()) {
            stockShareCount.add(currStock.getShareCount());
        }

        portfolioObject.add("stock_sharecount", stockShareCount);

        Gson gson = new Gson();
        String[] stringMyDict = new String[] { gson.toJson(myDict, new TypeToken<JsonObject>() {
        }.getType()) };

        appendNewTraderValue(username, stringMyDict);

    }

    public static void appendNewTraderValue(String username, String[] newTraderValue) {
        reader = createReader();
        List<String[]> allData;
        List<String[]> allData2 = new ArrayList<String[]>();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                JsonObject jsonObject = gson.fromJson(row[0], type);
                if (jsonObject.get("username").getAsString().equalsIgnoreCase(username)) {
                    continue;
                } else {
                    allData2.add(row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        allData2.add(newTraderValue);
        writer = createWriter();
        writer.writeAll(allData2);
        try {
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveTraderWithoutFunds(Trader trader) {
        String username = trader.getUsername();
        String password = trader.getPassword();
        List<String[]> allData;
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            JsonObject myDict = new JsonObject();
            myDict.addProperty("username", username);
            myDict.addProperty("password", password);
            myDict.addProperty("funds", 0.0);

            JsonObject portfolioObject = new JsonObject();
            JsonArray stocksObject = new JsonArray();

            portfolioObject.add("stock_names", stocksObject);
            myDict.add("portfolio", portfolioObject);

            JsonArray stockShareCount = new JsonArray();

            portfolioObject.add("stock_sharecount", stockShareCount);

            Gson gson = new Gson();
            String[] stringMyDict = new String[] { gson.toJson(myDict, new TypeToken<JsonObject>() {
            }.getType()) };

            allData.add(stringMyDict);

            writer = createWriter();
            writer.writeAll(allData);

            try {
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Trader readTrader(String username) {
        reader = createReader();
        JsonObject userOBject = new JsonObject();
        List<String[]> allData;
        java.lang.reflect.Type type = new TypeToken<JsonObject>() {
        }.getType();
        Gson gson = new Gson();

        String storedName;
        String storedPassword;
        Double storedFunds;

        Portfolio portfolio = new Portfolio();
        Trader trader = new Trader();
        Stock stock;

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                userOBject = gson.fromJson(row[0], type);

                storedName = userOBject.get("username").getAsString();
                if (storedName.equalsIgnoreCase(username)) {
                    storedPassword = userOBject.get("password").getAsString();
                    storedFunds = userOBject.get("funds").getAsDouble();

                    // load saved data to trader object
                    trader.setUsername(storedName);
                    trader.setPassword(storedPassword);
                    trader.setFunds(storedFunds);

                    JsonObject portF = userOBject.get("portfolio").getAsJsonObject();
                    JsonArray stockNames = portF.get("stock_names").getAsJsonArray();
                    JsonArray stockShareCount = portF.get("stock_sharecount").getAsJsonArray();

                    for (int i = 0; i < stockNames.size(); i++) {
                        String currStockName = stockNames.get(i).getAsString();
                        Double currShareCount = stockShareCount.get(i).getAsDouble();

                        // get ticker
                        String currTicker = StockMarketManager.getCurrentStockTicker(currStockName);

                        // estabilishing stock
                        stock = new Stock(currStockName, currTicker, currShareCount);

                        // add created stock to portfolio
                        portfolio.addStock(stock, currShareCount);
                    }

                    trader.setPortfolio(portfolio);
                    return trader;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean traderExists(String username) {
        reader = createReader();
        List<String[]> allData;

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                JsonObject jsonObject = gson.fromJson(row[0], type);
                if (username.equalsIgnoreCase(jsonObject.get("username").getAsString())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUsersHashedPassword(String username) {
        reader = createReader();
        List<String[]> allData;

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                JsonObject jsonObject = gson.fromJson(row[0], type);
                if (username.equalsIgnoreCase(jsonObject.get("username").getAsString())) {
                    return jsonObject.get("password").getAsString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CSVReader createReader() {
        file = new File(PATH);
        try {
            reader = new CSVReader(new FileReader(file));
            return reader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CSVWriter createWriter() {
        file = new File(PATH);
        try {
            writer = new CSVWriter(new FileWriter(file));
            return writer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
