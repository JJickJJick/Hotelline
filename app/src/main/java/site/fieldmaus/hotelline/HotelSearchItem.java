package site.fieldmaus.hotelline;

public class HotelSearchItem {
    String name;
    String value;
    String score;

    public HotelSearchItem(String name, String value, String score) {
        this.name = name;
        this.value = value;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
