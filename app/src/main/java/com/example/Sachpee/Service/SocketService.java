package com.example.Sachpee.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {
    private Socket mSocket;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // Khởi tạo Socket.IO client
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            mSocket = IO.socket("http://localhost:8080", options);

            // Lắng nghe sự kiện từ server
            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Log.d("SocketService", "Connected to server");
            }).on("message", args -> {
                String message = args[0].toString();
                Log.d("SocketService", "Received message: " + message);

                // Gửi broadcast tới Activity khi nhận được dữ liệu mới
                Intent intent = new Intent("socket_message");
                intent.putExtra("message", message);
                sendBroadcast(intent);
            });

            // Kết nối socket
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Ngắt kết nối khi service bị dừng
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off();
        }
    }

    public void sendMessage(String event, String message) {
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event, message);
        }
    }
}
