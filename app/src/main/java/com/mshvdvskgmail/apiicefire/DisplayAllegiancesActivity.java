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
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.mshvdvskgmail.apiicefire.DBHelper.databasePath;


public class DisplayAllegiancesActivity extends AppCompatActivity {

    private String allegianceLink;
    private String[] arrayOfJsonArrayNames = {"titles", "seats", "ancestralWeapons", "cadetBranches", "swornMembers"};
    private HashMap<String, String> allegianceInfo = new HashMap<>();
    private RetrieveName rn = new RetrieveName();
    private DBHelper mydb;

    // <Button/
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
                Intent intent = new Intent(DisplayAllegiancesActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
    //Button>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_allegiances);

        mydb = new DBHelper(this);
        Intent intent = getIntent();
        allegianceLink = intent.getStringExtra("link");

        // Well, you know the drill...
        RetrieveAllegiancesInfoTask a = new RetrieveAllegiancesInfoTask();
        a.execute();
    }

    class RetrieveAllegiancesInfoTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // check the DB...
            if (mydb.allegianceExists(allegianceLink)){
                allegianceInfo = mydb.getAllegianceInfo(allegianceLink);
                return null;
            } else {
                try {
                    URL url = new URL(allegianceLink);
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

                        allegianceInfo.put("url", JSONInfoOnActor.getString("url"));
                        allegianceInfo.put("name", JSONInfoOnActor.getString("name"));
                        allegianceInfo.put("region", JSONInfoOnActor.getString("region"));
                        allegianceInfo.put("coatOfArms", JSONInfoOnActor.getString("coatOfArms"));
                        allegianceInfo.put("words", JSONInfoOnActor.getString("words"));
                        allegianceInfo.put("founded",JSONInfoOnActor.getString("founded"));
                        allegianceInfo.put("diedOut", JSONInfoOnActor.getString("diedOut"));

                        allegianceInfo.put("currentLord", rn.getTheName(JSONInfoOnActor.getString("currentLord")));
                        allegianceInfo.put("heir", rn.getTheName(JSONInfoOnActor.getString("heir")));
                        allegianceInfo.put("overlord", rn.getTheName(JSONInfoOnActor.getString("overlord")));
                        allegianceInfo.put("founder", rn.getTheName(JSONInfoOnActor.getString("founder")));


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
                                        try {
                                            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                                            StringBuilder stringBuilder2 = new StringBuilder();
                                            String line2;
                                            while ((line2 = bufferedReader2.readLine()) != null) {
                                                stringBuilder2.append(line2).append("\n");
                                            }
                                            bufferedReader2.close();
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

                            // arrayGatherer.toString() - used if array has no links
                            // namesGatherer.toString() - used if array has links in it


                            if (isUrl) {
                                if (namesGatherer.length() > 0) {
                                    namesGatherer.setLength(namesGatherer.toString().length() - 2);
                                }
                                allegianceInfo.put(arrayOfJsonArrayNames[i], namesGatherer.toString());
                            } else {
                                if (arrayGatherer.length() > 0) {
                                    arrayGatherer.setLength(arrayGatherer.toString().length() - 2);
                                }
                                allegianceInfo.put(arrayOfJsonArrayNames[i], arrayGatherer.toString());
                            }
                        }
                        return new String();
                    } finally {
                        mydb.insertAllegianceInfo(allegianceInfo);
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }


        protected void onPostExecute(String response) {
            // no links
            String[] keysForViews = {"name", "region", "coatOfArms", "words", "founded", "diedOut", "titles", "seats", "ancestralWeapons"};
            TextView textView;
            int resID;

            for (int i = 0; i < keysForViews.length; i++) {
                resID = getResources().getIdentifier(keysForViews[i], "id", getPackageName());
                textView = (TextView) findViewById(resID);
                if ((allegianceInfo.get(keysForViews[i])) != null && !(allegianceInfo.get(keysForViews[i])).isEmpty()) {
                    SpannableStringBuilder spanTxt = new SpannableStringBuilder (keysForViews[i] + ": " + allegianceInfo.get(keysForViews[i]));
                    spanTxt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, keysForViews[i].length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spanTxt);
                } else textView.setVisibility(View.GONE);

            }

            // with links
            keysForViews = new String[]{"cadetBranches", "swornMembers", "currentLord", "heir", "overlord", "founder"};

            for (int i = 0; i < keysForViews.length; i++) {

                resID = getResources().getIdentifier(keysForViews[i], "id", getPackageName());
                textView = (TextView) findViewById(resID);
                if ((allegianceInfo.get(keysForViews[i])) != null && !(allegianceInfo.get(keysForViews[i])).isEmpty()) {
                    customTextView(textView, keysForViews[i]);
                } else textView.setVisibility(View.GONE);

            }
        }

        private void customTextView(TextView view, String key) {
            final String valueOfTheKey = key;
            SpannableStringBuilder spanTxt = new SpannableStringBuilder(key + ": ");
            spanTxt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, key.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // this line bolds the first word and a :
            final String[] spanAppendLines = allegianceInfo.get(key).split(", ");
            for (int i = 0; i < spanAppendLines.length; i++) {
                final int ExtraKey = i;
                spanTxt.append(spanAppendLines[i]);
                spanTxt.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (valueOfTheKey.equals("cadetBranches")||valueOfTheKey.equals("overlord")) {
                            Intent intent = getIntent();
                            intent.putExtra("link", mydb.getLinkByName(spanAppendLines[ExtraKey]));
                            finish();
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(DisplayAllegiancesActivity.this, DisplayActorsActivity.class);
                            intent.putExtra("link", mydb.getLinkByName(spanAppendLines[ExtraKey]));
                            startActivity(intent);
                            finish();
                        }

                    }
                }, spanTxt.length() - spanAppendLines[i].length(), spanTxt.length(), 0);
                if (i < spanAppendLines.length - 1) {
                    spanTxt.append(", ");
                }
            }
            view.setMovementMethod(LinkMovementMethod.getInstance());
            view.setText(spanTxt, TextView.BufferType.SPANNABLE);
        }
    }

    // my little devil!
    class RetrieveName {
        String result = new String();
        String getTheName(String url){

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

    private void doDBCheck() // метод стирает базу данных
    {
        try{
            File file = new File(databasePath);
            file.delete();
        }catch(Exception ex)
        {}
    }
}
