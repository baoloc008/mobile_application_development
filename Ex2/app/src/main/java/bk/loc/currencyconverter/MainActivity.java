package bk.loc.currencyconverter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

import bk.loc.currencyconverter.models.Currency;
import bk.loc.currencyconverter.models.Rates;
import bk.loc.currencyconverter.retrofit.APIUtils;
import bk.loc.currencyconverter.retrofit.DataClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText edtYourMoney, edtResult;
    private Spinner spinnerTo, spinnerFrom;
    private Button btnClear, btnConvert, btnRefresh;
    private DataClient dataClient;
    private String currency[] = {"AED","AFN","ALL","AMD","ANG","AOA","ARS","AUD","AWG","AZN","BAM","BBD","BDT","BGN","BHD","BIF","BMD","BND","BOB","BRL","BSD","BTC","BTN","BWP","BYN","BZD","CAD","CDF","CHF","CLF","CLP","CNH","CNY","COP","CRC","CUC","CUP","CVE","CZK","DJF","DKK","DOP","DZD","EGP","ERN","ETB","EUR","FJD","FKP","GBP","GEL","GGP","GHS","GIP","GMD","GNF","GTQ","GYD","HKD","HNL","HRK","HTG","HUF","IDR","ILS","IMP","INR","IQD","IRR","ISK","JEP","JMD","JOD","JPY","KES","KGS","KHR","KMF","KPW","KRW","KWD","KYD","KZT","LAK","LBP","LKR","LRD","LSL","LYD","MAD","MDL","MGA","MKD","MMK","MNT","MOP","MRO","MRU","MUR","MVR","MWK","MXN","MYR","MZN","NAD","NGN","NIO","NOK","NPR","NZD","OMR","PAB","PEN","PGK","PHP","PKR","PLN","PYG","QAR","RON","RSD","RUB","RWF","SAR","SBD","SCR","SDG","SEK","SGD","SHP","SLL","SOS","SRD","SSP","STD","STN","SVC","SYP","SZL","THB","TJS","TMT","TND","TOP","TRY","TTD","TWD","TZS","UAH","UGX","USD","UYU","UZS","VEF","VND","VUV","WST","XAF","XAG","XAU","XCD","XDR","XOF","XPD","XPF","XPT","YER","ZAR","ZMW","ZWL"};
    private ProgressDialog mDialog;
    private Rates rates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("loglog", "start");
        mapComponent();
        edtResult.setKeyListener(null);
        btnConvert.setEnabled(false);
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
                hideSoftKeyboard(MainActivity.this, view);
                edtYourMoney.setText("");
                spinnerFrom.setSelection(0);
                spinnerTo.setSelection(0);
                edtResult.setText("");
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
                getCurrency();
            }
        });
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(MainActivity.this, view);
                Double money = Double.valueOf(edtYourMoney.getText().toString());
                String from = spinnerFrom.getSelectedItem().toString();
                String to = spinnerTo.getSelectedItem().toString();
                Double rateFrom, rateTo;
                try {
                    rateFrom = (Double) rates.getClass().getMethod("get" + from).invoke(rates);
                    rateTo = (Double)rates.getClass().getMethod("get" + to).invoke(rates);
                    edtResult.setText(String.valueOf(money / rateFrom * rateTo));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        });
        edtYourMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) btnConvert.setEnabled(false);
                else if (charSequence.length() > 0) btnConvert.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void getCurrency() {
        dataClient.getCurrency().enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(@NonNull Call<Currency> call, @NonNull Response<Currency> response) {
                if (response.isSuccessful()) {
                    mDialog.dismiss();
                    rates = response.body().getRates();
                }
            }
            @Override
            public void onFailure(@NonNull Call<Currency> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "There is an error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mapComponent() {
        edtYourMoney = findViewById(R.id.edtYourMoney);
        spinnerTo = findViewById(R.id.spinnerTo);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        btnClear = findViewById(R.id.btnClear);
        btnConvert = findViewById(R.id.btnConvert);
        edtResult = findViewById(R.id.edtResult);
        btnRefresh = findViewById(R.id.btnRefresh);
    }
    private void hideSoftKeyboard (Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
}
