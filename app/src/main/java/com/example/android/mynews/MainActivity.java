package com.example.android.mynews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MyNews>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    Uri.Builder builder = new Uri.Builder();
    private  MyNewsAdapter adapter;
    private TextView mEmptyStateTextView;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);
        adapter = new MyNewsAdapter(this,new ArrayList<MyNews>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MyNews currentNews =  adapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        
        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.connection);
        }

    }

    @NonNull
    @Override
    public Loader<List<MyNews>> onCreateLoader(int i, @Nullable Bundle bundle) {
        builder.scheme("https");
        builder.authority("content.guardianapis.com");
        builder.appendPath("search");
        builder.appendQueryParameter("q","news");
        builder.appendQueryParameter("api-key","test");
        builder.appendQueryParameter("show-tags","contributor");
        String myUrl = builder.toString();
        return new NewsLoader(this,myUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MyNews>> loader, List<MyNews> news) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_news);
        adapter.clear();
        if (news != null && !news.isEmpty()){
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MyNews>> loader) {
        adapter.clear();
    }

    public static class NewsLoader extends AsyncTaskLoader<List<MyNews>> {

        private String mUrl;
        public NewsLoader(@NonNull Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Nullable
        @Override
        public List<MyNews> loadInBackground() {
            if(mUrl == null){
                return null;
            }
            List<MyNews> newsList = QueryUtils.fetchData(mUrl);
            return newsList;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }
}
