package existv2.com.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CommanResponse;
import existv2.com.model.CountryLista.CountryList;
import existv2.com.model.CountryLista.Info;
import existv2.com.model.Registera.Register;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class SignUp extends AppCompatActivity {

    private ArrayList<Info> contactList;
    private ArrayList<Info> imageList;

    String TAG="SignUp";
    Button btn_signup;
    String country, code, regid, validdata;
    com.rengwuxian.materialedittext.MaterialEditText txt_name, txt_username, txt_code, txt_mobile, txt_email, txt_password, txt_con_password;
    SharedPreference sharedPreference;
    LinearLayout lin_signin;
    Spinner spinner_country;
    Spinner spinnerCountryCode;
    CountryCodePicker pickerCountryCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPreference = new SharedPreference();

        validdata = sharedPreference.getValue(SignUp.this, WsConstant.valid_data);

        spinner_country = (Spinner) findViewById(R.id.spinner_country);
        spinnerCountryCode=findViewById(R.id.spinnerCountryCode);
        txt_name = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_name);
        txt_username = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_username);
        txt_code = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_code);
        txt_mobile = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_mobile);
        txt_email = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_email);
        txt_password = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_password);
        txt_con_password = (com.rengwuxian.materialedittext.MaterialEditText) findViewById(R.id.txt_con_password);
        lin_signin = (LinearLayout) findViewById(R.id.lin_signin);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        pickerCountryCode = (CountryCodePicker) findViewById(R.id.pickerCountryCode);



//        Log.d(TAG,"jigar the shared code value we have is "+code);
//        Log.d(TAG,"jigar the default spinner value we have is "+getIndex(spinnerCountryCode,code));
        pickerCountryCode.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                String strSelectedCode=pickerCountryCode.getSelectedCountryCode();
                String strSelectedName=pickerCountryCode.getSelectedCountryName();
//                String strSelectdCode=pickerCountryCode.getSelectedCountryEnglishName();

                Log.d(TAG,"jigar the selected code is  "+strSelectedCode+" and country name is "+strSelectedName);
                code=strSelectedCode;
                country=strSelectedName.toLowerCase();

                spinner_country.setSelection(getIndex(spinnerCountryCode, code));
                Log.d(TAG,"jigar the shared code value we have is "+code);
                Log.d(TAG,"jigar the default spinner value we have is "+getIndex(spinnerCountryCode,code));
            }
        });
        lin_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"jigar the pattern validation says "+isValidPassword(txt_password.getText().toString()));

                    if (txt_name.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUp.this, "Please enter Name", Toast.LENGTH_LONG).show();
                    } else if (txt_username.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUp.this, "Please enter Username", Toast.LENGTH_LONG).show();
                    } else if (txt_mobile.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUp.this, "Please enter Mobile", Toast.LENGTH_LONG).show();
                    } else if (txt_email.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUp.this, "Please enter Email", Toast.LENGTH_LONG).show();
                    } else if (!(AppGlobal.checkEmail(txt_email.getText().toString()))) {
                        Toast.makeText(SignUp.this, "Please enter valid Email address", Toast.LENGTH_LONG).show();
                    } else if (txt_password.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUp.this, "Please enter Password", Toast.LENGTH_LONG).show();
                    } else if (txt_con_password.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(SignUp.this, "Please enter Confirm Password", Toast.LENGTH_LONG).show();
                    } else if (!(txt_password.getText().toString().equalsIgnoreCase(txt_con_password.getText().toString()))) {
                        Toast.makeText(SignUp.this, "Password is not matching", Toast.LENGTH_LONG).show();

                    } else if(!isValidPassword(txt_password.getText().toString()))
                    {
                            Toast.makeText(SignUp.this, "(Password must have at least 6 characters with at least one Capital letter" +
                                    ", one lower case letter and at one number.", Toast.LENGTH_LONG).show();
                    }
                    else if (spinner_country.getSelectedItemPosition() == 0) {
                        Toast.makeText(SignUp.this, "Please select Country", Toast.LENGTH_LONG).show();
                    } else {
                        String fullname = txt_name.getText().toString();
                        String email = txt_email.getText().toString();
                        String mobile = txt_mobile.getText().toString();
                        String password = txt_password.getText().toString();
                        String username = txt_username.getText().toString();
                        int i = spinner_country.getSelectedItemPosition();
                        country = spinner_country.getSelectedItem().toString();
                        Signup(fullname, email, mobile, password, country, code, username, validdata);
                    }
            }
        });

        contactList = new ArrayList<>();
        imageList = new ArrayList<>();

        getCountry();

        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int i = spinner_country.getSelectedItemPosition();
                if (i == 0) {
                    String code = "Code";
           //         txt_code.setText(code);
                } else if (i > 0) {
                    int j = i - 1;
                    country = contactList.get(j).getCountryName();
                    code = contactList.get(j).getCountryCode();
                    String idd = contactList.get(j).getCountryId();
                    sharedPreference.save(SignUp.this, idd, WsConstant.country_id);
         //           txt_code.setText("+" + code);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }
    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "(?=^.{6,10}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$");


        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    private void getCountry() {
        if (AppGlobal.isNetwork(Objects.requireNonNull(SignUp.this))) {
            AppGlobal.showProgressDialog(SignUp.this);
            Map<String, String> optioMap = new HashMap<>();
            new RestClient(SignUp.this).getInstance().get().getCountry(optioMap).enqueue(new Callback<CountryList>() {
                @Override
                public void onResponse(Call<CountryList> call, Response<CountryList> response) {
                    AppGlobal.hideProgressDialog(SignUp.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            contactList = (ArrayList<Info>) response.body().getInfo();
                            String[] items = new String[contactList.size()+1];
                            items[0] = "Choose Country";
                            for (int i = 0; i < contactList.size(); i++) {
                                items[i+1] = contactList.get(i).getCountryName();
                            }
                            ArrayAdapter<String> adapter;
                            adapter = new ArrayAdapter<String>(Objects.requireNonNull(SignUp.this), R.layout.spinner_item_black, items) {
                                @Override
                                public boolean isEnabled(int position){
                                    if(position == 0)
                                    {
                                        return false;
                                    }
                                    else
                                    {
                                        return true;
                                    }
                                }
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if(position == 0) {
                                        tv.setTextColor(Color.GRAY);
                                    }
                                    else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            spinner_country.setAdapter(adapter);
                        } else {
                            Toast.makeText(SignUp.this, "Unable to load", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CountryList> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignUp.this);
                        Toast.makeText(SignUp.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(SignUp.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

    }


    private void Signup(final String fullname, final String email, final String mobile
            , final String pass, final String countryname, final String code, final String username, final String validdata) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(SignUp.this))) {
            AppGlobal.showProgressDialog(SignUp.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("FullName", fullname);
            optioMap.put("EmailId", email);
            optioMap.put("MobileNo", mobile);
            optioMap.put("Password", pass);
            optioMap.put("CountryName", countryname);
            optioMap.put("MobileCode", code);
            optioMap.put("UserName", username);
            optioMap.put("ValidData", validdata);
            optioMap.put("Type", "1");
            new RestClient(SignUp.this).getInstance().get().signup(optioMap).enqueue(new Callback<Register>() {
                @Override
                public void onResponse(Call<Register> call, Response<Register> response) {
                    AppGlobal.hideProgressDialog(SignUp.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            sharedPreference.save(SignUp.this, validdata, WsConstant.valid_data);
                            sharedPreference.save(SignUp.this, pass, WsConstant.Password);
                            regid = "";
                            try {
                                regid = String.valueOf(response.body().getInfo());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!(regid.equalsIgnoreCase(""))) {
                                resendmail(SignUp.this);
                            }
                        } else {
                            Toast.makeText(SignUp.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Register> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignUp.this);
                        Toast.makeText(SignUp.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(SignUp.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

    }

    public void resendmail(Context mContext) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (SignUp.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog_resend_mail, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setCustomTitle(View.inflate(mContext, R.layout.alert_back, null));
        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button btn_signinnn = dialogView.findViewById(R.id.btn_signinnn);
        Button btn_resendmail = dialogView.findViewById(R.id.btn_resendmail);

        btn_signinnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                startActivity(new Intent(SignUp.this, SignIn.class));
                finish();
            }
        });

        btn_resendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String token = sharedPreference.getValue(SignUp.this, WsConstant.valid_data);
                resendmaildone(regid, validdata);
            }
        });

        alertDialog.show();
    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    private void resendmaildone(final String regid, final String validdata) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(SignUp.this))) {
            AppGlobal.showProgressDialog(SignUp.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", validdata);
            optioMap.put("Type", "1");
            new RestClient(SignUp.this).getInstance().get().resendmail(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(SignUp.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(SignUp.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignUp.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SignUp.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(SignUp.this);
                        Toast.makeText(SignUp.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(SignUp.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

    }
}
