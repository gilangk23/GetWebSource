package com.example.gilangk.getwebpage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    Spinner a;
    ArrayAdapter<CharSequence> web;
    EditText page;
    TextView webpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = (Spinner)findViewById(R.id.spinner);
        page = (EditText)findViewById(R.id.editText2);
        webpage = (TextView)findViewById(R.id.Text);

        web = ArrayAdapter.createFromResource(this, R.array.protokol, android.R.layout.simple_spinner_item);
        web.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a.setAdapter(web);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
            }
        });

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new GetSource(this,args.getString("url_link"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        webpage.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void GetPageSource(View view) {
        String protokol , urls;
        protokol = a.getSelectedItem().toString();
        urls = page.getText().toString();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        if (checkConnection()){
            webpage.setText("loading...");
            Bundle bundle = new Bundle();
            bundle.putString("url_link", protokol+urls);
            getSupportLoaderManager().restartLoader(0, bundle, this);
        } else {
            webpage.setText("tidak ada koneksi");
        }

    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
