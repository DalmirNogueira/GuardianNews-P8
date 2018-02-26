package xyz.dnglabs.guardiannews;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static List<Neew> fetchNeewData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }

        List<Neew> neews = extractFeatureFromJson(jsonResponse);


        return neews;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Neew> extractFeatureFromJson(String neewJSON) {

        if (TextUtils.isEmpty(neewJSON)) {
            return null;
        }

        List<Neew> neews = new ArrayList<>();
        try {


            JSONObject baseJsonResponse = new JSONObject(neewJSON);
            JSONObject jsonResults = baseJsonResponse.getJSONObject("response");
            JSONArray neewArray = jsonResults.getJSONArray("results");

            for (int i = 0; i < neewArray.length(); i++) {

                JSONObject currentNeew = neewArray.getJSONObject(i);

                String title = currentNeew.getString("webTitle");
                String section = currentNeew.getString("sectionName");
                String date = "";

                if (currentNeew.has("webPublicationDate")){
                    date = currentNeew.getString("webPublicationDate");
                    if (date.length() > 10){
                        date = date.substring(0,10);
                    }
                }else{
                }

                String url = currentNeew.getString("webUrl");

                JSONArray tagsArray = currentNeew.getJSONArray("tags");
                String author = "";
                String authorAll = "";
                if (tagsArray.length() == 0){
                }else {
                    for (int i2 = 0; i2 < tagsArray.length(); i2++) {
                        JSONObject authorObject = tagsArray.getJSONObject(i2);
                        if (i2 == 0 ){
                            author = authorObject.getString("webTitle");
                        }else {
                            authorAll = authorObject.getString("webTitle");
                            author = author + " , " + authorAll;
                        }
                    }
                }

                Neew neew = new Neew(title, section, url, author, date);

                neews.add(neew);
            }

        } catch (JSONException e) {
        }
        return neews;
    }

}

