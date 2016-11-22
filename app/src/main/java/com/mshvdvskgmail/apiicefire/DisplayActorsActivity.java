package com.mshvdvskgmail.apiicefire;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;



public class DisplayActorsActivity extends AppCompatActivity {

    private String linkOnActor;
    private DBHelper mydb;
    private HashMap <String, String> actorInfo = new HashMap<>();

    // Actors have more arrays of info, only part of which are links. @RetrieveName is a class that is fed with a link, and gives back a name

    private String[] arrayOfJsonArrayNames = {"titles", "aliases", "allegiances", "books", "povBooks", "tvSeries", "playedBy"};
    private RetrieveName rn = new RetrieveName();


    // Home button!
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_button:
                Intent intent = new Intent(DisplayActorsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
    // Home button ends!

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_actors);

        mydb = new DBHelper(this);
        Intent intent = getIntent();
        linkOnActor = intent.getStringExtra("link");


        // Same stuff. I've got some style, haven't I? :D

        RetrieveActorInfoTask a = new RetrieveActorInfoTask();
        a.execute();
    }

    class RetrieveActorInfoTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        // Getting the JSON object from the actor's url
        protected String doInBackground(Void... params) {

            // DB check if contains

            if (mydb.exists(linkOnActor)){
                actorInfo = mydb.getActorInfo(linkOnActor);
                return null;
            } else {
                try {
                    URL url = new URL(linkOnActor);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        JSONObject JSONInfoOnActor = new JSONObject(stringBuilder.toString());

                        // mapOnActorInfo - map for the arrays of JSONInfoOnActor

                        actorInfo.put("url", JSONInfoOnActor.getString("url"));
                        actorInfo.put("name", JSONInfoOnActor.getString("name"));
                        actorInfo.put("gender", JSONInfoOnActor.getString("gender"));
                        actorInfo.put("culture", JSONInfoOnActor.getString("culture"));
                        actorInfo.put("born", JSONInfoOnActor.getString("born"));
                        actorInfo.put("died", JSONInfoOnActor.getString("died"));

                        // these got links
                        actorInfo.put("father", rn.getTheName(JSONInfoOnActor.getString("father")));
                        actorInfo.put("mother", rn.getTheName(JSONInfoOnActor.getString("mother")));
                        actorInfo.put("spouse", rn.getTheName(JSONInfoOnActor.getString("spouse")));


                        StringBuilder arrayGatherer;
                        StringBuilder namesGatherer;
                        JSONArray array;
                        String nameFromTheLink;

                        // going through the links and getting the names of the JSOBObjects

                        for (int i = 0; i < arrayOfJsonArrayNames.length; i++) {
                            arrayGatherer = new StringBuilder(); // to collect Strings from JSONArray
                            namesGatherer = new StringBuilder(); // to collect name parameter from the urls
                            array = JSONInfoOnActor.getJSONArray(arrayOfJsonArrayNames[i]);
                            boolean isUrl = true;

                            for (int k = 0; k < array.length(); k++) {

                                arrayGatherer.append(array.getString(k) + ", ");


                                if (isUrl) {
                                    try {
                                        URL url2 = new URL(array.getString(k));
                                        HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();

                                        // getting JSON
                                        try {
                                            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                                            StringBuilder stringBuilder2 = new StringBuilder();
                                            String line2;
                                            while ((line2 = bufferedReader2.readLine()) != null) {
                                                stringBuilder2.append(line2).append("\n");
                                            }
                                            bufferedReader2.close();

                                            // getting the name out of it
                                            JSONObject JSONInfoOnActor2 = new JSONObject(stringBuilder2.toString());
                                            nameFromTheLink = JSONInfoOnActor2.getString("name");

                                            mydb.insertNameAndLink(nameFromTheLink, array.getString(k));
                                            namesGatherer.append(nameFromTheLink + ", ");
                                        } finally {
                                            urlConnection2.disconnect();
                                        }
                                    } catch (Exception e) {
                                        isUrl = false;
                                    }
                                }
                            }


                            // Here we check if the array was built of URL's or Strings. AND WE CHOP OF THE BLOODY COMMAS

                            if (isUrl) {
                                if (namesGatherer.length() > 0) {
                                    namesGatherer.setLength(namesGatherer.toString().length() - 2);
                                }
                                actorInfo.put(arrayOfJsonArrayNames[i], namesGatherer.toString());
                            } else {
                                if (arrayGatherer.length() > 0) {
                                    arrayGatherer.setLength(arrayGatherer.toString().length() - 2);
                                }
                                actorInfo.put(arrayOfJsonArrayNames[i], arrayGatherer.toString());
                            }
                        }
                        return new String();
                    } finally {
                        mydb.insertActorInfo(actorInfo);
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


        //populating the views
        protected void onPostExecute (String response) {


            // non links
            String[] keysForViews = {"name", "gender", "culture", "born", "died", "titles", "aliases", "tvSeries", "playedBy"};
            TextView textView;
            int resID;

            for (int i = 0; i<keysForViews.length;i++){

                resID = getResources().getIdentifier(keysForViews[i], "id", getPackageName());
                textView = (TextView) findViewById (resID);
                if((actorInfo.get(keysForViews[i])) != null && !(actorInfo.get(keysForViews[i])).isEmpty()){
                    SpannableStringBuilder spanTxt = new SpannableStringBuilder(keysForViews[i]+": "+actorInfo.get(keysForViews[i]));
                    spanTxt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, keysForViews[i].length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spanTxt);
                } else textView.setVisibility(View.GONE);

            }

            // links, so we use @customTextView
            keysForViews = new String[]{"father", "mother", "spouse", "allegiances", "books", "povBooks"};

            for (int i = 0; i<keysForViews.length;i++){

                resID = getResources().getIdentifier(keysForViews[i], "id", getPackageName());
                textView = (TextView) findViewById (resID);
                if((actorInfo.get(keysForViews[i])) != null && !(actorInfo.get(keysForViews[i])).isEmpty()){
                    customTextView(textView, keysForViews[i]);
                } else textView.setVisibility(View.GONE);

            }

        }

        // is similar to the previous one
        private void customTextView(TextView view, String key) {
            final String valueOfTheKey = key;
            SpannableStringBuilder spanTxt = new SpannableStringBuilder(key+": ");
            spanTxt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, key.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // making text BOLD
            final String [] spanAppendLines = actorInfo.get(key).split(", ");

            // and here we tune the links up.
            // we have valueOfTheKey as BOOKS, ALLEGIANCES - or other actors. In which case I decided to kill activity first.
            // so the "back" button will get user into book section, not into another actor, as there might be a long chain of them.

            for (int i = 0; i < spanAppendLines.length; i++){
                final int ExtraKey = i;
                spanTxt.append(spanAppendLines[i]);
                spanTxt.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (valueOfTheKey.equals("books")||valueOfTheKey.equals("povBooks")){
                            Intent intent = new Intent(DisplayActorsActivity.this, DisplayBookInfoActivity.class);
                            intent.putExtra("link", mydb.getLinkByName(spanAppendLines[ExtraKey]));
                            startActivity(intent);
                        } else if (valueOfTheKey.equals("allegiances")){
                            Intent intent = new Intent(DisplayActorsActivity.this, DisplayAllegiancesActivity.class);
                            intent.putExtra("link", mydb.getLinkByName(spanAppendLines[ExtraKey]));
                            startActivity(intent);
                        } else {
                            Intent intent = getIntent();
                            intent.putExtra("link", mydb.getLinkByName(spanAppendLines[ExtraKey]));
                            finish();
                            startActivity(intent);
                        }
                    }
                }, spanTxt.length() - spanAppendLines[i].length(), spanTxt.length(), 0);
                if (i < spanAppendLines.length-1){
                    spanTxt.append(", ");
                }
            }
            view.setMovementMethod(LinkMovementMethod.getInstance());
            view.setText(spanTxt, TextView.BufferType.SPANNABLE);
        }
    }

    // Give him a url, he well find the name! Amazing, isn't it?
    public class RetrieveName {
        private String result;
        public String getTheName(String url){

            try {
                URL url2 = new URL(url);
                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    String line2;
                    while ((line2 = bufferedReader2.readLine()) != null) {
                        stringBuilder2.append(line2).append("\n");
                    }
                    bufferedReader2.close();
                    JSONObject JSONInfoOnActor2 = new JSONObject(stringBuilder2.toString());
                    result = JSONInfoOnActor2.getString("name");
                } finally {
                    urlConnection2.disconnect();
                }
            } catch (Exception e){
            }
            mydb.insertNameAndLink(result, url);
            return result;
        }


    }
}
