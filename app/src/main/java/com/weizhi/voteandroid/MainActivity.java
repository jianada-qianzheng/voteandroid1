package com.weizhi.voteandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

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

    private Button start;

    private Button readReport;

    private ConstraintLayout screen1;
    private ConstraintLayout screen2;
    private ConstraintLayout screen3;
    private ConstraintLayout screen4;
    private ConstraintLayout screen5;
    private ConstraintLayout screen6;

    private Button nextStep2;
    private Button lastStep2;

    private SeekBar seekBar;

    private TextView seekView;

    private Button nextStep3;
    private Button lastStep3;

    private Button nextStep4;
    private Button lastStep4;

    private Button nextStep5;
    private Button lastStep5;

    private Button nextStep6;
    private Button lastStep6;

    private RadioButton radioButton3_1;

    private RadioButton radioButton3_2;
    private RadioButton radioButton3_3;
    private RadioButton radioButton3_4;


    private RadioButton radioButton5_1;

    private RadioButton radioButton5_2;
    private RadioButton radioButton5_3;
    private RadioButton radioButton5_4;
    private RadioButton radioButton5_5;
    private RadioButton radioButton5_6;

    private TextView candidate6_1;
    private TextView candidate6_2;
    private TextView candidate6_3;
    private TextView candidate6_4;
    private TextView candidate6_5;
    private TextView candidate6_6;

    private TextView percentage6_1;
    private TextView percentage6_2;
    private TextView percentage6_3;
    private TextView percentage6_4;
    private TextView percentage6_5;
    private TextView percentage6_6;

    private ProgressBar progressBar6_1;
    private ProgressBar progressBar6_2;
    private ProgressBar progressBar6_3;
    private ProgressBar progressBar6_4;
    private ProgressBar progressBar6_5;
    private ProgressBar progressBar6_6;

    private TextView total;

    private int age=19;

    private int hukou=0;

    int vote = 0;

    int sex = 4;



    private Handler mHandler;

    Dialog remindDialog;

    private int noticeNormal=3;
    private  int noticeImportant=1;

    private InterstitialAd mInterstitialAd;


    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(MainActivity.this,
                            "selected number "+numberPicker.getValue(), Toast.LENGTH_SHORT);
                    Log.i("picker"," "+i+" "+i1);

                    hukou=i1;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();

        getUserInfo();


        //initCandidates();



    }


    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }

    public void initUI(){
        //getSupportActionBar().hide();


        screen1=findViewById(R.id.screen1);
        screen2=findViewById(R.id.screen2);
        screen3=findViewById(R.id.screen3);
        screen4=findViewById(R.id.screen4);
        screen5=findViewById(R.id.screen5);
        screen6=findViewById(R.id.screen6);



        //screen1
        start=findViewById(R.id.start);

        //readReport=findViewById(R.id.read_report);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen1.setVisibility(View.GONE);
                screen2.setVisibility(View.VISIBLE);
                setTitle("請滑動滑塊選擇年齡");
                initCandidates();


            }
        });

//        readReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                screen1.setVisibility(View.GONE);
//                screen6.setVisibility(View.VISIBLE);
//                setTitle("民調結果");
//                initCandidates();
//            }
//        });




        //screen2

        nextStep2=findViewById(R.id.nextstep2);
        lastStep2=findViewById(R.id.laststep2);

        nextStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen2.setVisibility(View.GONE);
                screen3.setVisibility(View.VISIBLE);
                setTitle("請選擇你的性別");


                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        lastStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen1.setVisibility(View.VISIBLE);
                screen2.setVisibility(View.GONE);
                setTitle("總統大選即時民調");
            }
        });

        seekBar=findViewById(R.id.seekBar);
        seekView=findViewById(R.id.seekView);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                age=i;
                if(i<20){
                    seekView.setText("小於20歲");

                }else if(i>=20&&i<30){
                    seekView.setText("20歲~29歲");

                }else if(i>=30&&i<40){
                    seekView.setText("30歲~39歲");

                }else if(i>=40&&i<50){
                    seekView.setText("40歲~49歲");

                }else if(i>=50&&i<60){
                    seekView.setText("50歲~59歲");

                }else if(i>=60&&i<70){
                    seekView.setText("60歲~69歲");

                }else if(i>=70){
                    seekView.setText("70歲~120歲");

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //screen3

        nextStep3=findViewById(R.id.nextstep3);
        lastStep3=findViewById(R.id.laststep3);

        nextStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen3.setVisibility(View.GONE);
                screen4.setVisibility(View.VISIBLE);
                setTitle("請選擇你的戶籍地");
            }
        });

        lastStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen3.setVisibility(View.GONE);
                screen2.setVisibility(View.VISIBLE);
                setTitle("請滑動滑塊選擇年齡");
            }
        });

        radioButton3_1=findViewById(R.id.radioButton3_1);

        radioButton3_2=findViewById(R.id.radioButton3_2);
        radioButton3_3=findViewById(R.id.radioButton3_3);
        radioButton3_4=findViewById(R.id.radioButton3_4);

        radioButton3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex=1;
            }
        });

        radioButton3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex=2;
            }
        });

        radioButton3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex=3;
            }
        });

        radioButton3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex=4;
            }
        });



        //screen4
        nextStep4=findViewById(R.id.nextstep4);
        lastStep4=findViewById(R.id.laststep4);

        nextStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen4.setVisibility(View.GONE);
                screen5.setVisibility(View.VISIBLE);
                setTitle("請選擇");
            }
        });

        lastStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen4.setVisibility(View.GONE);
                screen3.setVisibility(View.VISIBLE);
                setTitle("請選擇你的性別");
            }
        });


        //screen5
        nextStep5=findViewById(R.id.nextstep5);
        lastStep5=findViewById(R.id.laststep5);

        nextStep5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                new vote().execute("vote");

                screen5.setVisibility(View.GONE);
                screen6.setVisibility(View.VISIBLE);
                setTitle("民調結果");

            }
        });

        lastStep5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen5.setVisibility(View.GONE);
                screen4.setVisibility(View.VISIBLE);
                setTitle("請選擇你的戶籍地");
            }
        });


        radioButton5_1=findViewById(R.id.radioButton5_1);

         radioButton5_2=findViewById(R.id.radioButton5_2);
         radioButton5_3=findViewById(R.id.radioButton5_3);
         radioButton5_4=findViewById(R.id.radioButton5_4);
       //  radioButton5_5=findViewById(R.id.radioButton5_5);
         //radioButton5_6=findViewById(R.id.radioButton5_6);

        radioButton5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vote=1;
            }
        });

        radioButton5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vote=2;
            }
        });

        radioButton5_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vote=3;
            }
        });

        radioButton5_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vote=4;
            }
        });

//        radioButton5_5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                vote=5;
//            }
//        });

//        radioButton5_6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                vote=6;
//            }
//        });


        //screen6
        nextStep6=findViewById(R.id.nextstep6);
        lastStep6=findViewById(R.id.laststep6);

        nextStep6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = candidate6_1.getText()+":"+percentage6_1.getText()+" "+candidate6_2.getText()+":"+percentage6_2.getText()+" "+candidate6_3.getText()+":"+percentage6_3.getText() + " 由 總統大選即時民調app發布";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "最新民調");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "請選擇"));



            }
        });

        lastStep6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mInterstitialAd.show();

                screen6.setVisibility(View.GONE);
                screen1.setVisibility(View.VISIBLE);
                setTitle("總統大選即時民調");



            }
        });

        candidate6_1=findViewById(R.id.candidate6_1);
        candidate6_2=findViewById(R.id.candidate6_2);
        candidate6_3=findViewById(R.id.candidate6_3);
        candidate6_4=findViewById(R.id.candidate6_4);


        percentage6_1=findViewById(R.id.percentage6_1);
        percentage6_2=findViewById(R.id.percentage6_2);
        percentage6_3=findViewById(R.id.percentage6_3);
        percentage6_4=findViewById(R.id.percentage6_4);


        progressBar6_1=findViewById(R.id.progressBar6_1);
        progressBar6_2=findViewById(R.id.progressBar6_2);
        progressBar6_3=findViewById(R.id.progressBar6_3);
        progressBar6_4=findViewById(R.id.progressBar6_4);

        //total=findViewById(R.id.total);




        mHandler = new Handler();




//        candidate1=findViewById(R.id.candidate1);
//        candidate2=findViewById(R.id.candidate2);
//        candidate3=findViewById(R.id.candidate3);
//        candidate4=findViewById(R.id.candidate4);
//        candidate5=findViewById(R.id.candidate5);
//
//        candicateButtons.add(candidate1);
//        candicateButtons.add(candidate2);
//        candicateButtons.add(candidate3);
//        candicateButtons.add(candidate4);
//        candicateButtons.add(candidate5);

        MobileAds.initialize(this,"ca-app-pub-6463506718592054~4931635628");//TODO



        mAdView = findViewById(R.id.adView2);

//        mAdView.setAdSize(AdSize.LARGE_BANNER);
        //mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        NumberPicker np = findViewById(R.id.numberPicker);

        np.setMinValue(0);
        np.setMaxValue(22);

        np.setDisplayedValues( new String[] { "請選擇你的戶口所在地" , "新北市","臺北市", "桃園市", "臺中市", "臺南市","高雄市","宜蘭縣","新竹縣","苗栗縣","彰化縣","南投縣","雲林縣","嘉義縣","屏東縣","台東縣","花蓮縣","澎湖縣","基隆市","新竹市","嘉義市","金門縣","連江縣" } );


        np.setOnValueChangedListener(onValueChangeListener);

        remindDialog=new Dialog(this);

        subscribe();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6463506718592054/3678733863");


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




         String CREATE_TABLE_REMIND_SETTING = "create table if not exists remindsetting ("
                + "normal integer,"


                + "important integer)";

        db.execSQL(CREATE_TABLE_REMIND_SETTING);
        db.execSQL("insert into remindsetting values(3,1);");




        Cursor cursor1 = db.rawQuery("select * from remindsetting",null);

        if(cursor1.moveToNext()){
            noticeNormal=cursor1.getInt(0);
            noticeImportant=cursor1.getInt(1);
        }

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
        startActivity(Intent.createChooser(intent, "選擇分享方式"));
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
        MenuItem item = menu.findItem(R.id.remind_setting);

        // Fetch and store ShareActionProvider
        shareActionProvider =  MenuItemCompat.getActionProvider(item);

        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remind_setting:

                //todo setting pressed

                remindDialog.setContentView(R.layout.remind_setting);

//                final EditText left_count= (EditText) myDialog.findViewById(R.id.left_config);
//                final EditText arrive_count= (EditText) myDialog.findViewById(R.id.arrive_config);

                final RadioButton oneDayButton=(RadioButton) remindDialog.findViewById(R.id.one_day_button);
                final RadioButton threeDayButton=(RadioButton) remindDialog.findViewById(R.id.three_day_button);
                final RadioButton sevenDayButton=(RadioButton) remindDialog.findViewById(R.id.seven_day_button);

                final CheckBox checkBox=(CheckBox) remindDialog.findViewById(R.id.checkBox);
                Button compButton=(Button) remindDialog.findViewById(R.id.comp_button);





                if(noticeNormal==1&&!oneDayButton.isSelected()){
                    oneDayButton.toggle();
                }else if(noticeNormal==3&&!threeDayButton.isSelected()){
                    threeDayButton.toggle();
                }else if (noticeNormal==7&&!sevenDayButton.isSelected()){
                    sevenDayButton.toggle();
                }

                if(noticeImportant==1&&!checkBox.isChecked()){
                    checkBox.toggle();
                }


                remindDialog.show();


                compButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(oneDayButton.isChecked()){
                            noticeNormal=1;
                        }
                        if (threeDayButton.isChecked()){
                            noticeNormal=3;
                        }
                        if(sevenDayButton.isChecked()){
                            noticeNormal=7;
                        }

                        if(checkBox.isChecked()){
                            noticeImportant=1;
                        }else{
                            noticeImportant=0;
                        }

                        subscribe();
                        remindDialog.dismiss();

                        Log.i("notice_normal"," "+noticeNormal);

                        db.execSQL("update remindsetting set normal="+noticeNormal+",important="+noticeImportant+";");
                        Toast.makeText(getApplicationContext(),"設定成功",Toast.LENGTH_LONG).show();


                    }
                });




                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void subscribe(){
        //todo

        if(noticeImportant==1) {
            FirebaseMessaging.getInstance().subscribeToTopic("important").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"成功訂閱重大事件提醒",Toast.LENGTH_LONG).show();
                }
            });
        }else if(noticeImportant==0){
            FirebaseMessaging.getInstance().unsubscribeFromTopic("important").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   // Toast.makeText(getApplicationContext(),"成功取消訂閱重大事件提醒",Toast.LENGTH_LONG).show();
                }
            });

        }

        if(noticeNormal==1) {

            FirebaseMessaging.getInstance().subscribeToTopic("everyday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });
            FirebaseMessaging.getInstance().unsubscribeFromTopic("thirdday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });

            FirebaseMessaging.getInstance().unsubscribeFromTopic("seventhday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });
        }else if (noticeNormal==3){
            FirebaseMessaging.getInstance().unsubscribeFromTopic("everyday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });
            FirebaseMessaging.getInstance().subscribeToTopic("thirdday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });

            FirebaseMessaging.getInstance().unsubscribeFromTopic("seventhday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });

        }else if(noticeNormal==7){
            FirebaseMessaging.getInstance().unsubscribeFromTopic("everyday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });
            FirebaseMessaging.getInstance().unsubscribeFromTopic("thirdday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });

            FirebaseMessaging.getInstance().subscribeToTopic("seventhday").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                }
            });

        }

    }


    private class syncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlAdress = "https://2020tw.tw/mindiao/candidate2.php";

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

                DecimalFormat df = new DecimalFormat("#.##%");
                //System.out.println(df.format(0.19));df.format(candiateJSArray.getJSONObject(4).getString("name")

                if(candiateJSArray!=null) {

                    if(candiateJSArray.getJSONObject(0)!=null){
                        if(candiateJSArray.getJSONObject(0).getInt("active")==0){
                            radioButton5_1.setVisibility(View.GONE);
                            candidate6_1.setVisibility(View.GONE);
                            percentage6_1.setVisibility(View.GONE);
                            progressBar6_1.setVisibility(View.GONE);
                        }

                        radioButton5_1.setText(candiateJSArray.getJSONObject(0).getString("name"));

                        candidate6_1.setText(candiateJSArray.getJSONObject(0).getString("name"));
                        percentage6_1.setText(  df.format(candiateJSArray.getJSONObject(0).getDouble("vote"))    );

                        progressBar6_1.setProgress(  (int) Math.round(candiateJSArray.getJSONObject(0).getDouble("vote")*100)    );
                    }

                    if(candiateJSArray.getJSONObject(1)!=null){
                        if(candiateJSArray.getJSONObject(1).getInt("active")==0){
                            radioButton5_2.setVisibility(View.GONE);
                            candidate6_2.setVisibility(View.GONE);
                            percentage6_2.setVisibility(View.GONE);
                            progressBar6_2.setVisibility(View.GONE);
                        }
                        radioButton5_2.setText(candiateJSArray.getJSONObject(1).getString("name"));
                        candidate6_2.setText(candiateJSArray.getJSONObject(1).getString("name"));
                        percentage6_2.setText(  df.format(candiateJSArray.getJSONObject(1).getDouble("vote"))    );

                        progressBar6_2.setProgress(  (int) Math.round(candiateJSArray.getJSONObject(1).getDouble("vote")*100)    );

                    }

                    if(candiateJSArray.getJSONObject(2)!=null){
                        if(candiateJSArray.getJSONObject(2).getInt("active")==0){
                            radioButton5_3.setVisibility(View.GONE);
                            candidate6_3.setVisibility(View.GONE);
                            percentage6_3.setVisibility(View.GONE);
                            progressBar6_3.setVisibility(View.GONE);
                        }
                        radioButton5_3.setText(candiateJSArray.getJSONObject(2).getString("name"));

                        candidate6_3.setText(candiateJSArray.getJSONObject(2).getString("name"));
                        percentage6_3.setText(  df.format(candiateJSArray.getJSONObject(2).getDouble("vote"))    );

                        progressBar6_3.setProgress(  (int) Math.round(candiateJSArray.getJSONObject(2).getDouble("vote")*100)    );

                    }

                    if(candiateJSArray.getJSONObject(3)!=null){
                        if(candiateJSArray.getJSONObject(3).getInt("active")==0){
                            radioButton5_4.setVisibility(View.GONE);
                            candidate6_4.setVisibility(View.GONE);
                            percentage6_4.setVisibility(View.GONE);
                            progressBar6_4.setVisibility(View.GONE);
                        }
                        radioButton5_4.setText(candiateJSArray.getJSONObject(3).getString("name"));
                        candidate6_4.setText(candiateJSArray.getJSONObject(3).getString("name"));
                        percentage6_4.setText(  df.format(candiateJSArray.getJSONObject(3).getDouble("vote"))    );

                        progressBar6_4.setProgress(  (int) Math.round(candiateJSArray.getJSONObject(3).getDouble("vote")*100)    );

                    }

                    if(candiateJSArray.getJSONObject(4)!=null){
                        if(candiateJSArray.getJSONObject(4).getInt("active")==0){
                            radioButton5_5.setVisibility(View.GONE);
                            candidate6_5.setVisibility(View.GONE);
                            percentage6_5.setVisibility(View.GONE);
                            progressBar6_5.setVisibility(View.GONE);
                        }
                        radioButton5_5.setText(candiateJSArray.getJSONObject(4).getString("name"));
                        candidate6_5.setText(candiateJSArray.getJSONObject(4).getString("name"));
                        percentage6_5.setText(  df.format(candiateJSArray.getJSONObject(4).getDouble("vote"))    );

                        progressBar6_5.setProgress(  (int) Math.round(candiateJSArray.getJSONObject(4).getDouble("vote")*100)    );

                    }

                    if(candiateJSArray.getJSONObject(5)!=null){
                        if(candiateJSArray.getJSONObject(5).getInt("active")==0){
                            radioButton5_6.setVisibility(View.GONE);
                            candidate6_6.setVisibility(View.GONE);
                            percentage6_6.setVisibility(View.GONE);
                            progressBar6_6.setVisibility(View.GONE);
                        }
                        radioButton5_6.setText(candiateJSArray.getJSONObject(5).getString("name"));
                        candidate6_6.setText(candiateJSArray.getJSONObject(5).getString("name"));
                        percentage6_6.setText(  df.format(candiateJSArray.getJSONObject(5).getDouble("vote"))    );

                        progressBar6_6.setProgress(  (int) Math.round(candiateJSArray.getJSONObject(5).getDouble("vote")*100)    );

                    }

                    total.setText("有效樣本數："+candiateJSArray.getJSONObject(0).getString("count"));



//                    candidate1.setText("蔡英文   " + df.format(candiateJSArray.getJSONObject(0).getDouble("vote")) + "");
//
//                    candidate2.setText("韓國瑜   " + df.format(candiateJSArray.getJSONObject(1).getDouble("vote")) + "");
//
//                    candidate3.setText("柯文哲   " + df.format(candiateJSArray.getJSONObject(2).getDouble("vote")) + "");
//
//                    candidate4.setText("郭台銘   " + df.format(candiateJSArray.getJSONObject(4).getDouble("vote")) + "");
//
//                    candidate5.setText("沒意見   " + df.format(candiateJSArray.getJSONObject(3).getDouble("vote")) + "");

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
                //String vote = params[0];



                String  voteid = UUID.randomUUID().toString();


                String urlAdress ="https://2020tw.tw/mindiao/vote2.php?age="+age+"&sex="+sex+"&hukou="+hukou+"&vote="+vote+"&userGUID="+userGUID+"&voteId="+voteid;

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
