package com.developer.enjad.activities_fragments.activity_new_communication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.developer.enjad.R;
import com.developer.enjad.activities_fragments.activity_home.HomeActivity;
import com.developer.enjad.databinding.ActivityNewCommunicationBinding;
import com.developer.enjad.interfaces.Listeners;
import com.developer.enjad.language.LanguageHelper;
import com.developer.enjad.models.NewReport;
import com.developer.enjad.models.NewReport2;
import com.developer.enjad.models.UserModel;
import com.developer.enjad.preferences.Preferences;
import com.developer.enjad.share.Common;
import com.developer.enjad.tags.Tags;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class NewCommunicationActivity extends AppCompatActivity implements Listeners.BackListener, Listeners.NewRepListener {
    private ActivityNewCommunicationBinding binding;
    private String lang = "en";
    private DatabaseReference dRef;
    private NewReport newReport;
    private Preferences preferences;
    private UserModel userModel;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_communication);
        initView();

    }

    private void initView() {
        preferences =Preferences.newInstance();
        userModel  = preferences.getUserData(this);
        newReport = new NewReport();
        binding.setBackListener(this);
        binding.setLang(lang);
        binding.setNewRepListener(this);
        binding.setModel(newReport);
        dRef = FirebaseDatabase.getInstance().getReference(Tags.DATABASE_NAME);



    }




    private void sendData() {

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();




        String id = dRef.child(Tags.TABLE_REPORTS).child(userModel.getId()).push().getKey();
        NewReport2 newReport2 = new NewReport2(id,newReport.getNumber(),newReport.getLink());

        dRef.child(Tags.TABLE_REPORTS).child(newReport2.getId()).setValue(newReport2)
                .addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
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
        String lat=  newReport.getLink().substring(newReport.getLink().indexOf("!3d"),newReport.getLink().indexOf("!4d"));
        String lng=  newReport.getLink().substring(newReport.getLink().indexOf("!4d"));

        String[] lngs = lng.split("d");
        String[] lats = lat.split("d");

        String latitude = lats[1];
        String longitude = lngs[1];

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("number",newReport.getNumber());
        intent.putExtra("status","1");
        startActivity(intent);
        finish();


    }


    @Override
    public void back() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void checkNewReportData(NewReport newReport) {
        if (newReport.isDataValid(this))
        {
            sendData();
        }
    }
}
