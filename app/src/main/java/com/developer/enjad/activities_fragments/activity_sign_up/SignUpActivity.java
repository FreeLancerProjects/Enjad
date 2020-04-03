package com.developer.enjad.activities_fragments.activity_sign_up;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.developer.enjad.R;
import com.developer.enjad.activities_fragments.activity_home.HomeActivity;
import com.developer.enjad.activities_fragments.activity_login.LoginActivity;
import com.developer.enjad.databinding.ActivitySignUpBinding;
import com.developer.enjad.interfaces.Listeners;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.models.SignUpModel;
import com.developer.enjad.models.UserModel;
import com.developer.enjad.preferences.Preferences;
import com.developer.enjad.share.Common;
import com.developer.enjad.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.util.Locale;

import io.paperdb.Paper;

public class SignUpActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.SignUpListener, Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private ActivitySignUpBinding binding;
    private String lang = "en";
    private Preferences preferences;
    private DatabaseReference dRef;
    private SignUpModel signUpModel;
    private CountryPicker countryPicker;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        signUpModel = new SignUpModel();
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setShowCountryListener(this);
        binding.setSignupListener(this);
        binding.setSignUpModel(signUpModel);
        createCountryDialog();
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME);

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0")) {
                    binding.edtPhone.setText("");
                }
            }
        });



    }

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            if (countryPicker.getCountryFromSIM() != null) {
                updatePhoneCode(countryPicker.getCountryFromSIM());
            } else if (telephonyManager != null && countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
                updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
            } else if (countryPicker.getCountryByLocale(Locale.getDefault()) != null) {
                updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
            } else {
                String code = "+20";
                binding.tvCode.setText(code);
                signUpModel.setPhone_code(code);

            }
        } catch (Exception e) {
            String code = "+20";
            binding.tvCode.setText(code);
            signUpModel.setPhone_code(code);
        }


    }

    @Override
    public void checkDataSignUp() {
        if (signUpModel.isDataValid(this))
        {
            Common.CloseKeyBoard(this,binding.edtName);
            signUp();
        }
    }

    private void signUp() {

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        String user_id = dRef.child(Tags.TABLE_USERS).push().getKey();
        String phone = signUpModel.getPhone_code()+signUpModel.getPhone();
        UserModel userModel = new UserModel(user_id,signUpModel.getName(),signUpModel.getPassword(),phone,1);

        dRef.child(Tags.TABLE_USERS).child(phone)
                .setValue(userModel)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    preferences.create_update_userData(this,userModel);
                    navigateToHomeActivity();
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    if (e.getMessage()!=null)
                    {
                        Common.CreateDialogAlert(this,e.getMessage());
                    }else
                        {
                            Toast.makeText(this,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        }
                });

    }


    private void navigateToHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100)
        {
            if (resultCode==RESULT_OK)
            {

                binding.setSignUpModel(signUpModel);

            }
        }

    }

    @Override
    public void back() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void showDialog() {
        countryPicker.showDialog(this);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);
    }

    private void updatePhoneCode(Country country) {
        binding.tvCode.setText(country.getDialCode());
        signUpModel.setPhone_code(country.getDialCode());

    }

}
