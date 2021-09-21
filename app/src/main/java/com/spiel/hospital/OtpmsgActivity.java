package com.spiel.hospital;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.msg91.sendotpandroid.library.internal.Iso2Phone;
import com.msg91.sendotpandroid.library.utils.PhoneNumberUtils;

import java.util.Locale;

public class OtpmsgActivity extends AppCompatActivity {

    public static final String INTENT_PHONENUMBER = "phonenumber";
    public static final String INTENT_COUNTRY_CODE = "code";
    CheckBox checkbox_hospital,checkbox_doctor;
    private EditText mPhoneNumber;
    private TextView mSmsButton;
    private String mCountryIso,str_mobileno;
    private TextWatcher mNumberTextWatcher;
    String str_logintype;
    private  TextView text_login_forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpmsg);



        text_login_forgotpassword = findViewById(R.id.text_login_forgotpassword);
        checkbox_doctor = (CheckBox)findViewById(R.id.checkbox_doctor);
        checkbox_hospital = (CheckBox)findViewById(R.id.checkbox_hospital);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mSmsButton = findViewById(R.id.smsVerificationButton);

        Intent intent = getIntent();
        if (intent != null) {

            str_mobileno = intent.getStringExtra("mobile");
            mPhoneNumber.setText(str_mobileno);

        }


        LocalBroadcastManager.getInstance(OtpmsgActivity.this).registerReceiver(updateotp,
                new IntentFilter("updateotp"));

        checkbox_doctor.setChecked(false);
        checkbox_hospital.setChecked(true);
        str_logintype="hospital";
        checkbox_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_logintype="doctor";
                checkbox_doctor.setChecked(true);
                checkbox_hospital.setChecked(false);
            }
        });
        checkbox_hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_logintype="hospital";
                checkbox_doctor.setChecked(false);
                checkbox_hospital.setChecked(true);


            }
        });

        text_login_forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtpmsgActivity.this,LoginDoctorActivity.class);
                //Intent intent = new Intent(OtpmsgActivity.this,PayuActivity.class);
               // PayuActivity.payamount = String.valueOf(Math.round(Float.parseFloat("8")));
                //PayuActivity.str_address ="Tasgaon";
              //  PayuActivity.str_arrayList_productcode = arrayList_productcode.toString();

                startActivity(intent);

            }
        });
        mCountryIso = PhoneNumberUtils.getDefaultCountryIso(this);
        final String defaultCountryName = new Locale("", mCountryIso).getDisplayName();
        final CountrySpinner spinner = (CountrySpinner) findViewById(R.id.spinner);
        spinner.init(defaultCountryName);
        spinner.setEnabled(false);
        spinner.addCountryIsoSelectedListener(new CountrySpinner.CountryIsoSelectedListener() {
            @Override
            public void onCountryIsoSelected(String selectedIso) {
                if (selectedIso != null) {
                    mCountryIso = selectedIso;
                    resetNumberTextWatcher(mCountryIso);
                    // force update:
                    mNumberTextWatcher.afterTextChanged(mPhoneNumber.getText());
                }
            }
        });
        resetNumberTextWatcher(mCountryIso);

        tryAndPrefillPhoneNumber();
    }

    private void tryAndPrefillPhoneNumber() {
        if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
           // mPhoneNumber.setText(manager.getLine1Number());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    @SuppressLint("MissingSuperCall")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            tryAndPrefillPhoneNumber();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your phone number to automatically "
                        + "pre-fill it", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openActivity(String phoneNumber) {
        Intent verification = new Intent(this, VerificationActivity.class);
        verification.putExtra(INTENT_PHONENUMBER, phoneNumber);
        verification.putExtra(INTENT_COUNTRY_CODE, Iso2Phone.getPhone(mCountryIso));
        verification.putExtra("type",str_logintype);
        startActivity(verification);
    }

    private void setButtonsEnabled(boolean enabled) {
        mSmsButton.setEnabled(enabled);
    }

    public void onButtonClicked(View view) {
        openActivity(getE164Number());
    }

    private void resetNumberTextWatcher(String countryIso) {

        if (mNumberTextWatcher != null) {
            mPhoneNumber.removeTextChangedListener(mNumberTextWatcher);
        }

        mNumberTextWatcher = new com.msg91.sendotpandroid.library.utils.PhoneNumberFormattingTextWatcher(countryIso) {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public synchronized void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (isPossiblePhoneNumber())
                {
                    setButtonsEnabled(true);
                    mSmsButton.setTextColor(getResources().getColor(R.color.white));
                    mPhoneNumber.setTextColor(getResources().getColor(R.color.blue_1));
                } else {
                   setButtonsEnabled(false);
                    mPhoneNumber.setTextColor(getResources().getColor(R.color.colorred));
                    mSmsButton.setTextColor(getResources().getColor(R.color.colorred));
                }
            }
        };

        mPhoneNumber.addTextChangedListener(mNumberTextWatcher);
    }

    private boolean isPossiblePhoneNumber() {
        return PhoneNumberUtils.isPossibleNumber(mPhoneNumber.getText().toString(), mCountryIso);
    }

    private String getE164Number() {
        return mPhoneNumber.getText().toString().replaceAll("\\D", "").trim();
        // return PhoneNumberUtils.formatNumberToE164(mPhoneNumber.getText().toString(), mCountryIso);
    }
    private BroadcastReceiver updateotp = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (intent != null) {

                str_mobileno = intent.getStringExtra("mobile");
                mPhoneNumber.setText(str_mobileno);

            }


        }
    };
    public void onBackPressed() {

    }
}