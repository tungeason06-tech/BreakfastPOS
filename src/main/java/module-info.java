module mypos {

    requires javafx.controls;
    requires java.sql;

    exports db;

    exports mypos;
    exports data_type;
    exports tutorial_app_product_menu;
    exports tutorial_app_order_entry;
    exports tutorial_app_product_maintenance;
    exports app_order_entry_system;
    exports app_main;
}
