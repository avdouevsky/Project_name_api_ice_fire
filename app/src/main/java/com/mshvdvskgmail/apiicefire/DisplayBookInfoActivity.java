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



public class DisplayBookInfoActivity extends AppCompatActivity {

    private DBHelper mydb;
    private String linkOnBook;
    private HashMap <String, String> bookInfo = new HashMap<>(); // The map hold all info to be displaying on the book

    // onCreateOptionsMenu and onOptionsItemSelected handle the Home button

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
                Intent intent = new Intent(DisplayBookInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // I reckon it's a good idea. Though I am not sure.
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book_info);

        mydb = new DBHelper(this);
        Intent intent = getIntent();
        linkOnBook = intent.getStringExtra("link");

        // Same structure. @RetrieveBooksTask does all the job getting the info and displaying it

        RetrieveBooksTask a = new RetrieveBooksTask();
        a.execute();

    }


    class RetrieveBooksTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        protected void onPreExecute() {
        }

        protected HashMap<String, String> doInBackground(Void... params) {


            // check the DB.

            if (mydb.bookExists(linkOnBook)){
                bookInfo = mydb.getBookInfo(linkOnBook);
            } else {

                // If there's no record - go to the url.

                try{
                    URL url = new URL(linkOnBook);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try{
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null){
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();


                        // no array's in JSON is easy

                        JSONObject obj = new JSONObject(stringBuilder.toString());
                        bookInfo.put("url", obj.getString("url"));
                        bookInfo.put("name", obj.getString("name"));
                        bookInfo.put("isbn", obj.getString("isbn"));

                        JSONArray array =  obj.getJSONArray("authors");
                        StringBuilder arrayString = new StringBuilder();
                        for (int i = 0; i < array.length(); i++){
                            arrayString.append(array.getString(i));
                        }
                        String authors = arrayString.toString();
                        bookInfo.put("authors", authors);
                        bookInfo.put("numberOfPages", obj.getString("numberOfPages"));
                        bookInfo.put("publisher", obj.getString("publisher"));
                        bookInfo.put("country", obj.getString("country"));
                        bookInfo.put("mediaType", obj.getString("mediaType"));
                        bookInfo.put("released", obj.getString("released"));

                        String actorAdres;
                        StringBuilder nameGatherer = new StringBuilder();


                        // arrays with URL ARE NOT.

                        JSONArray PovActorsArray = obj.getJSONArray("povCharacters");

                        // going through all the links to get the names.

                        for (int i = 0; i < PovActorsArray.length(); i++){
                            actorAdres = PovActorsArray.getString(i);

                            try{
                                URL url2 = new URL(actorAdres);
                                HttpURLConnection urlConnection2 = (HttpURLConnection) url2.openConnection();

                                // get JSON object

                                try{
                                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    String line2;
                                    while ((line2 = bufferedReader2.readLine()) != null){
                                        stringBuilder2.append(line2).append("\n");
                                    }
                                    bufferedReader2.close();

                                    // get name from the object, and put it into the line, also put info on name&link it DB

                                    JSONObject obj2 = new JSONObject(stringBuilder2.toString());
                                    mydb.insertNameAndLink(obj2.getString("name"), actorAdres);
                                    nameGatherer.append(obj2.getString("name")+", ");

                                }
                                finally {
                                    urlConnection2.disconnect();
                                }
                            }
                            catch (Exception e){}
                        }

                        // if we found some names (some books have none), put it into the container

                        if (nameGatherer.length()>0){
                            nameGatherer.setLength(nameGatherer.toString().length() - 2);
                        }
                        bookInfo.put("povCharacters", nameGatherer.toString());

                    }


                    // finally, put all info in the TB

                    finally {
                        mydb.insertBookInfo(bookInfo);
                        urlConnection.disconnect();
                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return null;
        }

        protected void onPostExecute (HashMap<String, String> response) {


            // filling the TextViews. First part is w/o links

            String[] keysForViews = {"name", "isbn", "authors", "numberOfPages", "publisher", "country", "mediaType", "released"};
            String keyForView = new String ("povCharacters");
            TextView textView;
            int resID;

            for (int i = 0; i<keysForViews.length;i++){

                resID = getResources().getIdentifier(keysForViews[i], "id", getPackageName());
                textView = (TextView) findViewById (resID);
                if((bookInfo.get(keysForViews[i])) != null && !(bookInfo.get(keysForViews[i])).isEmpty()){
                    SpannableStringBuilder spanTxt = new SpannableStringBuilder(keysForViews[i]+": "+bookInfo.get(keysForViews[i]));
                    spanTxt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, keysForViews[i].length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spanTxt);
                } else textView.setVisibility(View.GONE);

            }


            // Second contains links. @customTextView class adds em

            resID = getResources().getIdentifier(keyForView, "id", getPackageName());
            textView = (TextView) findViewById (resID);
            if((bookInfo.get(keyForView)) != null && !(bookInfo.get(keyForView)).isEmpty()){
                customTextView(textView, keyForView);
            } else textView.setVisibility(View.GONE);

        }

    }

    private void customTextView(TextView view, String key) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(key + ": ");

        // this line bolds the first word and a ":"
        spanTxt.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, key.length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        final String[] spanAppendLines = bookInfo.get(key).split(", ");
        for (int i = 0; i < spanAppendLines.length; i++) {
            final int ExtraKey = i;
            spanTxt.append(spanAppendLines[i]);
            spanTxt.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(DisplayBookInfoActivity.this, DisplayActorsActivity.class);
                    intent.putExtra("link", mydb.getLinkByName(spanAppendLines[ExtraKey]));
                    startActivity(intent);
                }
            }, spanTxt.length() - spanAppendLines[i].length(), spanTxt.length(), 0);
            if (i < spanAppendLines.length - 1) {  // damn this commas. I hope there is a better way to deal with em
                spanTxt.append(", ");
            }
        }
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
}
