package aviapps.cryptosentiment.Custom;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/*
 * Created by Avijeet on 31-Dec-17.
 */

public class CustomWebSocket extends Thread {

    EchoWebSocketListener listener;
    WebSocket ws;

    private Context context;
    private String url;
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    public CustomWebSocket(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void run() {
        super.run();
        create();
    }

    private void create() {
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    public void close() {
        ws.close(NORMAL_CLOSURE_STATUS, "Bye");
    }

    public void sendMessage(String message) {
        ws.send(message);
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            Log.d("TICK: ", "Socket Open");
//            webSocket.send(message);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("TICK: ", text);
            context.sendBroadcast(new Intent("CustomWebSocket").putExtra("data", text));
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.d("TICK: ", reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            Log.d("TICK: ", t.getMessage());
        }
    }
}
