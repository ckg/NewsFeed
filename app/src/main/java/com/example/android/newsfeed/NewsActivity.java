package com.example.android.newsfeed;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    /** URL for News data from the GUARDIAN dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /** Adapter for the list of News */
    private NewsAdapter mAdapter;

    /**
     * Constant value for the News loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int News_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: News Activity onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);


        // Create a new adapter that takes an empty list of News as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected News.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current News that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri NewsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the News URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, NewsUri);
                // or
                // Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                // websiteIntent.setData(NewsUri);

                // Send the intent to launch a new activity
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);
                }
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            Log.i(LOG_TAG, "TEST: calling initLoader() ...");
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(News_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


    }





    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");
        // Create a new loader for the given URL
        //return new NewsLoader(this, GUARDIAN_REQUEST_URL);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String searchTopic = sharedPrefs.getString(
                getString(R.string.settings_search_topic_key),
                getString(R.string.settings_search_topic_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );


        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", searchTopic);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No News found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous News data
        mAdapter.clear();

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsList != null && !newsList.isEmpty()) {
            mAdapter.addAll(newsList);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "TEST: onLoadReset() called ...");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}