package bk.loc.currencyconverter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

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
    private String currency[] = {"USD - United States Dollar","VND - Vietnamese Dong","AED - United Arab Emirates Dirham","AFN - Afghan Afghani","ALL - Albanian Lek","AMD - Armenian Dram","ANG - Netherlands Antillean Guilder","AOA - Angolan Kwanza","ARS - Argentine Peso","AUD - Australian Dollar","AWG - Aruban Florin","AZN - Azerbaijani Manat","BAM - Bosnia-Herzegovina Convertible Mark","BBD - Barbadian Dollar","BDT - Bangladeshi Taka","BGN - Bulgarian Lev","BHD - Bahraini Dinar","BIF - Burundian Franc","BMD - Bermudan Dollar","BND - Brunei Dollar","BOB - Bolivian Boliviano","BRL - Brazilian Real","BSD - Bahamian Dollar","BTC - Bitcoin","BTN - Bhutanese Ngultrum","BWP - Botswanan Pula","BYN - Belarusian Ruble","BZD - Belize Dollar","CAD - Canadian Dollar","CDF - Congolese Franc","CHF - Swiss Franc","CLF - Chilean Unit of Account (UF)","CLP - Chilean Peso","CNH - Chinese Yuan (Offshore)","CNY - Chinese Yuan","COP - Colombian Peso","CRC - Costa Rican Colón","CUC - Cuban Convertible Peso","CUP - Cuban Peso","CVE - Cape Verdean Escudo","CZK - Czech Republic Koruna","DJF - Djiboutian Franc","DKK - Danish Krone","DOP - Dominican Peso","DZD - Algerian Dinar","EGP - Egyptian Pound","ERN - Eritrean Nakfa","ETB - Ethiopian Birr","EUR - Euro","FJD - Fijian Dollar","FKP - Falkland Islands Pound","GBP - British Pound Sterling","GEL - Georgian Lari","GGP - Guernsey Pound","GHS - Ghanaian Cedi","GIP - Gibraltar Pound","GMD - Gambian Dalasi","GNF - Guinean Franc","GTQ - Guatemalan Quetzal","GYD - Guyanaese Dollar","HKD - Hong Kong Dollar","HNL - Honduran Lempira","HRK - Croatian Kuna","HTG - Haitian Gourde","HUF - Hungarian Forint","IDR - Indonesian Rupiah","ILS - Israeli New Sheqel","IMP - Manx pound","INR - Indian Rupee","IQD - Iraqi Dinar","IRR - Iranian Rial","ISK - Icelandic Króna","JEP - Jersey Pound","JMD - Jamaican Dollar","JOD - Jordanian Dinar","JPY - Japanese Yen","KES - Kenyan Shilling","KGS - Kyrgystani Som","KHR - Cambodian Riel","KMF - Comorian Franc","KPW - North Korean Won","KRW - South Korean Won","KWD - Kuwaiti Dinar","KYD - Cayman Islands Dollar","KZT - Kazakhstani Tenge","LAK - Laotian Kip","LBP - Lebanese Pound","LKR - Sri Lankan Rupee","LRD - Liberian Dollar","LSL - Lesotho Loti","LYD - Libyan Dinar","MAD - Moroccan Dirham","MDL - Moldovan Leu","MGA - Malagasy Ariary","MKD - Macedonian Denar","MMK - Myanma Kyat","MNT - Mongolian Tugrik","MOP - Macanese Pataca","MRO - Mauritanian Ouguiya (pre-2018)","MRU - Mauritanian Ouguiya","MUR - Mauritian Rupee","MVR - Maldivian Rufiyaa","MWK - Malawian Kwacha","MXN - Mexican Peso","MYR - Malaysian Ringgit","MZN - Mozambican Metical","NAD - Namibian Dollar","NGN - Nigerian Naira","NIO - Nicaraguan Córdoba","NOK - Norwegian Krone","NPR - Nepalese Rupee","NZD - New Zealand Dollar","OMR - Omani Rial","PAB - Panamanian Balboa","PEN - Peruvian Nuevo Sol","PGK - Papua New Guinean Kina","PHP - Philippine Peso","PKR - Pakistani Rupee","PLN - Polish Zloty","PYG - Paraguayan Guarani","QAR - Qatari Rial","RON - Romanian Leu","RSD - Serbian Dinar","RUB - Russian Ruble","RWF - Rwandan Franc","SAR - Saudi Riyal","SBD - Solomon Islands Dollar","SCR - Seychellois Rupee","SDG - Sudanese Pound","SEK - Swedish Krona","SGD - Singapore Dollar","SHP - Saint Helena Pound","SLL - Sierra Leonean Leone","SOS - Somali Shilling","SRD - Surinamese Dollar","SSP - South Sudanese Pound","STD - São Tomé and Príncipe Dobra (pre-2018)","STN - São Tomé and Príncipe Dobra","SVC - Salvadoran Colón","SYP - Syrian Pound","SZL - Swazi Lilangeni","THB - Thai Baht","TJS - Tajikistani Somoni","TMT - Turkmenistani Manat","TND - Tunisian Dinar","TOP - Tongan Pa'anga","TRY - Turkish Lira","TTD - Trinidad and Tobago Dollar","TWD - New Taiwan Dollar","TZS - Tanzanian Shilling","UAH - Ukrainian Hryvnia","UGX - Ugandan Shilling","UYU - Uruguayan Peso","UZS - Uzbekistan Som","VEF - Venezuelan Bolívar Fuerte","VUV - Vanuatu Vatu","WST - Samoan Tala","XAF - CFA Franc BEAC","XAG - Silver Ounce","XAU - Gold Ounce","XCD - East Caribbean Dollar","XDR - Special Drawing Rights","XOF - CFA Franc BCEAO","XPD - Palladium Ounce","XPF - CFP Franc","XPT - Platinum Ounce","YER - Yemeni Rial","ZAR - South African Rand","ZMW - Zambian Kwacha","ZWL - Zimbabwean Dollar"};
    private ProgressDialog mDialog;
    private Rates rates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapComponent();
        initComponent();
        dataClient = APIUtils.getData();
        getCurrency();

        btnClear.setOnClickListener(new OnClickClear());
        btnRefresh.setOnClickListener(new OnClickFresh());
        btnConvert.setOnClickListener(new OnClickConvert());
        edtYourMoney.addTextChangedListener(new OnChangeText());
    }
    private class OnChangeText implements TextWatcher {

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
    }
    private class OnClickClear implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            hideSoftKeyboard(MainActivity.this, view);
            edtYourMoney.setText("");
            spinnerFrom.setSelection(0);
            spinnerTo.setSelection(0);
            edtResult.setText("");
        }
    }
    private class OnClickFresh implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showDialog();
            getCurrency();
        }
    }
    private class OnClickConvert implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            hideSoftKeyboard(MainActivity.this, view);
            Double money = Double.valueOf(edtYourMoney.getText().toString());
            String from = spinnerFrom.getSelectedItem().toString().substring(0, 3);
            String to = spinnerTo.getSelectedItem().toString().substring(0, 3);
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
    }
    private void initComponent() {
        edtResult.setKeyListener(null);
        btnConvert.setEnabled(false);
        mDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        mDialog.setMessage("Fetching data...");
        mDialog.setCancelable(false);
        showDialog();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currency);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerFrom.setAdapter(arrayAdapter);
        spinnerTo.setAdapter(arrayAdapter);
    }
    private void showDialog() {
        mDialog.show();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              mDialog.dismiss();
              timer.cancel();
            }
        }, 5000);
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
