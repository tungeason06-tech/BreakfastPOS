package app_order_entry_system;

import java.util.Map;
import java.util.TreeMap;

import data_type.Product;
import db.ProductDAO;
import file_read_write.ProductFileReader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/* =====================================================================
 * 【版本 V3：終極完整版 (結合圖片與錯誤保護機制)】
 * ===================================================================== */
class ProductMenuPane extends VBox {

    private OrderPane orderPane; // 注入 OrderPane 實例以便未來加入購物車功能

    private final String[] categories = {
            "早餐",
            "飲料"
    };
    private final Map<String, TilePane> menus = new TreeMap<>();
    private final VBox menuContainerPane = new VBox();

    public ProductMenuPane() {
        setSpacing(10);

        for (String category : categories) {
            menus.put(category, getProductCategoryMenu(category));
        }

        TilePane categoryContainer = new TilePane();
        categoryContainer.setVgap(10);
        categoryContainer.setHgap(10);

        for (String category : categories) {
            Button btn = new Button(category);
            btn.getStyleClass().setAll("button", "success");

            btn.setOnAction(event -> {
                menuContainerPane.getChildren().clear();
                menuContainerPane.getChildren().add(menus.get(category));
            });
            categoryContainer.getChildren().add(btn);
        }

        this.getChildren().add(categoryContainer);
        if (categories.length > 0) {
            menuContainerPane.getChildren().add(menus.get(categories[0]));
        }
        this.getChildren().add(menuContainerPane);
    }

    public void setOrderPane(OrderPane orderPane) {
        this.orderPane = orderPane;
    }

    // V3 修改重點在這裡：這是一個更厲害的產品按鈕製造機，會自動幫產品穿上圖片外衣！
    private TilePane getProductCategoryMenu(String category) {
        ProductDAO dao = new ProductDAO();
        Map<String, Product> product_dict = dao.getProducts();
        TilePane category_menu = new TilePane();
        category_menu.setVgap(10);
        category_menu.setHgap(10);
        category_menu.setPrefColumns(4);

        for (Product product : product_dict.values()) {
            // 確認這是我們要的分類的產品...
            if (product.getCategory().equals(category)) {
                Button btn = new Button();
                btn.getStyleClass().add("product-card");
                btn.setPrefSize(120, 120); // 固定產品按鈕大小為 120x120

                /*
                 * 【圖片防呆保護機制 (Try-Catch)】
                 * Try: 電腦去指定的資料夾中找圖片檔案並載入，如果成功，就把圖片塞入按鈕。
                 * Catch: 萬一檔案遺失、檔名打錯找不到，電腦就會跑到 Catch 這裡。
                 * 我們就在 Catch 這裡自己徒手畫一個方塊，把文字寫進去頂替掉原來圖片的位置。
                 */
                try {
                    String imageUrl = product.getImgUrl();
                    Image img;
                    if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                        img = new Image(imageUrl);
                    } else {
                        java.io.File file = new java.io.File(imageUrl);
                        if (!file.exists()) {
                            throw new java.io.FileNotFoundException("找不到檔案：" + file.getAbsolutePath());
                        }
                        img = new Image(file.toURI().toString());
                    }
                    if (img.isError()) {
                        throw new Exception("圖片載入失敗：" + imageUrl);
                    }
                    ImageView imgview = new ImageView(img);
                    imgview.setFitHeight(80); // 圖片高度設為80
                    imgview.setPreserveRatio(true); // 等比例縮放

                    btn.setGraphic(imgview); // 將圖片貼到按鈕上
                } catch (Exception e) {
                    // 萬一圖片遺失，自己手繪一個長得差不多的方塊來替代
                    VBox placeholderBox = new VBox();
                    placeholderBox.setAlignment(Pos.CENTER);
                    placeholderBox.setPrefHeight(80); // Match exact image height
                    placeholderBox.setPrefWidth(80); // Keep square ratio like typical product images
                    placeholderBox.setMinHeight(80);
                    placeholderBox.setMaxHeight(80);
                    placeholderBox.setStyle(
                            "-fx-border-color: #cccccc; -fx-background-color: #f8f8e8; -fx-border-radius: 5;");

                    // Use formatted text for better display
                    Text productText = new Text(product.getName());
                    productText.setWrappingWidth(70); // Slightly less than container width
                    productText.setTextAlignment(TextAlignment.CENTER);

                    placeholderBox.getChildren().add(productText);
                    btn.setGraphic(placeholderBox);
                    System.err.println(e);
                    System.out.println("Could not load image for product: " + product.getName());
                }

                // 設定產品按鈕點擊事件：代表未來的「加入購物車」功能！
                btn.setOnAction(event -> {
                    orderPane.addToCart(product); // 直接呼叫 OrderPane 的方法把產品加入購物車
                    System.out.println("V3 - 即將加入購物車: " + product.getName() + " - 單價: $" + product.getPrice());
                });

                category_menu.getChildren().add(btn);
            }
        }
        return category_menu;
    }
}// ProductMenuPane
