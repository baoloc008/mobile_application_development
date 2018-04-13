package bk.loc.currencyconverter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import bk.loc.currencyconverter.models.Currency;
import bk.loc.currencyconverter.models.Rates;
import bk.loc.currencyconverter.retrofit.APIUtils;
import bk.loc.currencyconverter.retrofit.DataClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText edtYourMoney;
    private Spinner spinnerTo, spinnerFrom;
    private Button btnClear, btnConvert;
    private TextView tvResult;
    private DataClient dataClient;
    private String currency[] = {"AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTC","BTN","BWP","BYN","BZD","CAD","CDF","CHF","CLF","CLP","CNH","CNY","COP","CRC","CUC","CUP","CVE","CZK","DJF","DKK","DOP","DZD","EGP","ERN","ETB","EUR","FJD","FKP","GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","IMP","INR","IQD","IRR","ISK","JEP","JMD","JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO","MRU","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP","SLL","SOS","SRD","SSP","STD","STN","SVC","SYP","SZL","THB","TJS","TMT","TND","TOP","TRY","TTD","TWD","TZS","UAH","UGX","USD","UYU","UZS","VEF","VND","VUV","WST","XAF","XAG","XAU","XCD","XDR","XOF","XPD","XPF","XPT","YER","ZAR","ZMW","ZWL"};
    private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("loglog", "start");
        mapComponent();
        mDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        mDialog.setMessage("Fetching data...");
        mDialog.setCancelable(false);
        mDialog.show();
        dataClient = APIUtils.getData();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currency);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerFrom.setAdapter(arrayAdapter);
        spinnerTo.setAdapter(arrayAdapter);

        getCurrency();
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
            }
        });
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }
    private void getCurrency() {
        dataClient.getCurrency().enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(Call<Currency> call, Response<Currency> response) {
                if (response.isSuccessful()) {
                    mDialog.dismiss();
                    Rates rates = response.body().getRates();
                    Double m = null;
                    try {
                        m = (Double) rates.getClass().getMethod("get" + "VND").invoke(rates);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    Log.d("loglog", m.toString());



                }else {
                    Log.d("loglog", "loi 1");
                }
            }

            @Override
            public void onFailure(Call<Currency> call, Throwable t) {
                Log.d("loglog", "loi 2");
            }
        });
    }
    private void mapComponent() {
        edtYourMoney = findViewById(R.id.edtYourMoney);
        spinnerTo = findViewById(R.id.spinnerTo);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        btnClear = findViewById(R.id.btnClear);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);
    }
//    private class MyProcessEvent implements AdapterView.OnItemSelectedListener {
//        //Khi có chọn lựa thì vào hàm này
//        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            //arg2 là phần tử được chọn trong data source
//            selection.setText(arr[arg2]);
//        }
//        //Nếu không chọn gì cả
//        public void onNothingSelected(AdapterView<?> arg0) {
//            selection.setText("");
//        }
//    }
}
