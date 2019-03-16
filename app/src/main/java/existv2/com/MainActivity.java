package existv2.com;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import existv2.com.activity.SignIn;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.fragment.Account_setting;
import existv2.com.fragment.Dashboard;
import existv2.com.fragment.Security;
import existv2.com.fragment.Setting_Kyc_Document;
import existv2.com.fragment.Ticket_History;
import existv2.com.fragment.Transaction_history;
import existv2.com.model.CommanResponse;
import existv2.com.model.Logina.Login;
import existv2.com.services.MyService;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    public static TextView tvtool;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static TextView txt_name,txt_email;
    public static de.hdodenhof.circleimageview.CircleImageView img_dp;
    public static SharedPreference sharedPreference;
    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;
    static String TAG="MainActivity";
//    private MyService myService;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d("FCM",token);

        sharedPreference = new SharedPreference();
        boolean serviceRunningStatus = isServiceRunning(MyService.class);


        if (serviceRunningStatus) {
            //true
            Log.d("service", "true");
        } else {
            //false
            Log.d("service", "false");
            Intent startIntent = new Intent(this, MyService.class);
            startIntent.setAction("start");
            startService(startIntent);
        }

        init();

        //set navigation header
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        txt_name = (TextView) hView.findViewById(R.id.txt_name);
        txt_email = (TextView) hView.findViewById(R.id.txt_email);
        img_dp = (de.hdodenhof.circleimageview.CircleImageView) hView.findViewById(R.id.img_dp);

        headername(MainActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.black));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toolbar.setNavigationIcon(R.color.colorPrimary);
        // navigation change icon color
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();
        Drawable dr = getResources().getDrawable(R.drawable.ic_hamburger_icon);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
       // Drawable drawable = ResourcesCompat.getDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true) , getTheme());
        Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 80, 80, true));
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            //Fingerprint API only available on from Android 6.0 (M)
//            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
//            if (!fingerprintManager.isHardwareDetected()) {
//                // Device doesn't support fingerprint authentication
//                ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setVisibility(View.GONE);
//
//
//            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
//                // User hasn't enrolled any fingerprints to authenticate with
//                ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setVisibility(View.VISIBLE);
//
//            } else {
//                // Everything is ready for fingerprint authentication
//            }
//        }else
        {
//            navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView().setVisibility(View.GONE);
//            navigationView.getMenu().findItem(R.id.nav_finger_print).setVisible(false);
//            sharedPreference.save(MainActivity.this,"false",WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
            // ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setVisibility(View.GONE);
        }
        //   drawer.addDrawerListener(toggle);

        setToolbarname("Dashboard");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new Dashboard()).addToBackStack(null).commit();

        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);


// To set whether switch is on/off use:
     //   ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(true);
      //  String bIsNotificationChecked=sharedPreference.getValue(MainActivity.this,WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
       // Log.d(TAG,"jigar the switch stored have this  " + bIsNotificationChecked);

        //-----------------------------------------------------login pin code start-------------------------------------

        View switchCompatLoginPin=navigationView.getMenu().findItem(R.id.nav_login_pin).getActionView();
        switchCompatLoginPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( AppGlobal.getStringPreference(MainActivity.this,WsConstant.SP_LOGIN_SET_PIN)!=null)
                {
                    if( !AppGlobal.getStringPreference(MainActivity.this,WsConstant.SP_LOGIN_SET_PIN).equals("")) {

                        if (((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_login_pin).getActionView()).isChecked()) {
                            sharedPreference.save(MainActivity.this, "true", WsConstant.TAG_STORED_PIN_SWITCH);
                //            sharedPreference.save(MainActivity.this, "false", WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
                  //          ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(false);
                        } else {
                            sharedPreference.save(MainActivity.this, "false", WsConstant.TAG_STORED_PIN_SWITCH);
                        }
                    }else
                    {
                    //    ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(false);
                        Fragment fragment = null;
                        setToolbarname("Security");
                        fragment = new Security();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);

                    }
                }else
                {
                   // ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(false);
                    Fragment fragment = null;
                    setToolbarname("Security");
                    fragment = new Security();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        if(sharedPreference.getValue(MainActivity.this,WsConstant.TAG_STORED_PIN_SWITCH)!=null)
        {
            String bIsNotificationChecked=sharedPreference.getValue(MainActivity.this,WsConstant.TAG_STORED_PIN_SWITCH);

            Log.d(TAG,"jigar the switch login pin stored have this  " + bIsNotificationChecked);
            //            switchGetComment.setChecked();
            if(bIsNotificationChecked.equals("true"))
            {
                ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_login_pin).getActionView()).setChecked(true);

            }else
            {
                ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_login_pin).getActionView()).setChecked(false);

//                switchGetComment.setChecked(false);
            }
        }else
        {
            ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_login_pin).getActionView()).setChecked(false);

            //          switchGetComment.setChecked(false);
        }
        //-----------------------------------------------------finger print code start-------------------------------------
//        View switchCompatFingerPrint=navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView();
//        switchCompatFingerPrint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//              if(  ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).isChecked())
//              {
//                  sharedPreference.save(MainActivity.this,"true",WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
//                  sharedPreference.save(MainActivity.this,"false",WsConstant.TAG_STORED_PIN_SWITCH);
//                  ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_login_pin).getActionView()).setChecked(false);
//
//
//              }else
//              {
//                  sharedPreference.save(MainActivity.this,"false",WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
//
//              }
//
//
//            }
//        });
//        if(sharedPreference.getValue(MainActivity.this,WsConstant.TAG_STORED_FINGER_PRINT_SWITCH)!=null)
//        {
//            String bIsNotificationChecked=sharedPreference.getValue(MainActivity.this,WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
//
//            Log.d(TAG,"jigar the switch stored have this  " + bIsNotificationChecked);
//            //            switchGetComment.setChecked();
//            if(bIsNotificationChecked.equals("true"))
//            {
//                ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(true);
//
//            }else
//            {
//                ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(false);
//
////                switchGetComment.setChecked(false);
//            }
//        }else
//        {
//            ((SwitchCompat) navigationView.getMenu().findItem(R.id.nav_finger_print).getActionView()).setChecked(false);
//
//            //          switchGetComment.setChecked(false);
//        }

//        Intent service = new Intent(getApplicationContext(), MyService.class);
//        service.setAction("start");
//        getApplicationContext().startService(service);
    }
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals("MyService")) {
                return true;
            }
        }
        return false;
    }
    public static void updateData(final Context context) {
        if (AppGlobal.isNetwork(context)) {
            Map<String, String> optioMap = new HashMap<>();

            optioMap.put("Password", sharedPreference.getValue(context, WsConstant.Password));
            optioMap.put("UserName", sharedPreference.getValue(context, WsConstant.username));
            optioMap.put("ValidData", sharedPreference.getValue(context, WsConstant.valid_data));
            optioMap.put("Type", "1");

            new RestClient(context).getInstance().get().signin(optioMap).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            sharedPreference.save(context, response.body().getInfo().getProfile(), WsConstant.Profile);
                            headername(context);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                }
            });
        }
    }

    public static void headername(Context context) {
        sharedPreference = new SharedPreference();
        final String name = sharedPreference.getValue(context, WsConstant.name);
        final String dp = sharedPreference.getValue(context, WsConstant.Profile);

        Log.d(TAG,"jigar the string in image url of profile is "+dp);
//        if (dp.equalsIgnoreCase("https://opulasoft.com/ExistV2//master/Users/profile/"))
//        {
//            Glide.with(context).load(dp)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    }).placeholder(R.drawable.image_boy)
//                    .into(img_dp);
//        }
//        else
//
            {
                Picasso.with(context)
                        .load(dp)
                        .placeholder(R.drawable.image_boy)
                        .fit()
                        .into(img_dp);
//            Glide.with(context).load(dp)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            return false;
//                        }
//                    })
//                    .placeholder(R.drawable.image_boy)
//                    .into(img_dp);
        }


        String email = sharedPreference.getValue(context, WsConstant.email_id);
        String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
        txt_name.setText(cap);
        txt_email.setText(email);

    }

    @Override
    protected void onResume() {
        doCallReceiveBitalgo();
        super.onResume();
    }

    @Override
    protected void onPause() {
        doCallReceiveBitalgo();
        super.onPause();
    }



    private void doCallReceiveBitalgo() {
        if (AppGlobal.isNetwork(MainActivity.this)) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", sharedPreference.getValue(MainActivity.this, WsConstant.register_id));
            optioMap.put("ValidData", sharedPreference.getValue(MainActivity.this,WsConstant.valid_data));
            optioMap.put("Type", "1");

            new RestClient(MainActivity.this).getInstance().get().receivebitalgo(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(MainActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) { }
            });
        }
    }

    @SuppressLint("CutPasteId")
    private void init() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvtool = toolbar.findViewById(R.id.toolbarname);
    }

    public static void setToolbarname(String name) {
        tvtool.setText(name);
    }


    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else if (frag instanceof Dashboard) {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                MainActivity.this.finish();
                return;
            } else {
                Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                mBackPressed = System.currentTimeMillis();
            }
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_dashboard) {
            fragment = new Dashboard();
            setToolbarname("Dashboard");
        } else if (id == R.id.nav_account_setting) {
            fragment = new Account_setting();
            setToolbarname("Account Setting");
        } else if (id == R.id.nav_transaction) {
            setToolbarname("Transaction History");
            fragment = new Transaction_history();
        } else if (id == R.id.nav_kyc) {
            setToolbarname("KYC Document");
            fragment = new Setting_Kyc_Document();
        }

        else if (id == R.id.nav_logout) {
            showDialog(MainActivity.this);
        } else if (id == R.id.nav_security) {
            setToolbarname("Security");
            fragment = new Security();
        } else if (id == R.id.nav_support) {
            setToolbarname("Ticket History");
            fragment = new Ticket_History();
        } else if (id == R.id.nav_login_pin) {
            //sharedPreference.save(MainActivity.this,"true",WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
        }
//        else if (id == R.id.nav_finger_print) {
//            sharedPreference.save(MainActivity.this,"true",WsConstant.TAG_STORED_FINGER_PRINT_SWITCH);
//
//        }
        else {

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showDialog(Context mContext) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (MainActivity.this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog_logout, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setCustomTitle(View.inflate(mContext, R.layout.alert_back, null));
        final AlertDialog alertDialog = alertDialogBuilder.create();

        Button btn_no = (Button) dialogView.findViewById(R.id.btn_no);
        Button btn_yes = (Button) dialogView.findViewById(R.id.btn_yes);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                navigationView.getMenu().getItem(1).setChecked(true);
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regid = sharedPreference.getValue(MainActivity.this, WsConstant.register_id);
                String token = sharedPreference.getValue(MainActivity.this, WsConstant.valid_data);
                logoutUser(regid,token);
            }
        });
        alertDialog.show();
    }

    private void logoutUser(String regid,String token) {
        if (AppGlobal.isNetwork(MainActivity.this)) {
            AppGlobal.showProgressDialog(MainActivity.this);
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");

            new RestClient(MainActivity.this).getInstance().get().logout(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(MainActivity.this);
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {

                            //remove session
                            sharedPreference.removeValue(MainActivity.this,WsConstant.SESSION);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.SP_LOGIN_SET_PIN);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.name);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.email_id);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.username);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.mobile_no);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.register_id);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.login_status);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.email_verification_status);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.resident_address);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.pincode);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.country_code);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.country_name);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.country_id);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.state_id);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.city_id);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.state_name);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.city_name);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.Profile);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.Fees);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.coin_address);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.public_key);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.private_key);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.merchant_key);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.ticket_number);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.oc);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.type);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.TAG_STORED_PIN_SWITCH);
                            sharedPreference.removeValue(MainActivity.this,WsConstant.TAG_LOGGED_USER_PASSWORD);
                            Intent startIntent = new Intent(MainActivity.this, MyService.class);
                            startIntent.setAction("stop");
                            startService(startIntent);

                            Toast.makeText(MainActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this,SignIn.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(MainActivity.this);
                        Toast.makeText(MainActivity.this,getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.empty_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public void onServiceConnected(ComponentName componentName, IBinder binder) {
////        MyService.MyBinder b = (MyService.MyBinder) binder;
//      //  myService = b.getService();
//  //      Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName componentName) {
//    }


}
