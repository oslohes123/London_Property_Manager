package com.example.london_property_market.ContactAPI;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This class represent the connection point between the java program and the server side.
 *
 * @author Yousef Altaher, K20047484
 * @version 25-03-2022
 */
public class SendPropertyInquire {

    // The API that stores the contact of the users.
    private final static String SENDER_URL = "https://yousef-altaher.com/special/informationReciever.php";

    /**
     * This method contacts the server side of the program with the user's information to perform the functionality
     * of contacting
     *
     * Reference: https://www.edureka.co/community/6308/java-sending-http-parameters-via-post-method-easily,
     * https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
     * Author: Rishabh Pathak, date: Jun 7, 2018. Author: Imaskar, May 4, 2018
     * @param email user's email
     * @param name user's name
     * @param property user's selected property
     * @return true if the request was successfully uploaded, false otherwise.
     */
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

            // Necessary to work
            httpURLConnection.getResponseCode();

        } catch (IOException e) {
            return false;
        }

        return true;

    }

}
