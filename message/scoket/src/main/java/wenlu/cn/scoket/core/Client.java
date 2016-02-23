package wenlu.cn.scoket.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.bson.types.ObjectId;
import wenlu.cn.beans.base.Msg;

public class Client {
	public static final String IP_ADDR = "localhost";
	public static final int PORT = 62701;
	
    public static void main(String[] args) {  
    	Msg msg = new Msg();
    	msg.setMsgId(new ObjectId().toString());
    	msg.setUserId(10086);
        while (true) {  
        	Socket socket = null;
        	try {
	        	socket = new Socket(IP_ADDR, PORT);  
	            DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
	            String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
	            msg.setContent(str);
	            System.out.println("json:"+msg.toString());
	            out.writeUTF(msg.toString());  
	            out.close();
        	} catch (Exception e) {
        		
        	} finally {
        		if (socket != null) {
        			try {
						socket.close();
					} catch (IOException e) {
						socket = null; 
					}
        		}
        	}
        }  
    }  
}  
