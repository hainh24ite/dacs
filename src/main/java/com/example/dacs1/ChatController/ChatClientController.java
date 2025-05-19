package com.example.dacs1.ChatController;

import DAO.ChatDAO;
import DAO.TaiKhoanDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import model.TaiKhoan;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

public class ChatClientController {

    @FXML private ListView<String> listUsers;
    @FXML private VBox chatBox;
    @FXML private TextField txtMessage;
    @FXML private Button btnSend;
    @FXML private Button btnDelete;
    @FXML private Label lblCurrentChat;
    @FXML private Button btnChatToggle;  // Nút bật/tắt chat

    private String username;
    private String selectedReceiver;
    private PrintWriter out;

    private ConcurrentHashMap<String, Boolean> displayedMessages = new ConcurrentHashMap<>();
    private long lastSentTime = 0;
    private String lastSentMessage = "";

    private HBox selectedMessageBox;
    private String selectedMessageId;

    // Quản lý kết nối
    private Socket socket;
    private BufferedReader in;
    private Thread listenThread;
    private boolean isChatOpen = false;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        btnSend.setDisable(true);
        btnDelete.setDisable(true);

        btnChatToggle.setOnAction(e -> toggleChat());

        listUsers.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                selectedReceiver = n;
                lblCurrentChat.setText("Đang trò chuyện với: " + n);
                chatBox.getChildren().clear();
                displayedMessages.clear();
                btnDelete.setDisable(true);
                for (String msg : ChatDAO.getChatHistory(username, selectedReceiver)) {
                    String[] p = msg.split("] ", 2);
                    String time = p[0].substring(1);
                    String[] sm = p[1].split(":", 2);
                    chatBox.getChildren().add(createMessageBubble(sm[0].trim(), sm[1].trim(), time));
                }
            }
        });

        btnSend.setOnAction(e -> sendMessage());
        btnDelete.setOnAction(e -> deleteSelectedMessage());
        txtMessage.setOnAction(e -> sendMessage());
    }

    public void toggleChat() {
        if (!isChatOpen) {
            connectToServer();
            btnChatToggle.setText("Tắt Chat");
        } else {
            disconnectFromServer();
            btnChatToggle.setText("Bật Chat");
        }
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            isChatOpen = true;
            Platform.runLater(() -> {
                btnSend.setDisable(false);
                lblCurrentChat.setText("Kết nối chat thành công");
                loadUserListFromDatabase();
            });

            listenThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        handleServerMessage(line);
                    }
                } catch (IOException e) {
                    if (e instanceof java.net.SocketException && "Socket closed".equals(e.getMessage())) {
                        System.out.println("Socket đã đóng, thread đọc dừng.");
                    } else {
                        e.printStackTrace();
                    }
                }
            });
            listenThread.setDaemon(true);
            listenThread.start();

        } catch (IOException e) {
            isChatOpen = false;
            Platform.runLater(() -> {
                btnSend.setDisable(true);
                new Alert(Alert.AlertType.ERROR, "Không thể kết nối đến server. Vui lòng thử lại sau.").showAndWait();
            });
            e.printStackTrace();
        }
    }

    private void disconnectFromServer() {
        try {
            isChatOpen = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (listenThread != null && listenThread.isAlive()) {
                listenThread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Platform.runLater(() -> {
                btnSend.setDisable(true);
                lblCurrentChat.setText("Đã ngắt kết nối chat");
                chatBox.getChildren().clear();
                listUsers.getItems().clear();
                out = null;
            });
        }
    }

    private void handleServerMessage(String line) {
        if (line.startsWith("USERNAME:")) {
            out.println(username);
        } else if (line.startsWith("ERROR:")) {
            Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, line).showAndWait());
        } else if (line.startsWith("USERS:")) {
            loadUserListFromDatabase();
        } else if (!line.startsWith("Me:")) {
            processIncomingChat(line);
        }
    }

    private void processIncomingChat(String finalLine) {
        Platform.runLater(() -> {
            try {
                String timestamp = "";
                String sender = "";
                String message = "";
                if (finalLine.startsWith("[")) {
                    String[] parts = finalLine.split("]", 2);
                    timestamp = parts[0].substring(1);
                    String[] sm = parts[1].trim().split(":", 2);
                    sender = sm[0].trim();
                    message = sm[1].trim();
                } else {
                    String[] sm = finalLine.trim().split(":", 2);
                    sender = sm[0].trim();
                    message = sm[1].trim();
                }
                String messageId = timestamp + "|" + sender + "|" + message;
                if (displayedMessages.putIfAbsent(messageId, true) != null || sender.equals(username)) return;
                HBox msgBox = createMessageBubble(sender, message, timestamp);
                chatBox.getChildren().add(msgBox);
                if (!sender.equals(username) && sender.equals(selectedReceiver)) {
                    ChatDAO.saveMessage(sender, username, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadUserListFromDatabase() {
        Platform.runLater(() -> {
            listUsers.getItems().clear();
            for (TaiKhoan tk : TaiKhoanDAO.getAll()) {
                if (!tk.getTenDangNhap().equals(username)) listUsers.getItems().add(tk.getTenDangNhap());
            }
            lblCurrentChat.setText(listUsers.getItems().isEmpty() ? "Không có người dùng khác online" : "Chọn một người dùng để bắt đầu cuộc trò chuyện");
        });
    }

    private void sendMessage() {
        if (out == null || !isChatOpen) return;
        String msg = txtMessage.getText().trim();
        if (selectedReceiver == null || msg.isEmpty()) return;
        long now = System.currentTimeMillis();
        if (msg.equals(lastSentMessage) && now - lastSentTime < 2000) return;
        btnSend.setDisable(true);
        out.println(selectedReceiver + "::" + msg);
        ChatDAO.saveMessage(username, selectedReceiver, msg);
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        HBox box = createMessageBubble(username, msg, timestamp);
        chatBox.getChildren().add(box);
        lastSentTime = now;
        lastSentMessage = msg;
        txtMessage.clear();
        btnSend.setDisable(false);
    }

    private void deleteSelectedMessage() {
        if (selectedMessageBox != null && selectedMessageId != null) {
            chatBox.getChildren().remove(selectedMessageBox);
            String[] parts = selectedMessageId.split("\\|", 3);
            ChatDAO.deleteMessage(parts[0], parts[1], parts[2], parts[0]);
            displayedMessages.remove(selectedMessageId);
            btnDelete.setDisable(true);
        }
    }

    private HBox createMessageBubble(String sender, String message, String timestamp) {
        Label lbl = new Label((timestamp.isEmpty() ? "" : "[" + timestamp + "] ") + sender + ": " + message);
        lbl.setWrapText(true);
        lbl.setMaxWidth(300);
        lbl.setStyle("-fx-padding: 10; -fx-background-radius: 15; -fx-font-size: 14;");
        VBox cont = new VBox(lbl);
        cont.setMaxWidth(300);
        cont.setStyle("-fx-background-radius: 15; -fx-padding: 5;");
        HBox box = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (sender.equals(username)) {
            cont.setStyle(cont.getStyle() + "-fx-background-color: #a0e7e5;");
            box.setAlignment(Pos.CENTER_RIGHT);
            box.getChildren().addAll(spacer, cont);
        } else {
            cont.setStyle(cont.getStyle() + "-fx-background-color: #f0f0f0;");
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().addAll(cont, spacer);
        }
        box.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && sender.equals(username)) {
                if (selectedMessageBox != null)
                    selectedMessageBox.setStyle(selectedMessageBox.getStyle().replace("-fx-border-color: yellow;", ""));
                selectedMessageBox = box;
                selectedMessageId = timestamp + "|" + sender + "|" + message;
                box.setStyle(box.getStyle() + "-fx-border-color: yellow; -fx-border-width: 2;");
                btnDelete.setDisable(false);
            }
        });
        return box;
    }
    
    public void startClient() {

        if (!isChatOpen) {
            connectToServer();
        }
    }
}

