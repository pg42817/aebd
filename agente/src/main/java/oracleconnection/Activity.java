package oracleconnection;

public class Activity {
    public String CPU;
    public String sessions;
    public String timestamp;
    public String memoria;

    public Activity(String CPU, String sessions, String timestamp, String memoria) {
        this.CPU = CPU;
        this.sessions = sessions;
        this.timestamp=timestamp;
        this.memoria=memoria;
    }

}