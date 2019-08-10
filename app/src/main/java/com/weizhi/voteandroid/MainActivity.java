package com.weizhi.voteandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private SQLiteOpenHelper dbhelper;

    private String username;

    private String userGUID;

    private ArrayList<Button> candicateButtons= new ArrayList();

    private ArrayList<LinearLayout> boxes = new ArrayList<>();

    JSONArray candiateJSArray;

    private AdView mAdView;

    private ActionProvider shareActionProvider;

    private Button candidate1;

    private Button candidate2;

    private Button candidate3;

    private Button candidate4;

    private Button candidate5;



    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();

        getUserInfo();


        //initCandidates();



    }

    public void initUI(){
        //getSupportActionBar().hide();



        mHandler = new Handler();




        candidate1=findViewById(R.id.candidate1);
        candidate2=findViewById(R.id.candidate2);
        candidate3=findViewById(R.id.candidate3);
        candidate4=findViewById(R.id.candidate4);
        candidate5=findViewById(R.id.candidate5);

        candicateButtons.add(candidate1);
        candicateButtons.add(candidate2);
        candicateButtons.add(candidate3);
        candicateButtons.add(candidate4);
        candicateButtons.add(candidate5);

        MobileAds.initialize(this,
                "ca-app-pub-6463506718592054~4931635628");



        mAdView = findViewById(R.id.adView2);

//        mAdView.setAdSize(AdSize.LARGE_BANNER);
//        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        candidate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingTask();

                startRepeatingTask();
                new vote().execute("1");

            }
        });

        candidate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingTask();

                startRepeatingTask();

                new vote().execute("2");

            }
        });

        candidate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingTask();

                startRepeatingTask();
                new vote().execute("3");


            }
        });

        candidate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingTask();

                startRepeatingTask();
                new vote().execute("5");


            }
        });

        candidate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRepeatingTask();

                startRepeatingTask();
                new vote().execute("4");


            }
        });



    }

    public void getUserInfo(){
        dbhelper = new CustomSQLiteOpenHelper(this);

        db = dbhelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from user",null);

        if(cursor.moveToNext()){
            username=cursor.getString(2);
            userGUID=cursor.getString(1);
        }

        Log.i(userGUID,"userguid");


    }



    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                initCandidates();
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 3000);
            }
        }
    };

    void startRepeatingTask() {
        Toast toast = Toast.makeText(getApplicationContext(),"感謝投票",Toast.LENGTH_SHORT);

        toast.setMargin(50,50);
        toast.show();

        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }



    public void initCandidates(){
        new syncTask().execute("candidates");


    }

    public static String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(
                        ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public void shareText(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBodyText = "2020tw.tw   2020總統大選即時民調";
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "選擇分享的方法"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        stopRepeatingTask();
//    }

    @Override
    public void onStop() {
        super.onStop();
        stopRepeatingTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        shareActionProvider =  MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "https://2020tw.tw  2020總統大選即時民調,需要你的投票";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"2020總統大選即時民調");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "選擇分享的方法"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private class syncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlAdress = "https://2020tw.tw/mindiao/candidate.php";

                Log.i("url", urlAdress);
                URL url = new URL(urlAdress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);



                InputStream is = conn.getInputStream();
                String parsedString = convertinputStreamToString(is);

                is.close();

                Log.i("data_from_service",parsedString);


                //syncCompleted("small");

                candiateJSArray = new JSONArray(parsedString);

                Log.i("json array",candiateJSArray.getJSONObject(1).toString());


            }catch (Exception e) {
                e.printStackTrace();

                Log.i("syncback error",e.toString());
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                DecimalFormat df = new DecimalFormat("#%");
                //System.out.println(df.format(0.19));

                if(candiateJSArray!=null) {


                    candidate1.setText("蔡英文   " + df.format(candiateJSArray.getJSONObject(0).getDouble("vote")) + "");

                    candidate2.setText("韓國瑜   " + df.format(candiateJSArray.getJSONObject(1).getDouble("vote")) + "");

                    candidate3.setText("柯文哲   " + df.format(candiateJSArray.getJSONObject(2).getDouble("vote")) + "");

                    candidate4.setText("郭台銘   " + df.format(candiateJSArray.getJSONObject(4).getDouble("vote")) + "");

                    candidate5.setText("沒意見   " + df.format(candiateJSArray.getJSONObject(3).getDouble("vote")) + "");

                    //candicateButtons.get(i).setText(candiateJSArray.getJSONObject(i).getString("name")+);

                    //candidate2.setText("");
                }


            }catch (JSONException i){

            }





        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }


    private class vote extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String vote = params[0];
                String  voteid = UUID.randomUUID().toString();

                String urlAdress ="https://2020tw.tw/mindiao/vote.php?vote="+vote+"&userGUID="+userGUID+"&voteid="+voteid;

                Log.i("url", urlAdress);
                URL url = new URL(urlAdress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);



                InputStream is = conn.getInputStream();
                String parsedString = convertinputStreamToString(is);

                is.close();

                Log.i("data_from_service",parsedString);


                //syncCompleted("small");

//                candiateJSArray = new JSONArray(parsedString);
//
//                Log.i("json array",candiateJSArray.getJSONObject(1).toString());


            }catch (Exception e) {
                e.printStackTrace();

                Log.i("syncback error",e.toString());
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {







        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
