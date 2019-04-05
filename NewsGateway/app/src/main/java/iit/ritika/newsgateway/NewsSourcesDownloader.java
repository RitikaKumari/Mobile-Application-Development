package iit.ritika.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsSourcesDownloader extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private String key = "9a099cc5e2734222b106851ce5f04608";
    private String url = "https://newsapi.org/v1/sources?";
    private ArrayList<Source> srclist = new ArrayList<>();
    private ArrayList<String> ctgrlist = new ArrayList<>();
    private String category;

    public NewsSourcesDownloader (MainActivity mact, String category){
        mainActivity = mact;
        if(category.equalsIgnoreCase("all") || category.isEmpty()){
            this.category = "";
        } else {
            this.category = category;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        parseJSON(s);
        mainActivity.setSources(srclist, ctgrlist);
    }

    @Override
    protected String doInBackground(String... params) {

        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        buildURL.appendQueryParameter("language", "en");
        buildURL.appendQueryParameter("country", "us");
        buildURL.appendQueryParameter("apiKey", key);
        buildURL.appendQueryParameter("category", category);
        String urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            return null;
        }
        return sb.toString();
    }


    private void parseJSON(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArrSrc = jsonObject.getJSONArray("sources");
            for (int i = 0; i < jsonArrSrc.length(); i++) {
                Source source = new Source();
                source.setId(jsonArrSrc.getJSONObject(i).getString("id"));
                source.setName(jsonArrSrc.getJSONObject(i).getString("name"));
                source.setUrl(jsonArrSrc.getJSONObject(i).getString("url"));
                source.setCategory(jsonArrSrc.getJSONObject(i).getString("category"));
                srclist.add(source);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i< srclist.size(); i++){
            if( !ctgrlist.contains(srclist.get(i).getCategory()) ){
                ctgrlist.add(srclist.get(i).getCategory());
            }
        }

    }
}
