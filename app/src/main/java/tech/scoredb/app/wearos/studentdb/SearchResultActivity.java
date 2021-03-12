package tech.scoredb.app.wearos.studentdb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NavUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import tech.scoredb.app.wearos.BuildConfig;
import tech.scoredb.app.wearos.R;

public class SearchResultActivity extends Activity {

    LinearLayout layout;
    View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentdb_activity_search_result);

        layout = findViewById(R.id.layout);
        loading = findViewById(R.id.loading);

        Intent intent = getIntent();
        String query = intent.getStringExtra("tech.scoredb.app.wearos.studentdb.SEARCH_QUERY");
        if (query == null || query.isEmpty()) {
            NavUtils.navigateUpFromSameTask(this);
        } else {
            performSearch(query);
        }
    }

    protected void performSearch(String query) {
        loading.setVisibility(View.VISIBLE);

        String encodedQuery;
        try {
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedQuery = query;
        }
        final String url = "https://api.scoredb.tech/studentdb/search" +
                "?query=" + encodedQuery +
                "&page=1&page_size=20";
        final String token = BuildConfig.SCOREDB_TOKEN;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("data");
                            renderResult(result);
                        } catch (JSONException e) {
                            Log.e("SearchResultActivity", "Search failed", e);
                            searchFailed(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        searchFailed(e);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(request);
    }

    protected void renderResult(JSONArray result) throws JSONException {
        loading.setVisibility(View.GONE);

        if (result == null) {
            searchFailed(new NullPointerException("No search result available."));
            return;
        }

        int count = result.length();
        for (int i = 0; i < count; i++) {
            JSONObject student = result.getJSONObject(i);

            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setText(student.getString("name"));

            layout.addView(button);
        }

        if (count == 0) {
            TextView noMatchText = new TextView(this);
            noMatchText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            noMatchText.setTextSize(16);
            noMatchText.setGravity(Gravity.CENTER_HORIZONTAL);
            noMatchText.setText(R.string.no_matching_result);

            layout.addView(noMatchText);
        }
    }

    protected void searchFailed(Exception e) {
        Log.e("SearchResultActivity", "Search failed", e);
        Context ctx = this.getApplicationContext();
        Toast toast = Toast.makeText(ctx, R.string.request_failed, Toast.LENGTH_LONG);
        toast.show();
        NavUtils.navigateUpFromSameTask(this);
    }
}
