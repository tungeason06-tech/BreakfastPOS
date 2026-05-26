package tutorial_app_order_entry;

import data_type.OrderDetail;
import data_type.OrderDetailEntry;
import data_type.Product;
import data_type.SaleOrder;
import file_read_write.OrderFileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;


/**
 * 整合產品菜單和訂單組件的主要POS應用程式
 */
public class AppOrderEntryTutorial extends Application {
    
    @Override
    public void start(Stage primaryStage) {
    
        // 建立場景並設置舞台
        Scene scene = new Scene(  getRootPane()  );  // 設定視窗大小為1000x600
        primaryStage.setTitle("飲料POS系統");  // 設定視窗標題
        primaryStage.setScene(scene);  // 將場景加入舞台
        primaryStage.show();  // 顯示舞台
    }

    public HBox getRootPane()
    {
            // 建立根容器，使用水平布局
        HBox root = new HBox();
        root.setPrefWidth(1200);
        root.setSpacing(10);  // 設定組件間間隔
        root.setPadding(new Insets(10));  // 設定內邊距
        
        // 修正CSS路徑，使用正確的類路徑格式
        root.getStylesheets().add(getClass().getResource("/css/bootstrap3.css").toExternalForm());
        
        // 不同的版本來觀察功能演進
        //OrderPaneV0 orderPane = new OrderPaneV0(); // 第0版：只有基本介面外觀
        //OrderPaneV1 orderPane = new OrderPaneV1(); // 第1版：加入表格欄位與資料綁定
        //OrderPaneV2 orderPane = new OrderPaneV2(); // 第2版：加入按鈕點擊事件
        OrderPaneV3 orderPane = new OrderPaneV3();        // 第3版最終版：購物車
        
        // 將兩個組件添加到根容器
        root.getChildren().add(orderPane);
        
        return root;
    }
    // 主程式入口
    public static void main(String[] args) {
        launch(args);  // 啟動JavaFX應用程式
    }
}



// =========================================================================
// V0：第0版 - 只有基本介面外觀，沒有任何功能與資料
// 學習重點：如何在畫面上放置元件 (TableView, Button, TextArea)
// =========================================================================
class OrderPaneV0 extends VBox {
    private final TableView<OrderDetailEntry> table = new TableView<>();
    private final TextArea display = new TextArea();

    public OrderPaneV0() {
        this.setSpacing(10); // 設定元件之間的垂直距離為 10 像素
        this.setPadding(new Insets(10)); // 設定與外框的距離

        // 1. 建立表格元件 (TableView)
        table.setPrefHeight(300); // 設定表格預設高度

        // 2. 建立操作按鈕 (放進一個小容器 TilePane 裡水平排列)
        TilePane operationBtnTile = new TilePane();
        operationBtnTile.setHgap(10); // 按鈕之間的水平距離
        
        Button btnDelete = new Button("刪除一筆");
        btnDelete.getStyleClass().setAll("button", "danger"); // 設定Bootstrap紅色樣式
        
        Button btnCheckout = new Button("結帳");
        btnCheckout.getStyleClass().setAll("button", "primary"); // 設定Bootstrap藍色樣式
        
        operationBtnTile.getChildren().addAll(btnDelete, btnCheckout);

        // 3. 建立資訊顯示區 (TextArea)
        display.setPrefHeight(50);
        display.setEditable(false); // 設定為不可編輯 (純顯示用)
        display.setText("這裡是顯示資訊的區域");

        // 4. 將所有元件加入到目前這個 VBox 容器中 (由上到下排列)
        this.getChildren().addAll(operationBtnTile, table, display);
    }
}

// =========================================================================
// V1：第1版 - 加入表格欄位 (TableColumn) 與資料綁定 (ObservableList)
// 學習重點：定義表格有哪些欄位，並將資料清單與表格綁定
// =========================================================================
class OrderPaneV1 extends VBox {
    // 建立一個可觀察的清單，當清單內容改變時，表格會自動更新畫面
    private final ObservableList<OrderDetailEntry> orderCart = FXCollections.observableArrayList();
    private final TableView<OrderDetailEntry> table = new TableView<>();
    private final TextArea display = new TextArea();

    public OrderPaneV1() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // --- 沿用 V0 的按鈕與顯示區設定 ---
        TilePane operationBtnTile = new TilePane();
        operationBtnTile.setHgap(10);
        Button btnDelete = new Button("刪除一筆");
        btnDelete.getStyleClass().setAll("button", "danger");
        Button btnCheckout = new Button("結帳");
        btnCheckout.getStyleClass().setAll("button", "primary");
        operationBtnTile.getChildren().addAll(btnDelete, btnCheckout);

        display.setPrefHeight(50);
        display.setEditable(false);
        
        // --- V1 新增：表格與欄位設定 ---
        table.setPrefHeight(300);
        table.setItems(orderCart); // 將表格的資料來源設定為 orderCart

        // 定義【品名】欄位，並指定對應到 OrderDetailEntry 模型中的 name 屬性
        TableColumn<OrderDetailEntry, String> colName = new TableColumn<>("品名");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(100);

        // 定義【價格】欄位，指定對應 price 屬性
        TableColumn<OrderDetailEntry, Integer> colPrice = new TableColumn<>("價格");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // 定義【數量】欄位，指定對應 quantity 屬性
        TableColumn<OrderDetailEntry, Integer> colQty = new TableColumn<>("數量");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // 將這三個欄位加入表格中
        table.getColumns().add(colName);
        table.getColumns().add(colPrice);
        table.getColumns().add(colQty);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // 表格欄寬自動填滿

        // 加入兩筆假資料來測試表格顯示是否正常
        orderCart.add(new OrderDetailEntry("p1", "蘋果汁", 50, 1));
        orderCart.add(new OrderDetailEntry("p2", "西瓜汁", 60, 2));

        this.getChildren().addAll(operationBtnTile, table, display);
    }
}

// =========================================================================
// V2：第2版 - 加入按鈕點擊事件 (Action Event) 與金額計算
// 學習重點：為按鈕加上 setOnAction 事件監聽，讓按鈕點擊時有反應
// =========================================================================
class OrderPaneV2 extends VBox {
    private final ObservableList<OrderDetailEntry> orderCart = FXCollections.observableArrayList();
    private final TableView<OrderDetailEntry> table = new TableView<>();
    private final TextArea display = new TextArea();

    public OrderPaneV2() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        TilePane operationBtnTile = new TilePane();
        operationBtnTile.setHgap(10);
        Button btnDelete = new Button("刪除一筆");
        btnDelete.getStyleClass().setAll("button", "danger");
        Button btnCheckout = new Button("結帳");
        btnCheckout.getStyleClass().setAll("button", "primary");
        operationBtnTile.getChildren().addAll(btnDelete, btnCheckout);

        display.setPrefHeight(50);
        display.setEditable(false);

        table.setPrefHeight(300);
        table.setItems(orderCart);

        TableColumn<OrderDetailEntry, String> colName = new TableColumn<>("品名");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(100);
        TableColumn<OrderDetailEntry, Integer> colPrice = new TableColumn<>("價格");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<OrderDetailEntry, Integer> colQty = new TableColumn<>("數量");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // 將這三個欄位加入表格中 (拆分成逐一加入，避免泛型的 Varargs 警告)
        table.getColumns().add(colName);
        table.getColumns().add(colPrice);
        table.getColumns().add(colQty);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        orderCart.add(new OrderDetailEntry("p1", "蘋果汁", 50, 1));
        orderCart.add(new OrderDetailEntry("p2", "西瓜汁", 60, 2));

        // --- V2 新增：按鈕事件設定 ---
        
        // 刪除按鈕事件設定
        btnDelete.setOnAction(event -> {
            // 取得使用者游標在表格中選取的那行資料
            OrderDetailEntry selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) { // 有選中才執行刪除
                orderCart.remove(selectedItem); // 只要從 orderCart 移除，畫面就會跟著更新
                display.setText("已刪除: " + selectedItem.getName() + "\n");
                checkTotal(); // 重新計算總金額
            }
        });

        // 結帳按鈕事件設定
        btnCheckout.setOnAction(event -> {
            if (orderCart.isEmpty()) { // 防呆檢查：如果購物車是空的
                display.setText("購物車是空的，無法結帳！");
            } else {
                
                // 將訂單資料存入資料庫
                // boolean saveSuccess = saveOrderToDatabase(total, orderCart);
                
                checkTotal(); // 算出總金額
                display.appendText("\n[模擬結帳完成，準備開發連接資料庫功能...]");
                orderCart.clear(); // 結帳後清空購物車 (表格也會同步清空)
            }
        });

        this.getChildren().addAll(operationBtnTile, table, display);
        checkTotal(); // 程式剛啟動時先算一次總金額
    }

    // 獨立出一個計算總金額的方法，方便重複呼叫
    private void checkTotal() {
        double total = 0;
        // 跑迴圈把每一列資料的 (單價 * 數量) 加起來
        for (OrderDetailEntry od : orderCart) {
            total += od.getPrice() * od.getQuantity();
        }
        display.setText("總金額: " + Math.round(total) + " 元");
    }
}

// =========================================================================
// V3最終版：包含完整功能 (資料庫操作與表格編輯)
// =========================================================================
class OrderPaneV3 extends VBox {

    // 用於儲存訂單項目的可觀察列表，以便與 TableView 進行綁定和自動更新
    private final ObservableList<OrderDetailEntry> orderCart = FXCollections.observableArrayList();


    // 訂單表格控制項
    private final TableView<OrderDetailEntry> table = new TableView<>();

    // 顯示總金額的文字區域
    private final TextArea display = new TextArea();

    // 建構子 - 初始化UI組件和事件處理
    public OrderPaneV3() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // 初始化表格和控件
        initializeOrderTable();

        // 添加訂單操作按鈕
        initializeOrderOperationContainer();
        //this.getChildren().add(getOrderOperationContainer());

        // 添加表格和總金額顯示
        this.getChildren().add(table);
        this.getChildren().add(display);

    }

    private void initializeOrderOperationContainer() {
        
        // Create buttons for order operations
        Button btnAdd = new Button("新增早餐");
        btnAdd.getStyleClass().setAll("button", "success"); // 使用Bootstrap的success綠色樣式
        btnAdd.setOnAction((ActionEvent event) -> {
            // 將範例果汁加入購物車 (實際應用中會從產品菜單那邊選擇加入購物車，這裡先模擬一筆資料)
            addToCart(new Product("p-j-105", "早餐","卡啦雞腿堡", 65, "", ""));
            System.out.println("新增早餐商品");
        });

        Button btnDelete = new Button("刪除一筆");
        btnDelete.getStyleClass().add("btn-danger"); // 使用Bootstrap的danger樣式
        btnDelete.getStyleClass().setAll("button", "danger");

        btnDelete.setOnAction((ActionEvent event) -> {
            OrderDetailEntry selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // 從訂單列表中移除所選項目
                orderCart.remove(selectedItem);
                // 重新計算總金額
                checkTotal();
                System.out.println("Deleted order: " + selectedItem.getName());
            }
        });

        Button btnCheckout = new Button("結帳");
        btnCheckout.getStyleClass().setAll("button", "primary");

        btnCheckout.setOnAction((ActionEvent event) -> {
            if (!orderCart.isEmpty()) {
                // 取得總金額
                double total = 0;
                for (OrderDetailEntry od : orderCart) {
                    total += od.getPrice() * od.getQuantity();
                }

                // 將訂單資料存入資料庫或CSV
                boolean saveSuccess = saveOrderTo(total, orderCart);

                // 清空購物車
                orderCart.clear();
                // 更新顯示
                if (saveSuccess) {
                    display.setText("結帳完成！金額：" + Math.round(total) + "元\n訂單已儲存");
                } else {
                    display.setText("結帳完成！金額：" + Math.round(total) + "元\n但儲存失敗");
                }
            } else {
                display.setText("購物車是空的，無法結帳");
            }
        });


        // Create container for buttons
        // 訂單操作按鈕容器
        TilePane operationBtnTile = new TilePane();
        operationBtnTile.setVgap(10);
        operationBtnTile.setHgap(10);

        operationBtnTile.getChildren().add(btnDelete);
        operationBtnTile.getChildren().add(btnCheckout);
        // 最後版本可刪除新增按鈕，因為在菜單那邊已經有加入購物車的功能了
        operationBtnTile.getChildren().add(btnAdd);

        this.getChildren().add(operationBtnTile);

        //return operationBtnTile;
    }

    // 初始化訂單表格及相關控件
    private void initializeOrderTable() {

        // 初始化表格並設置為可編輯
        table.setEditable(true);
        table.setPrefHeight(300);

        // 定義品名欄位
        TableColumn<OrderDetailEntry, String> order_item_name = new TableColumn<>("品名");
        order_item_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        order_item_name.setPrefWidth(100);
        order_item_name.setMinWidth(100);

        // 定義價格欄位
        TableColumn<OrderDetailEntry, Integer> order_item_price = new TableColumn<>("價格");
        order_item_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        // 定義數量欄位(可編輯)
        TableColumn<OrderDetailEntry, Integer> order_item_qty = new TableColumn<>("數量");
        order_item_qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        // 設置數量欄位為可編輯，並處理字串到整數的轉換
        order_item_qty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // 定義數量欄位編輯完成後的處理邏輯
        order_item_qty.setOnEditCommit(event -> {
            // 取得被編輯的那一行資料
            OrderDetailEntry target = event.getRowValue();
            target.setQuantity (event.getNewValue());
            checkTotal();  // 重新計算總金額

            System.out.println("哪個產品被修改數量:" + target.getName());
            System.out.println("數量被修改為:" + target.getQuantity());
        });

        // 將訂單列表設為表格的數據來源
        orderCart.add(new OrderDetailEntry("p-j-101", "卡啦雞腿堡", 65, 1));  // 預設一筆訂單項目
        orderCart.add(new OrderDetailEntry("p-j-102", "奶茶", 25, 2));  // 預設一筆訂單項目
        table.setItems(orderCart);

        // 將所有欄位加入表格
        table.getColumns().add(order_item_name);
        table.getColumns().add(order_item_price);
        table.getColumns().add(order_item_qty);

        // 設定表格列寬調整策略，避免出現空白列
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // 設定顯示區域的屬性
        display.setWrapText(true);  // 自動換行
        display.setEditable(false);  // 設置為不可編輯
        display.setPrefWidth(200);  // 設定寬度為200像素
        display.setPrefHeight(50);  // 指定精確高度為50像素
        display.setPrefRowCount(2);  // 或設置首選行數為2

    }

    // 計算購物車中所有項目的總金額，並更新顯示區域的文字
    private void checkTotal() {
        double total = 0;
        for (OrderDetailEntry od : orderCart) {
            total += od.getPrice() * od.getQuantity();
        }
        String totalmsg = String.format("%s %d\n", "總金額:", Math.round(total));
        display.setText(totalmsg);
    }

    // 實現addToCart方法
    public void addToCart(Product product) {

        // 檢查產品是否已經在購物車中
        for (OrderDetailEntry item : orderCart) {
            if (item.getId().equals(product.getProductId())) {
                // 如果已存在，增加數量
                int qty = item.getQuantity() + 1;
                item.setQuantity(qty);
                table.refresh();  // 刷新表格顯示
                checkTotal();     // 重新計算總金額
                System.out.println(product.getProductId() + " 已經在購物車中，數量 +1");
                return;  // 結束方法，避免重複添加
            }
        }

        // 如果是新產品，則添加到購物車
        OrderDetailEntry new_ord = new OrderDetailEntry(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                1);  // 初始數量為1
        orderCart.add(new_ord);  // 添加到訂單列表
        System.out.println("已添加新項目到購物車: " + product.getProductId());
        checkTotal();  // 更新總金額
    }


    // 將訂單資料存入CSV檔案中或資料庫中
    private boolean saveOrderTo(double totalAmount, ObservableList<OrderDetailEntry> orderDetails) {
        try {
            // 創建訂單編號 (例如: ord-yyyyMMdd-HHmmss)
            LocalDateTime now = LocalDateTime.now();
            String orderId = "ord-" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

            // 創建 SaleOrder 物件主檔
            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setOrderId(orderId);
            saleOrder.setOrderDate(now);
            saleOrder.setTotalAmount(totalAmount);
            saleOrder.setCustomerId("customer-101"); // 假設使用預設客戶

            // 1. 寫入訂單主檔
            boolean success = OrderFileWriter.insertSaleOrder(saleOrder);
            //boolean success = odderEntryDao.sertSaleOrder(saleOrder);

            // 2. 寫入訂單明細檔
            if (success) {
                for (OrderDetailEntry item : orderDetails) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderId(orderId);
                    detail.setProductId(item.getId());
                    detail.setQuantity(item.getQuantity());

                    // 單筆存入明細
                    boolean detailSuccess = OrderFileWriter.insertOrderDetail(detail);
                    //boolean detailSuccess = orderDetailDao.insertOrderDetail(detail);
                    if (!detailSuccess) {
                        System.err.println("儲存訂單明細失敗: " + item.getId());
                    }
                }
            }

            if (success) {
                System.out.println("訂單 " + orderId + " 已成功儲存");
            } else {
                System.err.println("儲存訂單時發生錯誤");
            }

            return success;
        } catch (Exception e) {
            System.err.println("儲存過程中發生錯誤: " + e.getMessage());
            return false;
        }
    }//saveOrderTo()
    
}
