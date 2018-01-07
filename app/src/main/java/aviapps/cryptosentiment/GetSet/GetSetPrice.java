package aviapps.cryptosentiment.GetSet;

/*
 * Created by Avijeet on 03-Jan-18.
 */

public class GetSetPrice {
    private String symbol;
    private double price;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
