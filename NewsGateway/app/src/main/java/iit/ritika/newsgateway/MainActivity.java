package iit.ritika.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.support.v7.app.AlertDialog;


public class MainActivity extends AppCompatActivity {

    static final String NEWS_STORY_ACTION = "NEWS_STORY_ACTION";
    static final String SERVICE_ACTION_MSG = "SERVICE_ACTION_MSG";
    static final String DATA_SRC = "DATA_SRC";
    static final String DATA_ART = "DATA_ART";

    private DrawerLayout dLayout;
    private ListView lView;
    private ActionBarDrawerToggle dToggle;
    private NewsReceiver nReceiver;
    private HashMap<String, Source> srcmap = new HashMap<String, Source>();
    private ArrayList<String> srclist = new ArrayList<>();
    private ArrayList<String> ctgrlist = null;
    private ArrayList<Article> artlist = new ArrayList<>();
    private Menu menu;
    private MyPageAdapter pageAdapt;
    private List<Fragment> lFragments;
    private ViewPager vPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        nReceiver = new NewsReceiver();
        IntentFilter filter1 = new IntentFilter("NEWS_STORY_ACTION");
        registerReceiver(nReceiver, filter1);

        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lView = (ListView) findViewById(R.id.left_drawer);
        lView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, srclist));
        lView.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                    }
                }
        );

        dToggle = new ActionBarDrawerToggle(
                this,
                dLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (networkCheck()) {
            if (savedInstanceState != null) {
                setTitle(savedInstanceState.getCharSequence("title"));
                setSources((ArrayList<Source>) savedInstanceState.getSerializable("sourcelist"), savedInstanceState.getStringArrayList("ctgrlist"));
            } else {
                NewsSourcesDownloader nsdl = new NewsSourcesDownloader(this, "");
                nsdl.execute();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No network connection");
            builder.setMessage("News cannot be loaded without a network connection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        lFragments = getlFragments();
        pageAdapt = new MyPageAdapter(getSupportFragmentManager());
        vPager = (ViewPager) findViewById(R.id.viewpager);
        vPager.setAdapter(pageAdapt);
        vPager.setOffscreenPageLimit(10);
        if (savedInstanceState != null) {
            for (int i = 0; i < savedInstanceState.getInt("size"); i++) {
                lFragments.add(getSupportFragmentManager().getFragment(savedInstanceState, "NewsFragment" + Integer.toString(i)));
            }
        } else {
            vPager.setBackgroundResource(R.drawable.img_bgnews);
        }
        pageAdapt.notifyDataSetChanged();

    }

    public void alertbox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No network connection");
        builder.setMessage("News cannot be loaded without a network connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (dToggle.onOptionsItemSelected(item)) {
            return true;
        }

        NewsSourcesDownloader nsdl = new NewsSourcesDownloader(this, "" + item);
        nsdl.execute();
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Toast.makeText(this, srclist.get(position), Toast.LENGTH_SHORT).show();
        vPager.setBackground(null);
        setTitle(srclist.get(position));
        Intent intent = new Intent(SERVICE_ACTION_MSG);
        intent.putExtra(DATA_SRC, srcmap.get(srclist.get(position)));
        sendBroadcast(intent);
        dLayout.closeDrawer(lView);
    }

    public void setSources(ArrayList<Source> sourcelist, ArrayList<String> categorylist) {

        srcmap.clear();
        srclist.clear();
        Collections.sort(sourcelist);
        for (Source s : sourcelist) {
            srclist.add(s.getName());
            srcmap.put(s.getName(), s);
        }
        ((ArrayAdapter<String>) lView.getAdapter()).notifyDataSetChanged();

        if (this.ctgrlist == null) {
            this.ctgrlist = new ArrayList<>(categorylist);
            if (menu != null) {
                this.ctgrlist.add(0, "all");
                for (String c : this.ctgrlist) {
                    menu.add(c);
                }
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.ctgrlist != null) {
            menu.clear();
            for (String c : this.ctgrlist) {
                menu.add(c);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private List<Fragment> getlFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return lFragments.get(position);
        }

        @Override
        public int getCount() {
            return lFragments.size();
        }

        @Override
        public long getItemId(int position) {
            return baseId + position;
        }

        public void notifyChangeInPosition(int n) {
            baseId += getCount() + n;
        }

    }

    public class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "NEWS_STORY_ACTION":
                    if (intent.hasExtra(DATA_ART)) {
                        artlist = (ArrayList) intent.getSerializableExtra(DATA_ART);
                        reDoFragments(artlist);
                    }
                    break;
            }
        }



        private void reDoFragments(List<Article> al) {

           if(!networkCheck())
           {
             alertbox();
           }

            for (int i = 0; i < pageAdapt.getCount(); i++)
                pageAdapt.notifyChangeInPosition(i);

            lFragments.clear();

            for (int i = 0; i < al.size(); i++) {
                lFragments.add(NewsFragment.newInstance(al.get(i), "" + i, "" + al.size()));
            }

            pageAdapt.notifyDataSetChanged();
            vPager.setCurrentItem(0);
        }

        private void unDoFragments(List<Article> al) {

            for (int i = 0; i < pageAdapt.getCount(); i++)
                pageAdapt.notifyChangeInPosition(i);

            lFragments.clear();

            for (int i = 0; i < al.size(); i++) {
                lFragments.add(NewsFragment.newInstance(al.get(i), "" + i, "" + al.size()));
            }

            pageAdapt.notifyDataSetChanged();
            vPager.setCurrentItem(0);
        }

    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(nReceiver);
        super.onDestroy();
    }

    public boolean networkCheck() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        int compteur = 0;
        for (int i = 0; i < lFragments.size(); i++) {
            if (lFragments.get(i).isAdded()) {
                compteur++;
                String temp = "NewsFragment" + Integer.toString(i);
                getSupportFragmentManager()
                        .putFragment(savedInstanceState, temp, lFragments.get(i));
            }

        }
        savedInstanceState.putInt("size", compteur);

        savedInstanceState.putStringArrayList("ctgrlist", ctgrlist);

        ArrayList<Source> temp = new ArrayList<>();
        for (String key : srcmap.keySet()) {
            temp.add(srcmap.get(key));
        }
        savedInstanceState.putSerializable("sourcelist", temp);

        savedInstanceState.putCharSequence("title", getTitle());
    }

    private class MyPageAdapter1 extends FragmentPagerAdapter {
        private long baseId = 0;

        public MyPageAdapter1(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return lFragments.get(position);
        }

        @Override
        public int getCount() {
            return lFragments.size();
        }

        @Override
        public long getItemId(int position) {
            return baseId + position;
        }

        public void notifyChangeInPosition(int n) {
            baseId += getCount() + n;
        }


    }
}