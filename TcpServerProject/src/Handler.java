import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Handler extends Thread{

    public static ArrayList<Handler> clientsArray = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Handler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = bufferedReader.readLine();
            clientsArray.add(this);
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {
        try {

        String input;
        String message="";
        String name="";

        while (true) {
           if ((input = bufferedReader.readLine()) != null) {
                if (input.equals("close")){
                    removeClient();
               }
               else if (input.equals("get clients array")){
                   message = "connected clients: ";
                    for (Handler client : clientsArray) {
                       message += client.userName;
                       message += ",";
                        }
                   singleMessage(message);
                }
               else if (input.equals("private chat")){
                   message = bufferedReader.readLine();
                   name = bufferedReader.readLine();
                   messageToOneClient(message,name);
                }
                else if (input.equals("group chat")){
                    message = bufferedReader.readLine();
                    broadCastMessage(message);
                }
           }
                }
            } catch (IOException e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }

        public void singleMessage(String messageToSend) {
          for (Handler client : clientsArray) {
              try {
                  if (client.userName.equals(userName)) {
                      client.bufferedWriter.write(messageToSend);
                      client.bufferedWriter.newLine();
                      client.bufferedWriter.flush();
                  }
              }
            catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
          }
    }

    public void messageToOneClient(String messageToSend,String name) {
        for (Handler client : clientsArray) {
            try {
                if (client.userName.equals(name)) {
                    client.bufferedWriter.write(messageToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            }
            catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void broadCastMessage(String messageToSend) {
        for (Handler client : clientsArray) {
            try {
                if (!client.userName.equals(userName)) {
                    client.bufferedWriter.write(messageToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
     }
    }

    public void removeClient() {
        clientsArray.remove(this);
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        removeClient();
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
