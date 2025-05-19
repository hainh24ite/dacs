package com.example.dacs1;
import com.example.dacs1.ChatController.ChatClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class MainControllernv {

    @FXML
    private AnchorPane mainContent;
    @FXML
    private Button btnSanPham;
    @FXML
    private Button btnBanHang;
    @FXML
    private Button btnHoaDonXuat;
    private String currentUsername;
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @FXML
    public void initialize() {
        if (mainContent == null) {
            System.err.println(" Lỗi: mainContent chưa được khởi tạo! Hãy kiểm tra lại fx:id trong main.fxml.");
        } else {
            System.out.println(" mainContent đã được khởi tạo thành công!");
        }
    }

    private void resetButtonStyles(){
        String defaultStyle = "-fx-background-color: #353A56; -fx-border-width: 2px; -fx-border-radius: 20px; -fx-border-style: solid; -fx-border-color: #303551;";
        btnSanPham.setStyle(defaultStyle);
        btnBanHang.setStyle(defaultStyle);
        btnHoaDonXuat.setStyle(defaultStyle);
    }
    private void setStyle(Button button){
        button.setStyle("-fx-background-color: #748CF1; -fx-border-width: 2px;-fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-style: solid; -fx-border-color: #748CF1");
    }


    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newView = loader.load();

            if (mainContent != null) {
                mainContent.getChildren().clear();
                mainContent.getChildren().add(newView);
            } else {
                System.err.println("⚠ Lỗi: mainContent vẫn là null!");
            }
        } catch (IOException e) {
            System.err.println(" Không thể tải FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    private void SanPham1() {
        switchScene("/com/example/dacs1/Nhanvien/Sanphamnv/sanphamnv.fxml");
        resetButtonStyles();
        setStyle(btnSanPham);
    }

    @FXML
    private void handleBanHang() {
        switchScene("/com/example/dacs1/Nhanvien/Banhangnv/banhangnv.fxml");
        resetButtonStyles();
        setStyle(btnBanHang);
    }

    @FXML
    private void handlehoadonxuatnv() {
        switchScene("/com/example/dacs1/Nhanvien/Hoadonxuatnv/hoadonnv.fxml");
        resetButtonStyles();
        setStyle(btnHoaDonXuat);
    }


    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Dangnhap/dangnhap.fxml"));
            Parent loginView = loader.load();
            Scene scene=mainContent.getScene();
            scene.setRoot(loginView);
            Stage stage = (Stage) scene.getWindow();
            stage.setWidth(760);
            stage.setHeight(580);
            stage.centerOnScreen();
        } catch (IOException e) {
            System.err.println(" Không thể tải login.fxml");
            e.printStackTrace();
        }
    }
    @FXML
    private void openchat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dacs1/Chat/chat.fxml"));
            Parent root = loader.load();

            // Lấy controller của giao diện chat
            ChatClientController controller = loader.getController();

            // Gán username cho chat controller và khởi động client
            controller.setUsername(currentUsername);
            controller.startClient();

            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
