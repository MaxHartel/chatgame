import java.net.Socket;


public class Client {

    public Socket connectionSock = null;
  
    public String username = "";
    
    public int score;

    public boolean host;

    public long timeStamp = 0;
  
  
    public long getCreationTime() {
      System.out.println(System.currentTimeMillis());
      return System.currentTimeMillis();
  }
  
    public Client(Socket sock,  String username) {
  
      this.connectionSock = sock;
  
      this.username = username;

      this.score = 0;

      this.host = false;

      this.timeStamp = getCreationTime();
  
    }
  
   }