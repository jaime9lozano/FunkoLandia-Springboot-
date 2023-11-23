package jaime.funkoext2.Config.WebSocket;

import java.io.IOException;

public interface WebSocketSender {
    void sendMessage(String message) throws IOException;

    void sendPeriodicMessages() throws IOException;
}
