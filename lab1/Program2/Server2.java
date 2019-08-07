import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
public class Server2
{
	// initializing socket and inputstream
	private DataInputStream in = null;
	private ServerSocket server = null;
	private Socket socket = null;
	
	public static void main(String[] args)throws IOException
	{
		System.out.println("Enter port number:");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int port = Integer.parseInt(in.readLine());
		ServerSocket serverSocket = new ServerSocket(port);
		while(true)
		{
			Socket s = null;
			try
			{
				s = serverSocket.accept();
				// in = DataInputStream(new BufferedInputStream(socket.getInputStream()));
				// out = DataOutputStream()
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				System.out.println("Assigning new thread for this client...");
				Thread t = new ClientHandler(s,dis,dos,"Hello");
				t.start();
			}
			catch(Exception e)
			{
				s.close();
				e.printStackTrace();
			}
		}
	}
}
	// ClientHandler class 
class ClientHandler extends Thread  
{ 
    final DataInputStream dis;
    final DataOutputStream dos;
    final String serverTalk;
    final Socket s;
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
  
    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, String serverTalk) 
    { 
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.serverTalk = serverTalk;
    }
  
    @Override
    public void run()
    { 
        String received;
        // String serverTalk = "";
        String toreturn;
        // BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        { 
            // serverTalk = "";
            try {
  
                // Ask user what he wants 
                dos.writeUTF("Type Exit to terminate connection.");
                  
                // receive the answer from client
                received = dis.readUTF();
                // serverTalk = in.readLine();
                if(received.equals("Exit"))
                {  
                    // System.out.println(serverTalk);
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed.");
                    break;
                }
                
                // creating Date object 
                Date date = new Date();
                  
                // write on output stream based on the 
                // answer from the client 
                // System.out.println(serverTalk);
                // System.out.println(received);
                
                switch (received) {
                  
                    case "Date" :
                        toreturn = fordate.format(date);
                        dos.writeUTF(toreturn);
                        break;
                          
                    case "Time" :
                        toreturn = fortime.format(date);
                        dos.writeUTF(toreturn);
                        break;
                          
                    default:
                        dos.writeUTF(received);
                        break;
                }
                // dos.writeUTF(serverTalk);
                // dos.writeUTF(received); 
                        
                          
                    
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
          
        try
        { 
            // closing resources 
            this.dis.close();
            // in.close();
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 
