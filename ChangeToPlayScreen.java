package com.example.daniel.rhetoric;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Handler;
import android.widget.EditText;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class ChangeToPlayScreen extends AppCompatActivity {

    ArrayList<String> wordChecker = new ArrayList<String>();
    String fileName = "words.txt";
    String line = null;
    String userName;
    EditText topTimer;
    Button firstLetter, secondLetter, thirdLetter, fourthLetter, fifthLetter,
            sixthLetter, seventhLetter, eighthLetter, ninthLetter, tenthLetter;
    long startTime = 0;
    int timeTaken = 0;
    List vowelList = new ArrayList();
    List consonantList = new ArrayList();
    List letterList = new ArrayList();
    Random rand = new Random();
    int numOfVowels = rand.nextInt(3) + 3;
    int numOfConsonants = 10 - numOfVowels;
    String wordToCheck = "";
    int wordValue = 0;
    int health = 91;
    int turnNumber;
    int gameID;
    static String player;

    JSONParser jsonParser = new JSONParser();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GAMEID = "gameID";
    private static final String TAG_USERNAME = "userName";
    private static final String TAG_USERHP = "userHP";
    private static final String TAG_USERTIME = "userTime";
    private static final String TAG_USERSCORE = "userScore";
    private static final String TAG_USERTURN = "userTurn";

    private static final String url_submit_gamestate = "http://localhost/rhetoric/submit_gamestate_" + player;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable(){

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);

            topTimer.setText(String.format("%d", seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_to_play_screen);

        try{
            InputStream fileReader = getAssets().open(fileName);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));

            while ((line = bufferedReader.readLine()) != null) {
                wordChecker.add(line);
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex){
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex){
            System.out.println("Error reading file '" + fileName + "'");
        }

        topTimer = (EditText) findViewById(R.id.topTimer);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        //Create a list of vowels to be added to the pool of available letters
        for(int i = 0; i < 12; i++){
            vowelList.add('e');
        }
        for(int i = 12; i < 21; i++){
            vowelList.add('a');
        }
        for(int i = 21; i < 30; i++){
            vowelList.add('i');
        }
        for(int i = 30; i < 38; i++){
            vowelList.add('o');
        }
        for(int i = 38; i < 42; i++){
            vowelList.add('u');
        }

        //Create a list of consonants to be added to the pool of available letters
        for(int i = 0; i < 6; i++){
            consonantList.add('n');
        }
        for(int i = 6; i < 12; i++){
            consonantList.add('r');
        }
        for(int i = 12; i < 18; i++){
            consonantList.add('t');
        }
        for(int i = 18; i < 22; i++){
            consonantList.add('l');
        }
        for(int i = 22; i < 26; i++){
            consonantList.add('s');
        }
        for(int i = 26; i < 30; i++){
            consonantList.add('d');
        }
        for(int i = 30; i < 33; i++){
            consonantList.add('g');
        }
        for(int i = 33; i < 35; i++){
            consonantList.add('b');
        }
        for(int i = 35; i < 37; i++){
            consonantList.add('c');
        }
        for(int i = 37; i < 39; i++){
            consonantList.add('m');
        }
        for(int i = 39; i < 41; i++){
            consonantList.add('p');
        }
        for(int i = 41; i < 43; i++){
            consonantList.add('f');
        }
        for(int i = 43; i < 45; i++){
            consonantList.add('h');
        }
        for(int i = 45; i < 47; i++){
            consonantList.add('v');
        }
        for(int i = 47; i < 49; i++){
            consonantList.add('w');
        }
        for(int i = 49; i < 51; i++){
            consonantList.add('y');
        }
        for(int i = 51; i < 52; i++){
            consonantList.add('k');
        }
        for(int i = 52; i < 53; i++){
            consonantList.add('j');
        }
        for(int i = 53; i < 54; i++){
            consonantList.add('x');
        }
        for(int i = 54; i < 55; i++){
            consonantList.add('q');
        }
        for(int i = 55; i < 56; i++){
            consonantList.add('z');
        }

        //Decide which letters are added to the ten available buttons
        while(numOfVowels > 0){
            int nextVowel = rand.nextInt(vowelList.size());
            letterList.add(vowelList.get(nextVowel));
            vowelList.remove(nextVowel);
            numOfVowels--;
        }
        while(numOfConsonants > 0){
            int nextConsonant = rand.nextInt(consonantList.size());
            letterList.add(consonantList.get(nextConsonant));
            consonantList.remove(nextConsonant);
            numOfConsonants--;
        }
        Collections.shuffle(letterList);

        //Map the letters to the buttons
        firstLetter = (Button) findViewById(R.id.firstLetter);
        firstLetter.setText(letterList.get(0).toString());

        secondLetter = (Button) findViewById(R.id.secondLetter);
        secondLetter.setText(letterList.get(1).toString());

        thirdLetter = (Button) findViewById(R.id.thirdLetter);
        thirdLetter.setText(letterList.get(2).toString());

        fourthLetter = (Button) findViewById(R.id.fourthLetter);
        fourthLetter.setText(letterList.get(3).toString());

        fifthLetter = (Button) findViewById(R.id.fifthLetter);
        fifthLetter.setText(letterList.get(4).toString());

        sixthLetter = (Button) findViewById(R.id.sixthLetter);
        sixthLetter.setText(letterList.get(5).toString());

        seventhLetter = (Button) findViewById(R.id.seventhLetter);
        seventhLetter.setText(letterList.get(6).toString());

        eighthLetter = (Button) findViewById(R.id.eighthLetter);
        eighthLetter.setText(letterList.get(7).toString());

        ninthLetter = (Button) findViewById(R.id.ninthLetter);
        ninthLetter.setText(letterList.get(8).toString());

        tenthLetter = (Button) findViewById(R.id.tenthLetter);
        tenthLetter.setText(letterList.get(9).toString());

        EditText healthBar = (EditText) findViewById(R.id.healthBar);
        healthBar.setText(Integer.toString(health));
    }

    public void letterClick(View view){
        EditText currentWord = (EditText) findViewById(R.id.currentWord);
        Button b = (Button)view;
        String word = currentWord.getText().toString();
        word += b.getText().toString();
        wordValue += LetterValue(b.getText().toString().charAt(0));
        EditText currentScore = (EditText) findViewById(R.id.score);
        currentScore.setText(Integer.toString(wordValue));
        wordToCheck = word;
        currentWord.setText(word);
        b.setClickable(false);
        b.setAlpha(.4f);
    }

    public void clearClick(View view){
        EditText currentWord = (EditText) findViewById(R.id.currentWord);
        EditText currentScore = (EditText) findViewById(R.id.score);
        firstLetter = (Button) findViewById(R.id.firstLetter);
        firstLetter.setAlpha(1f);
        firstLetter.setClickable(true);
        secondLetter = (Button) findViewById(R.id.secondLetter);
        secondLetter.setAlpha(1f);
        secondLetter.setClickable(true);
        thirdLetter = (Button) findViewById(R.id.thirdLetter);
        thirdLetter.setAlpha(1f);
        thirdLetter.setClickable(true);
        fourthLetter = (Button) findViewById(R.id.fourthLetter);
        fourthLetter.setAlpha(1f);
        fourthLetter.setClickable(true);
        fifthLetter = (Button) findViewById(R.id.fifthLetter);
        fifthLetter.setAlpha(1f);
        fifthLetter.setClickable(true);
        sixthLetter = (Button) findViewById(R.id.sixthLetter);
        sixthLetter.setAlpha(1f);
        sixthLetter.setClickable(true);
        seventhLetter = (Button) findViewById(R.id.seventhLetter);
        seventhLetter.setAlpha(1f);
        seventhLetter.setClickable(true);
        eighthLetter = (Button) findViewById(R.id.eighthLetter);
        eighthLetter.setAlpha(1f);
        eighthLetter.setClickable(true);
        ninthLetter = (Button) findViewById(R.id.ninthLetter);
        ninthLetter.setAlpha(1f);
        ninthLetter.setClickable(true);
        tenthLetter = (Button) findViewById(R.id.tenthLetter);
        tenthLetter.setAlpha(1f);
        tenthLetter.setClickable(true);

        currentWord.setText("");
        currentScore.setText("0");
        wordValue = 0;
    }

    public void shuffleClick(View view){
        firstLetter = (Button) findViewById(R.id.firstLetter);

        letterList.clear();
        letterList.add(firstLetter.getText().toString());

        secondLetter = (Button) findViewById(R.id.secondLetter);
        letterList.add(secondLetter.getText().toString());

        thirdLetter = (Button) findViewById(R.id.thirdLetter);
        letterList.add(thirdLetter.getText().toString());

        fourthLetter = (Button) findViewById(R.id.fourthLetter);
        letterList.add(fourthLetter.getText().toString());

        fifthLetter = (Button) findViewById(R.id.fifthLetter);
        letterList.add(fifthLetter.getText().toString());

        sixthLetter = (Button) findViewById(R.id.sixthLetter);
        letterList.add(sixthLetter.getText().toString());

        seventhLetter = (Button) findViewById(R.id.seventhLetter);
        letterList.add(seventhLetter.getText().toString());

        eighthLetter = (Button) findViewById(R.id.eighthLetter);
        letterList.add(eighthLetter.getText().toString());

        ninthLetter = (Button) findViewById(R.id.ninthLetter);
        letterList.add(ninthLetter.getText().toString());

        tenthLetter = (Button) findViewById(R.id.tenthLetter);
        letterList.add(tenthLetter.getText().toString());

        Collections.shuffle(letterList);

        EditText currentWord = (EditText) findViewById(R.id.currentWord);
        EditText currentScore = (EditText) findViewById(R.id.score);

        firstLetter.setAlpha(1f);
        firstLetter.setClickable(true);
        secondLetter.setAlpha(1f);
        secondLetter.setClickable(true);
        thirdLetter.setAlpha(1f);
        thirdLetter.setClickable(true);
        fourthLetter.setAlpha(1f);
        fourthLetter.setClickable(true);
        fifthLetter.setAlpha(1f);
        fifthLetter.setClickable(true);
        sixthLetter.setAlpha(1f);
        sixthLetter.setClickable(true);
        seventhLetter.setAlpha(1f);
        seventhLetter.setClickable(true);
        eighthLetter.setAlpha(1f);
        eighthLetter.setClickable(true);
        ninthLetter.setAlpha(1f);
        ninthLetter.setClickable(true);
        tenthLetter.setAlpha(1f);
        tenthLetter.setClickable(true);

        currentWord.setText("");
        currentScore.setText("0");
        wordValue = 0;

        firstLetter = (Button) findViewById(R.id.firstLetter);
        firstLetter.setText(letterList.get(0).toString());

        secondLetter = (Button) findViewById(R.id.secondLetter);
        secondLetter.setText(letterList.get(1).toString());

        thirdLetter = (Button) findViewById(R.id.thirdLetter);
        thirdLetter.setText(letterList.get(2).toString());

        fourthLetter = (Button) findViewById(R.id.fourthLetter);
        fourthLetter.setText(letterList.get(3).toString());

        fifthLetter = (Button) findViewById(R.id.fifthLetter);
        fifthLetter.setText(letterList.get(4).toString());

        sixthLetter = (Button) findViewById(R.id.sixthLetter);
        sixthLetter.setText(letterList.get(5).toString());

        seventhLetter = (Button) findViewById(R.id.seventhLetter);
        seventhLetter.setText(letterList.get(6).toString());

        eighthLetter = (Button) findViewById(R.id.eighthLetter);
        eighthLetter.setText(letterList.get(7).toString());

        ninthLetter = (Button) findViewById(R.id.ninthLetter);
        ninthLetter.setText(letterList.get(8).toString());

        tenthLetter = (Button) findViewById(R.id.tenthLetter);
        tenthLetter.setText(letterList.get(9).toString());
    }

    public void submitClick(View view){

        if (wordChecker.contains(wordToCheck)){
            AlertDialog.Builder goodWordAlert = new AlertDialog.Builder(this);
            goodWordAlert.setMessage("Submit with word: " + wordToCheck + "?");
            goodWordAlert.setPositiveButton("Submit",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            timerHandler.removeCallbacks(timerRunnable);
                            timeTaken = Integer.parseInt(topTimer.getText().toString());
                            EditText currentScore = (EditText) findViewById(R.id.score);
                            turnNumber++;

                            //Build params to send
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair(TAG_GAMEID, Integer.toString(gameID)));
                            params.add(new BasicNameValuePair(TAG_USERNAME, userName));
                            params.add(new BasicNameValuePair(TAG_USERHP, Integer.toString(health)));
                            params.add(new BasicNameValuePair(TAG_USERTIME, topTimer.getText().toString()));
                            params.add(new BasicNameValuePair(TAG_USERSCORE, currentScore.getText().toString()));
                            params.add(new BasicNameValuePair(TAG_USERTURN, Integer.toString(turnNumber)));

                            JSONObject json = jsonParser.makeHttpRequest(url_submit_gamestate, "POST", params);

                            try{
                                int success = json.getInt(TAG_SUCCESS);

                                if (success == 1){
                                    Intent i = getIntent();
                                    setResult(100, i);
                                    finish();
                                }
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }

                            jumpToPreviousScreen();
                        }
                    });
            goodWordAlert.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            goodWordAlert.setCancelable(true);
            goodWordAlert.create().show();
        }
        else{
            AlertDialog.Builder badWordAlert = new AlertDialog.Builder(this);
            badWordAlert.setMessage("Sorry, " + wordToCheck + " is not a valid word.");
            badWordAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            badWordAlert.setCancelable(true);
            badWordAlert.create().show();
        }
    }

    public void jumpToPreviousScreen(){
        AlertDialog.Builder wordSubmitted = new AlertDialog.Builder(this);
        wordSubmitted.setMessage("Word \"" + wordToCheck + "\" submitted.");
        wordSubmitted.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        wordSubmitted.setCancelable(true);
        wordSubmitted.create().show();
    }

    public int LetterValue(char charValue){

        if (charValue == 'a' || charValue == 'e' || charValue == 'i' || charValue == 'o' ||
                charValue == 'u'|| charValue == 'l' || charValue == 'n' || charValue == 's' ||
                charValue == 't' || charValue == 'r')
            return 1;
        else if (charValue == 'd' || charValue == 'g')
            return 2;
        else if (charValue == 'b' || charValue == 'c' || charValue == 'm' || charValue == 'p')
            return 3;
        else if (charValue == 'f' || charValue == 'h' || charValue == 'v' || charValue == 'w' ||
                charValue == 'y')
            return 4;
        else if (charValue == 'k')
            return 5;
        else if (charValue == 'j' || charValue == 'x')
            return 8;
        else
            return 10;
    }
}


