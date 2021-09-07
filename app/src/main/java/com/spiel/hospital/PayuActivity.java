package com.spiel.hospital;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class PayuActivity extends AppCompatActivity {

    private boolean isDisableExitConfirmation = false;

    public SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText email_et,mobile_et, amount_et;
    private TextView logoutBtn,payNowButton,text_back_payu;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;
    public  static String payamount="0",str_address="",str_arrayList_productcode="";
    ProgressDialog progressDialog;
    AlertDialog alert11;

    String str_merchantResponse,str_postBackParamId,str_mihpayid,str_paymentId,str_mode,str_status,str_key,str_txnid,
            str_amount,str_additionalCharges,str_addedon,str_createdOn,str_firstname,str_email,
            str_phone,str_hash,str_field1,str_field2,str_field3,str_field4,str_field5,str_field6,
            str_field7,str_field8,str_field9,str_bank_ref_num,str_bankcode,str_cardToken,str_cardnum,
            str_payuMoneyId,str_message;
    String emailPattern,str_emailvlid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payu);


     //   Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
//        setSupportActionBar(toolbar);
       // toolbar.setTitleTextColor(Color.WHITE);
      //  toolbar.setTitle(getString(R.string.app_name));
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        logoutBtn = (TextView) findViewById(R.id.logout_button);
        text_back_payu= (TextView) findViewById(R.id.text_back_payu);
       email_et = (EditText) findViewById(R.id.email_et);
        mobile_et = (EditText) findViewById(R.id.mobile_et);
        amount_et = (EditText) findViewById(R.id.amount_et);
        payNowButton = (TextView) findViewById(R.id.pay_now_button);
        amount_et.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});
      emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        str_emailvlid="false";
        //radioGroup_color_theme = (RadioGroup) findViewById(R.id.radio_grp_color_theme);
        //radio_btn_default = (AppCompatRadioButton) findViewById(R.id.radio_btn_theme_default);
        // radio_btn_theme_pink = (AppCompatRadioButton) findViewById(R.id.radio_btn_theme_pink);
        // radio_btn_theme_purple = (AppCompatRadioButton) findViewById(R.id.radio_btn_theme_purple);
        //radio_btn_theme_green = (AppCompatRadioButton) findViewById(R.id.radio_btn_theme_green);
        //radio_btn_theme_grey = (AppCompatRadioButton) findViewById(R.id.radio_btn_theme_grey);
        selectProdEnv();
       // AppPreference.selectedTheme = R.style.AppTheme_pink;


        email_et.setText("");
        mobile_et.setText(pref.getString("userid",""));
        amount_et.setText("₹"+payamount);

        payNowButton.setEnabled(false);
        payNowButton.setBackgroundColor(getResources().getColor(R.color.light_gray));

        if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);
        }
        text_back_payu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });





        emailPattern = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        final Pattern pattern = Pattern.compile(emailPattern);


        email_et.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                Matcher matcher = pattern.matcher(email_et.getText().toString().trim());
                if (!matcher.find()) {
                    str_emailvlid = "false";
                }
                else {
                    str_emailvlid = "true";
                }

//                if (email_et.getText().toString().trim().matches(emailPattern) && s.length() > 0)
//                {
//                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
//                    str_emailvlid = "true";
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
//                    str_emailvlid = "false";
//                }

                checkverify();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mobile_et.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkverify();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        amount_et.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                checkverify();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

//        logoutBtn.setOnClickListener((View.OnClickListener) this);
        // AppCompatRadioButton radio_btn_sandbox = (AppCompatRadioButton) findViewById(R.id.radio_btn_sandbox);
        // AppCompatRadioButton radio_btn_production = (AppCompatRadioButton) findViewById(R.id.radio_btn_production);
        // radioGroup_select_env = (RadioGroup) findViewById(R.id.radio_grp_env);


      //  payNowButton.setOnClickListener((View.OnClickListener) this);

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    payNowButton.setEnabled(false);
                    launchPayUMoneyFlow();



            }
        });

       // initListeners();

        //Set Up SharedPref
      //  setUpUserDetails();

//        if (settings.getBoolean("is_prod_env", false)) {
            ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
            //radio_btn_production.setChecked(true);
//        } else {
//            ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
//            //radio_btn_sandbox.setChecked(true);
//        }
        setupCitrusConfigs();
    }
    public void checkverify()
    {
        if ( str_emailvlid.equalsIgnoreCase("true") && mobile_et.getText().length() !=0 && amount_et.getText().length() !=0)
        {
            payNowButton.setEnabled(true);
            payNowButton.setBackgroundColor(getResources().getColor(R.color.blue_1));
        }
        else
        {
            payNowButton.setEnabled(false);
            payNowButton.setBackgroundColor(getResources().getColor(R.color.light_gray));

        }
    }
    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }


//    public static void setErrorInputLayout(EditText editText, String msg, TextInputLayout textInputLayout)
//    {
//        textInputLayout.setError(msg);
//        editText.requestFocus();
//    }

    public static boolean isValidEmail(String strEmail) {
        return strEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
    }

    public static boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(AppPreference.PHONE_PATTERN);

        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

//    private void setUpUserDetails() {
//        userDetailsPreference = getSharedPreferences(AppPreference.USER_DETAILS, MODE_PRIVATE);
//        userEmail = userDetailsPreference.getString(AppPreference.USER_EMAIL, mAppPreference.getDummyEmail());
//
//        userMobile = userDetailsPreference.getString(AppPreference.USER_MOBILE, "");
//
//        email_et.setText(userEmail);
//        mobile_et.setText(userMobile);
//        amount_et.setText(payamount);
//        AppPreference.selectedTheme = R.style.AppTheme_pink;
//
//    }



    @Override
    protected void onResume() {
        super.onResume();
        payNowButton.setEnabled(true);

        if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);
        }
    }

    /**
     * This function sets the mode to PRODUCTION in Shared Preference
     */
    private void selectProdEnv() {

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
//                editor.putBoolean("is_prod_env", true);
//                editor.apply();

                if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
                    logoutBtn.setVisibility(View.VISIBLE);
                } else {
                    logoutBtn.setVisibility(View.GONE);
                }

                setupCitrusConfigs();
            }
        }, AppPreference.MENU_DELAY);
    }

    /**
     * This function sets the mode to SANDBOX in Shared Preference
     */
    private void selectSandBoxEnv() {
        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);

//        editor.putBoolean("is_prod_env", false);
//        editor.apply();

        if (PayUmoneyFlowManager.isUserLoggedIn(getApplicationContext())) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);

        }
        setupCitrusConfigs();
    }

    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.SANDBOX) {
            Toast.makeText(PayuActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PayuActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null)
            {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL))
                {

                    String payuResponse = transactionResponse.getPayuResponse();
                    str_merchantResponse = transactionResponse.getTransactionDetails();

                    try {
                        JSONObject obj = new JSONObject(payuResponse);

                        str_message =obj.getString("message");
                        String str_result =obj.getString("result");

                        JSONObject obj1 = new JSONObject(str_result);


                        str_postBackParamId = obj1.getString("postBackParamId");
                        str_mihpayid = obj1.getString("mihpayid");
                        str_paymentId = obj1.getString("paymentId");
                        str_mode = obj1.getString("mode");
                        str_status= obj1.getString("status");
                        str_key= obj1.getString("key");;
                        str_txnid= obj1.getString("txnid");
                        str_amount= obj1.getString("amount");
                        str_additionalCharges= obj1.getString("additionalCharges");
                        str_addedon= obj1.getString("addedon");
                        str_createdOn= obj1.getString("createdOn");
                        str_firstname = obj1.getString("firstname");
                        str_email= obj1.getString("email");
                        str_phone = obj1.getString("phone");
                        str_hash = obj1.getString("hash");
                        str_field1= obj1.getString("field1");
                        str_field2= obj1.getString("field2");
                        str_field3= obj1.getString("field3");
                        str_field4= obj1.getString("field4");
                        str_field5= obj1.getString("field5");
                        str_field6= obj1.getString("field6");
                        str_field7= obj1.getString("field7");
                        str_field8= obj1.getString("field8");
                        str_field9= obj1.getString("field9");
                        str_bank_ref_num= obj1.getString("bank_ref_num");
                        str_bankcode= obj1.getString("bankcode");
                        str_cardToken = obj1.getString("cardToken");
                        str_cardnum= obj1.getString("cardnum");
                        str_payuMoneyId= obj1.getString("payuMoneyId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog = new ProgressDialog(PayuActivity.this);
                    progressDialog.setMessage("Loading..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    new Communication_Order().execute();

//                    progressDialog = new ProgressDialog(PayuActivity.this);
//                    progressDialog.setMessage("Loading..."); // Setting Message
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                    progressDialog.show(); // Display Progress Dialog
//                    progressDialog.setCancelable(false);
//                    new Communication_Order().execute();
//                    new AlertDialog.Builder(this)
//                            .setCancelable(false)
//                            .setMessage("Success Transaction ")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.dismiss();
//
//                                }
//                            }).show();
                    Log.d("payua", "Success Transaction"); //Success Transaction
                } else {

                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setMessage("Failure Transaction ")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();



                    Log.d("payua", "Failure Transaction");//Failure Transaction
                }

                // Response from Payumoney
             //   String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
            //    String merchantResponse = transactionResponse.getTransactionDetails();

//                new AlertDialog.Builder(this)
//                        .setCancelable(false)
//                        .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }).show();

            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d("payua", "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d("payua", "Both objects are null!");
            }
        }
    }


    public void onClick(View v) {

        if (v.getId() == R.id.logout_button) {
            switch (v.getId()) {
                case R.id.pay_now_button:
                    payNowButton.setEnabled(false);
                    launchPayUMoneyFlow();
                    break;
                case R.id.logout_button:
                    PayUmoneyFlowManager.logoutUser(getApplicationContext());
                    logoutBtn.setVisibility(View.GONE);
                    break;
            }
        }
    }

//    private void initListeners() {
//        email_et.addTextChangedListener(new EditTextInputWatcher(email_til));
//        mobile_et.addTextChangedListener(new EditTextInputWatcher(mobile_til));
//
//
////        radioGroup_color_theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
////                mAppPreference.setOverrideResultScreen(true);
////
////                switch (i) {
////                    case R.id.radio_btn_theme_default:
////                        AppPreference.selectedTheme = -1;
////                        break;
////                    case R.id.radio_btn_theme_pink:
////                        AppPreference.selectedTheme = R.style.AppTheme_pink;
////                        break;
////                    case R.id.radio_btn_theme_grey:
////                        AppPreference.selectedTheme = R.style.AppTheme_Grey;
////                        break;
////                    case R.id.radio_btn_theme_purple:
////                        AppPreference.selectedTheme = R.style.AppTheme_purple;
////                        break;
////                    case R.id.radio_btn_theme_green:
////                        AppPreference.selectedTheme = R.style.AppTheme_Green;
////                        break;
////                    default:
////                        AppPreference.selectedTheme = -1;
////                        break;
////                }
////            }
////        });
////
////        radioGroup_select_env.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
////                switch (i) {
////                    case R.id.radio_btn_sandbox:
////                        selectSandBoxEnv();
////                        break;
////                    case R.id.radio_btn_production:
////                        selectProdEnv();
////                        break;
////                    default:
////                        selectSandBoxEnv();
////                        break;
////                }
////            }
////        });
//    }

    /**
     * This fucntion checks if email and mobile number are valid or not.
     *
     * @param email  email id entered in edit text
     * @param mobile mobile number entered in edit text
     * @return boolean value
     */
//    public boolean validateDetails(String email, String mobile) {
//        email = email.trim();
//        mobile = mobile.trim();
//
//        if (TextUtils.isEmpty(mobile)) {
//            setErrorInputLayout(mobile_et, getString(R.string.err_phone_empty), mobile_til);
//            return false;
//        } else if (!isValidPhone(mobile)) {
//            setErrorInputLayout(mobile_et, getString(R.string.err_phone_not_valid), mobile_til);
//            return false;
//        } else if (TextUtils.isEmpty(email)) {
//            setErrorInputLayout(email_et, getString(R.string.err_email_empty), email_til);
//            return false;
//        } else if (!isValidEmail(email)) {
//            setErrorInputLayout(email_et, getString(R.string.email_not_valid), email_til);
//            return false;
//        } else {
//            return true;
//        }
//    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("");

        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(payamount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = System.currentTimeMillis() + "";
        //String txnId = "TXNID720431525261327973";
        String phone = mobile_et.getText().toString().trim();
        String productName = "product_info";//mAppPreference.getProductInfo();
        String firstName = "Sachin";
        String email =email_et.getText().toString().trim();
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        builder.setAmount(String.valueOf(amount))
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();

            /*
            * Hash should always be generated from your server side.
            * */
               // generateHashFromServer(mPaymentParams);

/*            *//**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             * */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams);

//            if (AppPreference.selectedTheme != -1) {
                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayuActivity.this, R.style.AppTheme_pink,true);
//            } else {
//                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams,PayuActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
//            }

        } catch (Exception e) {
            // some exception occurred
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            payNowButton.setEnabled(true);

        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private PayUmoneySdkInitializer.PaymentParam calculateServerSideHashAndInitiatePayment1(final PayUmoneySdkInitializer.PaymentParam paymentParam) {

        StringBuilder stringBuilder = new StringBuilder();
        HashMap<String, String> params = paymentParam.getParams();
        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");

        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        stringBuilder.append(appEnvironment.salt());

        String hash = hashCal(stringBuilder.toString());
        paymentParam.setMerchantHash(hash);

        return paymentParam;
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();

        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PayuActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            payNowButton.setEnabled(true);

            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(PayuActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {
                mPaymentParams.setMerchantHash(merchantHash);

//                if (AppPreference.selectedTheme != -1) {
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayuActivity.this, R.style.AppTheme_pink, true);
//                } else {
//                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PayuActivity.this, R.style.AppTheme_default, mAppPreference.isOverrideResultScreen());
//                }
            }
        }
    }

//    public static class EditTextInputWatcher implements TextWatcher {
//
//        private TextInputLayout textInputLayout;
//
//        EditTextInputWatcher(TextInputLayout textInputLayout) {
//            this.textInputLayout = textInputLayout;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            if (s.toString().length() > 0) {
//               // textInputLayout.setError(null);
//               // textInputLayout.setErrorEnabled(false);
//            }
//        }
//    }

    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }
    public class Communication_Order extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.paymentpay);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("custid", pref.getString("custid",""));
                postDataParams.put("mobile", pref.getString("userid",""));
                postDataParams.put("custname", pref.getString("custname",""));
                postDataParams.put("address",str_address);
                postDataParams.put("paymentmode", "online");
                postDataParams.put("total", payamount);

                postDataParams.put("message",str_message);
                postDataParams.put("merchantResponse",str_merchantResponse);
                postDataParams.put("postBackParamId",str_postBackParamId);
                postDataParams.put("mihpayid",str_mihpayid);
                postDataParams.put("paymentId",str_paymentId);
                postDataParams.put("mode",str_mode);
                postDataParams.put("status",str_status);
                postDataParams.put("key",str_key);
                postDataParams.put("txnid",str_txnid);
                postDataParams.put("amount",str_amount);
                postDataParams.put("additionalCharges",str_additionalCharges);
                postDataParams.put("addedon",str_addedon);
                postDataParams.put("createdOn",str_createdOn);
                postDataParams.put("firstname",str_firstname);
                postDataParams.put("email",str_email);
                postDataParams.put("phone",str_phone);
                postDataParams.put("hash",str_hash);
                postDataParams.put("field1",str_field1);
                postDataParams.put("field2",str_field2);
                postDataParams.put("field3",str_field3);
                postDataParams.put("field4",str_field4);
                postDataParams.put("field5",str_field5);
                postDataParams.put("field6",str_field6);
                postDataParams.put("field7",str_field7);
                postDataParams.put("field8",str_field8);
                postDataParams.put("field9",str_field9);
                postDataParams.put("bank_ref_num",str_bank_ref_num);
                postDataParams.put("bankcode",str_bankcode);
                postDataParams.put("cardToken",str_cardToken);
                postDataParams.put("cardnum",str_cardnum);
                postDataParams.put("payuMoneyId",str_payuMoneyId);


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000 /* milliseconds */);
                conn.setConnectTimeout(30000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            if (result != null) {

                if (result.equalsIgnoreCase("nullerror"))
                {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PayuActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Some field missing,Please try again.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();
                }
                else if (result.equalsIgnoreCase("error"))
                {

                   AlertDialog.Builder builder2 = new AlertDialog.Builder(PayuActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found,Please try again.");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();
                }
                else if (result.equalsIgnoreCase("added"))
                {
//                    SingletonObject.Instance().setstr_revieworder("yes");
//                    SingletonObject.Instance().setstr_updatehomecart("yes");



                    Intent intent1 = new Intent("ordercomplete");
                    intent1.putExtra("productcodes",str_arrayList_productcode);
                    LocalBroadcastManager.getInstance(PayuActivity.this).sendBroadcast(intent1);

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PayuActivity.this);
                    builder2.setTitle("Successful order!");
                    builder2.setMessage("Your order has been submited");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

//                                    Intent intent1 = new Intent("cartupdate");
//                                    LocalBroadcastManager.getInstance(PayuActivity.this).sendBroadcast(intent1);
//                                    ReviewOrderActivity.activity_review.finish();
                                    finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();

                }
                else
                {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PayuActivity.this);
                    builder2.setTitle("Oops");
                    builder2.setMessage("Server could not found");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    // finish();
                                }
                            });
                    alert11 = builder2.create();
                    alert11.show();
                }


            }
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }


    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    public  void Connections()
    {
        AlertDialog.Builder builder4 = new AlertDialog.Builder(PayuActivity.this);
        builder4.setTitle("No Internet");
        builder4.setMessage("Your internet connection not available");
        builder4.setCancelable(false);
        builder4.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        alert11 = builder4.create();
        alert11.show();
    }
    public void onBackPressed() {

    }
}


//{"status":0,"message":"payment status for :339476990","result":{"postBackParamId":255291219,"mihpayid":"10756514063","paymentId":339476990,"mode":"DC","status":"success","unmappedstatus":"captured","key":"bu3M9JbL","txnid":"1595514359136","amount":"5.00","additionalCharges":"","addedon":"2020-07-23 19:56:39","createdOn":1595514470000,"productinfo":"product_info","firstname":"sachin mokashi","lastname":"","address1":"","address2":"","city":"","state":"","country":"","zipcode":"","email":"sachinmokashi.1989@gmail.com","phone":"9850017872","udf1":"","udf2":"","udf3":"","udf4":"","udf5":"","udf6":"","udf7":"","udf8":"","udf9":"","udf10":"","hash":"8ed93218309569b7e10cf8fa09eecfdf0a15bd5cf1c8508e1059d56ed69965f0bbe655435abab9349713381747dd3bea5743a4fdfaccbc3f2f7c02724b2f9ca4","field1":"020579086751","field2":"195749","field3":"202057092764938","field4":"2202057007199917","field5":"05","field6":"00","field7":"AUTHPOSITIVE","field8":"Approved or completed successfully","field9":"No Error","bank_ref_num":"202057092764938","bankcode":"VISA","error":"E000","error_Message":"No Error","cardToken":"4031a02cfacffd68657a6a","offer_key":"","offer_type":"","offer_availed":"","pg_ref_no":"","offer_failure_reason":"","name_on_card":"payu","cardnum":"452011XXXXXX7811","cardhash":"","card_type":"","card_merchant_param":null,"version":"","postUrl":"https:\/\/www.payumoney.com\/mobileapp\/payumoney\/success.php","calledStatus":false,"additional_param":"","amount_split":"{\"PAYU\":\"5.00\"}","discount":"","net_amount_debit":"5","fetchAPI":null,"paisa_mecode":"","meCode":"{\"tranportalid\":\"70007469\",\"tranportalpwd\":\"70007469\"}","payuMoneyId":"339476990","encryptedPaymentId":null,"id":null,"surl":null,"furl":null,"baseUrl":null,"retryCount":0,"merchantid":null,"payment_source":null,"isConsentPayment":0,"giftCardIssued":true,"pg_TYPE":"HDFC_Internal_Plus","s2SPbpFlag":false},"errorCode":null,"responseCode":null}