package existv2.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CommanResponse;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_Pin extends AppCompatActivity {

    LinearLayout lin_forgot;
    Button btn_submit;
    String validdata,strUserLoggedPassword;
    com.rengwuxian.materialedittext.MaterialEditText editTextLoginPassword;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);

        sharedPreference = new SharedPreference();

        lin_forgot = (LinearLayout) findViewById(R.id.lin_forgot);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        editTextLoginPassword = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.editTextLoginPassword);
        validdata = sharedPreference.getValue(Forgot_Pin.this, WsConstant.valid_data);

        if(sharedPreference.getValue(Forgot_Pin.this, WsConstant.TAG_LOGGED_USER_PASSWORD)!=null) {
            strUserLoggedPassword = sharedPreference.getValue(Forgot_Pin.this, WsConstant.TAG_LOGGED_USER_PASSWORD);
        }
        lin_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLoginPassword.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(Forgot_Pin.this, "Please enter Email", Toast.LENGTH_LONG).show();
                }
//                else if (!(AppGlobal.checkEmail(editTextLoginPassword.getText().toString()))){
//                    Toast.makeText(Forgot_Pin.this, "Please enter valid Email address", Toast.LENGTH_LONG).show();
                //}
                else {


                    if(strUserLoggedPassword.equals(editTextLoginPassword.getText().toString()))
                    {
                        Toast.makeText(Forgot_Pin.this, "Create New Pin", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Forgot_Pin.this, SetupPinActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("From", "set_pin");
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Toast.makeText(Forgot_Pin.this, "Password is not valid", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });
    }

    private void forgotpass(final String email,final String validdata) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(Forgot_Pin.this))) {
            AppGlobal.showProgressDialog(Forgot_Pin.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("EmailId", email);
            optioMap.put("ValidData", validdata);
            optioMap.put("Type", "1");
            new RestClient(Forgot_Pin.this).getInstance().get().forgotpass(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(Forgot_Pin.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(Forgot_Pin.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            editTextLoginPassword.setText("");
                        } else {
                            Toast.makeText(Forgot_Pin.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Forgot_Pin.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(Forgot_Pin.this);
                        Toast.makeText(Forgot_Pin.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(Forgot_Pin.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
}
