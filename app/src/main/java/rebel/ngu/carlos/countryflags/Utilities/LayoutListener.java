package rebel.ngu.carlos.countryflags.Utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import rebel.ngu.carlos.countryflags.CountryInfo;
import rebel.ngu.carlos.countryflags.R;


/**
 * Created by Carlos P on 07/09/2016.
 */
public class LayoutListener implements View.OnClickListener, View.OnTouchListener {

    private String country_code = null;

    public LayoutListener(String country_code) {
        this.country_code = country_code;
    }

    public void onClick(View view) {

        Context context = view.getContext();
        Intent intent = new Intent(context, CountryInfo.class);

        intent.putExtra("REBEL.NGU.COUNTRY_CODE", country_code);

        context.startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if( event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setBackgroundResource(R.color.greyPressed);
            return true;
        } else if( event.getAction() == MotionEvent.ACTION_UP) {
            v.setBackgroundResource(R.color.background);
            v.performClick();
            return true;
        } else if( event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.setBackgroundResource(R.color.background);
            return true;
        }
        return false;
    }
}
