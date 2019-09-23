
import java.io.*;
import java.net.*;
 
class UDPServer
{
   public static void main(String args[]) throws Exception
      {
         DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
	     BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            while(true)
               {
		  sendData = new byte[1024];
		  receiveData = new byte[1024];
                  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                  serverSocket.receive(receivePacket);
                  String sentence = new String( receivePacket.getData());
                  System.out.println("RECEIVED: " + sentence);
                  InetAddress IPAddress = receivePacket.getAddress();
                  int port = receivePacket.getPort();
                  System.out.print("SEND: ");
		  String capitalizedSentence = inFromUser.readLine();
                  
		  sendData = capitalizedSentence.getBytes();
                  DatagramPacket sendPacket =
                  new DatagramPacket(sendData, sendData.length, IPAddress, port);
                  serverSocket.send(sendPacket);
		  if(capitalizedSentence.equals("logout")||sentence.equals("logout"))
			break;
               }
      }
}
