import java.io.*;
import java.net.*;
 
class UDPClient
{
   public static void main(String args[]) throws Exception
   {
      BufferedReader inFromUser =
         new BufferedReader(new InputStreamReader(System.in));
      System.out.print("Enter your name : ");
      String name=inFromUser.readLine();
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName("localhost");
      while(true){
      byte[] sendData = new byte[1024];
      byte[] receiveData = new byte[1024];
      System.out.print("SEND: ");
      String sentence = inFromUser.readLine();
      sentence=name+": "+sentence;
      sendData = sentence.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
      clientSocket.send(sendPacket);
      /*DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      clientSocket.receive(receivePacket);
      String modifiedSentence = new String(receivePacket.getData());
      System.out.println("RECEIVED:" + modifiedSentence);*/
      if(sentence.equals("logout"))
      {
	     clientSocket.close();
		break;
	}
      }
 
   }
}

