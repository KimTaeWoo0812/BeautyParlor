package com.beautyparlor.a2_core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class FCM_Magager {
    private final String url = "https://fcm.googleapis.com/fcm/send";
    private String user_id;
    private String title;
    private String message;
    private String isMaster;

    public FCM_Magager() {
    }
    
    public FCM_Magager(String user_id, String title, String message, String isMaster) {
        this.user_id = user_id;
        this.title = title;
        this.message = message;
        this.isMaster = isMaster;
    }

    public boolean Send_FCM() {
        String line = null;
        try {
            URL object = new URL(url);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization",
                    "key=AAAAKzHekBg:APA91bEjHDFR26MNmp_hwCpAzu-NOmJynXS4v3rp2Lpjf6yktnW7_b27Pv5gln2vbPIeLMBdK0GHqwGtLHCXYmB6D9b9CoCYHSxSA-n3-MpCvmr2mKflPxgd3OXtOGP67gGbHK6SA9Ob");

            con.setRequestMethod("POST");

            JsonObject params = new JsonObject();
            // params.addProperty("to",
            // "cfELvUY7frU:APA91bHsNjLH8KmRarz9TwpSjZvMV-yMy-16jbs2dpWrRfOecE3ndI3vZyK4up1430q3OGktTxArowDm6c6MT0tvinmLcL8gMTaGXguTnUSQ0lbEUUCBBxvxga65Gsz0dolV15r3A0fV");
            params.addProperty("to", user_id);

            JsonObject params2 = new JsonObject();

            params2.addProperty("title", title);
            params2.addProperty("messageBody", message);
            params2.addProperty("isMaster", isMaster);

            JsonObject notification = new JsonObject();
            params.add("data", params2);

            System.out.println("send data: " + params.toString());
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(params.toString());
            wr.flush();

            // display what returns the POST request
            String sb = null;

            int HttpResult = con.getResponseCode();
            System.out.println("HttpResult: " + HttpResult);

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            line = br.readLine();
            br.close();
            System.out.println(line);
            Gson gson = new Gson();
            gson.toJson(line);
            
            return gson.fromJson("success", String.class).equals("1") ? true : false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

        }
        return false;
    }

}
