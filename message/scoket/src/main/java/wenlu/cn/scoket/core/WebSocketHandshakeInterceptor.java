package wenlu.cn.scoket.core;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;


public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private static Logger logger = Logger.getLogger(WebSocketHandshakeInterceptor.class);
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
    		Map<String, Object> attributes) throws Exception {
    	
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}