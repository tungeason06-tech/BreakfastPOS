package tutorial_app_product_menu;

/* =====================================================================
 * 【教學指引】
 * 所有的 import (套件引入) 都統一寫在檔案的最前面。
 * 這是 Java 的規定，所有的工具包都必須在類別 (class) 宣告前引入。
 * ===================================================================== */
import java.util.Map;
import java.util.TreeMap;

import data_type.Product;
import file_read_write.ProductFileReader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/* =====================================================================
 * 【主程式：AppProductMenu】
 * 這裡是整個 POS 系統視覺介面的入口，負責開啟一個白色的視窗畫布。
 * 學生可以透過修改 getRootPane() 裡面的「版本號」，來檢視學習的進度。
 * ===================================================================== */
public class AppProductMenuTutorial extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 建立場景並設置視窗大小 (寬600 x 高400)
        Scene scene = new Scene(this.getRootPane(), 600, 400);
        primaryStage.setTitle("飲料POS系統 - 產品菜單教學");  // 設定視窗左上角的標題文字
        primaryStage.setScene(scene);  // 將畫布(Scene)放入視窗(Stage)
        primaryStage.show();  // 把視窗顯示在螢幕上
    }

    // 準備最底層的容器 (就像畫布的底色)
    public HBox getRootPane() {
        HBox root = new HBox();
        root.setPrefWidth(1200);
        root.setSpacing(10);  // 裡面東西的間距
        root.setPadding(new Insets(10));  // 邊框留白

        // 載入 CSS 樣式 (讓按鈕變好看)
        root.getStylesheets().add(getClass().getResource("/css/bootstrap3.css").toExternalForm());

        /* ---------------------------------------------------------
         * 【切換版本區】
         * 這裡決定畫面上要顯示哪一個版本的「產品菜單」。
         * 同學可以修改數字：V0, V1, V2, V3 來看看不同開發階段的長相。
         * --------------------------------------------------------- */
        ProductMenuPaneV3 productMenuPane = new ProductMenuPaneV3();

        // 把我們要的菜單放進畫布裡
        root.getChildren().add(productMenuPane);

        return root;
    }

    public static void main(String[] args) {
        launch(args);  // 啟動 JavaFX 視窗應用程式
    }
}


/* =====================================================================
 * 【版本 V0：純文字測試版】
 * 目標：只放一段簡單的文字，測試剛剛骨架寫的 HBox 有沒有成功顯示在畫面上。
 * ===================================================================== */
class ProductMenuPaneV0 extends VBox {

    public ProductMenuPaneV0() {
        // 設定垂直排列的間距
        setSpacing(10);
        // 加一個隨便的標籤 (Label) 到畫面上
        this.getChildren().add(new Label("V0：這是最簡單的純文字測試，如果您看到這行字，代表主畫面成功啟動了！"));
    }
}


/* =====================================================================
 * 【版本 V1：單純產生「上方切換分類的按鈕」】
 * 目標：從資料檔案中，把所有的「分類」(如果汁、茶飲、咖啡) 抓出來。
 *        並把它們變成一排綠色的按鈕，顯示在畫面上半部。
 * ===================================================================== */
class ProductMenuPaneV1 extends VBox {

    // 從檔案中讀取所有的「分類名稱」裝進一個陣列中
    private final String[] categories = ProductFileReader.readCategories();

    public ProductMenuPaneV1() {
        setSpacing(10);
        this.getChildren().add(new Label("V1版：從檔案讀取分類，自動產生上方綠色按鈕"));

        // 1. 建立一個可以裝按鈕的盒子 (TilePane：東西放不下會自動換行)
        TilePane categoryContainer = new TilePane();
        categoryContainer.setVgap(10); // 上下間距
        categoryContainer.setHgap(10); // 左右間距

        // 2. 將陣列裡面的每一種 "分類" 都拿出來，做成一顆按鈕
        for (String category : categories) {
            Button btn = new Button(category);                   // 建立按鈕
            btn.getStyleClass().setAll("button", "success");     // 套用 Bootstrap的綠色樣式

            // 設定點擊事件：當使用者點選這顆按鈕，就在系統控制台印出字來測試
            btn.setOnAction(event -> {
                System.out.println("V1 - 你點擊了分類: " + category);
            });

            // 把做好的按鈕放進盒子裡
            categoryContainer.getChildren().add(btn);
        }

        // 3. 把裝滿按鈕的盒子，加入到目前的畫面上
        this.getChildren().add(categoryContainer);
    }
}


/* =====================================================================
 * 【版本 V2：加入下方產品「純文字清單」，並能切換分類】
 * 目標：畫面上半部是分類按鈕，下半部是一個用來裝該分類商品的「空盒子」。
 *       點擊上方不同分類，下方盒子的內容就要跟著抽換！(暫時只顯示純文字按鈕)
 * ===================================================================== */
class ProductMenuPaneV2 extends VBox {

    private final String[] categories = ProductFileReader.readCategories();

    // 【新變數】：準備一個 Map 字典，提早把 "每個分類(Key)" 對應的 "整組產品畫面(Value)" 都做出來存好。
    private final Map<String, TilePane> menus = new TreeMap<>();

    // 【新變數】：這是在畫面下方的「大空盒子」，點擊上方分類時，這裡面的內容會被切換替換。
    private final VBox menuContainerPane = new VBox();

    public ProductMenuPaneV2() {
        setSpacing(10);
        this.getChildren().add(new Label("V2版：點擊上方的綠色分類按鈕，下方的文字產品清單會跟著換！"));

        // 1. 提早幫每一個分類都做好它們各自的「專屬產品按鈕畫面」並存入 Map 字典裡
        for (String category : categories) {
            menus.put(category, getProductCategoryMenu(category));
        }

        // 2. 製作上方的一排綠色分類按鈕
        TilePane categoryContainer = new TilePane();
        categoryContainer.setVgap(10);
        categoryContainer.setHgap(10);

        for (String category : categories) {
            Button btn = new Button(category);
            btn.getStyleClass().setAll("button", "success");

            // 【最關鍵的一步】：點擊上方分類按鈕時，讓下方空盒子清空，然後塞入我們在步驟 1 提早準備好對應的畫面
            btn.setOnAction(event -> {
                menuContainerPane.getChildren().clear();                         // 掃掉舊內容
                menuContainerPane.getChildren().add(menus.get(category));        // 放入新內容
                System.out.println("V2 - 畫面已切換至分類: " + category);
            });
            categoryContainer.getChildren().add(btn);
        }
        // 將上方按鈕區加入畫面
        this.getChildren().add(categoryContainer);

        // 3. 一開始開啟程式時，畫面預設先顯示陣列中的「第一種分類(索引1是果汁)」
        if (categories.length > 1) {
            menuContainerPane.getChildren().add(menus.get(categories[1]));
        }

        // 4. 將下方會一直變化內容的「空盒子」也加入畫面
        this.getChildren().add(menuContainerPane);
    }

    // 這個方法專門負責製造出「某一分類下所有的文字產品按鈕」，做完了整坨丟給你
    private TilePane getProductCategoryMenu(String category) {
        Map<String, Product> product_dict = ProductFileReader.readProduct();

        TilePane category_menu = new TilePane(); // 這個分類專屬的小容器
        category_menu.setVgap(10);
        category_menu.setHgap(10);
        category_menu.setPrefColumns(4); // 指定每排長出4個產品就換行

        // 從所有的產品中去篩選，如果是屬於這個分類的，就長出一顆文字按鈕
        for (Product product : product_dict.values()) {
            if (product.getCategory().equals(category)) {
                // 做一個帶有名稱和價格文字的產品大按鈕
                Button btn = new Button(product.getName() + "\n$" + product.getPrice());
                btn.setPrefSize(120, 120);

                // 放進小容器裡
                category_menu.getChildren().add(btn);
            }
        }
        return category_menu;
    }
}


/* =====================================================================
 * 【版本 V3：終極完整版 (結合圖片與錯誤保護機制)】
 * 目標：把 V2 版本下面醜醜的純文字按鈕，換成有精美圖片的按鈕。
 *       如果因為粗心忘記放圖片，程式不能當機，要幫我們自動用「文字」來代替圖片。
 * ===================================================================== */
class ProductMenuPaneV3 extends VBox {

    private final String[] categories = ProductFileReader.readCategories();
    private final Map<String, TilePane> menus = new TreeMap<>();
    private final VBox menuContainerPane = new VBox();

    // V3 的畫面上半部分與 V2 一模一樣，差別只在於下半部製造產品按鈕那邊使用了進階的方法。
    public ProductMenuPaneV3() {
        setSpacing(10);
        this.getChildren().add(new Label("V3 終極進化版：加入圖片載入功能與防呆 Exception 機制！"));

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

        if (categories.length > 1) {
            menuContainerPane.getChildren().add(menus.get(categories[1]));
        }
        this.getChildren().add(menuContainerPane);
    }

    // V3 修改重點在這裡：這是一個更厲害的產品按鈕製造機，會自動幫產品穿上圖片外衣！
    private TilePane getProductCategoryMenu(String category) {
        Map<String, Product> product_dict = ProductFileReader.readProduct();

        TilePane category_menu = new TilePane();
        category_menu.setVgap(10);
        category_menu.setHgap(10);
        category_menu.setPrefColumns(4);

        for (Product product : product_dict.values()) {
            // 確認這是我們要的分類的產品...
            if (product.getCategory().equals(category)) {
                Button btn = new Button();
                btn.setPrefSize(120, 120); // 固定產品按鈕大小為 120x120

                /* 
                * 根據產品定義的圖檔名稱 (product.getImageUrl()) 去找檔案
                * 從外部檔案系統載入圖片 (與專案根目錄下的 imgs 資料夾對應)
                * 這樣店長只需要把圖片丟進資料夾，不需重新寫程式就能生效
                 * 【圖片防呆保護機制 (Try-Catch)】
                 * Try: 電腦去指定的資料夾中找圖片檔案並載入，如果成功，就把圖片塞入按鈕。
                 * Catch: 萬一檔案遺失、檔名打錯找不到，電腦就會跑到 Catch 這裡。
                 *        我們就在 Catch 這裡自己徒手畫一個方塊，把文字寫進去頂替掉原來圖片的位置。
                 */
                try {
                    // 根據產品定義的圖檔名稱或網址 (product.getImageUrl()) 去找檔案或圖片
                    String imageUrl = product.getImgUrl();
                    Image img;
                    if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                        // 來源為網路 URL
                        img = new Image(imageUrl);
                    } else {
                        // 來源為專案底下的本地資料夾 (預設為相對路徑)
                        java.io.File file = new java.io.File(imageUrl);
                        if (!file.exists()) {
                            throw new java.io.FileNotFoundException("找不到檔案：" + file.getAbsolutePath());
                        }
                        img = new Image(file.toURI().toString());
                    }
                    
                    // 檢查圖片是否載入失敗
                    if (img.isError()) {
                        throw new Exception("圖片載入失敗：" + imageUrl);
                    }
                    // 成功載入圖片，將圖片放入按鈕
                    ImageView imgview = new ImageView(img);
                    imgview.setFitHeight(80);          // 圖片高度設為80
                    imgview.setPreserveRatio(true);    // 等比例縮放

                    btn.setGraphic(imgview);           // 將圖片貼到按鈕上
                } catch (Exception e) {
                    // 萬一圖片遺失，自己手繪一個長得差不多的方塊來替代
                   VBox placeholderBox = new VBox();
                    placeholderBox.setAlignment(Pos.CENTER);
                    placeholderBox.setPrefHeight(80); // Match exact image height
                    placeholderBox.setPrefWidth(80); // Keep square ratio like typical product images
                    placeholderBox.setMinHeight(80);
                    placeholderBox.setMaxHeight(80);
                    placeholderBox.setStyle("-fx-border-color: #cccccc; -fx-background-color: #f8f8e8; -fx-border-radius: 5;");
                    
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
                    System.out.println("V3 - 即將加入購物車: " + product.getName() + " - 單價: $" + product.getPrice());
                });

                category_menu.getChildren().add(btn);
            }
        }
        return category_menu;
    }
}//ProductMenuPane
