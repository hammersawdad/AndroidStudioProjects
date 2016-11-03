package com.example.gtillett.helloworldone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Constants
    public final static String EXTRA_MESSAGE = "com.example.gtillett.helloworldone.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onClickMenuOther(MenuItem item){
        Toast toast = Toast.makeText(this, "Other Click", Toast.LENGTH_LONG);
        toast.show();
    }
    public void onClickMenuExit(MenuItem item){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view){
        // Get the text that was entered in the Message textbox
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        // Create an instance of the Display Message activity,
        //  and pass it the value
        Intent intent = new Intent(this, DisplayMessageActivity.class );
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void startBlueToothController(View view){
        // Create an instance of the BlueToothController activity,
        //  and pass it the value
        Intent intent = new Intent(this, BlueControllerActivity.class );
        startActivity(intent);

    }
}
