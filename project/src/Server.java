

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    public LinkedList<Handler> clientsHandler = new LinkedList<>();

    public Server(int port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(port);

        while(true)
        {
            Socket client = serverSocket.accept();
            System.out.println("A new client has connected");

            Handler handler = new Handler(client, this);
            clientsHandler.add(handler);
            handler.start();
        }
    }

    public long getClientCount() {
        return clientsHandler.size();
    }

    public void sendGroupMessage(String message) {
      for(int i = 0; i< clientsHandler.size(); i++)
      {
                clientsHandler.get(i).sendMessage(message);
      }
    }

    public void closeClientConnection(Handler client) {
        clientsHandler.remove(client);
        client.interrupt();
    }

    public static void main(String[] args) throws IOException {

        Server server = new Server(8041);
        server.startServer();
    }
}