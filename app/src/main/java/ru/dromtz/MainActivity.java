package ru.dromtz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private GetLinks getLinks = new GetLinks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        ListView lvMain = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = null;
        try {

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    getLinks.getLinks(textView.getText().toString()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        lvMain.setAdapter(adapter);
    }
}
