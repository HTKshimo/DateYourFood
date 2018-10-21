package happyeater.com.dateyourfood;

public class Food
{
    private String name;

    private String expiry_date;

    private int days_left;

    private int ID;

    public Food() {

    }

    public Food(int ID, String name, String expiry_date, int days_left) {
        this.ID = ID;
        this.name = name;
        this.expiry_date = expiry_date;
        this.days_left = days_left;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getExpiry_date() {
        return this.expiry_date;
    }

    public void setDays_left(int days_left) {
        this.days_left = days_left;
    }

    public int getDays_left() {
        return this.days_left;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

}
