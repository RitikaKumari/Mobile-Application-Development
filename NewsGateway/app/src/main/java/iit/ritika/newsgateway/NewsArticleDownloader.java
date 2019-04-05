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
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class NewsArticleDownloader extends AsyncTask<String, Void, String> {

    private NewsService newsService;
    private String key = "9a099cc5e2734222b106851ce5f04608";
    private String url = "https://newsapi.org/v1/articles?";
    private ArrayList<Article> listArticle = new ArrayList<>();
    private String source;

    public NewsArticleDownloader (NewsService nService, String src){
        this.newsService = nService;
        this.source = src;
    }

    @Override
    protected void onPostExecute(String result) {
        parseJSON(result);
        newsService.setArticles(listArticle);
    }


    @Override
    protected String doInBackground(String[] params) {

        Uri.Builder buildURL = Uri.parse(url).buildUpon();
        buildURL.appendQueryParameter("apiKey", key);
        buildURL.appendQueryParameter("source", source);
        String urlToUse = buildURL.build().toString();

        StringBuilder sbuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sbuilder.append(line).append('\n');
            }


        } catch (Exception e) {
            return null;
        }
        return sbuilder.toString();
    }


    private void parseJSON(String s) {
        try {
            JSONObject jsonObj = new JSONObject(s);
            JSONArray jsonArrSrc = jsonObj.getJSONArray("articles");
            for (int i = 0; i < jsonArrSrc.length(); i++) {
                Article article = new Article();
                article.setAuthorName(jsonArrSrc.getJSONObject(i).getString("author").replace("null",""));
                article.setNewsTitle(jsonArrSrc.getJSONObject(i).getString("title").replace("null",""));
                article.setArticleUrl(jsonArrSrc.getJSONObject(i).getString("url"));
                article.setUrlToImage(jsonArrSrc.getJSONObject(i).getString("urlToImage"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                article.setPublishedAt(jsonArrSrc.getJSONObject(i).getString("publishedAt"));
                article.setNewsDescription(jsonArrSrc.getJSONObject(i).getString("description"));
                listArticle.add(article);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
