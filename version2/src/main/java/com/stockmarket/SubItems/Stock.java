package com.stockmarket.SubItems;

import java.util.Objects;

import com.stockmarket.Manager_classes.StockMarketManager;

public class Stock {
    private String stockName;
    private String ticker;
    private Double price;
    private Double shareCount;


    public Stock() {
    }

    public Stock(Stock copyStock) {
        this.stockName = copyStock.stockName;
        this.ticker = copyStock.ticker;
        this.price = copyStock.price;
        this.shareCount = copyStock.shareCount;
    }

    public Stock(String stockName, String ticker, Double quantity) {
        this.stockName = stockName;
        this.ticker = ticker;
        this.price = StockMarketManager.getCurrentStockPrice(stockName);
        this.shareCount = quantity;
    }

    public String getStockName() {
        return this.stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getTicker() {
        return this.ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getPrice() {
        this.setPrice();
        return this.price;
    }

    public void setPrice() {
        this.price = StockMarketManager.getCurrentStockPrice(stockName);
    }

    public Double getShareCount() {
        return this.shareCount;
    }

    public void setShareCount(Double quantity) {
        this.shareCount = quantity;
    }

    public void updateShareCount(double shareCount) {
        this.setShareCount(this.getShareCount() + shareCount);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Stock)) {
            return false;
        }
        Stock stock = (Stock) o;
        return Objects.equals(stockName, stock.stockName) && Objects.equals(ticker, stock.ticker) && Objects.equals(price, stock.price) && Objects.equals(shareCount, stock.shareCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, ticker, price, shareCount);
    }

    @Override
    public String toString() {
        return "{" +
            " stockName='" + getStockName() + "'" +
            ", ticker='" + getTicker() + "'" +
            ", price='" + getPrice() + "'" +
            ", quantity='" + getShareCount() + "'" +
            "}";
    }

}
