package entity;

public class School {

    private String id;
    private String title;
    private String address;
    private String tel;

    @Override
    public String toString() {
        return "{" + this.id + "," + this.title + "," + this.address + "," + this.tel + "}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
