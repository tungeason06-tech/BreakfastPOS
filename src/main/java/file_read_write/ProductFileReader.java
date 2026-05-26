package file_read_write;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import data_type.Product;

public class ProductFileReader {

    //dictionary  (key, value)
    //Map --> dict  {'key':'value'}
    public static Map<String, Product> readProduct() {
        //從檔案或資料庫讀入產品菜單資訊
        Scanner input;
        //放所有產品  產品編號  產品物件Product
        Map<String, Product> productMap = new TreeMap<>();
        //讀取產品資料，放入字典變數productMap中 置放在根目錄下的 mydatasets/products.csv
        try {
            input = new Scanner(new File("mydatasets/products.csv"), "utf-8");
            while (input.hasNextLine()) {
                String[] item = input.nextLine().split(",");
                //System.out.println(row);
                Product product = new Product(
                        item[0],
                        item[1],
                        item[2],
                        (int) Double.parseDouble(item[3]), //價格轉為int (支援小數點字串如 "70.0")
                        item[4],
                        item[5]);
                //將這一筆放入字典變數productMap中 
                productMap.put(product.getProductId(), product);
            }
            input.close();
        } catch (Exception ex) {
            System.out.println("檔案讀取錯誤!");
            System.out.println(ex);
        }
        return productMap;
    }

    //讀取產品類別
    public static String[] readCategories() {
        Map<String, Product> products = readProduct();
        // Use a TreeSet to automatically store unique categories in sorted order
        TreeSet<String> uniqueCategories = new TreeSet<>();

        for (Product product : products.values()) {
            uniqueCategories.add(product.getCategory());
        }

        // Convert the set to a string array
        String[] categoryArray = new String[uniqueCategories.size()];
        return uniqueCategories.toArray(categoryArray);
    }

    public static void main(String[] args) {
        System.out.println(ProductFileReader.readProduct());
        System.out.println(Arrays.toString(ProductFileReader.readCategories()));
    }

    //從檔案讀取產品資料，以列表形式回傳，供 AppProductMaintenance 顯示在 TableView 中
    public static List<Product> readProductList() {
        Map<String, Product> productMap = readProduct();
        return new ArrayList<>(productMap.values());
    }

}//ProductFileReader