package of.media.bq.bean;


import android.graphics.Bitmap;

public class Call {
    private String name;
    private String number;
    private int state;
    private String duration;
    private Bitmap photo;

    public Call() {
        this.name = null;
        this.number = null;
        this.state = 0;
        this.duration = null;
        this.photo = null;
    }

    public Call(String name, String number, int state, String duration, Bitmap photo) {
        this.name = name;
        this.number = number;
        this.state = state;
        this.duration = duration;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
