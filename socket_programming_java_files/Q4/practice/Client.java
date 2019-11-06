import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    
       private Socket socket = null;
       private DataInputStream input = null;
       private DataOutputStream output = null;
    

    public Client(String address, int port) {

        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            input = new DataInputStream(System.in);

            output = new DataOutputStream(socket.getOutputStream());
        } catch (Exception u) {
            System.out.println(u);
        }

        String line = "";
        System.out.println("Enter your name:");
        try {
            line = input.readLine();
            output.writeUTF(line);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        // here you keep reading and writing once the client has logged in and until the client logs out
        while (!line.equals("logout")) {
            try {
                line = input.readLine();
                output.writeUTF(line);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 1234);
    }
}