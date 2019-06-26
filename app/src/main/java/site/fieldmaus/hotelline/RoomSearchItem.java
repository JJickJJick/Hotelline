package site.fieldmaus.hotelline;

public class RoomSearchItem {
    String name;
    String limit_cnt;
    String value;

    public RoomSearchItem(String name, String limit_cnt, String value) {
        this.name = name;
        this.limit_cnt = limit_cnt;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnt() {
        return limit_cnt;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLimitcnt(String limit_cnt) {
        this.limit_cnt = limit_cnt;
    }
}
