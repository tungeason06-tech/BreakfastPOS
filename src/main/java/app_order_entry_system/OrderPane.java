package app_order_entry_system;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import data_type.OrderDetail;
import data_type.OrderDetailEntry;
import data_type.Product;
import data_type.SaleOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

// =========================================================================
// 最終版：包含完整功能 (資料庫操作與表格編輯)
// =========================================================================
class OrderPane extends VBox {

    // 用於儲存訂單項目的可觀察列表，以便與 TableView 進行綁定和自動更新
    private final ObservableList<OrderDetailEntry> orderCart = FXCollections.observableArrayList();

    // 訂單表格控制項
    private final TableView<OrderDetailEntry> table = new TableView<>();

    // 顯示總金額的文字區域
    private final TextArea display = new TextArea();

    // 建構子 - 初始化UI組件和事件處理
    public OrderPane() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        // 初始化表格和控件
        initializeOrderTable();

        // 添加訂單操作按鈕
        initializeOrderOperationContainer();
        // this.getChildren().add(getOrderOperationContainer());

        // 添加表格和總金額顯示
        this.getChildren().add(table);
        this.getChildren().add(display);

    }

    private void initializeOrderOperationContainer() {

        // Create buttons for order operations
        Button btnAdd = new Button("新增早餐");

        btnAdd.getStyleClass().setAll("button", "success");

        btnAdd.setOnAction((ActionEvent event) -> {

            addToCart(
                    new Product(
                            "p-b-105",
                            "早餐",
                            "卡啦雞腿堡",
                            65,
                            "",
                            "香酥卡啦雞腿堡"));

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

                // 將訂單資料存入資料庫(尚未實作資料庫功能，先註解掉)
                // boolean saveSuccess = saveOrderToDatabase(total, orderCart);

                // 將訂單資料存入CSV
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
        // operationBtnTile.getChildren().add(btnAdd);

        this.getChildren().add(operationBtnTile);

        // return operationBtnTile;
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
            int row_num = event.getTablePosition().getRow(); // 取得被修改的行號
            int new_val = event.getNewValue(); // 取得用戶輸入的新數量
            OrderDetailEntry target = event.getTableView().getItems().get(row_num); // 取得對應的訂單項目
            target.setQuantity(new_val); // 更新數量
            checkTotal(); // 重新計算總金額

            System.out.println("哪個產品被修改數量:" + orderCart.get(row_num).getName());
            System.out.println("數量被修改為:" + orderCart.get(row_num).getQuantity());
        });

        // 將訂單列表設為表格的數據來源
        orderCart.add(new OrderDetailEntry("p-b-101", "卡啦雞腿堡", 65, 1));

        orderCart.add(new OrderDetailEntry("p-b-102", "奶茶", 25, 2));

        orderCart.add(new OrderDetailEntry("p-b-103", "起司蛋餅", 45, 1));

        orderCart.add(new OrderDetailEntry("p-b-104", "火腿吐司", 40, 1));
        table.setItems(orderCart);

        // 將所有欄位加入表格
        table.getColumns().add(order_item_name);
        table.getColumns().add(order_item_price);
        table.getColumns().add(order_item_qty);

        // 設定表格列寬調整策略，避免出現空白列
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // 設定顯示區域的屬性
        display.setWrapText(true); // 自動換行
        display.setEditable(false); // 設置為不可編輯
        display.setPrefWidth(200); // 設定寬度為200像素
        display.setPrefHeight(50); // 指定精確高度為50像素
        display.setPrefRowCount(2); // 或設置首選行數為2

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
                table.refresh(); // 刷新表格顯示
                checkTotal(); // 重新計算總金額
                System.out.println(product.getProductId() + " 已經在購物車中，數量 +1");
                return; // 結束方法，避免重複添加
            }
        }

        // 如果是新產品，則添加到購物車
        OrderDetailEntry new_ord = new OrderDetailEntry(
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                1); // 初始數量為1
        orderCart.add(new_ord); // 添加到訂單列表
        System.out.println("已添加新項目到購物車: " + product.getProductId());
        checkTotal(); // 更新總金額
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
            boolean success = file_read_write.OrderFileWriter.insertSaleOrder(saleOrder);
            // boolean success = odderEntryDao.sertSaleOrder(saleOrder);

            // 2. 寫入訂單明細檔
            if (success) {
                for (OrderDetailEntry item : orderDetails) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderId(orderId);
                    detail.setProductId(item.getId());
                    detail.setQuantity(item.getQuantity());

                    // 單筆存入明細
                    boolean detailSuccess = file_read_write.OrderFileWriter.insertOrderDetail(detail);
                    // boolean detailSuccess = orderDetailDao.insertOrderDetail(detail);
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
    }
}// OrederPane
