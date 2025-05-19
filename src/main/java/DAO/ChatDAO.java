package DAO;


import database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    /**
     * Lưu tin nhắn sau khi kiểm tra nếu một tin nhắn giống hệt đã được gửi gần đây
     * để ngăn chặn tin nhắn trùng lặp
     */
    public static boolean saveMessage(String sender, String receiver, String message) {
        // Đầu tiên kiểm tra xem tin nhắn giống hệt đã được gửi gần đây chưa (trong 10 giây qua)
        if (isDuplicateMessage(sender, receiver, message)) {
            System.out.println("Phát hiện tin nhắn trùng lặp - không lưu lại: " + sender + " -> " + receiver + ": " + message);
            return false; // Không lưu tin nhắn trùng lặp
        }

        // Lưu tin nhắn mới
        String sql = "INSERT INTO chat_message (sender, receiver, message) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, message);
            stmt.executeUpdate();
            System.out.println("Đã lưu tin nhắn: " + sender + " -> " + receiver + ": " + message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Kiểm tra xem tin nhắn có phải là bản sao hay không
     * (trùng người gửi, người nhận và nội dung trong khoảng thời gian gần đây)
     */
    private static boolean isDuplicateMessage(String sender, String receiver, String message) {
        // Tăng thời gian kiểm tra lên 10 giây để phòng trường hợp độ trễ mạng
        String sql = "SELECT COUNT(*) FROM chat_message WHERE sender = ? AND receiver = ? " +
                "AND message = ? AND timestamp > (NOW() - INTERVAL 10 SECOND)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, message);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu số lượng > 0, đó là bản sao
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy lịch sử chat giữa 2 người
    public static List<String> getChatHistory(String user1, String user2) {
        List<String> history = new ArrayList<>();
        // Loại bỏ các tin nhắn trùng lặp bằng cách nhóm theo nội dung và thời gian gần nhau
        String sql = "WITH uniqueMsgs AS (" +
                "SELECT sender, receiver, message, MIN(timestamp) as timestamp " +
                "FROM chat_message " +
                "WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) " +
                "GROUP BY sender, receiver, message, FLOOR(UNIX_TIMESTAMP(timestamp)/10) " +
                ") " +
                "SELECT sender, message, timestamp FROM uniqueMsgs " +
                "ORDER BY timestamp ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user1);
            stmt.setString(2, user2);
            stmt.setString(3, user2);
            stmt.setString(4, user1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String sender = rs.getString("sender");
                String msg = rs.getString("message");
                String time = rs.getTimestamp("timestamp").toString();
                history.add("[" + time + "] " + sender + ": " + msg);
            }
        } catch (Exception e) {
            // Nếu có lỗi với SQL phức tạp, dùng cách đơn giản hơn
            try {
                return getSimpleChatHistory(user1, user2);
            } catch (Exception ex) {
                e.printStackTrace();
                ex.printStackTrace();
            }
        }
        return history;
    }

    // Phương pháp đơn giản hơn để lấy lịch sử chat nếu phương pháp chính bị lỗi
    private static List<String> getSimpleChatHistory(String user1, String user2) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT sender, message, timestamp FROM chat_message " +
                "WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) " +
                "ORDER BY timestamp ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user1);
            stmt.setString(2, user2);
            stmt.setString(3, user2);
            stmt.setString(4, user1);
            ResultSet rs = stmt.executeQuery();

            // Biến để theo dõi tin nhắn cuối cùng để loại bỏ trùng lặp
            String lastSender = "";
            String lastMessage = "";
            Timestamp lastTime = null;

            while (rs.next()) {
                String sender = rs.getString("sender");
                String msg = rs.getString("message");
                Timestamp time = rs.getTimestamp("timestamp");

                // Bỏ qua tin nhắn trùng lặp gần nhau
                if (lastTime != null &&
                        sender.equals(lastSender) &&
                        msg.equals(lastMessage) &&
                        (time.getTime() - lastTime.getTime()) < 10000) { // 10 giây
                    continue;
                }

                history.add("[" + time.toString() + "] " + sender + ": " + msg);

                // Cập nhật thông tin tin nhắn cuối cùng
                lastSender = sender;
                lastMessage = msg;
                lastTime = time;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    // ✅ Lấy danh sách tất cả người dùng trừ người đang đăng nhập
    public static List<String> getAllUsernamesExcept(String currentUser) {
        List<String> users = new ArrayList<>();
        String sql = "SELECT username FROM taikhoan WHERE username != ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, currentUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean deleteMessage(String sender, String receiver, String content, String timestamp) {
        String sql = "DELETE FROM chat_message WHERE sender = ? AND receiver = ? AND message = ? AND timestamp = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, content);
            stmt.setString(4, timestamp);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}