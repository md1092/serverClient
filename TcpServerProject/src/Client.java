import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

    public Client(Socket socket, String userName) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;

        } catch (IOException e) {
            e.printStackTrace();
            closeEverything();
        }
    }
    public void sendName() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException ex) {
            ex.printStackTrace();
            closeEverything();
        }
    }

    public void sendPrivateMessage() {
        try {
            Scanner in = new Scanner(System.in);
            String message = in.nextLine();
            bufferedWriter.write("private message from " + userName + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Enter the name of the client that you want to send the message to");
            message = in.nextLine();
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
            closeEverything();
        }
    }

    public void sendGroupMessage() {
        try {
            Scanner in = new Scanner(System.in);
            String message = in.nextLine();
            bufferedWriter.write("group message from " + userName + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            closeEverything();
        }
    }

    public void receiveMessage() {
                String messageFromChat ="";
                    try {
                        messageFromChat += bufferedReader.readLine();
                        System.out.println(messageFromChat);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeEverything();
                    }
                }

    public void closeEverything() {
        try {
            if (bufferedReader != null) {
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

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Socket socket = new Socket("localhost", 8056);
        System.out.println("please enter user name");
        String username = in.nextLine();
        Client client = new Client(socket, username);
        client.sendName();

        PrintStream output = new PrintStream(socket.getOutputStream());
        String input;
        String message="";
        while (true) {
            System.out.println("Enter the appropriate digit for what you want to do:");
            System.out.println("Enter 1 to to disconnect from the server");
            System.out.println("Enter 2  to get a list of the connected clients");
            System.out.println("Enter 3 to send a message to a specific client");
            System.out.println("Enter 4  to send a message to all the clients");

            input = in.next();
            if (input.equals("1")) {
                System.out.println("Connection is closed");
                output.println("close");
                client.closeEverything();
                break;
            } else if (input.equals("2")) {
                output.println("get clients array");
                client.receiveMessage();
            } else if (input.equals("3")) {
                output.println("private chat");
                System.out.println("Enter the message that you want to send");
                client.sendPrivateMessage();
            } else if (input.equals("4")) {
                output.println("group chat");
                System.out.println("Enter the message that you want to send");
                client.sendGroupMessage();
            }
        }
        socket.close();
        output.close();
    }
}

