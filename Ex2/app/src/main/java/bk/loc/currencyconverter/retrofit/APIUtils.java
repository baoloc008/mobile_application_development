package bk.loc.currencyconverter.retrofit;

/**
 * Created by loc on 13/04/2018.
 */

public class APIUtils {
    public static final String BASE_URL = "https://openexchangerates.org/api/";
    public static DataClient getData() {
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
