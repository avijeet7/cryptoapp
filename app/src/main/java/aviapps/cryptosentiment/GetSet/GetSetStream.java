package aviapps.cryptosentiment.GetSet;

/*
 * Created by Avijeet on 31-Dec-17.
 */

public class GetSetStream {
    private double ltp;
    private double pc;
    private String pair;
    private int chanId;
    private double ask;
    private double bid;

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

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }
}
