package com.example.dacs1.ServerSocket;

import DAO.ChatDAO;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Map<String, ClientHandler> clients = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server is running...");
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }

    public static synchronized void broadcastOnlineUsers() {
        String users = "USERS:" + String.join(",", clients.keySet());
        System.out.println("Broadcasting users: " + users);
        for (ClientHandler client : clients.values()) {
            client.send(users);
        }
    }

    public static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void send(String msg) {
            if (out != null) {
                out.println(msg);
            }
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("USERNAME:");
                username = in.readLine();

                if (username == null || username.trim().isEmpty()) {
                    System.out.println("Username invalid or empty. Closing connection.");
                    socket.close();
                    return;
                }

                synchronized (clients) {
                    if (clients.containsKey(username)) {
                        System.out.println("Username " + username + " đã tồn tại. Đóng client cũ.");
                        ClientHandler oldClient = clients.get(username);
                        try {
                            oldClient.socket.close(); // Ngắt kết nối cũ
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    clients.put(username, this); // Ghi đè
                    System.out.println("User connected: " + username);
                    ChatServer.broadcastOnlineUsers();
                }

                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split("::", 2);
                    if (parts.length == 2) {
                        String receiver = parts[0];
                        String message = parts[1];
                        System.out.println("Tin nhắn từ " + username + " tới " + receiver + ": " + message);
                        ChatDAO.saveMessage(username, receiver, message);

                        if (clients.containsKey(receiver)) {
                            // Gửi tin nhắn tới người nhận
                            clients.get(receiver).send(username + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Lỗi kết nối với user " + username);
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
                synchronized (clients) {
                    if (username != null) {
                        clients.remove(username);
                        System.out.println("User disconnected: " + username);
                        ChatServer.broadcastOnlineUsers();
                    }
                }
            }
        }
    }
}
