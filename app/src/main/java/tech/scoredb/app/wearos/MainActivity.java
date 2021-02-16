package tech.scoredb.app.wearos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    protected EditText editTextSearch;
    protected Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = findViewById(R.id.editTextSearch);
        searchButton = findViewById(R.id.searchButton);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchButton.setEnabled(s.length() > 0);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    protected void search() {
        String query = editTextSearch.getText().toString();
        if (!query.isEmpty()) {
            Intent intent = new Intent(this, SearchResultActivity.class);
            intent.putExtra("tech.scoredb.app.wearos.SEARCH_QUERY", query);
            startActivity(intent);
        }
    }
}
