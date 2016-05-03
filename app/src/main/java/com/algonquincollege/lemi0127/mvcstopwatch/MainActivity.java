package com.algonquincollege.lemi0127.mvcstopwatch;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import model.StopwatchModel;

/**
 *  Stopwatch assignment for Android Development Class at Algonquin College
 *  Teaching OO Design Patterns such as Observable/Observer & MVC Architecture
 *  Main Activity: App, dialog fragment, fab, and observer init
 *  @author Alice Lee (lemi0127)
 */

public class MainActivity extends AppCompatActivity implements Observer {

    private DialogFragment aboutDialog;
    private FloatingActionButton fab;
    private static final String ABOUT_DIALOG_TAG = "About";
    private StopwatchModel stopwatchModel;
    private TextView stopwatchView;
    private Runnable updateStopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopwatchModel.stopwatchRunning()) {
                    stopwatchModel.stop();
                } else {
                    stopwatchModel.start();
                }
            }
        });

        aboutDialog = new AboutDialogFragment();
        stopwatchModel = new StopwatchModel( settings.getInt("Hours", 0),
                settings.getInt("Minutes", 0),
                settings.getInt("Seconds", 0),
                settings.getInt("TenthOfASecond", 0) );
        stopwatchModel.addObserver(this);
        stopwatchView = (TextView) findViewById(R.id.textViewStopwatch);
        updateStopwatch = new Runnable() {
            @Override
            public void run() {
                stopwatchView.setText(stopwatchModel.toString());

                if (stopwatchModel.stopwatchRunning()) {
                    fab.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    fab.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        };


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            aboutDialog.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            return true;
        }
        if (id == R.id.action_reset) {
            aboutDialog.show(getFragmentManager(), ABOUT_DIALOG_TAG);
            stopwatchModel.reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt( "hours",   stopwatchModel.getHours() );
        editor.putInt( "minutes", stopwatchModel.getMinutes() );
        editor.putInt( "seconds",  stopwatchModel.getSeconds() );
        editor.putInt( "tenthOfASecond", stopwatchModel.getTenthOfASecond() );

        editor.commit();
    }

    @Override
    public void update(Observable observable, Object data) {
        runOnUiThread(updateStopwatch);
    }
}
