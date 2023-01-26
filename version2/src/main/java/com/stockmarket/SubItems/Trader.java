package com.stockmarket.SubItems;

import java.util.Objects;

import javax.sound.sampled.Port;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.stockmarket.Manager_classes.StockMarketManager;
import com.stockmarket.Manager_classes.TradersManager;

public class Trader {
    private String username;
    private String password;
    private Double funds;
    private Portfolio portfolio;


    public Trader() {
        this.portfolio = new Portfolio();
        this.setFunds(0.0);
    }

    public Trader(String username, String password) {
        this.username = username;
        this.password = password;
        this.setFunds(0.0);
    }

    public Trader(String username, String password, Double funds, Portfolio portfolio) {
        this.username = username;
        this.password = password;
        this.funds = funds;
        this.portfolio = portfolio;
    }

    public void updateFunds(Double funds, String operation) {
        Double updatedFunds;
        switch (operation) {
            case "-":
                updatedFunds = this.funds - funds;
                this.setFunds(updatedFunds);
                break;
            case "+":
                updatedFunds = this.funds + funds;
                this.setFunds(updatedFunds);
                break;

            default:
                throw new IllegalArgumentException("Incorrent operation type given");
        }
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Double getFunds() {
        return this.funds;
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFunds(Double funds) {
        this.funds = funds;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void buyStock(String stockName, double shareCount) {
        Stock createdStock = new Stock(StockMarketManager.creatStockObject(stockName));
        double valueOfPurchase = createdStock.getPrice() * shareCount;

        if (this.funds == null || this.funds == 0) {
            System.out.println("You have no funds added yet.");
            return;
        }
        if (!(StockMarketManager.stockExits(stockName))) {
            System.out.println("The stock: " + stockName + " does not exists!");
            return;
        }

        if (valueOfPurchase > this.getFunds()) {
            System.out.println("You have not enough fund to buy: " + stockName);
            return;
        }

        
        if (StockMarketManager.isEnoughShareToBuy(shareCount)) {
            this.updateFunds(valueOfPurchase, "-");
            this.getPortfolio().addStock(createdStock, shareCount);

            // stockmanager
            StockMarketManager.updateShareCount(createdStock, shareCount, "-");
            StockMarketManager.updateStockPrice(createdStock, "+");

            TradersManager.saveTrader(this);
        }



    }

    public void sellStock(String stockName, double shareCount) {
        Stock createdStock = new Stock(StockMarketManager.creatStockObject(stockName));
        double valueOfPurchase = createdStock.getPrice() * shareCount;

        if (this.funds == null || this.funds == 0) {
            System.out.println("You have no funds added yet.");
            return;
        }
        if (!(StockMarketManager.stockExits(stockName))) {
            System.out.println("The stock: " + stockName + " does not exists!");
            return;
        }

        if (valueOfPurchase > this.getFunds()) {
            System.out.println("You have not enough fund to buy: " + stockName);
            return;
        }

        
        if (StockMarketManager.isEnoughShareToBuy(shareCount)) {
            this.updateFunds(valueOfPurchase, "-");
            this.getPortfolio().addStock(createdStock, shareCount);
            StockMarketManager.updateShareCount(createdStock, shareCount, "-");
            StockMarketManager.updateStockPrice(createdStock, "+");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Trader)) {
            return false;
        }
        Trader trader = (Trader) o;
        return Objects.equals(username, trader.username) && Objects.equals(funds, trader.funds)
                && Objects.equals(portfolio, trader.portfolio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, funds, portfolio);
    }

    @Override
    public String toString() {
        return "Username: " + this.getUsername() + "\n" +
        "Available Funds: " + this.getFunds() + "\n";
    }

}
