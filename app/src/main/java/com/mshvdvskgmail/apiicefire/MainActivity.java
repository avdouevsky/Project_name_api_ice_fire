package com.mshvdvskgmail.apiicefire;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private ListView responseView;  // shows the list of all books
    private DBHelper mydb;  // communicates with DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);
        //doDBCheck();
        responseView = (ListView) findViewById(R.id.responseview);
        responseView.addFooterView(new View(this), null, true);


        // this thing gets links from DB by name if an item is clicked

        responseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DisplayBookInfoActivity.class);
                Object listItem = responseView.getItemAtPosition(position);
                intent.putExtra("link", mydb.getLinkByName(listItem.toString()));
                startActivity(intent);
            }
        });


        // @RetrieveBooksTask class does the main job of getting info from the Web/DB, and of displaying it

        RetrieveBooksTask a = new RetrieveBooksTask();
        a.execute();
    }

    private class RetrieveBooksTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... params) {

            // Здесь есть нюанс. Теоретически можно было бы просто обратиться к одной страничке - http://www.anapioficeandfire.com/api/books
            // Однако на ней есть всего 11 книг, а по URL "book/12" и "book/13" записи есть. Чтобы не ставить носорога и жирафа в один загон, прощупал все url book/=?

            if (!mydb.booksListExists()) {
                LinkMaker lm = new LinkMaker();
                NameFisher nm = new NameFisher();
                int hook = 1;
                String nameOfBook;
                String urlOfBook;
                do {
                    urlOfBook = lm.getLink(hook);
                    nameOfBook = nm.getTheName(urlOfBook);
                    hook++;
                    if (!nameOfBook.equals("nothing")) {
                        mydb.insetBookAndLink(nameOfBook, urlOfBook);
                        mydb.insertNameAndLink(nameOfBook, urlOfBook);
                    }
                } while (!nameOfBook.equals("nothing"));
            }

            return null;
        }

        protected void onPostExecute(String response) {
            ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this, R.layout.activity_listview, mydb.getListOfAllBooks());
            responseView.setAdapter(adapter);
        }

    }


    // Does what is says. Strait as a man should be.

    class LinkMaker {
        StringBuilder defaultAdress = new StringBuilder("http://anapioficeandfire.com/api/books/");
        int defaultAdressLength = defaultAdress.length();
        String result;

        public LinkMaker() {
        }

        public String getLink(int i) {
            defaultAdress.append(i);
            result = defaultAdress.toString();
            defaultAdress.delete(defaultAdressLength, defaultAdress.length());
            return result;
        }
    }


    // Same noble being as a previous one.

    class NameFisher {
        String result = new String();

        String getTheName(String url) {
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
            } catch (Exception e) {
                return new String("nothing");
            }
            return result;
        }

    }
}