package beauty.tw.beauty_mother.a6_master_Package;

/**
 * Created by kimtaewoo on 2017-01-19.
 */
public class adapter_master_bookingList {

    String num;
    String user_id;
    String user_name;
    String user_sex;
    String booking_date;
    String booking_time;
    String end_time;
    String create_date;
    String memo;
    String hair_style;

    public adapter_master_bookingList(String num, String user_id, String user_name, String user_sex, String booking_date,
                                      String booking_time, String end_time, String create_date, String memo, String hair_style) {
        this.num = num;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_sex = user_sex;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.end_time = end_time;
        this.create_date = create_date;
        this.memo = memo;
        this.hair_style = hair_style;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getHair_style() {
        return hair_style;
    }

    public void setHair_style(String hair_style) {
        this.hair_style = hair_style;
    }
}
