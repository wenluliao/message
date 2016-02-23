package wenlu.cn.scoket.core;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.bson.Document;

import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;

import wenlu.cn.scoket.mongodb.MongoManager;

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
        private MongoDatabase db = MongoManager.getDB("message");
        public HandlerThread(Socket client) {  
            socket = client;  
            new Thread(this).start();  
        }  
  
        public void run() {  
            try {  
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String clientInputStr = input.readUTF();
                Document document = new Document();
                document = Document.parse(clientInputStr);
                db.getCollection("msg").insertOne(document);
                System.out.println("getMsg:"+clientInputStr); 
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