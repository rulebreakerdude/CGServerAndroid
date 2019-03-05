package dev.n.d.cgserverandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadioGroup serverToggle;
    private RadioButton radioButtonIMI;
    private RadioButton radioButtonCGNet;
    private EditText editTextPhoneNumber;
    SharedPreferences sp;
    public static final String MyPREFERENCES = "MyPrefs" ;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    public static final String REQUESTTAG = "requesttag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private void displayMessageToChooseServer() {
        Toast.makeText(getBaseContext(), "For first time only, choose the server.", Toast.LENGTH_LONG).show();
    }

    private void setupCallback(String currentServerName) {//the url address setup script
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

    public void onMissCall(String number) {//the url request on miss call script
        String urlCall=sp.getString((getString(R.string.Current_Server_Address)),"DNE")+"?number="+number;
        Toast.makeText(getBaseContext(), urlCall , Toast.LENGTH_LONG).show();
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, urlCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ;//TODO:later Save in DB
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setTag(REQUESTTAG);
        requestQueue.add(stringRequest);
    }


    public void onMissCall(View view) {//the url request on miss call script
        String number = editTextPhoneNumber.getText().toString();
        onMissCall(number);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUESTTAG);
        }
    }
}
