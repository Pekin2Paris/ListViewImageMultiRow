package ye.tian.listviewimagemultirow;

/**
 * Created by Ye Tian on 29/05/2015.
 */
public class SearchResults {

    private String name = "";
    private String cityState = "";
    private String phone = "";
    private int imageNumber = 0;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getCityState() {
        return cityState;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }
    public int getImageNumber() {
        return imageNumber;
    }
}