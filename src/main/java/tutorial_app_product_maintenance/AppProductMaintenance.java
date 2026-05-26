package tutorial_app_product_maintenance;

import java.util.List;

import data_type.Product;
import file_read_write.ProductFileReader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

//--------------------
// 1. 主程式類別宣告
//--------------------
public class AppProductMaintenance extends Application {

    //--------------------
    // 2. 欄位宣告：資料存取、資料清單、狀態顯示
    //--------------------
    //private final ProductDAO productDao = new ProductDAO();
    //private final List<Product> products = productDao.getAllProducts();
    private final List<Product> products = ProductFileReader.readProductList(); // 從檔案讀取產品資料

    private final ObservableList<Product> product_list = FXCollections.observableList(products);
    private final Label statusLabel = new Label("狀態顯示區");

    //--------------------
    // 3. JavaFX應用程式進入點
    //--------------------
    @Override
    public void start(Stage primaryStage) {
        VBox root = getRootPane();
        Scene scene = new Scene(root, 1100, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("高科資管POS系統");
        primaryStage.show();
    }

    // 模組化：查詢區塊
    private HBox createSearchPane() {
        HBox searchPane = new HBox();
        searchPane.setSpacing(10);
        searchPane.setAlignment(Pos.CENTER_LEFT);

        Label lblId = new Label("ID:");
        TextField tfId = new TextField();
        Button btnSearchId = new Button("查詢ID");
        btnSearchId.setOnAction(e -> statusLabel.setText("查詢產品ID: " + tfId.getText()));

        Label lblCategory = new Label("類別:");
        TextField tfCategory = new TextField();
        Button btnSearchCategory = new Button("查詢類別");
        btnSearchCategory.setOnAction(e -> statusLabel.setText("查詢產品類別: " + tfCategory.getText()));

        Button btnAll = new Button("全部產品");
        btnAll.setOnAction(e -> statusLabel.setText("顯示全部產品"));

        Label lblName = new Label("名稱:");
        TextField tfName = new TextField();
        Button btnSearchName = new Button("搜尋名稱");
        btnSearchName.setOnAction(e -> statusLabel.setText("搜尋產品名稱: " + tfName.getText()));

        searchPane.getChildren().addAll(
                lblId, tfId, btnSearchId,
                lblCategory, tfCategory, btnSearchCategory,
                lblName, tfName, btnSearchName,
                btnAll
        );
        return searchPane;
    }

    // 模組化：操作按鈕區塊
    private HBox createToolbar(TableView<Product> table) {
        Button btnUpdate = new Button("Update");
        Button btnDuplicate = new Button("Duplicate");
        Button btnSave = new Button("Save");
        Button btnDelete = new Button("Delete");

        btnUpdate.getStyleClass().setAll("button", "primary");
        btnDuplicate.getStyleClass().setAll("button", "warning");
        btnSave.getStyleClass().setAll("button", "success");
        btnDelete.getStyleClass().setAll("button", "danger");

        btnUpdate.setOnAction(event -> {
            Product product = table.getSelectionModel().getSelectedItem();
            if (product != null) {
                //boolean updateSuccess = productDao.update(product);
                boolean updateSuccess = true; // 模擬更新成功
                if (updateSuccess) {
                    statusLabel.setText("Updated: " + product.getName());
                    System.out.println("Updated: " + product.getName());
                } else {
                    statusLabel.setText("Update failed: " + product.getName());
                    System.out.println("Update failed for: " + product.getName());
                }
            }
        });

        btnDuplicate.setOnAction(event -> {
            Product product = table.getSelectionModel().getSelectedItem();
            if (product != null) {
                String newProductId = product.getProductId() + "_copy_";
                Product duplicatedProduct = new Product(
                        newProductId,
                        product.getCategory(),
                        product.getName() + " (Copy)",
                        product.getPrice(),
                        product.getImgUrl(),
                        product.getDescription()
                );
                product_list.add(duplicatedProduct);
                statusLabel.setText("Duplicated: " + product.getName() + " (尚未存檔)");
                System.out.println("Duplicated: " + product.getName() + " (尚未存檔)");
            }
        });

        btnSave.setOnAction(event -> {
            Product product = table.getSelectionModel().getSelectedItem();
            if (product != null) {
                //productDao.add(product);
                statusLabel.setText("Save: " + product.getName());
                System.out.println("Save: " + product.getName());
            }
        });

        btnDelete.setOnAction(event -> {
            Product product = table.getSelectionModel().getSelectedItem();
            if (product != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("確認刪除");
                alert.setHeaderText("您即將刪除產品: " + product.getName());
                alert.setContentText("確定要刪除這個產品嗎?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        //boolean deleteSuccess = productDao.delete(product.getProductId());
                        boolean deleteSuccess = true; // 模擬刪除成功
                        if (deleteSuccess) {
                            product_list.remove(product);
                            statusLabel.setText("Deleted: " + product.getName());
                            System.out.println("Deleted: " + product.getName());
                        } else {
                            statusLabel.setText("Delete failed: " + product.getName());
                            System.out.println("Delete failed: " + product.getName());
                        }
                    }
                });
            }
        });

        HBox toolbar = new HBox(btnUpdate, btnDuplicate, btnSave, btnDelete);
        toolbar.setSpacing(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        return toolbar;
    }

    //--------------------
    // 4. 建立主畫面元件
    //--------------------
    public VBox getRootPane() {
        HBox searchPane = createSearchPane();
        TableView<Product> table = initializeProductTable();
        HBox toolbar = createToolbar(table);

        VBox vbox = new VBox(searchPane, toolbar, table, statusLabel);
        vbox.getStylesheets().add(getClass().getResource("/css/bootstrap3.css").toExternalForm());
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        return vbox;
    }

    //--------------------
    // 5. 主程式進入點
    //--------------------
    public static void main(String[] args) {
        launch(args);
    }

    //--------------------
    // 6. 建立TableView欄位（字串型別）
    //--------------------
    private TableColumn<Product, String> createColumn(String title, String propertyName) {
        TableColumn<Product, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        return column;
    }

    //--------------------
    // 7. 建立TableView欄位（整數型別）
    //--------------------
    private TableColumn<Product, Integer> createColumn(String title, String propertyName, IntegerStringConverter converter) {
        TableColumn<Product, Integer> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        return column;
    }

    //--------------------
    // 8. 設定欄位編輯完成的事件處理
    //--------------------
    /*
    private <T> void setEditCommitHandler 方法說明：
    - private：只有這個類別內部可以使用
    - <T>：泛型型別參數，代表任何型別（String、Integer等）
    - void：方法沒有回傳值
    - setEditCommitHandler：方法名稱，用來設定欄位編輯完成後的處理
    - TableColumn<Product, T> column：要設定的表格欄位，T代表欄位的資料型別
    - String propertyName：Product物件中對應的屬性名稱

    這個方法的用途是統一處理表格欄位編輯完成後要做的事情，
    避免每個欄位都要重複寫相同的程式碼。

    若不使用此方法，每個欄位都要分別設定，例如：
    idColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        product.setProductId(event.getNewValue().toString());
        System.out.println("productId updated: " + product);
    });

    nameColumn.setOnEditCommit(event -> {
        Product product = event.getRowValue();
        product.setName(event.getNewValue().toString());
        System.out.println("name updated: " + product);
    });
    // 每個欄位都要重複寫類似的程式碼...
     */
    private <T> void setEditCommitHandler(TableColumn<Product, T> column, String propertyName) {
        column.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            switch (propertyName) {
                // 如果propertyName是"productId"，將新的值轉換為字串並設置到product的productId屬性中
                case "productId":
                    product.setProductId(event.getNewValue().toString());
                    break;
                // 如果propertyName是"category"，將新的值轉換為字串並設置到product的category屬性中
                case "category":
                    product.setCategory(event.getNewValue().toString());
                    break;
                case "name":
                    product.setName(event.getNewValue().toString());
                    break;
                case "price":
                    product.setPrice((Integer) event.getNewValue());
                    break;
                case "imageUrl":
                    product.setImgUrl(event.getNewValue().toString());
                    break;
                case "description":
                    product.setDescription(event.getNewValue().toString());
                    break;
            }
            System.out.println(propertyName + " updated: " + product);
        });
    }

    //--------------------
    // 9. 初始化TableView與所有欄位、按鈕
    //--------------------
    private TableView<Product> initializeProductTable() {
        TableView<Product> table = new TableView<>();
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> idColumn = createColumn("Product ID", "productId");
        idColumn.setPrefWidth(120);
        TableColumn<Product, String> categoryColumn = createColumn("Category", "category");
        categoryColumn.setPrefWidth(60);
        TableColumn<Product, String> nameColumn = createColumn("Name", "name");
        nameColumn.setPrefWidth(150);
        TableColumn<Product, Integer> priceColumn = createColumn("Price", "price", new IntegerStringConverter());
        priceColumn.setPrefWidth(60);
        TableColumn<Product, String> imageUrlColumn = createColumn("image url", "imgUrl");
        imageUrlColumn.setPrefWidth(150);
        TableColumn<Product, String> descriptionColumn = createColumn("Description", "description");
        descriptionColumn.setPrefWidth(300);

        setEditCommitHandler(idColumn, "productId");
        setEditCommitHandler(categoryColumn, "category");
        setEditCommitHandler(nameColumn, "name");
        setEditCommitHandler(priceColumn, "price");
        setEditCommitHandler(imageUrlColumn, "imageUrl");
        setEditCommitHandler(descriptionColumn, "description");

        table.getColumns().add(idColumn);
        table.getColumns().add(categoryColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(priceColumn);
        table.getColumns().add(imageUrlColumn);
        table.getColumns().add(descriptionColumn);

        table.setItems(product_list);

        return table;
    }
    //--------------------
    // End of class
    //--------------------
}
