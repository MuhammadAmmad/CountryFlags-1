package rebel.ngu.carlos.countryflags;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rebel.ngu.carlos.countryflags.JSONClasses.CountryComplex;
import rebel.ngu.carlos.countryflags.JSONClasses.CountrySimple;
import rebel.ngu.carlos.countryflags.Utilities.FlagImage;
import rebel.ngu.carlos.countryflags.Utilities.LayoutListener;
import rebel.ngu.carlos.countryflags.Utilities.Utilities;

import static rebel.ngu.carlos.countryflags.DataBase.DataBaseMethods.*;
import static rebel.ngu.carlos.countryflags.Utilities.Utilities.decodeSampledBitmapFromResource;

public class Flags extends AppCompatActivity  implements SearchView.OnQueryTextListener {

    // Size of the flag in % to the total width of the device
    public static final double SIZE_FLAG_SMALL = 0.06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags);

        // Linear Layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);



        ArrayList<CountrySimple> country_list = null;
        country_list = getSimpleCountries(this);
        if(country_list.size() == 0) {  // DB is not created yet
            try {
                AssetManager am = getAssets();
                InputStream inputStream = am.open("allcountries.json");
                ArrayList<CountryComplex> cc_list = CountryComplex.readAllCC(inputStream);
                createDataBase(cc_list, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        country_list = getSimpleCountries(this);


        // SIZE OF DEVICE
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // DIVIDER
        addDivider(layout);

        // Creates small size flags
        FlagImage fImg = new FlagImage(this, SIZE_FLAG_SMALL, FlagImage.SMALL_FLAG);

        // Displays every country (FLAG AND NAME) in the layout
        for (CountrySimple country : country_list) {

            RelativeLayout h_layout = new RelativeLayout(this);
            h_layout.setMinimumHeight((int)(width*SIZE_FLAG_SMALL));
            layout.addView(h_layout);

            // FLAG
            ImageView country_flag = null;
            try {
                // Gets ImageView of the flag async
                country_flag = fImg.getFlag(country.getCode(), width);
                h_layout.addView(country_flag);
            } catch (FlagImage.ImgNotFoundException e) {
                e.printStackTrace();
            }

            // NAME
            TextView country_name = new TextView(this);
            country_name.setText( country.getCountry_name());

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);

            // ClickListener for this country layout
            LayoutListener l_listener = new LayoutListener(country.getCode());
            h_layout.setOnClickListener(l_listener);
            h_layout.setOnTouchListener(l_listener);

            // Add this layout to the main one
            h_layout.addView(country_name, layoutParams);


            // DIVIDER
            addDivider(layout);
        }

    }

    private void addDivider(LinearLayout layout) {
        ImageView divider = new ImageView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        lp.setMargins(10, 10, 10, 10);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(Color.BLACK);
        layout.addView(divider);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text

        // Linear Layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);

        for(int i=1;i<layout.getChildCount();i++) {
            layout.getChildAt(i).setVisibility(View.GONE);
        }

        ArrayList<CountrySimple> country_list = getSimpleCountries(this, newText);

        for(CountrySimple country :  country_list) {
            layout.getChildAt(country.getId()*2-1).setVisibility(View.VISIBLE);
            layout.getChildAt(country.getId()*2).setVisibility(View.VISIBLE);
        }


        return true;
    }


}
