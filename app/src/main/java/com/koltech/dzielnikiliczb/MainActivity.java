package com.koltech.dzielnikiliczb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    EditText Wejscie;
    TextView PrzeliczeniePow;
    TextView Ouput;
    private AdView adView;
    int dzielnik;

    private FrameLayout adContainerView;
    private static final String AD_UNIT_ID = "ca-app-pub-4834003578511022/6062611200";

    private static long back_pressed;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

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

        adContainerView = findViewById(R.id.adFrame);

        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });


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

                         CiongWyjsciowy = CiongWyjsciowy + "\n" + i+"    ||   "+ouput;
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


    @Override
    public void onBackPressed()
    {
        if (back_pressed + 4000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), R.string.pop_up_zamykanie, Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }


    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(this);
        adView.setAdUnitId(AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }



}
