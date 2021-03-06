package of.media.bq.bean;

import android.graphics.Bitmap;

public class CallLog {
    private String name;
    private String number;
    private String timestamp;
    private String duration;
    private int type;
    private Bitmap photo;

    public CallLog() {
        this.name = null;
        this.number = null;
        this.timestamp = null;
        this.duration = null;
        this.type = 0;
        this.photo = null;
    }

    public CallLog(String name, String number, String timestamp, String duration, int type, Bitmap photo) {
        this.name = name;
        this.number = number;
        this.timestamp = timestamp;
        this.duration = duration;
        this.type = type;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", timestamp='" + timestamp + '\'' +
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
