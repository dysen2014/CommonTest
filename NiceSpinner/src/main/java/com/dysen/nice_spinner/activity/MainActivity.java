package com.dysen.nice_spinner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dysen.nice_spinner.R;
import com.dysen.nice_spinner.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intView();
    }

    private void intView() {
        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        final List<Integer> dataset = new LinkedList<>(Arrays.asList(3, 5, 15, 30));
//        final List<String> dataset = Arrays.asList("One", "Two", "Three", "Four", "Five");
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, l + "your select item :" + dataset.get(i), Toast
                        .LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
