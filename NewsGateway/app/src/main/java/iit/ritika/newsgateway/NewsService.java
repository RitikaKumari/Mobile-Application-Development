package iit.ritika.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.os.IBinder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.Serializable;
import java.util.ArrayList;
import static iit.ritika.newsgateway.MainActivity.SERVICE_ACTION_MSG;
import static iit.ritika.newsgateway.MainActivity.NEWS_STORY_ACTION;
import static iit.ritika.newsgateway.MainActivity.DATA_ART;
import static iit.ritika.newsgateway.MainActivity.DATA_SRC;

public class NewsService extends Service {

    private boolean executing = true;
    private ServiceReceiver sReceiver;
    private ArrayList<Article> artlist = new ArrayList<>();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sReceiver = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(SERVICE_ACTION_MSG);
        registerReceiver(sReceiver, filter1);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(sReceiver);
        executing = false;
        super.onDestroy();
    }

    public void setArticles(ArrayList<Article> list) {
        artlist.clear();
        artlist = new ArrayList<>(list);
        sendIntent();
    }

    private void sendIntent(){
        Intent intent = new Intent(NEWS_STORY_ACTION);
        intent.putExtra(DATA_ART, (Serializable) artlist);
        sendBroadcast(intent);
        artlist.clear();
    }

    public class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case SERVICE_ACTION_MSG:
                    if (intent.hasExtra(DATA_SRC)) {
                        Source src = (Source) intent.getSerializableExtra(DATA_SRC);
                        NewsArticleDownloader newsartdownloader = new NewsArticleDownloader(NewsService.this, "" + src.getId());
                        newsartdownloader.execute();
                    }
                    break;
            }
        }
    }

}
