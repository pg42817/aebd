package oracleconnection;

public class Datafile {
    public String nome;
    public String max_size;
    public String used_size;
    public String auto_extend;
    public String free_size;
    public String status;
    public String tablespace;
    public String timestamp;

    public Datafile(String nome, String max_size, String used_size, String auto_extend, String free_size, String status, String tablespace, String timestamp){
        this.nome=nome;
        this.max_size=max_size;
        this.used_size=used_size;
        this.auto_extend=auto_extend;
        this.free_size=free_size;
        this.status=status;
        this.tablespace=tablespace;
        this.timestamp=timestamp;
    }
}