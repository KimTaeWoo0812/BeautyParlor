package beauty.tw.beauty_mother.a2_Roading_Join;

/**
 * Created by kimtaewoo on 2017-01-15.
 */
public class adapter_Seach_beauty {
    public String num			= "";
    public String title		= "";
    public String loc		= "";


    public adapter_Seach_beauty(String num, String title, String loc){
        this.num = num;
        this.title = title;
        this.loc = loc;
    }
    public String getLoc() {
        return loc;
    }

    public String getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }
}
