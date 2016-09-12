package rebel.ngu.carlos.countryflags;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rebel.ngu.carlos.countryflags.JSONClasses.CountryComplex;
import rebel.ngu.carlos.countryflags.JSONClasses.CountryData;
import rebel.ngu.carlos.countryflags.Utilities.FlagImage;

import static rebel.ngu.carlos.countryflags.DataBase.DataBaseMethods.*;

public class CountryInfo extends AppCompatActivity {

    // Size of the flag in % to the total width of the device
    public static final double SIZE_FLAG_BIG = 0.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_info);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setVisibility(View.INVISIBLE);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Updating info?", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // SIZE OF DEVICE
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        // Draws the flag
        Intent intent = getIntent();
        String country_code = intent.getStringExtra("REBEL.NGU.COUNTRY_CODE");

        FlagImage fImg = new FlagImage(this, SIZE_FLAG_BIG, FlagImage.BIG_FLAG);
        ImageView flag = (ImageView) findViewById(R.id.imageView);

        try {
            fImg.getFlag(country_code, width, flag);
        } catch (FlagImage.ImgNotFoundException e) {
            e.printStackTrace();
        }

        // Country Information
        CountryData country_data = getCountryInfo(this,country_code);

        if(country_data!= null) {

            // Present date
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
            String stringNow = simpleDate.format(new Date());

            // Writes info in screen
            write_info_country(country_data.getCountry());


            // If NOT UPDATED => Updates database async
            if (!country_data.getDate().equals(stringNow)) {
                new DownloadCountryInfo().execute(country_code);
            }

        } else {    // If NOT FOUND => Updates database async
            new DownloadCountryInfo().execute(country_code);
        }

    }

    /**
     * Updates the database with the newest information of every country
     *
     * Should every country be updated or just only the one that is being viewed?
     * There are pros and cons in both cases, for this case only one update per day is needed
     * while the user can download all the information to use it later in an offline mode.
     */
    private class DownloadCountryInfo extends AsyncTask<String, Void, ArrayList<CountryComplex>> {

        @Override
        protected ArrayList<CountryComplex> doInBackground(String... params)  {

            ArrayList<CountryComplex> data = null;

            try {

                URL url = new URL("https://restcountries.eu/rest/v1/all");
//                URL url = new URL("https://restcountries.eu/rest/v1/alpha?codes=" + params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    data = CountryComplex.readAllCC(in);
                } finally {
                    urlConnection.disconnect();
                }

            } catch (IOException e) {
                return null;
            }

            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<CountryComplex> result) {

            updateDataBase(result, getApplicationContext());

        }
    }


    /**
     * Writes all information of the country in the text views
     * @param country       Country to extract the info
     */
    private void write_info_country (CountryComplex country) {

        // COUNTRY NAME
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(country.getCountry_name());

        // REST OF INFORMATION
        TextView textViewDetailed = (TextView) findViewById(R.id.textView2);

        String details = "";

        // NATIVE NAME
        if(country.getNative_name() != null && !country.getNative_name().equals("")) {
            details += "Native Name: " + country.getNative_name() + "\n";
        }

        // CALLING CODES
        if(country.getCallingCodes()!= null && !country.getCallingCodes().get(0).equals("")) {
            details += "Calling codes: ";
            for (String c_code : country.getCallingCodes()) {
                details += "+" + c_code + " ";
            }
            details+= "\n";
        }

        // REGION & SUBREGION
        if(country.getRegion() != null && !country.getRegion().equals("")) {
            details += "\n" + "Region : " + country.getRegion() + "\n";
        }
        if(country.getSubregion() != null && !country.getSubregion().equals("")) {
            details+=  "Subregion : " + country.getSubregion() + "\n";
        }

        details+= "\n";

        // SPOKEN LANGUAGES
        if(country.getLanguages() != null && country.getLanguages().size() != 0) {
            details += "Languages: ";
            for (String lang : country.getLanguages()) {
                details += lang + " ";
            }
            details+= "\n";
        }

        // POPULATION
        if(country.getPopulation() != 0) {
            details += "Population : " + country.getPopulation() + "\n";
        }

        details+= "\n";

        // CURRENCIES
        if(country.getCurrencies() != null && country.getCurrencies().size()!=0) {
            details += "Currencies:   ";
            for (String curr : country.getCurrencies()) {
                details += curr + "  ";
            }
        }

        details+= "\n\n";

        // TIMEZONES
        if(country.getTimezones()!= null && country.getTimezones().size()!=0) {
            details += "Timezones: ";
            for (String time : country.getTimezones()) {
                details += time + "\n";
            }
        }

        details += "\n\n\n\n\n\n\n";


        textViewDetailed.setText(details);

    }

}
