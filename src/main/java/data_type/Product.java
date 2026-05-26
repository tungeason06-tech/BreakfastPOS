package data_type;

public class Product {

    private String productId;
    private String category;
    private String name;
    private int price;
    private String imgUrl;

    private String description;

    public Product(String productId, String category, String name, int price, String imgUrl, String description) {
        this.productId = productId;
        this.category = category;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.description = description;
    }

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Product [id=" + productId + ", name=" + name + ", price=" + price + "]";
    }

}
