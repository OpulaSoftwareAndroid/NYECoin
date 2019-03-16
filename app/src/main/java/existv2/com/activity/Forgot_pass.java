package existv2.com.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Forgot_pass extends AppCompatActivity {

    LinearLayout lin_forgot;
    Button btn_submit;
    String validdata;
    com.rengwuxian.materialedittext.MaterialEditText et_forgot_email;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        sharedPreference = new SharedPreference();

        lin_forgot = (LinearLayout) findViewById(R.id.lin_forgot);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        et_forgot_email = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.et_forgot_email);
        validdata = sharedPreference.getValue(Forgot_pass.this, WsConstant.valid_data);

        lin_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_forgot_email.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(Forgot_pass.this, "Please enter Email", Toast.LENGTH_LONG).show();
                } else if (!(AppGlobal.checkEmail(et_forgot_email.getText().toString()))){
                    Toast.makeText(Forgot_pass.this, "Please enter valid Email address", Toast.LENGTH_LONG).show();
                } else {
                    String email = et_forgot_email.getText().toString();
                    forgotpass(email,validdata);
                }
            }
        });
    }

    private void forgotpass(final String email,final String validdata) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(Forgot_pass.this))) {
            AppGlobal.showProgressDialog(Forgot_pass.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("EmailId", email);
            optioMap.put("ValidData", validdata);
            optioMap.put("Type", "1");
            new RestClient(Forgot_pass.this).getInstance().get().forgotpass(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(Forgot_pass.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(Forgot_pass.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                            et_forgot_email.setText("");
                        } else {
                            Toast.makeText(Forgot_pass.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(Forgot_pass.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(Forgot_pass.this);
                        Toast.makeText(Forgot_pass.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(Forgot_pass.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
}
