package rebel.ngu.carlos.countryflags.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import rebel.ngu.carlos.countryflags.JSONClasses.CountryComplex;

import rebel.ngu.carlos.countryflags.DataBase.DataBaseUtils.*;
import rebel.ngu.carlos.countryflags.JSONClasses.CountryData;
import rebel.ngu.carlos.countryflags.JSONClasses.CountrySimple;

/**
 * Created by Carlos P on 10/09/2016.
 */
public class DataBaseMethods {

    // Date from the time the JSON old data was retrieved
    private static final String JSON_DATE = "2016-09-10";

    /**
     * Create database for the first time
     * @param countries
     * @param context
     */
    public static void createDataBase(ArrayList<CountryComplex> countries, Context context) {

        CountriesDbHelper cDbHelper = new CountriesDbHelper(context);
        // Gets the data repository in write mode
        SQLiteDatabase db = cDbHelper.getWritableDatabase();

        for(CountryComplex country : countries) {

            // C_CODES
            ContentValues values = getColumnsCCodes(country);

            long newRowId = -1;
            if(values != null) {
                newRowId = db.insert(CallingCodesEntry.TABLE_NAME, null, values);
            }


            values = getColumnsCountry(country,JSON_DATE, newRowId);
            // Insert the new row, returning the primary key value of the new row
            db.insert(CountriesEntry.TABLE_NAME, null, values);
        }

    }


    /**
     * Update database with information from internet
     * @param countries
     * @param context
     */
    public static void updateDataBase(ArrayList<CountryComplex> countries, Context context) {

        CountriesDbHelper cDbHelper = new CountriesDbHelper(context);
        // Gets the data repository in write mode
        SQLiteDatabase db = cDbHelper.getWritableDatabase();

        // Erases the previous information of the database
        db.delete(CountriesEntry.TABLE_NAME, null, null);
        db.delete(CallingCodesEntry.TABLE_NAME, null, null);

        // Adds the new information
        for(CountryComplex country : countries) {
            // C_CODES
            ContentValues values = getColumnsCCodes(country);

            long newRowId = -1;
            if(values != null) {
                newRowId = db.insert(CallingCodesEntry.TABLE_NAME, null, values);
            }

            values = getColumnsCountry(country, newRowId);
            // Insert the new row, returning the primary key value of the new row
            db.insert(CountriesEntry.TABLE_NAME, null, values);
        }
    }

    /**
     * Get a simple representation of every country stored in the database
     * @return The list of countries: with their name and their alpha 2 code for retrieving the flags
     */
    public static ArrayList<CountrySimple> getSimpleCountries(Context context) {
        ArrayList<CountrySimple> countries = new ArrayList<>();

        CountriesDbHelper cDbHelper = new CountriesDbHelper(context);
        SQLiteDatabase db = cDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CountriesEntry._ID,
                CountriesEntry.COLUMN_NAME,
                CountriesEntry.COLUMN_A2CODE
        };


        Cursor cursor = db.query(
                CountriesEntry.TABLE_NAME,                  // The table to query
                projection,                                 // The columns to return
                null,                                       // ALL COUNTRIES
                null,                                       // The values for the WHERE clause / ALL COUNTRIES
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );


        if (cursor.moveToFirst()) {
            do {
                CountrySimple country = new CountrySimple(cursor.getString(2), cursor.getString(1));
                // Adding contact to list
                countries.add(country);
            } while (cursor.moveToNext());
        }

        return countries;
    }


    /**
     * Get a simple representation of every country that matches the pattern
     * @param pattern   Pattern to match
     * @return The list of countries: with their name and their alpha 2 code for retrieving the flags
     */
    public static ArrayList<CountrySimple> getSimpleCountries(Context context, String pattern) {
        ArrayList<CountrySimple> countries = new ArrayList<>();

        CountriesDbHelper cDbHelper = new CountriesDbHelper(context);
        SQLiteDatabase db = cDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CountriesEntry._ID,
                CountriesEntry.COLUMN_NAME,
                CountriesEntry.COLUMN_A2CODE
        };

        // Filter results
        String selection = CountriesEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = { pattern+"%" };


        Cursor cursor = db.query(
                CountriesEntry.TABLE_NAME,                  // The table to query
                projection,                                 // The columns to return
                selection,                                  // The columns for the WHERE clause
                selectionArgs,                              // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                   // The sort order
        );


        if (cursor.moveToFirst()) {
            do {
                CountrySimple country = new CountrySimple(cursor.getString(2), cursor.getString(1));
                country.setId(cursor.getInt(0));
                // Adding contact to list
                countries.add(country);
            } while (cursor.moveToNext());
        }

        return countries;
    }










    /**
     * Get the information of a single country
     * @param context
     * @param a2code    Alpha 2 code representing this country
     * @return          Representation of all the information that was saved from that country or NULL if the country is not found.
     *
     */
    public static CountryData getCountryInfo(Context context, String a2code) {
        CountryData country_data = null;

        CountriesDbHelper cDbHelper = new CountriesDbHelper(context);
        SQLiteDatabase db = cDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CountriesEntry._ID,
                CountriesEntry.COLUMN_NAME,
                CountriesEntry.COLUMN_A2CODE,
                CountriesEntry.COLUMN_REGION,
                CountriesEntry.COLUMN_SUB_REGION,
                CountriesEntry.COLUMN_POP,
                CountriesEntry.COLUMN_N_NAME,
                CountriesEntry.COLUMN_DATE,
                CountriesEntry.COLUMN_C_CODES
        };

        // Filter results WHERE "COLUMN_A2CODE" = a2code
        String selection = CountriesEntry.COLUMN_A2CODE + " = ?";
        String[] selectionArgs = { a2code };


        Cursor cursor = db.query(
                CountriesEntry.TABLE_NAME,                // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't order the results
        );


        if (cursor.moveToFirst()) {
            CountryComplex country = new CountryComplex();
            country.setCountry_name(cursor.getString(1));
            country.setA2code(cursor.getString(2));
            country.setRegion(cursor.getString(3));
            country.setSubregion(cursor.getString(4));
            country.setPopulation(cursor.getInt(5));
            country.setNative_name(cursor.getString(6));
            country.setCallingCodes(retrieveCallingCodes (db, cursor.getLong(8)));

            String date = cursor.getString(7);

            country_data = new CountryData(country, date);
        }

        return country_data;
    }


    /**
     * Retrieves the calling codes assigned to this id
     * @param db    Database in which the data is stored
     * @param id
     * @return      A list of the calling codes
     */
    private static ArrayList<String> retrieveCallingCodes (SQLiteDatabase db, long id) {

        String[] projection = CallingCodesEntry.COLUMNS ();


        String selection = CallingCodesEntry._ID + " = ?";
        String[] selectionArgs = { id + "" };

        Cursor cursor = db.query(
                CallingCodesEntry.TABLE_NAME,                // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't order the results
        );

        ArrayList<String> c_codes = null;

        if (cursor.moveToFirst()) {
            c_codes = new ArrayList<String>();

            int i = 0;
            for(String str : projection) {
                if(cursor.getString(i)!= null) {
                    c_codes.add(cursor.getString(i));
                    i++;
                } else {
                    break;
                }
            }
        }

        return c_codes;
    }







    /**
     * Representation of the calling codes of one country to save in the database
     * @param country   country to be saved
     * @return          Values of country codes to save in the data base
     */
    private static ContentValues getColumnsCCodes(CountryComplex country) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        if(country.getCallingCodes()!=null && country.getCallingCodes().size()>0) {
            int i = 0;
            for (String c_code : country.getCallingCodes()) {
                values.put(CallingCodesEntry.COLUMN_CC(i), c_code);
                i++;
            }
        } else {
            return null;
        }

        return values;
    }

    /**
     * Representation of the country to save in the data base
     * @param country   country to be saved
     * @return          Values to save in the data base
     */
    private static ContentValues getColumnsCountry(CountryComplex country, long id_ccode) {
        // Create a new map of values, where column names are the keys

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String stringNow = simpleDate.format(new Date());

        return getColumnsCountry(country, stringNow, id_ccode);
    }

    /**
     * Representation of the country to save in the data base.
     * Used to store older data from a JSON file.
     * @param country   country to be saved
     * @param date      Date when this information was retrieved
     * @param id_ccode  Key pointing to the primary key of the calling code table that contains these country's calling codes
     * @return          Values to save in the data base
     */
    private static ContentValues getColumnsCountry(CountryComplex country, String date, long id_ccode) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(CountriesEntry.COLUMN_NAME, country.getCountry_name());
        values.put(CountriesEntry.COLUMN_A2CODE , country.getA2code());
        values.put(CountriesEntry.COLUMN_REGION, country.getRegion());
        values.put(CountriesEntry.COLUMN_SUB_REGION , country.getSubregion());
        values.put(CountriesEntry.COLUMN_POP , country.getPopulation());
        values.put(CountriesEntry.COLUMN_N_NAME , country.getNative_name());
        values.put(CountriesEntry.COLUMN_DATE , date);
        if(id_ccode >=0)
        values.put(CountriesEntry.COLUMN_C_CODES , id_ccode);

        return values;
    }




}
