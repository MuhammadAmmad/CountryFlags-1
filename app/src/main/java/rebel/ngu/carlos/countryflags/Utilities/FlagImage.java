package rebel.ngu.carlos.countryflags.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import static rebel.ngu.carlos.countryflags.Utilities.Utilities.decodeSampledBitmapFromResource;

/**
 * Created by Carlos P on 07/09/2016.
 */
public class FlagImage {

    // Types of flag
    public static final int SMALL_FLAG = 1;
    public static final int BIG_FLAG = 2;

    public final double SIZE_FLAG;

    Context context;
    private int type;

    /**
     *
     * @param size_flag Syze of the flag depending on the device width
     * @param type      Type of flag
     */
    public FlagImage(Context context, double size_flag, int type) {
        this.context = context;
        this.SIZE_FLAG = size_flag;
        this.type = type;
    }

    public ImageView getFlag(String code, int width) throws ImgNotFoundException {

        ImageView country_flag = new ImageView(context);

        getFlag( code, width, country_flag);

        return country_flag;
    }



    public void getFlag(String code, int width, ImageView country_flag) throws ImgNotFoundException {

        String id_code = code.toLowerCase();
        if(id_code.equals("do")) id_code="do_";  // do is a java word

        // Get the flag using the country code
        int id = context.getResources().getIdentifier(id_code,"drawable",context.getPackageName());

        // EXCEPTION - If the img is not found
        if(id==0) throw new ImgNotFoundException();

        // Calculate img height and weight of the resource img
        Drawable d = ContextCompat.getDrawable(context, id);
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();

        // png Flag into the Image View
        loadBitmap(id, country_flag, (int)(width*SIZE_FLAG),  (int)((h*width*SIZE_FLAG)/w));

        // Setting the params of this flag depending on the type
        if( type == SMALL_FLAG) {
            small_flag_setProperties(country_flag, width, h, w);
        } else if (type == BIG_FLAG) {
            big_flag_setProperties(country_flag, width, h, w);
        }
    }

    /**
     * Properties designed for the small flags
     * @param country_flag The ImageView of this flag
     */
    public void small_flag_setProperties(ImageView country_flag, int width, int h, int w) {
        // Size depending on the device screen
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int)(width*SIZE_FLAG),  (int)((h*width*SIZE_FLAG)/w));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT);
        layoutParams.setMargins(10, 10, 10, 10);

        country_flag.setLayoutParams(layoutParams);
    }

    /**
     * Properties designed for the small flags
     * @param country_flag The ImageView of this flag
     */
    public void big_flag_setProperties(ImageView country_flag, int width, int h, int w) {
        // Size depending on the device screen
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int)(width*SIZE_FLAG),  (int)((h*width*SIZE_FLAG)/w));
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.setMargins(20, 20, 20, 20);

        country_flag.setLayoutParams(layoutParams);
    }

    // In case the flag is not in our resources
    public class ImgNotFoundException extends Exception {
        public ImgNotFoundException(String message) {
            super(message);
        }
        public ImgNotFoundException() {
            super();
        }
    }

    private void loadBitmap(int resId, ImageView imageView, int width, int height) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId, width, height);
    }


    /**
     * Draws an image in its ImageView asynchronously
     */
    private class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        private BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(context.getResources(), data, params[1], params[2]);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
