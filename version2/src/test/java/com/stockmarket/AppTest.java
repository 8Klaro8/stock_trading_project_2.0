package com.stockmarket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Policy;

import javax.sound.sampled.Port;

import org.junit.Test;

import com.stockmarket.Manager_classes.StockMarketManager;
import com.stockmarket.Manager_classes.TradersManager;
import com.stockmarket.SubItems.Portfolio;
import com.stockmarket.SubItems.Stock;
import com.stockmarket.SubItems.Trader;

public class AppTest

{
    Stock stock = new Stock("Bitcoin", "BTC", 234.0);


    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    // Stock
    @Test
    public void checkCreateStock() {
        Stock stock = new Stock("Bitcoin", "BTC", 20.2);
        Double expectedStockPRice = StockMarketManager.getCurrentStockPrice("Bitcoin");
        assertEquals(expectedStockPRice, stock.getPrice());
    }

    // Trader
    @Test
    public void checkBuyStockCheck() {
        Trader trader = new Trader();
        Portfolio portfolio = new Portfolio();
        trader.setFunds(200000.0);
        trader.buyStock("Bitcoin", 2.0);

    }

    // Portfolio
    @Test
    public void checkToString() {
        Portfolio portf = new Portfolio();
        portf.addStock(stock,2.0);

        portf.toString();
    }

    // StockMarketManager
    @Test
    public void checkAddNewStockToDataBase() {
        StockMarketManager.addNewStockToDataBase(stock);
    }

    @Test
    public void checkUpdateStockPriceIncrease() {
        Stock stock = new Stock("Bitcoin", "BTC", 234.0);
        Double currentPice = StockMarketManager.getCurrentStockPrice(stock.getStockName());
        Double expectedPrice = currentPice * 1.05;

        StockMarketManager.updateStockPrice(stock, "+");
        assertEquals(expectedPrice, StockMarketManager.getCurrentStockPrice(stock.getStockName()));
    }

    @Test
    public void checkUpdateStockPriceDecrease() {
        Stock stock = new Stock("Bitcoin", "BTC", 234.0);
        Double currentPice = StockMarketManager.getCurrentStockPrice(stock.getStockName());
        Double expectedPrice = currentPice * 0.95;

        StockMarketManager.updateStockPrice(stock, "-");
        assertEquals(expectedPrice, StockMarketManager.getCurrentStockPrice(stock.getStockName()));
    }

    @Test
    public void checkUpdateShareCountIncrease() {
        Double shareCountToAdd = 5.0;
        Stock stock = new Stock("Bitcoin", "BTC", 234.0);

        Double expctedNum = StockMarketManager.getCurrentStockShareCount(stock.getStockName()) + shareCountToAdd;

        StockMarketManager.updateShareCount(stock, shareCountToAdd, "+");


        assertEquals(expctedNum, StockMarketManager.getCurrentStockShareCount(stock.getStockName()));
    }

    @Test
    public void checkUpdateShareCountDecrease() {
        Double shareCountToAdd = 5.0;
        Stock stock = new Stock("Bitcoin", "BTC", 234.0);

        Double expctedNum = StockMarketManager.getCurrentStockShareCount(stock.getStockName()) - shareCountToAdd;

        StockMarketManager.updateShareCount(stock, shareCountToAdd, "-");


        assertEquals(expctedNum, StockMarketManager.getCurrentStockShareCount(stock.getStockName()));
    }




    // TradersManager
    @Test
    public void checkSaveTrader() {
        Stock stock = new Stock("LiteCoin", "LTC", 402.0);
        Stock stock2 = new Stock("DogeCoin", "DGC", 122.0);
        Portfolio portfolio = new Portfolio();
        Trader trader = new Trader();

        portfolio.addStock(stock, 2.0);
        portfolio.addStock(stock2, 2.0);

        trader.setPortfolio(portfolio);
        trader.setFunds(232.0);
        trader.setUsername("Robin");
        trader.setPassword("CicaKutya");

        TradersManager.saveTrader(trader);
    }

    @Test
    public void checkReadTrader() {
        TradersManager.readTrader("Robin");
    }


    // HashPassword
    @Test
    public void checkPassword() {
        String hashedPW = HashPassword.hashPassword("a");
        System.out.println(hashedPW);
    }

    @Test
    public void checkValidateHashedPassword() {
        String testPW = "a";
        String hashedPW = HashPassword.hashPassword(testPW);

        assertTrue(HashPassword.arePasswordEqual(testPW, hashedPW));
    }
}
