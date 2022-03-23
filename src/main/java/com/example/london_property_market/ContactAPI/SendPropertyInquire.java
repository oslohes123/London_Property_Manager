package com.example.london_property_market.ContactAPI;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SendPropertyInquire {

    private final static String SENDER_URL = "https://yousef-altaher.com/special/informationReciever.php";

    //https://www.edureka.co/community/6308/java-sending-http-parameters-via-post-method-easily
    public static boolean sendContact(String email, String name, String property){

        try {
            URL urlConnection = new URL(SENDER_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
            String urlParameters  = "email="+email+"&name="+ name +"&property="+ property;

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            httpURLConnection.setUseCaches(false);

            try (DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream())) {
                wr.write(postData);
            }

        } catch (IOException e) {
            return false;
        }

        return true;

    }

}
