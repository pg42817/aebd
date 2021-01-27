package oracleconnection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static LocalDateTime atual = LocalDateTime.now();
    public static Connection conn;
    public static Connection conn2;

    public static Connection connect() throws SQLException, ClassNotFoundException {
        //connect to database
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String serverName = "localhost";
        String portNumber = "1521";
        String serviceName = "orclpdb1.localdomain";
        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + "/" + serviceName;
        String username = "system";
        String password = "272520";

        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public static Connection connect2() throws SQLException, ClassNotFoundException {
        //connect to database
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String serverName = "localhost";
        String portNumber = "1521";
        String serviceName = "orclpdb1.localdomain";
        String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + "/" + serviceName;
        String username = "admin_tp";
        String password = "123456";

        Connection conn2 = DriverManager.getConnection(url, username, password);
        return conn2;
    }

    public static Datafile trataDatafiles (ResultSet result) throws SQLException{

        String datafile_nome = result.getString("FILE_NAME");
        String max_size = result.getString("MAX_SIZE_MB");
        String used_size = result.getString("USED_MB");
        String auto_extend = result.getString("AUTOEXTENSIBLE");
        String free_size = result.getString("FREE_MB");
        String status = result.getString("STATUS");
        String id_tablespace = result.getString("TABLESPACE_NAME");
        String timestamp = result.getString("DATA");

        return new Datafile(datafile_nome,max_size,used_size,auto_extend,free_size,status,id_tablespace,timestamp);
    }

    public static Tablespace trataTablespaces (ResultSet result) throws SQLException{

        String tablespace_nome = result.getString("TABLESPACE_NAME");
        String max_size = result.getString("MAX_SIZEMB");
        String free_size = result.getString("FREE_MB");
        String used_size = result.getString("USED_MB");
        String status = result.getString("STATUS");
        String timestamp = result.getString("DATA");
        String contents = result.getString("CONTENTS");

        return new Tablespace(tablespace_nome, max_size, free_size, used_size, status,timestamp, contents);
    }


    public static Activity trataActivity (ResultSet result) throws SQLException{

        String cpu = result.getString("CPU Usage");
        String sessions = result.getString("SID");
        String timestamp = result.getString("DATA");
        String memoria = result.getString("MEMORY");

        return new Activity(cpu,sessions,timestamp,memoria);
    }

    public static User trataUsers (ResultSet result) throws SQLException{

        int users_id = result.getInt("USER_ID");
        String users_nome = result.getString("USERNAME");
        String state = result.getString("ACCOUNT_STATUS");
        String data_criacao = result.getString("CREATED");
        String data_expiracao = result.getString("EXPIRY_DATE");
        String profile = result.getString("PROFILE");
        String tablespace = result.getString("DEFAULT_TABLESPACE");
        String timestamp = result.getString("DATA");


        return new User(users_id, users_nome,state,data_criacao,data_expiracao,profile,tablespace,timestamp);
    }

    public static void main(String args[]) {

        try {
            conn = connect();
            System.out.println("Conectado com sucesso à base de dados 'system.pdb1'!");
            conn2 = connect2();
            System.out.println("Conectado com sucesso à base de dados 'system.tp'!");

            //users
            String query = "SELECT USER_ID, USERNAME, account_status, created, expiry_date, profile, default_tablespace, (SELECT SYSDATE FROM DUAL) data FROM DBA_USERS";

            //datafiles
            String query2 = "SELECT distinct df.file_name, df.max_size_mb,(df.max_size_mb)-(f.free_mb) used_mb,DBA_DATA_FILES.autoextensible,f.free_mb,V$DATAFILE.status,DBA_DATA_FILES.tablespace_name,(SELECT SYSDATE FROM DUAL) data FROM (SELECT file_id, file_name, tablespace_name,TRUNC(bytes/1024/1024) AS size_mb,TRUNC(GREATEST(bytes,maxbytes)/1024/1024) AS max_size_mb FROM dba_data_files) df,(SELECT TRUNC(SUM(bytes)/1024/1024) AS free_mb, file_id FROM dba_free_space GROUP BY file_id) f\n" +
                    "inner join DBA_DATA_FILES on DBA_DATA_FILES.file_id=f.file_id\n" +
                    "inner join V$DATAFILE on V$DATAFILE.file#=f.file_id\n" +
                    "WHERE  df.file_id = f.file_id";

            //tablespaces
            String query3 = "select A.contents,A.tablespace_name,trunc(B.MAXSIZE/1024/1024) Max_sizeMB,trunc(decode(B.autoextensible,'YES',B.MAXSIZE-B.bytes+C.free,'NO',C.free)/1024/1024) free_mb,(trunc(B.MAXSIZE/1024/1024))-(trunc(decode(B.autoextensible,'YES',B.MAXSIZE-B.bytes+C.free,'NO',C.free)/1024/1024)) used_mb,A.status,(SELECT SYSDATE FROM DUAL) data\n" +
                    "from(select * from dba_tablespaces) A,\n" +
                    "(select file_id, file_name, tablespace_name, autoextensible,bytes, decode(autoextensible,'YES',maxbytes,bytes) maxsize from dba_data_files) B,\n" +
                    "(SELECT file_id,tablespace_name,sum(bytes) free FROM   dba_free_space GROUP BY file_id, tablespace_name) C\n" +
                    "where B.tablespace_name=A.tablespace_name\n" +
                    "AND b.file_id=c.file_id(+)";

            //activity
            String query4 = "SELECT ss.sid, ROUND (value/100) \"CPU Usage\",(SELECT SYSDATE FROM DUAL) data, (SELECT ROUND (used.bytes / 1024 / 1024, 2) used_mb FROM (SELECT SUM (bytes) bytes FROM v$sgastat WHERE name != 'free memory') used) MEMORY FROM v$session se, v$sesstat ss, v$statname st\n" +
                    "WHERE ss.statistic# = st.statistic# AND name LIKE  '%CPU used by this session%' AND se.sid = ss.SID  AND se.username IS NOT NULL";

            while(true)  {

                Statement st = conn.createStatement();
                Statement st_tp = conn2.createStatement();

                if( Duration.between(atual, LocalDateTime.now()).toMinutes() > 10){ break;}

                ResultSet result4 = st.executeQuery(query4);

                while (result4.next()) {

                    Activity mem = trataActivity(result4);

                    ObjectMapper om = new ObjectMapper();

                    String r = om.writeValueAsString(mem);

                    System.out.println(r);

                    JSONObject activityJSON = new JSONObject(r);

                    String nova = "INSERT INTO ACTIVITY(MEMORY, CPU, SESSIONS, TIMESTAMP) VALUES ('" + activityJSON.getString("memoria") + "'," + activityJSON.getInt("CPU") + "," + activityJSON.getInt("sessions") + ",'" + activityJSON.getString("timestamp") + "')";

                    st_tp.executeQuery(nova);

                    System.out.println(nova);

                }

                if( Duration.between(atual, LocalDateTime.now()).toMinutes() > 10){ break;}

                ResultSet result3 = st.executeQuery(query3);


                while (result3.next()) {

                    Tablespace tablespace = trataTablespaces(result3);

                    ObjectMapper om = new ObjectMapper();

                    String r = om.writeValueAsString(tablespace);

                    System.out.println(r);

                    JSONObject tablespaceJSON = new JSONObject(r);

                    String nova = "INSERT INTO TABLESPACES(NOME, MAX_SIZE, FREE_SIZE, USED_SIZE, STATUS,\"Timestamp\", TIPO) VALUES ('" + tablespaceJSON.getString("nome") + "','" + tablespaceJSON.getString("max_size") + "','" + tablespaceJSON.getString("free_size") + "','" + tablespaceJSON.getString("used_size") + "','" + tablespaceJSON.getString("status") + "','" + tablespaceJSON.getString("timestamp") + "','" + tablespaceJSON.getString("contents") + "')";

                    st_tp.executeQuery(nova);

                    System.out.println(nova);
                }

                if( Duration.between(atual, LocalDateTime.now()).toMinutes() > 10){ break;}

                ResultSet result2 = st.executeQuery(query2);

                while (result2.next()) {

                    Datafile datafile = trataDatafiles(result2);

                    ObjectMapper om = new ObjectMapper();

                    String r = om.writeValueAsString(datafile);

                    System.out.println(r);

                    JSONObject datafileJSON = new JSONObject(r);

                    String nova = "INSERT INTO DATAFILE(NOME, MAX_SIZE, USED_SIZE, AUTO_EXTEND, FREE_SIZE, STATUS, IDTABLESPACES, TIMESTAMP) VALUES ('" + datafileJSON.getString("nome") + "','" + datafileJSON.getString("max_size") + "','" + datafileJSON.getString("used_size") + "','" + datafileJSON.getString("auto_extend") + "','" + datafileJSON.getString("free_size") + "','" + datafileJSON.getString("status") + "',(select MAX(idtablespaces) from tablespaces where nome = '" + datafileJSON.getString("tablespace") + "'),'" + datafileJSON.getString("timestamp") + "')";

                    st_tp.executeQuery(nova);

                    System.out.println(nova);
                }

                if( Duration.between(atual, LocalDateTime.now()).toMinutes() > 10){ break;}

                ResultSet result = st.executeQuery(query);

                while (result.next()) {

                    User user = trataUsers(result);

                    ObjectMapper om = new ObjectMapper();

                    String r = om.writeValueAsString(user);

                    System.out.println(r);

                    JSONObject userJSON = new JSONObject(r);

                    String nova = "INSERT INTO USERS(id_User, NOME, ESTADO, DATA_CRIAÇÃO, DATA_EXPIRAÇÃO, PROFILE, TABLESPACE, TIMESTAMP) VALUES (" + userJSON.getInt("id") + ",'" + userJSON.getString("nome") + "','" + userJSON.getString("state") + "','" + userJSON.getString("data_criacao") + "','" + userJSON.getString("data_expiracao") + "','" + userJSON.getString("profile") + "',(select MAX(idtablespaces) from tablespaces where nome = '" + userJSON.getString("tablespace") + "'),'" + userJSON.getString("timestamp") + "')";

                    st_tp.executeQuery(nova);

                    System.out.println(nova);
                }

                if( Duration.between(atual, LocalDateTime.now()).toMinutes() > 10){ break;}

                String nova = "INSERT INTO relacional(IDDATAFILE, IDACTIVITY, IDTABLESPACES, IDUSERS, SERVICE_NAME) VALUES ((SELECT MAX(IDDATAFILE) FROM DATAFILE), (SELECT MAX(IDACTIVITY) FROM ACTIVITY), (SELECT MAX(IDTABLESPACES) FROM TABLESPACES), (SELECT MAX(IDUSERS) FROM USERS),'orclpdb1')";

                st_tp.executeQuery(nova);

                st.close();
                st_tp.close();

                Thread.sleep(60 * 1000); //De 1 minutos em 1 minuto corre.
            }
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Ups, aLgo correu mal");
        }


    }
}