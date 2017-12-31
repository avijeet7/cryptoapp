package aviapps.cryptosentiment.GetSet;

/*
 * Created by Avijeet on 31-Dec-17.
 */

public class GetSetStream {
    private double ltp = -1;
    private double pc = -1;
    private String symbol = "";
    private String pair = "";
    private int chanId = -1;

    public double getLtp() {
        return ltp;
    }

    public void setLtp(double ltp) {
        this.ltp = ltp;
    }

    public double getPc() {
        return pc;
    }

    public void setPc(double pc) {
        this.pc = pc;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getChanId() {
        return chanId;
    }

    public void setChanId(int chanId) {
        this.chanId = chanId;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }
}
