package rebel.ngu.carlos.countryflags.JSONClasses;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Carlos P on 06/09/2016.
 */
public class CountrySimple {

    private String code;
    private String country_name;
    private int id;


    public CountrySimple (String code, String country_name) {
        this.code = code;
        this.country_name = country_name;
    }


    public String getCountry_name() {
        return country_name;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Reads the initial JSON file with 2 letter codes and country names
     */
    public static ArrayList<CountrySimple> readSimpleCC(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readCCArray(reader);
        } finally {
            reader.close();
        }
    }

    public static ArrayList<CountrySimple> readCCArray(JsonReader reader) throws IOException {
        ArrayList<CountrySimple> countries = new ArrayList<CountrySimple>();

        reader.beginObject();
        while (reader.hasNext()) {
            countries.add(readCountry(reader));
        }
        reader.endObject();
        return countries;
    }

    public static CountrySimple readCountry(JsonReader reader) throws IOException {
        String    c_code = reader.nextName();
        String    c_name = reader.nextString();

        return new CountrySimple(c_code, c_name);
    }


}
