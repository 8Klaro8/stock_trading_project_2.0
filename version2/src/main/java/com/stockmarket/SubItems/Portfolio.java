package com.stockmarket.SubItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.Port;

public class Portfolio {
    public ArrayList<Stock> stocks;

    public Portfolio() {
        this.stocks = new ArrayList<Stock>();
    }

    public Portfolio(Portfolio portfolio) {
        this.stocks = new ArrayList<>(portfolio.getStocks());
    }

    public ArrayList<Stock> getStocks() {
        return new ArrayList<>(this.stocks);
    }

    private boolean stockExits(Stock stock) {
        List<Stock> stocks = this.getStocks();
        for (Stock stock2 : stocks) {
            if (stock2.getStockName().equalsIgnoreCase(stock.getStockName())) {
                return true;
            }
        }
        return false;
    }

    public void addStock(Stock stock, double shareCount) {
        // checking if stock already in the protfolio
        if (stockExits(stock)) {
            // getting new sahrecount from the stock bought
            for (Stock stock2 : stocks) {
                if (stock2.getStockName().equalsIgnoreCase(stock.getStockName())) {
                    stock2.updateShareCount(shareCount);
                }
            }
        } else {
            // creating a new Stock object and set it's sharecount to the amount the user
            // bought
            Stock newStock = new Stock(stock);
            newStock.setShareCount(shareCount);
            this.stocks.add(newStock);
        }
    }

    public void dispalyStocksForSell() {
        StringBuffer myStocks = new StringBuffer();
        List<Stock> stocks = this.getStocks();
        for (Stock stock : stocks) {
            myStocks.append("\nName: " + stock.getStockName() + ", Price: " + stock.getPrice());
        }

        System.out.println(myStocks);

    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        String currLine = "";
        Double portfolioValue = 0.0;
        Double totalShareCount = 0.0;

        for (Stock stock : stocks) {
            currLine += "\n--------------------\nStock\nname: " + stock.getStockName();
            currLine += "\nTicker: " + stock.getStockName();
            currLine += "\nShare: " + stock.getShareCount();
            currLine += "\nCurrent value: " + stock.getPrice();
            portfolioValue += stock.getPrice() * stock.getShareCount();
            totalShareCount += stock.getShareCount();
        }

        currLine += "\n--------------------------------------------------\nTotal share count: " + totalShareCount;
        currLine += "\nTotal value of portfolio: " + portfolioValue + "\n--------------------------------------------------\n";
        return currLine;
    }

}
