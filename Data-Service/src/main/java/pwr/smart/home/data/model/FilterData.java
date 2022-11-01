package pwr.smart.home.data.model;


import java.sql.Timestamp;

public class FilterData {
    private String serialNumber;
    private Timestamp timestamp;
    private String type;
    private float PM25;
    private float PM10;
    private int IAI;
    private int gas;

    FilterData() {}

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public float getPM25() {
        return PM25;
    }

    public void setPM25(float PM25) {
        this.PM25 = PM25;
    }

    public float getPM10() {
        return PM10;
    }

    public void setPM10(float PM10) {
        this.PM10 = PM10;
    }

    public int getIAI() {
        return IAI;
    }

    public void setIAI(int IAI) {
        this.IAI = IAI;
    }

    public int getGas() {
        return gas;
    }

    public void setGas(int gas) {
        this.gas = gas;
    }

    @Override
    public String toString() {
        return "Data{" +
                "serialNumber='" + serialNumber + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", PM25=" + PM25 +
                ", PM10=" + PM10 +
                ", IAI=" + IAI +
                ", gas=" + gas +
                '}';
    }
}
