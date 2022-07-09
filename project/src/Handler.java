import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Handler extends Thread {
    private Socket client;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Server server;

    public Handler(Socket socket, Server server) throws IOException {
        this.client = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
    }

    @Override
    public void run() {

        String digit;
        
            try {
                while(true) {
                    if ((digit = bufferedReader.readLine()) != null) {
                        System.out.println(digit);
//                      The string tokenizer class allows an application to break a string into token
                        StringTokenizer st1 ;
                        String st2 = "";
                        String clientNumber = "";
                        String messageToSend = "";
                        if (digit.equals("close connection")) {
                            server.closeClientConnection(this);
                            client.close();
                            break;
                        }
                        else if (digit.equals("2")) {
                            String message = "";// Send the message to all other clients.
                            for (int i = 0; i < server.clientsHandler.size(); i++) {
                                message += "Client " + i;
                                message += ",";
                            }
                            printWriter.println(message);
                        } else {

                            st1 = new StringTokenizer(digit, ",");
                            st2 = st1.nextToken();
                            if (st2.equals("3")) {
                                clientNumber = st1.nextToken();
                                messageToSend = st1.nextToken();

                            } else if (st2.equals("4")) {
                                messageToSend = st1.nextToken();
                            }
                        }
                        if (st2.equals("3")) {
                            for (int i = 0; i < server.clientsHandler.size(); i++) {
                               if(i==Integer.parseInt(clientNumber))
                               {
                                   server.clientsHandler.get(i).sendMessage(messageToSend);
                               }
                            }

                        } else if (st2.equals("4")) {
                            server.sendGroupMessage(messageToSend);

                        }


                    }
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
                try {
                    client.close();
                }
                catch (IOException ioe1) {
                    ioe1.printStackTrace();
                }
            }

        try {
            client.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        printWriter.println("message arriving:" + message);
    }


}