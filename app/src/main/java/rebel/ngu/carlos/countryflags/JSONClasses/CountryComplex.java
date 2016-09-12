package rebel.ngu.carlos.countryflags.JSONClasses;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Carlos P on 07/09/2016.
 */
public class CountryComplex {

    private String country_name;
    private String a2code;
    private String native_name;
    private ArrayList<String> callingCodes;
    private String region;
    private String subregion;
    private ArrayList<String> languages;
    private int population;

    private ArrayList<String> timezones;
    private ArrayList<String> currencies;


    private String[] name_translations;
    /**
     * Avaiable tranlations
     */
    public static final int NUMBER_TRANS = 5;
    public static final int TRANS_DE = 0;   // German
    public static final int TRANS_ES = 1;   // Spanish
    public static final int TRANS_FR = 2;   // French
    public static final int TRANS_JA = 3;   // Japanese
    public static final int TRANS_IT = 4;   // Italian

    public CountryComplex() {
        population = 0;
    }


    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    public String getCountry_name() {
        return country_name;
    }

    public String getA2code() {        return a2code;    }
    public void setA2code(String a2code) {        this.a2code = a2code;    }

    public String[] getName_translations() {        return name_translations;    }
    public void setName_translations(String[] name_translations) {        this.name_translations = name_translations;    }

    public int getPopulation() {        return population;    }
    public void setPopulation(int population) {        this.population = population;    }

    public ArrayList<String> getLanguages() {        return languages;    }
    public void setLanguages(ArrayList<String> languages) {        this.languages = languages;    }

    public String getSubregion() {        return subregion;    }
    public void setSubregion(String subregion) {        this.subregion = subregion;    }

    public String getRegion() {        return region;    }
    public void setRegion(String region) {        this.region = region;    }

    public ArrayList<String> getCallingCodes() {        return callingCodes;    }
    public void setCallingCodes(ArrayList<String> callingCodes) {        this.callingCodes = callingCodes;    }

    public String getNative_name() {        return native_name;    }
    public void setNative_name(String native_name) {        this.native_name = native_name;    }

    public ArrayList<String> getTimezones() {        return timezones;    }
    public void setTimezones(ArrayList<String> timezones) {        this.timezones = timezones;    }

    public ArrayList<String> getCurrencies() {        return currencies;    }
    public void setCurrencies(ArrayList<String> currencies) {        this.currencies = currencies;    }


    /**
     * Returns all the countries from the list.
     * Reads the JSON file from the webpage
     */
    public static ArrayList<CountryComplex> readAllCC(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        ArrayList<CountryComplex> countries = new ArrayList<>();
        try {
            reader.beginArray();

            while(reader.hasNext()) {

                reader.beginObject();

                countries.add( readCCObject(reader) );

                reader.endObject();

            }
            reader.endArray();

            return countries;
        } finally {
            reader.close();
        }
    }



    /**
     * Gives the first country of the list. Made for queries that return a unique country.
     * Reads the JSON file from the webpage
     */
    public static CountryComplex readComplexCC(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            reader.beginArray();

            if(!reader.hasNext()) return null;  // In case there is no objects

            reader.beginObject();

            CountryComplex country = readCCObject(reader);

            reader.endObject();
            reader.endArray();

            return country;
        } finally {
            reader.close();
        }
    }

    public static CountryComplex readCCObject(JsonReader reader) throws IOException {
        CountryComplex country = new CountryComplex();


        while (reader.hasNext()) {
            readCountry(reader,country);
        }

        return country;
    }

    public static void readCountry(JsonReader reader, CountryComplex country) throws IOException {

        String key = reader.nextName();
        if (key.equals("name")) {
            String name = reader.nextString();
            country.setCountry_name(name);

        } else if (key.equals("alpha2Code")) {
            String alpha2Code = reader.nextString();
            country.setA2code(alpha2Code);

        } else if (key.equals("nativeName")) {
            String nat_name = reader.nextString();
            country.setNative_name(nat_name);

        } else if (key.equals( "callingCodes")) {
            ArrayList<String> callingCodes = getCallingCodes(reader);
            country.setCallingCodes(callingCodes);

        } else if (key.equals("region")) {
            String region = reader.nextString();
            country.setRegion(region);

        } else if (key.equals("subregion")) {
            String subregion = reader.nextString();
            country.setSubregion(subregion);

        } else if (key.equals("languages")) {
            ArrayList<String> languages = getLanguages(reader);
            country.setLanguages(languages);

        } else if (key.equals("population")) {
            int population = reader.nextInt();
            country.setPopulation(population);

        } else if (key.equals("translations")) {
            String[] translations = getTranslations(reader);
            country.setName_translations(translations);

        } else if (key.equals("timezones")) {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                country.setTimezones(new ArrayList<String>());
            } else {
                ArrayList<String> timezones = getTimezones(reader);
                country.setTimezones(timezones);
            }
        } else if (key.equals("currencies")) {
            ArrayList<String> currencies = getCurrencies(reader);
            country.setCurrencies(currencies);

        } else {
            reader.skipValue();
        }




    }

    private static ArrayList<String> getTimezones (JsonReader reader) throws IOException {
        ArrayList<String> timezones = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            String t_zone = reader.nextString();
            timezones.add(t_zone);
        }
        reader.endArray();

        return timezones;
    }
    private static ArrayList<String> getCurrencies (JsonReader reader) throws IOException {
        ArrayList<String> currencies = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            String currency = reader.nextString();
            currencies.add(currency);
        }
        reader.endArray();

        return currencies;
    }



    private static ArrayList<String> getCallingCodes (JsonReader reader) throws IOException {
        ArrayList<String> callingCodes = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            String c_code = reader.nextString();
            callingCodes.add(c_code);
        }
        reader.endArray();

        return callingCodes;
    }

    private static ArrayList<String> getLanguages (JsonReader reader) throws IOException {
        ArrayList<String> languages = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            String lang = reader.nextString();
            languages.add(lang);
        }
        reader.endArray();

        return languages;
    }


    private static String[] getTranslations (JsonReader reader) throws IOException {
        String[] translations = new String[NUMBER_TRANS];

        reader.beginObject();
        while (reader.hasNext()) {
            String key = reader.nextName();
            if (key.equals("de")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                } else {
                    String name = reader.nextString();
                    translations[TRANS_DE] = name;
                }

            } else if (key.equals("es")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                } else {
                    String name = reader.nextString();
                    translations[TRANS_ES] = name;
                }

            } else if (key.equals("fr")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                } else {
                    String name = reader.nextString();
                    translations[TRANS_FR] = name;
                }

            } else if (key.equals("ja")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                } else {
                    String name = reader.nextString();
                    translations[TRANS_JA] = name;
                }

            } else if (key.equals("IT")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                } else {
                    String name = reader.nextString();
                    translations[TRANS_IT] = name;
                }

            } else {
                reader.skipValue();
            }

        }
        reader.endObject();

        return translations;
    }


}
