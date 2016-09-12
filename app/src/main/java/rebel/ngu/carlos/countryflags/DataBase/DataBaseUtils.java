package rebel.ngu.carlos.countryflags.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Carlos P on 10/09/2016.
 */
public class DataBaseUtils {

    private DataBaseUtils() {}

    public static class CountriesEntry implements BaseColumns {
        public static final String TABLE_NAME = "countries";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_A2CODE = "a2code";
        public static final String COLUMN_REGION = "region";
        public static final String COLUMN_SUB_REGION = "sub_region";
        public static final String COLUMN_POP = "population";
        public static final String COLUMN_N_NAME = "native_name";
        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_C_CODES = "c_codes";


        private static final String TEXT_TYPE = " TEXT";
        private static final String INT_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_A2CODE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_REGION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_SUB_REGION + TEXT_TYPE + COMMA_SEP +
                        COLUMN_POP + INT_TYPE + COMMA_SEP +
                        COLUMN_N_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_C_CODES + INT_TYPE + COMMA_SEP +
                        "FOREIGN KEY(" + COLUMN_C_CODES + ") REFERENCES " +
                        CallingCodesEntry.TABLE_NAME + "(" + CallingCodesEntry._ID + ") " +
                        " )";


        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public static class CallingCodesEntry implements BaseColumns {
        public static final String TABLE_NAME = "calling_codes";

        public static final int NUMBER_C_CODES = 9; // Number max. of calling codes to save



        public static final String[] COLUMNS () {
            String[] columns = new String[NUMBER_C_CODES];
            for(int i = 0; i<NUMBER_C_CODES; i++) {
                columns[i] = "cc" + i;
            }
            return columns;
        }

        public static final String COLUMN_CC (int column) {
            return COLUMNS ()[column];
        }

        private static final String TEXT_TYPE = " TEXT";
        private static final String INT_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES() {

            String create = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY";

            int i=0;

            for(String col : COLUMNS()) {
                create += COMMA_SEP+COLUMN_CC (i)+TEXT_TYPE;
                i++;
            }
            create += " )";

            return create;
        }

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


}
