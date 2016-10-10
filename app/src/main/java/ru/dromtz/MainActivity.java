package ru.dromtz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;
    private SharedPreferences preferences;
    private Resources res = this.getResources();

    private List<String> linkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        button = (Button) findViewById(R.id.button);
        button.setText(res.getString(R.string.button));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyIfOffline();
                adapter.clear();

                GetLinksAsync getLinksAsync = new GetLinksAsync(adapter, linkList);
                getLinksAsync.execute(editText.getText().toString());

                button.setEnabled(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(((TextView) view).getText());
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    setButtonState(s.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    setButtonState(s.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    setButtonState(s.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        loadLinkList(editText, adapter);
    }

    class GetLinksAsync extends AsyncTask<String, Void, List<String>> {

        private List<String> links;
        private ArrayAdapter<String> adapter;

        public GetLinksAsync(ArrayAdapter<String> adapter, List<String> stringList) {
            this.adapter = adapter;
            this.links = stringList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                links = new GetLinks().getLinks(params[0]);
                return links;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            adapter.addAll(links);
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
            try {
                saveLinkList(links);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveLinkList(List<String> linkList) throws ExecutionException, InterruptedException {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EDIT_TEXT", editText.getText().toString());
        editor.putStringSet("LINKS", new HashSet<>(linkList));
        editor.commit();
        editor.apply();
    }

    private void loadLinkList(EditText editText, ArrayAdapter<String> adapter) {
        preferences = getPreferences(MODE_PRIVATE);
        editText.setText(preferences.getString("EDIT_TEXT", "http://ya.ru"));
        adapter.addAll(preferences.getStringSet("LINKS", new HashSet<String>()));
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void notifyIfOffline() {
        if (!isOnline()) {
            Toast.makeText(this, "No connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void setButtonState(String url) throws MalformedURLException {
        if (validateUrl(url)) {
            button.setEnabled(true);
        } else {
            button.setEnabled(false);
        }
    }

    private boolean validateUrl(String url) throws MalformedURLException {
        return URLUtil.isValidUrl(url);
    }
}
