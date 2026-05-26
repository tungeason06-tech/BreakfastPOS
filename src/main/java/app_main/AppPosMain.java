package app_main;

import app_order_entry_system.AppOrderEntry;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tutorial_app_product_maintenance.AppProductMaintenance;


public class AppPosMain extends Application {
    
    private StackPane contentArea;
    private VBox sidebar;
    private boolean isSidebarExpanded = true;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("宇軒早餐店 POS 系統");

        BorderPane mainLayout = new BorderPane();
        
        // -------------------------
        // 上方工具列 (Top Bar) 設定
        // -------------------------
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        topBar.setPadding(new Insets(10, 15, 10, 15));
        topBar.setAlignment(Pos.CENTER_LEFT);

        // 漢堡選單按鈕 (Toggle Sidebar)
        Button toggleBtn = new Button("☰");
        toggleBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-cursor: hand; -fx-text-fill: #333333;");
        
        
        Label headerTitle = new Label("  🍔宇軒早餐店 POS");
        headerTitle.getStyleClass().add("header-title");

        headerTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        headerTitle.setTextFill(Color.web("#333333"));
        
        topBar.getChildren().addAll(toggleBtn, headerTitle);

        // -------------------------
        // 左側側邊欄 (Sidebar) 設定
        // -------------------------
        sidebar = new VBox();
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #252526;"); // 專業低調的深灰色
        
        // 側邊欄標題
        Label appTitle = new Label("選單 MENU");
        appTitle.setTextFill(Color.web("#aaaaaa"));
        appTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        appTitle.setPadding(new Insets(20, 20, 10, 20));
        sidebar.getChildren().add(appTitle);

        // -------------------------
        // 側邊欄收合邏輯
        // -------------------------
        toggleBtn.setOnAction(e -> {
            isSidebarExpanded = !isSidebarExpanded;
            if (isSidebarExpanded) {
                sidebar.setVisible(true);
                sidebar.setManaged(true);
            } else {
                sidebar.setVisible(false);
                sidebar.setManaged(false);
            }
        });

        // -------------------------
        // 右側主要內容區 (Content)
        // -------------------------
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #f3f3f3;"); // 乾淨的淺灰背景

        // 建立功能導覽按鈕
        Button btnOrderEntry = createNavButton("點餐系統 (Order)");
        Button btnProductMgt = createNavButton("產品維護 (Product)");
        Button btnReport = createNavButton("營業報表 (Report)");
        Button btnSettings = createNavButton("系統設定 (Settings)");

        // 設定各個按鈕的點擊事件
        btnOrderEntry.setOnAction(e -> {
            setActiveButton(btnOrderEntry);
            try {
                AppOrderEntry appOrderEntry = new AppOrderEntry();
                switchView(appOrderEntry.getRootPane());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnProductMgt.setOnAction(e -> {
            setActiveButton(btnProductMgt);
            try {
                AppProductMaintenance appProductMaintenance = new AppProductMaintenance();
                switchView(appProductMaintenance.getRootPane());
            } catch (Exception ex) {
                ex.printStackTrace();
                switchView(createPlaceholder("產品維護系統 (建置中)"));
            }
        });

        btnReport.setOnAction(e -> {
            setActiveButton(btnReport);
            switchView(createPlaceholder("營業報表系統 (建置中)"));
        });

        btnSettings.setOnAction(e -> {
            setActiveButton(btnSettings);
            switchView(createPlaceholder("系統設定 (建置中)"));
        });

        // 將按鈕加入側邊欄
        sidebar.getChildren().addAll(btnOrderEntry, btnProductMgt, btnReport, btnSettings);
        
        // 設定主畫面佈局
        mainLayout.setTop(topBar); // 放置上方工具列包含漢堡按鈕
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        // 預設進入點餐系統畫面
        btnOrderEntry.fire(); 

        Scene scene = new Scene(mainLayout, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * 建立統一風格的側邊選單按鈕
     */
    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 16px; -fx-alignment: center-left; -fx-padding: 15 20 15 20;");
        
        // 滑鼠懸停效果 (Hover)
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyleClass().contains("active")) {
                btn.setStyle("-fx-background-color: #3e3e42; -fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-alignment: center-left; -fx-padding: 15 20 15 20;");
            }
        });
        
        // 滑鼠移開效果
        btn.setOnMouseExited(e -> {
            if (!btn.getStyleClass().contains("active")) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 16px; -fx-alignment: center-left; -fx-padding: 15 20 15 20;");
            }
        });
        
        return btn;
    }

    /**
     * 設定按鈕為「作用中(Active)」的高亮狀態
     */
    private void setActiveButton(Button activeBtn) {
        // 重置所有按鈕樣式
        for (Node node : sidebar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.getStyleClass().remove("active");
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #cccccc; -fx-font-size: 16px; -fx-alignment: center-left; -fx-padding: 15 20 15 20;");
            }
        }
        // 反白目前點選的按鈕 (使用專業的藍色 highlight)
        activeBtn.getStyleClass().add("active");
        activeBtn.setStyle("-fx-background-color: #007acc; -fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-alignment: center-left; -fx-padding: 15 20 15 20;");
    }

    /**
     * 切換主畫面右側內容
     */
    private void switchView(Node node) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(node);
    }

    /**
     * 開發中的佔位功能畫面
     */
    private Node createPlaceholder(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", 24));
        label.setTextFill(Color.web("#888888"));
        return new StackPane(label);
    }

    public static void main(String[] args) {
        launch(args);
    }
}