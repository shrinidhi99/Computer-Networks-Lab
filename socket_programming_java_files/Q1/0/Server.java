import java.io.*;  
import java.net.*;  
public class Server {  
public static void main(String[] args){  
try{  
ServerSocket ss=new ServerSocket(6666);  
Socket s=ss.accept();//establishes connection   

DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
dout.writeUTF("Hello Client");  
dout.flush();  
dout.close(); 


ss.close(); 
}catch(Exception e){System.out.println(e);}  
}  
}  
