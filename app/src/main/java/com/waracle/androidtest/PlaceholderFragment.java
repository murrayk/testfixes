package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment is responsible for loading in some JSON and
 * then displaying a list of cakes with images.
 * Fix any crashes
 * Improve any performance issues
 * Use good coding practices to make code more secure
 */
public class PlaceholderFragment extends ListFragment {


    private static final String JSON_TITLE_KEY = "title";
    private static final String JSON_DESC_KEY = "desc";
    private static final String JSON_IMAGE_KEY = "image";

    private static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private static final String TAG = PlaceholderFragment.class.getSimpleName();


    public PlaceholderFragment() { /**/ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        LoadDataIntoAdapter loadDataIntoAdapter = new LoadDataIntoAdapter();
        loadDataIntoAdapter.execute();

    }


    /**
     * Returns the charset specified in the Content-Type of this header,
     * or the HTTP default (ISO-8859-1) if none can be found.
     */
    public static String parseCharset(String contentType) {
        if (contentType != null) {
            String[] params = contentType.split(",");
            for (int i = 1; i < params.length; i++) {
                String[] pair = params[i].trim().split("=");
                if (pair.length == 2) {
                    if (pair[0].equals("charset")) {
                        return pair[1];
                    }
                }
            }
        }
        return "UTF-8";
    }

    static class ViewHolder {

        TextView title;
        TextView desc;
        ImageView imageView;

    }

    private class MyAdapter extends BaseAdapter {

        // Can you think of a better way to represent these items???
        private List<Recipe> mItems;


        public MyAdapter(List<Recipe> items) {
            mItems = items;

        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Recipe getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if(rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View root = inflater.inflate(R.layout.list_item_layout, parent, false);
                if (root != null) {
                    ViewHolder viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) root.findViewById(R.id.title);
                    viewHolder.desc = (TextView) root.findViewById(R.id.desc);
                    viewHolder.imageView = (ImageView) root.findViewById(R.id.image);
                    rowView.setTag(viewHolder);


                }
            }
            ViewHolder holder = (ViewHolder) rowView.getTag();
            Recipe recipe = getItem(position);
            holder.title.setText(recipe.getTitle());
            holder.desc.setText(recipe.getDesc());
            ImageWorkerTask imageWorkerTask = new ImageWorkerTask(holder.imageView, recipe.getImageUrl());
            imageWorkerTask.execute();

            return rowView;
        }


    }

    /********************
     * Async Tasks
     ********************************/

    private class ImageWorkerTask extends AsyncTask<Void, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private String imageUrl;
        private ImageLoader imageLoader = new ImageLoader();

        public ImageWorkerTask(ImageView imageView, String imageUrl) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.imageUrl = imageUrl;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Void... params) {

            return imageLoader.load(imageUrl);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private class LoadDataIntoAdapter extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(JSON_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Can you think of a way to improve the performance of loading data
                // using HTTP headers???
                //get content length to set up buffer size to use in streamutils?

                // Also, Do you trust any utils thrown your way????

                byte[] bytes = StreamUtils.readUnknownFully(in);

                // Read in charset of HTTP content.
                String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

                // Convert byte array to appropriate encoded string.
                String jsonText = new String(bytes, charset);

                // Read string as JSON.
                return new JSONArray(jsonText);

            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            throw new IllegalStateException();

        }

        @Override
        protected void onPostExecute(JSONArray array) {

            setListAdapter(new MyAdapter(constructRecipeList(array)));

        }

        private List<Recipe> constructRecipeList(JSONArray array) {

            List<Recipe> recipeList = new ArrayList<>();
            try {
                for (int i = 0; i < array.length(); i++) {

                    JSONObject row = array.getJSONObject(i);
                    String title = row.getString(JSON_TITLE_KEY);
                    String desc = row.getString(JSON_DESC_KEY);
                    String image = row.getString(JSON_IMAGE_KEY);
                    recipeList.add(new Recipe(title, desc, image));

                }
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            return recipeList;
        }
    }

}