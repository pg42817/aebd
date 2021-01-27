package oracleconnection;

public class Tablespace {
    public String nome;
    public String max_size;
    public String free_size;
    public String used_size;
    public String status;
    public String timestamp;
    public String contents;

    public Tablespace(String nome, String max_size, String free_size, String used_size, String status,String timestamp,String contents) {
        this.nome = nome;
        this.max_size = max_size;
        this.free_size = free_size;
        this.used_size = used_size;
        this.status = status;
        this.timestamp=timestamp;
        this.contents=contents;
    }

}