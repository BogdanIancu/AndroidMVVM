package ro.facemsoft.coolweather.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloadingService {
    public Bitmap downloadImage(String icon) throws Exception {
        String address = String.format("https://openweathermap.org/img/w/%s.png", icon);
        URL url = new URL(address);
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        InputStream inputStream = connection.getInputStream();
        Bitmap image = BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        return image;
    }
}
