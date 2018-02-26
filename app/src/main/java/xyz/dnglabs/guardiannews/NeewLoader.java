package xyz.dnglabs.guardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class NeewLoader extends AsyncTaskLoader<List<Neew>> {


    private String mUrl;

    public NeewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Neew> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Neew> neews = QueryUtils.fetchNeewData(mUrl);
        return neews;
    }
}
