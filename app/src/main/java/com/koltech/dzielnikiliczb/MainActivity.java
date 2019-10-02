package com.koltech.dzielnikiliczb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    EditText Wejscie;
    TextView PrzeliczeniePow;
    TextView Ouput;
    private AdView mAdView;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.dzielniki_liczb);
        Wejscie = (EditText)findViewById(R.id.editText);
        PrzeliczeniePow = (TextView)findViewById(R.id.textView2);
        Ouput=(TextView) findViewById(R.id.textViewOput);
       // Wyniki =(Layout) findViewById(R.layout.dzielniki_wyniki);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    public void Sprawdz(View v){

        String Input = Wejscie.getText().toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if(Input.equals(""))
        {
            Powiadomienie(getString(R.string.Wartosc_wej));
        }

        try {

        if(Long.parseLong(Input) > 2147483646)
        {
            Powiadomienie(getString(R.string.Wartosc_wejsciowa_poza_zasiengiem));
        }
        else
        {
        AsynckOblicz task = new AsynckOblicz();
        task.execute(Input);
        }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }



     private  class AsynckOblicz extends AsyncTask<String,String,String>{


         @Override
         protected void onPreExecute() {
             super.onPreExecute();
            // Toast.makeText(getApplicationContext(), "start wontku", Toast.LENGTH_SHORT).show();
         }

         @Override
         protected String doInBackground(String... parametr) {

             int Wejscieint = Integer.parseInt(parametr[0]);
             int ouput;
             String CiongWyjsciowy="";

             for (int i = 1; i < Wejscieint; i++) {
                 try {
                     ouput = Wejscieint / i;
                     if (Wejscieint == ouput * i) {

                         CiongWyjsciowy = CiongWyjsciowy + "\n" + i;
                     }

                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             }
             return CiongWyjsciowy;

     }

         @Override
         protected void onPostExecute(String CiongWyjsciowy) {
             super.onPostExecute(CiongWyjsciowy);


           Ouput.setText(CiongWyjsciowy + "\n" + Wejscie.getText().toString());
           PrzeliczeniePow.setText(getString(R.string.wyniki_dla)+" " + Wejscie.getText().toString()+" :");


         }

    }







    void Powiadomienie(String tresc){

        Toast.makeText(getApplicationContext(), tresc, Toast.LENGTH_SHORT).show();

    }




}
