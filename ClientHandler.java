import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ClientHandler.java
 *
 * <p>This class handles communication between the client
 * and the server.  It runs in a separate thread but has a
 * link to a common list of sockets to handle broadcast.
 *
 */
public class ClientHandler implements Runnable {
  private Socket connectionSock = null;
  private ArrayList<Client> clientList;
  private boolean firstMessege = true;
  private String username;
  private Client thisClient;
  private boolean first = true;

  public ClientHandler(Socket sock, ArrayList<Client> clientList) {
    this.connectionSock = sock;
    this.clientList = clientList;  // Keep reference to master list
  }

  public Client getClient(Socket sock, ArrayList<Client> clientList){
    for (Client x : clientList) {
      if (x.connectionSock == sock) {
        return x;
      }
    }

    return null;
  }

  public Client getClient(String name, ArrayList<Client> clientList){
    for (Client x : clientList) {
      if (x.username == name) {
        return x;
      }
    }

    return null;
  }

  /**
   * received input from a client.
   * sends it to other clients.
   */
  public void run() {
    try {
      System.out.println("Connection made with socket " + connectionSock);
      // Send a welcome message to the client
      OutputStream output = connectionSock.getOutputStream();
      PrintWriter writer = new PrintWriter(output,true);

      if(getClient(connectionSock,clientList).id == 0){
        writer.println("Welcome to the server! You are the host!" + getClient(connectionSock,clientList).id);
        writer.flush();
      }else{
        writer.println("Welcome to the server! Please enter your username:");
        writer.flush();
      }

      BufferedReader clientInput = new BufferedReader(
          new InputStreamReader(connectionSock.getInputStream()));
      
      while (true) {
        // Get data sent from a client
        String clientText = clientInput.readLine();
        if(getClient(connectionSock,clientList).id == 0){
          first = false;
          username = "host";

          for (Client z : clientList) {
            if (z.connectionSock == connectionSock) {
              z.username = username;
              z.host = true;
              writer.println("Username: " + z.username);
              writer.flush();
              writer.println("Type 'SCORES' to see the scores of each current player");
              writer.flush();
            }else{
              OutputStream otherClientsOutput = z.connectionSock.getOutputStream();
              PrintWriter otherClientsWriter = new PrintWriter(otherClientsOutput,true);
              otherClientsWriter.println(username + " has joined the chat");
              otherClientsWriter.flush();
            }
          }

        }else if ((clientText != null) && (firstMessege)) {
          firstMessege = false;
          username = clientText;

          int taken = 0;
          for (Client x : clientList) {
            if (x.username.equals(username)) {
              writer.println("Username taken, please try again ");
              writer.flush();
              firstMessege = true;
              taken = 1;
            }
          }
          if(taken == 0){
            for (Client z : clientList) {
              if (z.connectionSock == connectionSock) {
                z.username = username;
                writer.println("Username Accepted: " + z.username);
                writer.flush();
                writer.println("Commands: 'Goodbye' -> Exit Chat, 'Who?' -> List Clients, 'SCORES' -> List client scores");
                writer.flush();
              }else{
                OutputStream otherClientsOutput = z.connectionSock.getOutputStream();
                PrintWriter otherClientsWriter = new PrintWriter(otherClientsOutput,true);
                otherClientsWriter.println(username + " has joined the chat");
                otherClientsWriter.flush();
              }
              }
          }
        }else if (clientText != null) {

          switch (clientText) {
            case "Goodbye":
              System.out.println("Received: " + clientText);
              for (Client s : clientList) {
                if (s.connectionSock != connectionSock) {
                  DataOutputStream clientOutput = new DataOutputStream(s.connectionSock.getOutputStream());
                  clientOutput.writeBytes(getClient(connectionSock, clientList).username + " has left the chat\n");
                  clientList.remove(getClient(connectionSock, clientList));
                  connectionSock.close();
                  break;
                }
              }

            case "Who?":
              for (Client x : clientList) {
                  writer.print(x.username + ", ");
                  writer.flush();
              }
                  writer.println();
                  writer.flush();

            case "SCORES":

              for (Client x : clientList) {
                  writer.print("[" + x.username + ", " + x.score + "],");
                  writer.flush();
              }
                  writer.println();
                  writer.flush();

            case "POINT":
              writer.print("Who would you like to award a point to?");
              writer.flush();
              String input = clientInput.readLine();
              getClient(input, clientList).score += 1;


            default:
              System.out.println("Received: " + clientText);
              // Turn around and output this data
              // to all other clients except the one
              // that sent us this information
              for (Client s : clientList) {
                if (s.connectionSock != connectionSock) {
                  DataOutputStream clientOutput = new DataOutputStream(s.connectionSock.getOutputStream());
                  clientOutput.writeBytes(getClient(connectionSock, clientList).username + ": " + clientText + "\n");
                }
              }
          }
        }else{
          // Connection was lost
          System.out.println("Closing connection for socket " + thisClient.connectionSock);
          // Remove from arraylist
          clientList.remove(thisClient);
          thisClient.connectionSock.close();
          break;
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
      // Remove from arraylist
      clientList.remove(thisClient);
    }
  }
} // ClientHandler for MtServer.java
