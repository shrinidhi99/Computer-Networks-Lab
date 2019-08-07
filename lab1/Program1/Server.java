import java.io.*;
import java.net.*;
import java.util.*;
public class Server
{
	// initializing socket and inputstream
	private DataInputStream in = null;
	private ServerSocket server = null;
	private Socket socket = null;
	// Server taking port number
	public Server(int port)
	{
		try
		{
			// establishing connection
			server = new ServerSocket(port);
			System.out.println("Server is waiting for a client...");
			socket = server.accept();
			System.out.println("Connection established between server and client");
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			// now read the message from the client
			String line = "";
			while(!line.equals("Over"))
			{
				try
				{
					line = in.readUTF();
					System.out.println(line);
				}
				catch(IOException i)
				{
					System.out.println(i);
				}
			}
			// close connection
			System.out.println("Connection closed.");
			socket.close();
			in.close();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}
	public static void main(String[] args)throws IOException
	{
		System.out.println("Enter port number:");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int port = Integer.parseInt(in.readLine());
		Server server = new Server(port);
	}
}