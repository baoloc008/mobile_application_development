package bk.loc.currencyconverter.retrofit;

import bk.loc.currencyconverter.models.Currency;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by loc on 13/04/2018.
 */

public interface DataClient {

    @GET("/latest.json?app_id=0f43fc8b687848529f6527a4bb53b67b")
    Call<Currency> getCurrency();

}
