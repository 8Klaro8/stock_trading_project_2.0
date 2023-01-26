package com.stockmarket.Manager_classes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.stockmarket.SubItems.Stock;

public class StockMarketManager {

    private static CSVWriter writer;
    private static CSVReader reader;
    private static File file;
    // private static String PATH = "version2/src/main/java/com/stockmarket/Stock_market/stockMarket.csv";
    private static String PATH = "C:/Users/gergr/OneDrive/Dokumentumok/Java/stock_trading_project_2.0/version2/src/main/java/com/stockmarket/Stock_market/stockMarket.csv";
    // 

    public static Double getCurrentStockPrice(String stockName) {
        if (!(stockExits(stockName))) {
            throw new IllegalArgumentException("The stock: " + stockName + " doesnt exists!");
        }
        List<String[]> allData = new ArrayList<String[]>();
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(stockName)) {
                    return Double.valueOf(row[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentStockTicker(String stockName) {
        if (!(stockExits(stockName))) {
            throw new IllegalArgumentException("The stock: " + stockName + " doesnt exists!");
        }
        List<String[]> allData = new ArrayList<String[]>();
        String[] lineToAdd;
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(stockName)) {
                    return row[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double getCurrentStockShareCount(String stockName) {
        if (!(stockExits(stockName))) {
            throw new IllegalArgumentException("The stock: " + stockName + " doesnt exists!");
        }
        List<String[]> allData = new ArrayList<String[]>();
        String[] lineToAdd;
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(stockName)) {
                    return Double.valueOf(row[3]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCurrentStock(Stock stock) {
        String name = stock.getStockName();
        String ticker = stock.getTicker();
        Double price = stock.getPrice();
        Double share = stock.getShareCount();
        List<String[]> allData = new ArrayList<String[]>();
        String[] lineToAdd;
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewStockToDataBase(Stock stock) {
        if (stockExits(stock.getStockName())) {
            System.out.println("Stock: " + stock.getStockName() + " already exists!");
            return;
        }
        String name = stock.getStockName();
        String ticker = stock.getTicker();
        Double price = stock.getPrice();
        Double share = stock.getShareCount();
        List<String[]> allData = new ArrayList<String[]>();
        String[] lineToAdd;
        reader = createReader();

        // first read all data
        try {
            allData = reader.readAll();
            reader.close();
            // if data has not yet been added then add first line
            if (allData.size() < 1) {
                lineToAdd = new String[] { name, ticker, String.valueOf(price), String.valueOf(share) };
                allData.add(lineToAdd);

                writer = createWriter();
                writer.writeAll(allData);
                writer.flush();
                writer.close();
                return;
            } else {
                lineToAdd = new String[] { name, ticker, String.valueOf(price), String.valueOf(share) };
                allData.add(lineToAdd);

                writer = createWriter();
                writer.writeAll(allData);
                writer.flush();
                writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean stockExits(String stockName) {
        reader = createReader();
        List<String[]> allData;

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(stockName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

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

    public static void updateStockPrice(Stock stock, String operation) {
        String name = stock.getStockName();
        String ticker = stock.getTicker();
        Double price = StockMarketManager.getCurrentStockPrice(name);
        Double shareCount = stock.getShareCount();
        List<String[]> allData = new ArrayList<String[]>();
        List<String[]> allData2 = new ArrayList<String[]>();
        String[] lineToAdd;
        Double newPrice = 0.0;
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(name)) {
                    switch (operation) {
                        case "+":
                            newPrice = price * 1.05;
                            break;
                        case "-":
                            newPrice = price * 0.95;
                            break;

                        default:
                            break;
                    }

                    lineToAdd = new String[] { name, ticker, String.valueOf(newPrice), String.valueOf(shareCount) };
                    allData2.add(lineToAdd);
                } else {
                    allData2.add(row);
                }
            }

            writer = createWriter();
            writer.writeAll(allData2);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateShareCount(Stock stock, Double shareCountToOperateWith, String operation) {
        String name = stock.getStockName();
        List<String[]> allData = new ArrayList<String[]>();
        List<String[]> allData2 = new ArrayList<String[]>();
        String[] lineToAdd;
        Double newShareCount = 0.0;
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(name)) {
                    switch (operation) {
                        case "+":
                            newShareCount = Double.valueOf(row[3]) + shareCountToOperateWith;
                            break;
                        case "-":
                            newShareCount = Double.valueOf(row[3]) - shareCountToOperateWith;
                            break;

                        default:
                            break;
                    }

                    lineToAdd = new String[] { row[0], row[1], row[2], String.valueOf(newShareCount) };
                    allData2.add(lineToAdd);
                } else {
                    allData2.add(row);
                }
            }

            writer = createWriter();
            writer.writeAll(allData2);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stock creatStockObject(String stockname) {
        reader = createReader();
        List<String[]> allData;
        String stockName;
        String ticker;
        Double shareCount;
        Stock stock;

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (row[0].equalsIgnoreCase(stockname)) {
                    stockName = row[0];
                    ticker = row[1];
                    shareCount = Double.valueOf(row[3]);

                    stock = new Stock(stockName, ticker, shareCount);

                    return stock;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void dispalyStockMarket() {
        List<String[]> allData = new ArrayList<String[]>();
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            int index = 1;
            for (String[] row : allData) {
                System.out.println("\n" + index + ".) " + "Name: " + row[0] + " | " + "Ticker: " + row[1] + " | " + "Price: " + row[2] + " | " + "Share Count: " + row[3] + " | ");
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> collectStocksNames() {
        List<String[]> allData;
        List<String> stockNames = new ArrayList<>();
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                stockNames.add(row[0]);
            }
            return stockNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isEnoughShareToBuy(Double shareToBuy) {
        List<String[]> allData = new ArrayList<String[]>();
        reader = createReader();

        try {
            allData = reader.readAll();
            reader.close();

            for (String[] row : allData) {
                if (Double.valueOf(row[3]) >= shareToBuy) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}