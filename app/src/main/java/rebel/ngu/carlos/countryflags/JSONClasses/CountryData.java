package rebel.ngu.carlos.countryflags.JSONClasses;

/**
 * Class that contains the information of a country and the date this information was retrieved from internet
 * Created by Carlos P on 11/09/2016.
 */
public class CountryData {

    private CountryComplex country;
    private String date;

    public CountryData ( CountryComplex country, String date ) {
        this.country = country;
        this.date = date;
    }


    public String getDate() {
        return date;
    }

    public CountryComplex getCountry() {
        return country;
    }
}
