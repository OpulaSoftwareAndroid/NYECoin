package existv2.com.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.fragment.Security;
import existv2.com.model.Logina.Login;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

    TextView txt_forgot;
    Button btn_signinn;
    com.rengwuxian.materialedittext.MaterialEditText et_signin_username,et_signin_password;
    String token;
    SharedPreference sharedPreference;
    LinearLayout lin_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        sharedPreference = new SharedPreference();

        lin_signup = (LinearLayout) findViewById(R.id.lin_signup);
        txt_forgot = (TextView) findViewById(R.id.txt_forgot);
        btn_signinn = (Button) findViewById(R.id.btn_signinn);
        et_signin_username = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.et_signin_username);
        et_signin_password = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.et_signin_password);
        token = sharedPreference.getValue(SignIn.this, WsConstant.valid_data);

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,Forgot_pass.class));
            }
        });

        lin_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,SignUp.class));
                finish();
            }
        });

        btn_signinn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_signin_username.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(SignIn.this, "Please enter Username", Toast.LENGTH_LONG).show();
                } else if (et_signin_password.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(SignIn.this, "Please enter Password", Toast.LENGTH_LONG).show();
                } /*else if (et_signin_password.getText().toString().length() < 8){
                    Toast.makeText(SignIn.this, "Minimum 8 digit Password required", Toast.LENGTH_LONG).show();
                } else if (!(AppGlobal.checkPassword(et_signin_password.getText().toString()))){
                    Toast.makeText(SignIn.this, "Password must be Strong ex:Demo@123", Toast.LENGTH_LONG).show();
                }*/ else {
                    String fullname = et_signin_username.getText().toString();
                    String password = et_signin_password.getText().toString();
                    Signin(password,fullname,token);
                }
            }
        });
    }

    private void Signin(final String password,final String username,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(SignIn.this))) {
            AppGlobal.showProgressDialog(SignIn.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("Password", password);
            optioMap.put("UserName", username);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");
//            optioMap.put("FCM", FirebaseInstanceId.getInstance().getToken());
            new RestClient(SignIn.this).getInstance().get().signin(optioMap).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    AppGlobal.hideProgressDialog(SignIn.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(SignIn.this, response.body().getMsg(), Toast.LENGTH_LONG).show();

                            //save data
                            sharedPreference.save(SignIn.this,response.body().getInfo().getName(),WsConstant.name);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getEmailId(),WsConstant.email_id);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getUsername(),WsConstant.username);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getMobileNo(),WsConstant.mobile_no);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getRegisterId(),WsConstant.register_id);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getLoginStatus(),WsConstant.login_status);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getEmailVerificationStatus(),WsConstant.email_verification_status);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getResidentAddress(),WsConstant.resident_address);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getPincode(),WsConstant.pincode);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getCountryCode(),WsConstant.country_code);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getCountryName(),WsConstant.country_name);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getCountryId(),WsConstant.country_id);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getState(),WsConstant.state_id);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getCity(),WsConstant.city_id);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getProfile(),WsConstant.Profile);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getCityname(),WsConstant.city_name);
                            sharedPreference.save(SignIn.this,response.body().getInfo().getStatename(),WsConstant.state_name);
                            sharedPreference.save(SignIn.this,password,WsConstant.Password);

                            String session = "1";
                            sharedPreference.save(SignIn.this,session, WsConstant.SESSION);
                            sharedPreference.save(SignIn.this,password, WsConstant.TAG_LOGGED_USER_PASSWORD);

                            SharedPreference sharedPreference;
                            sharedPreference=new SharedPreference();
                            sharedPreference.removeValue(SignIn.this,WsConstant.SP_LOGIN_SET_PIN);

//                            AppGlobal.setStringPreference(SignIn.this, stConfirmPin, WsConstant.SP_LOGIN_SET_PIN);
//                            AppGlobal.removeSharedPreferencebyKey(SignIn.this,WsConstant.SP_LOGIN_SET_PIN);
//                            Intent intent = new Intent(SignIn.this, SetupPinActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("From", "set_pin");
//                            startActivity(intent);
//                            finish();

//                            Intent intent = new Intent(SignIn.this, MainActivity.class);
////                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            intent.putExtra("From", "set_pin");
//                            startActivity(intent);
//                            finish();

                            Intent intent = new Intent(SignIn.this, SetupPinActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("From", "set_pin");
                            startActivity(intent);
                            finish();


                        } else {
                            Toast.makeText(SignIn.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignIn.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignIn.this);
                        Toast.makeText(SignIn.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(SignIn.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
}
