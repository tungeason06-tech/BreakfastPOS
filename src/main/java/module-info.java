module mypos {
    requires javafx.controls; //JavaFX controls模組
    
    exports mypos; //
    exports data_type; //開放data_type目錄，因為JavaFX要操作到OrderDetailEntry
    exports tutorial_app_product_menu;  //菜單選擇套件目錄
    exports tutorial_app_order_entry;  //訂單輸入套件目錄
    exports tutorial_app_product_maintenance;  //開放產品維護管理目錄
    exports app_order_entry_system;
    exports app_main;
}
