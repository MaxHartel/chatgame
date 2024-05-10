# chatgame
This repo contains programs to implement a multi-threaded TCP chat server and client

* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client
* ClientHandler.java receives messages from a client and relays it to the other clients.

Use these files as a stating point do develop a trivia game.  


## Identifying Information

* Name: Max Hartel, Kathy Dao, Neil Azimi, Luke Morrisette
* Student ID:
* Email:
* Course: CPSC 353
* Assignment: Chatgame 03


Each member of the group contributed in all phases of the project, we all asssisted with writing code, program design, communication, and project organization. 
## Source Files

* MtClient.java handles keyboard input from the user.
* ClientListener.java receives responses from the server and displays them
* MtServer.java listens for client connections and creates a ClientHandler for each new client
* ClientHandler.java receives messages from a client and relays it to the other clients.

## References

* Class notes and slides

## Known Errors

* The program has some trouble finding which player has joined first and making them the host. When the program correctly assigns the host however, there are no known errors.

## Build Execution Insructions

* Navigate to the directory containing the source files
* javac *.java
* java MtServer.java
* Open a seperate terminal for each client to run
* in each new terminal navigate to the source directory and run 'java MtClient.java'

