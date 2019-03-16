package existv2.com.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CommanResponse;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {

    public static int REQUEST_CAMERA = 101;
    private SharedPreference sharedPreference;
    String version = "";
    String TAG="Splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sharedPreference = new SharedPreference();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sharedPreference.save(Splash.this,"adsEFhsjcnshsuj@kjmjkmrtghy8qq",WsConstant.valid_data);

        if (ActivityCompat.checkSelfPermission(Splash.this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, android.Manifest.permission.ACCESS_WIFI_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.ACCESS_WIFI_STATE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            }
        } else {
            getvirsion();
        }
    }

    private void getvirsion() {
        if (AppGlobal.isNetwork(Objects.requireNonNull(Splash.this))) {
            new RestClient(Splash.this).getInstance().get().getVersion().enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            if (response.body().getInfo().equalsIgnoreCase(version)) {
                                initView();
                            } else {
                                Toast.makeText(Splash.this, "Please update application for use our latest app features", Toast.LENGTH_SHORT).show();
                                try {
                                    String url = "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "&hl=en";
                                    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));

                                    startActivity(viewIntent);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...", Toast.LENGTH_LONG).show();
                                    Splash.this.finish();
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) { }});
        }
    }

    private void getPermission() {
        if (ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.ACCESS_WIFI_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(Splash.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                getvirsion();

            } else {
                getPermission();
            }
        }
    }

    private void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String session = sharedPreference.getValue(Splash.this, WsConstant.SESSION);

                if (session == null || session.equalsIgnoreCase("0")) {
                    Intent i = new Intent(Splash.this, BeforeGetStarted.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.d(TAG,"jigar the switch splash login pin stored have this  "
                            + sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_PIN_SWITCH));
                    Log.d(TAG,"jigar the switch splah finger pin stored have this  "
                            + sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_FINGER_PRINT_SWITCH));

                    if(sharedPreference.getValue(Splash.this,WsConstant.TAG_STORED_FINGER_PRINT_SWITCH)!=null)
                    {
                            if (sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_FINGER_PRINT_SWITCH).equalsIgnoreCase("true")) {
                                Intent intent = new Intent(Splash.this, MainScanFingerPrintActivity.class);

                                startActivity(intent);
                                Splash.this.finish();
                            }
                            else if (sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_PIN_SWITCH).equalsIgnoreCase("true"))
                            {
                                Intent intent = new Intent(Splash.this, SetupPinActivity.class);
                                intent.putExtra("From", "check_pin");
                                startActivity(intent);
                                Splash.this.finish();
                            }else
                            {
                                Intent intent = new Intent(Splash.this, MainActivity.class);
                                //   intent.putExtra("From", "check_pin");
                                startActivity(intent);
                                Splash.this.finish();

                            }
                    }else if (sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_PIN_SWITCH)!=null)
                    {
                        Log.d(TAG,"jigar the switch login pin stored have this  "
                                + sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_PIN_SWITCH));

                        if (sharedPreference.getValue(Splash.this, WsConstant.TAG_STORED_PIN_SWITCH).equalsIgnoreCase("true"))
                        {
                            Intent intent = new Intent(Splash.this, SetupPinActivity.class);
                            intent.putExtra("From", "check_pin");
                            startActivity(intent);
                            Splash.this.finish();
                        }else
                        {
                            Intent intent = new Intent(Splash.this, MainActivity.class);
                            //   intent.putExtra("From", "check_pin");
                            startActivity(intent);
                            Splash.this.finish();

                        }
                    }else
                    {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                     //   intent.putExtra("From", "check_pin");
                        startActivity(intent);
                        Splash.this.finish();

                    }

                }
            }
        }, 1500);
    }
}
