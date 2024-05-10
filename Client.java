import java.net.Socket;


public class Client {

    public Socket connectionSock = null;
  
    public String username = "";
    
    public int score;

    public boolean host;
    public int id = 0;
  
  
    private int genID(){
        return id++;
    }
  
    public Client(Socket sock,  String username) {
  
      this.connectionSock = sock;
  
      this.username = username;

      this.score = 0;

      this.host = false;

      this.id = genID();
  
    }
  
   }