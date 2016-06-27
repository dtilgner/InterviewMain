package com.example.daniel.rhetoric;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    private static String url_login = "http://localhost/rhetoric/login.php";
    private static String url_create_user = "http://localhost/rhetoric/create_user.php";
    private String newUserName = "";

    //JSON Nodes
    private static final String TAG_SUCCESS = "success";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int success;
        String androidID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        try{
            List<NameValuePair> params = new ArrayList<>();
            JSONObject json = jsonParser.makeHttpRequest(url_login, "GET", params);
            params.add(new BasicNameValuePair("userID", androidID));

            Log.d("User ", json.toString());
            success = json.getInt(TAG_SUCCESS);
            while (success == 1) {
                List<NameValuePair> params2 = new ArrayList<>();
                json = jsonParser.makeHttpRequest(url_create_user, "GET", params2);
                params2.add(new BasicNameValuePair("userID", androidID));

                //Create popup for new user name
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please create a user name");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newUserName = input.getText().toString();
                    }
                });
                builder.show();

                params2.add(new BasicNameValuePair("userName", newUserName));

                Log.d("User ", json.toString());
                success = json.getInt(TAG_SUCCESS);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openGames(View view){
        Intent intent = new Intent(this, OpenGamesActivity.class);
        startActivity(intent);
    }

    public void optionsScreen(View view){
        //TODO: add options screen
        //Do I even need this?  What options would I have?
    }
}


