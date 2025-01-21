package UserManagementConsoleSystem.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramService {

    private static final String BOT_TOKEN = "7703566154:AAHz73o_mXRSCkknUnEnlkl4nec3lQcs-U8";
    private static final String CHAT_ID = "748861779";
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

    public boolean sendNotification(String message) {
        try {
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            URL url = new URL(TELEGRAM_API_URL + "?chat_id=" + CHAT_ID + "&text=" + encodedMessage);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
