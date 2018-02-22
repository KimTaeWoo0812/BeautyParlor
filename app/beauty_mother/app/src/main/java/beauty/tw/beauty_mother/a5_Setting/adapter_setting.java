package beauty.tw.beauty_mother.a5_Setting;

/**
 * Created by kimtaewoo on 2017-01-19.
 */
public class adapter_setting {
    public String num;
    public String title;
    public String style;
    public String memo;
    public String date;


    public adapter_setting(String num_, String title_, String style_, String memo_, String date_){
        this.num = num_;
        this.title = title_;
        this.style = style_;
        this.memo = memo_;
        this.date = date_;
    }


    public String getNum(){
        return num;
    }
    public String gettitle(){
        return title;
    }
    public String getStyle(){
        return style;
    }
    public String getMemo(){
        return memo;
    }
    public String getDate(){
        return date;
    }

}
