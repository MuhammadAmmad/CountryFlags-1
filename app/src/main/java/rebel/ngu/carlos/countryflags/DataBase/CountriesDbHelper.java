package rebel.ngu.carlos.countryflags.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos P on 10/09/2016.
 */
public class CountriesDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Countries";

    public CountriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseUtils.CountriesEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DataBaseUtils.CallingCodesEntry.SQL_CREATE_ENTRIES());
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DataBaseUtils.CountriesEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DataBaseUtils.CallingCodesEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }





}
