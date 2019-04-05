package iit.ritika.newsgateway;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import 	java.text.SimpleDateFormat;
import java.util.Date;
import com.squareup.picasso.Picasso;


public class NewsFragment extends Fragment implements View.OnClickListener {

    public static final String DATA_ARTICLE_FRAGMENT = "DATA_ARTICLE_FRAGMENT";
    public static final String DATA_INDICE = "DATA_INDICE";
    public static final String DATA_SUM = "DATA_SUM";
    private Article article;
    ImageView article_image;

    public static final NewsFragment newInstance(Article article, String indice, String total)
    {
        NewsFragment frag1 = new NewsFragment();
        Bundle bundle1 = new Bundle(1);
        bundle1.putSerializable(DATA_ARTICLE_FRAGMENT, article);
        bundle1.putString(DATA_INDICE, indice);
        bundle1.putString(DATA_SUM, total);
        frag1.setArguments(bundle1);
        return frag1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.news_fragment, container, false);
        article =  (Article) getArguments().getSerializable(DATA_ARTICLE_FRAGMENT);
        String pos = getArguments().getString(DATA_INDICE);
        String sum = getArguments().getString(DATA_SUM);

        TextView article_title = (TextView) view1.findViewById(R.id.article_title);
        TextView article_author = (TextView) view1.findViewById(R.id.article_author);
        TextView article_date = (TextView) view1.findViewById(R.id.article_date);
        TextView article_preview = (TextView) view1.findViewById(R.id.article_preview);
        TextView article_count = (TextView) view1.findViewById(R.id.article_count);
        article_image = (ImageView) view1.findViewById(R.id.article_image);

        article_title.setOnClickListener(this);
        article_preview.setOnClickListener(this);
        article_image.setOnClickListener(this);

        article_title.setText(article.getNewsTitle().replace("null",""));
        article_title.setText(article.getNewsTitle().replace("null",""));
        article_author.setText(article.getAuthorName().replace("null",""));
        article_preview.setText(article.getNewsDescription().replace("null",""));
        article_count.setText(Integer.parseInt(pos)+1 + " of " + sum);

        if(article.getPublishedAt()!= null) {
            DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat("MMM dd,yyyy hh:mmaa");
            toFormat.setLenient(false);
            String str1 = article.getPublishedAt();
            Date date;

            int count = 0;
            int maxAttempts = 2;
            boolean val = false;
            while(!val) {
                try {
                    date = fromFormat.parse(str1);
                    article_date.setText(toFormat.format(date));
                    val = true;
                } catch (ParseException e) {
                    fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
                    if (++count == maxAttempts) {
                        val =true;
                        article_date.setText("");
                    }
                }
            }

        }
        System.out.println("length :" +article.getUrlToImage().length());
        if(article.getUrlToImage().length()>0)
        loadPhoto(article.getUrlToImage(), view1);
        else
            loadPhoto("null", view1);

        return view1;
    }

    private void loadPhoto(String url, View v){

        Picasso picasso = new Picasso.Builder(this.getContext())
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        picasso.load(R.drawable.brokenimage)
                                .into(article_image);
                    }
                })
                .build();

        picasso.load(url)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(article_image);
    }
    private void downloadPhoto(String url, View v){

        Picasso picasso = new Picasso.Builder(this.getContext())
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        picasso.load(R.drawable.brokenimage)
                                .into(article_image);
                    }
                })
                .build();

        picasso.load(url)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(article_image);
    }

    @Override
    public void onClick(View v) {
        String url = article.getArticleUrl();
        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        intent1.setData(Uri.parse(url));
        startActivity(intent1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}