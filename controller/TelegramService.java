package UserManagementConsoleSystem.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramService {

    private static final String BOT_TOKEN = "7703566154:AAHz73o_mXRSCkknUnEnlkl4nec3lQcs-U8";
    private static final String CHAT_ID = "748861779";

    public boolean sendNotification(String message) {
        try {
            String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
            String parameters = "chat_id=" + CHAT_ID + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

            URL obj = new URL(url + "?" + parameters);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                return true;
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Error: " + response.toString());
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
