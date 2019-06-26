package site.fieldmaus.hotelline;

public class HotelItem {
    String name;
    String local;
    String score;

    public HotelItem(String name, String local, String score) {
        this.name = name;
        this.local = local;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
