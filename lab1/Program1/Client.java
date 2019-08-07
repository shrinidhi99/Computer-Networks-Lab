import java.net.*;
import java.util.*;
import java.io.*;
public class Client
{
	// intialize socket ports
	private Socket socket = null;
	private DataInputStream dataInputStream = null;
	private DataOutputStream dataOutputStream = null;
	// constructor to establish connection 
	public Client(String address, int port)
	{
		// establish connection
		try
		{
			socket = new Socket(address, port);
			System.out.println("Socket connected");
			dataInputStream = new DataInputStream(System.in);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
		}
		catch(UnknownHostException u)
		{
			System.out.println(u);
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

		// taking input
		String line = "";
		while(!line.equals("Over"))
		{
			// keep reading
			try
			{
				line = dataInputStream.readLine();
				dataOutputStream.writeUTF(line);
			}
			catch(IOException i)
			{
				System.out.println(i);
			}
		}

		// close the connection
		try
		{
			dataInputStream.close();
			dataOutputStream.close();
			socket.close();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

	}
	public static void main(String[] args)throws IOException {
		String IPaddress;
		int port;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter IP address:");
		IPaddress = in.readLine();
		System.out.println("Enter port number:");
		port = Integer.parseInt(in.readLine());
		Client client = new Client(IPaddress,port);
	}
}