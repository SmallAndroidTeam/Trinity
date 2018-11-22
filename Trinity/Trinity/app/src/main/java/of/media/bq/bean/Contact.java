package of.media.bq.bean;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Contact {

    private String name;
    private Map<Integer, String> numberMap;
    private Bitmap photo;

    public Contact() {
        this.name = null;
        this.numberMap = null;
        this.photo = null;
    }

    public Contact(String name, Map numberMap, Bitmap photo) {
        this.name = name;
        this.numberMap = numberMap;
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", numberList='" + numberMap.toString() + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        String number = getMobileNumber();
        if (number == null) {
            number = getAnyNumber();
        }
        return (number == null)?"":number;
    }

    public String getAnyNumber() {
        String number = null;
        for (Integer type : numberMap.keySet()) {
            number = numberMap.get(type);
            break;
        }
        return number;
    }

    public String getMobileNumber() {
        String number = null;
        for (Integer type : numberMap.keySet()) {
            if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                number = numberMap.get(type);
            }
        }
        return number;
    }

    public List<String> getNumberList() {
        List<String> numberList= new ArrayList<>();
        for (Integer type : numberMap.keySet()) {
            numberList.add(numberMap.get(type));
        }
        return numberList;
    }

    public Map getNumberMap() {
        return numberMap;
    }

    public void setNumberMap(Map numberMap) {
        this.numberMap = numberMap;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
