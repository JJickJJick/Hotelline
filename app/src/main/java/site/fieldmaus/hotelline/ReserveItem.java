package site.fieldmaus.hotelline;

public class ReserveItem {
    String product_type;
    String reserve_date;
    String product_name;
    String product_company;
    String product_value;
    String product_score;

    public ReserveItem(String product_type, String reserve_date, String product_name, String product_company, String product_value, String product_score) {
        this.product_type = product_type;
        this.reserve_date = reserve_date;
        this.product_name = product_name;
        this.product_company = product_company;
        this.product_value = product_value;
        this.product_score = product_score;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public void setReserve_date(String reserve_date) {
        this.reserve_date = reserve_date;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_company(String product_company) {
        this.product_company = product_company;
    }

    public void setProduct_value(String product_value) {
        this.product_value = product_value;
    }

    public void setProduct_score(String product_score) {
        this.product_score = product_score;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getReserve_date() {
        return reserve_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_company() {
        return product_company;
    }

    public String getProduct_value() {
        return product_value;
    }

    public String getProduct_score() {
        return product_score;
    }
}
