package app_order_entry_system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 整合產品菜單和訂單組件的主要POS應用程式
 */
public class AppOrderEntry extends Application {

    @Override
    public void start(Stage primaryStage) {

        // 建立場景並設置舞台
        Scene scene = new Scene(getRootPane()); // 設定視窗大小為1000x600
        primaryStage.setTitle("宇軒早餐店 POS 系統"); // 設定視窗標題
        primaryStage.setScene(scene); // 將場景加入舞台
        primaryStage.show(); // 顯示舞台
    }

    public HBox getRootPane() {
        // 建立根容器，使用水平布局
        HBox root = new HBox();
        root.getStyleClass().add("root");
        root.setPrefWidth(1200);
        root.setSpacing(10); // 設定組件間間隔
        root.setPadding(new Insets(10)); // 設定內邊距

        // 修正CSS路徑，使用正確的類路徑格式
        root.getStylesheets().add(getClass().getResource("/css/bootstrap3.css").toExternalForm());

        // 建立訂單面板
        OrderPane orderPane = new OrderPane();
        orderPane.getStyleClass().add("card");
        orderPane.setPrefWidth(450);

        // 建立產品菜單面板，並傳入訂單管理器

        // 方式1:這裡使用了依賴注入設計模式：將orderPane注入到menuPane中
        ProductMenuPane productMenuPane = new ProductMenuPane();
        productMenuPane.getStyleClass().add("card");
        productMenuPane.setPrefWidth(700);
        productMenuPane.setOrderPane(orderPane); // 注入OrderPane實例到ProductMenuPane

        // 方式2:使用 Consumer 設定回調
        // ProductMenuPane productMenuPane = new ProductMenuPane();
        // productMenuPane.setOnProductSelected(product -> {
        // // 將來自 Menu 的產品 ID 傳遞給 Order 處理
        // orderPane.addToCart(product);
        // });

        // 將兩個組件添加到根容器
        productMenuPane.setPrefWidth(600);

        root.getChildren().add(orderPane);
        root.getChildren().add(productMenuPane);
        return root;
    }

    // 主程式入口
    public static void main(String[] args) {
        launch(args); // 啟動JavaFX應用程式
    }
}
