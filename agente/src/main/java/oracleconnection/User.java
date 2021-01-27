package oracleconnection;

public class User {
    public int id;
    public String nome;
    public String state;
    public String data_criacao;
    public String data_expiracao;
    public String profile;
    public String tablespace;
    public String timestamp;


    public User(int id, String nome, String state, String data_criacao, String data_expiracao, String profile, String tablespace,String timestamp){
        this.id=id;
        this.nome=nome;
        this.state=state;
        this.data_criacao=data_criacao;
        this.data_expiracao=data_expiracao;
        this.profile=profile;
        this.tablespace=tablespace;
        this.timestamp=timestamp;

    }
}