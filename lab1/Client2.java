import java.net.*;
import java.util.*;
import java.io.*;
public class Client2
{
	// intialize socket ports
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	public static void main(String[] args)throws IOException {
		String IPaddress;
		int port;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter IP address:");
			IPaddress = in.readLine();
			System.out.println("Enter port number:");
			port = Integer.parseInt(in.readLine());
			InetAddress ip = InetAddress.getByName("localhost");
			Socket s = new Socket(ip, port);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			
			while(true)
			{
				System.out.println(dis.readUTF()); 
                String tosend = in.readLine(); 
                dos.writeUTF(tosend); 
                  
                // If client sends exit,close this connection  
                // and then break from the while loop 
                if(tosend.equals("Exit")) 
                { 
                    System.out.println("Closing this connection : " + s); 
                    s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // printing date or time as requested by client 
                String received = dis.readUTF(); 
                System.out.println(received); 
            } 
              
            // closing resources 
            in.close(); 
            dis.close(); 
            dos.close(); 
        }
        catch(Exception e){ 
            e.printStackTrace(); 
        } 
		
	}
}