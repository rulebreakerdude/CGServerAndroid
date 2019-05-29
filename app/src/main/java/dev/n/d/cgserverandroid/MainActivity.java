package dev.n.d.cgserverandroid;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadioGroup serverToggle;
    private RadioButton radioButtonIMI;
    private RadioButton radioButtonCGNet;
    private EditText editTextPhoneNumber;
    private CallerViewModel mCallerViewModel;
    private CallerListAdapter callerListAdapter;
    private RecyclerView recyclerView;
    List<Caller> traineeRealList;

    SharedPreferences sp;
    public static final String MyPREFERENCES = "MyPrefs" ;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    public final String REQUESTTAG = "requesttag";
    final int REQUEST_READ_PHONE_STATE=1;
    final int REQUEST_MODIFY_PHONE_STATE=1;
    final int REQUEST_CALL_PHONE=1;
    static final int CALL_NUMBER_REQUEST = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //******************************************************************************************************************************
        //checking permissions
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE);

        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_PHONE_STATE}, REQUEST_MODIFY_PHONE_STATE);
        }
        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck3 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        }
        //******************************************************************************************************************************

        //Registering receiver for miss calls
        BroadcastReceiver br1 = new CallReceiver();
        this.registerReceiver(br1, new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED));

        //Registering receiver for coming back to mainActivity calls
        BroadcastReceiver br2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String number = intent.getStringExtra("incomingNumber");
                onMissCall(number);
            }
        };
        this.registerReceiver(br2, new IntentFilter("CUSTOM_ON_MISS_CALL_CALLBACK_RECEIVER"));
        //******************************************************************************************************************************

        //Radio Group functionality
        radioButtonIMI=findViewById(R.id.radioButtonIMI);
        radioButtonCGNet=findViewById(R.id.radioButtonCGNet);
        //Test number setup
        editTextPhoneNumber=findViewById(R.id.editTextPhoneNumber);
        //Radio Group functionality
        serverToggle = findViewById(R.id.radioGroup);
        serverToggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged (RadioGroup group,int checkedId){
                if (checkedId == R.id.radioButtonCGNet) {
                    setupCallback("CGNet");
                } else if (checkedId == R.id.radioButtonIMI) {
                    setupCallback("IMI");
                }
            }
        });
        //******************************************************************************************************************************

        //Shared preference saving data and checking first install
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sp.contains(getString(R.string.Current_Server_Name))){
            String currentServerName=sp.getString((getString(R.string.Current_Server_Name)),"DNE");
            switch(currentServerName){
                case "CGNet":radioButtonIMI.setChecked(true);
                break;
                case "IMI":radioButtonCGNet.setChecked(true);
                    break;
            }
        }
        else{
            displayMessageToChooseServer();
        }
        //******************************************************************************************************************************

        //recyclerview implementation
        recyclerView = findViewById(R.id.recyclerview);
        callerListAdapter = new CallerListAdapter(this);
        recyclerView.setAdapter(callerListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCallerViewModel = ViewModelProviders.of(this).get(CallerViewModel.class);
        mCallerViewModel.getAllCallers().observe(MainActivity.this, new Observer<List<Caller>>() {
            @Override
            public void onChanged(@Nullable List<Caller> callerList) {
                callerListAdapter.setCallers(callerList);
            }
        });


    }
    //onCreate ends here
    //******************************************************************************************************************************

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("state:","Reached permission resolution");
                }
                break;

            default:
                break;
        }
    }
    //******************************************************************************************************************************

    private void displayMessageToChooseServer() {
        Toast.makeText(getBaseContext(), "For first time only, choose the server.", Toast.LENGTH_LONG).show();
    }
    //******************************************************************************************************************************

    //the url address setup script
    private void setupCallback(String currentServerName) {
        Toast.makeText(getBaseContext(), currentServerName, Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.Current_Server_Name),currentServerName);
        editor.apply();
        switch(currentServerName){
            case "CGNet":editor.putString(getString(R.string.Current_Server_Address),getString(R.string.CGNet_API));
                break;
            case "IMI":editor.putString(getString(R.string.Current_Server_Address),getString(R.string.IMI_API));
                break;
        }
        editor.apply();
    }
    //******************************************************************************************************************************

    //the url request on miss call script
    public void onMissCall(String number) {
        Toast.makeText(getBaseContext(), "Calling Number: "+number, Toast.LENGTH_LONG).show();
        String urlCall=sp.getString((getString(R.string.Current_Server_Address)),"DNE")+"?number="+number;
        Log.d("url:",urlCall);
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, urlCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Throw Test","error:",error);
            }
        });
        stringRequest.setTag(REQUESTTAG);
        requestQueue.add(stringRequest);

    }
    //******************************************************************************************************************************


    //the url request on miss call script
    public void onMissCall(View view) {
        mCallerViewModel.deleteAll();
        String number = editTextPhoneNumber.getText().toString();
        onMissCall(number);
        SimpleDateFormat ft = new SimpleDateFormat ("yyMMddhhmmss");
        Caller caller=new Caller();
        caller.setCallerNumber(number);
        Date dt1=new Date();
        caller.setApiDatetime(ft.format(dt1));
        Date dt2=new Date();
        caller.setCallDatetime(ft.format(dt2));
        Date dt3=new Date();
        caller.setSuccessfulCallbackDatetime(ft.format(dt3));
        caller.setResponseCGNet("S");
        caller.setResponseIMI("S");
        caller.setCallFromIMI(1);
        mCallerViewModel.insert(caller);
    }
    //******************************************************************************************************************************

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUESTTAG);
        }
    }
    //******************************************************************************************************************************
}
