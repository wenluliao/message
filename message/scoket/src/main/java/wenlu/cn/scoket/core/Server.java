package wenlu.cn.scoket.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int PORT = 62701;
	
    public static void main(String[] args) {  
        Server server = new Server();  
        server.init();  
    }  
  
    public void init() {  
        try {  
            ServerSocket serverSocket = new ServerSocket(PORT);  
            while (true) {  
                Socket client = serverSocket.accept();  
                new HandlerThread(client);  
            }  
        } catch (Exception e) {  
        }  
    }  
  
    private class HandlerThread implements Runnable {  
        private Socket socket;  
        public HandlerThread(Socket client) {  
            socket = client;  
            new Thread(this).start();  
        }  
  
        public void run() {  
            try {  
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String clientInputStr = input.readUTF();
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
                String s = new BufferedReader(new InputStreamReader(System.in)).readLine();  
                out.writeUTF(s);  
                
                out.close();  
                input.close();  
            } catch (Exception e) {  
            } finally {  
                if (socket != null) {  
                    try {  
                        socket.close();  
                    } catch (Exception e) {  
                        socket = null;  
                    }  
                }  
            } 
        }  
    }  
}  