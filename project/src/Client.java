import java.io.*;
import java.net.*;

public class Client {

    public static void main(String args[]) throws Exception {
        Socket socket = new Socket("localhost", 8041);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream output = new PrintStream(socket.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String ch;
        while (true) {
            System.out.println("Enter the appropriate digit for what you want to do:");
            System.out.println("Enter 1 to to disconnect from the server");
            System.out.println("Enter 2  to get a list of the connected clients");
            System.out.println("Enter 3 to send a message to a specific client");
            System.out.println("Enter 4  to send a message to all the clients");

            ch = bufferedReader.readLine();
            String  serverMessage="";

            if(ch.equals("1"))
            {
                System.out.println("Connection is closed.....");
                output.println("close connection");
                break;
            }
            else if(ch.equals("2"))
            {
                output.println(ch);

            }
            else if(ch.equals("3"))
            {
                System.out.println("Enter the number of the client you wish to send the message to");

                String number= bufferedReader.readLine();

                System.out.println("Enter the message you want to send to this client");
                String message= bufferedReader.readLine();
                String mess="";
                mess+='3';
                mess+=" to ";
                mess+=number;
                mess+=':';
                mess+=message;

                output.println(mess);

            }
            else if(ch.equals("4"))
            {
                System.out.println("Enter the message you wish to send to all clients");
                String message= bufferedReader.readLine();
                message="4"+":"+message;

                System.out.println(message);
                output.println(message);

            }
            serverMessage = input.readLine();
            System.out.print("Server : " + serverMessage + "\n");


        }
        socket.close();
        input.close();
        output.close();
        bufferedReader.close();
    }

}