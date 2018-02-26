package xyz.dnglabs.guardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Neew>> {

    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=";
    private static final String GUARDIAN_REQUEST_URL_APPEND =
            "&show-tags=contributor&api-key=test";

    private static final int NEEW_LOADER_ID = 1;

    NeewAdapter mAdapter;
    EditText inputTxt;
    ImageButton searchButton;
    String REQUEST_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputTxt = (EditText) findViewById(R.id.search);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_URL = "";
                REQUEST_URL = GUARDIAN_REQUEST_URL + inputTxt.getText().toString() + GUARDIAN_REQUEST_URL_APPEND;


                ListView neewListView = (ListView) findViewById(R.id.list);
                mAdapter = new NeewAdapter(MainActivity.this, new ArrayList<Neew>());
                neewListView.setAdapter(mAdapter);
                neewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Neew currentNeew = mAdapter.getItem(position);
                        Uri neewUri = Uri.parse(currentNeew.getUrl());
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, neewUri);
                        startActivity(websiteIntent);
                    }
                });

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    mAdapter.clear();
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(NEEW_LOADER_ID, null, MainActivity.this);
                    loaderManager.restartLoader(NEEW_LOADER_ID, null, MainActivity.this);
                } else {
                }
            }
        });

    }

    @Override
    public Loader<List<Neew>> onCreateLoader(int i, Bundle bundle) {
        return new NeewLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Neew>> loader, List<Neew> neews) {

        mAdapter.clear();

        if (neews != null && !neews.isEmpty()) {
            mAdapter.addAll(neews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Neew>> loader) {
        mAdapter.clear();
    }
}
