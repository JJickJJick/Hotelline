package site.fieldmaus.hotelline;

public class RoomItem {
    String name;
    String limitcnt;

    public RoomItem(String name, String limitcnt) {
        this.name = name;
        this.limitcnt = limitcnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLimitcnt() {
        return limitcnt;
    }

    public void setLimitcnt(String limitcnt) {
        this.limitcnt = limitcnt;
    }
}
