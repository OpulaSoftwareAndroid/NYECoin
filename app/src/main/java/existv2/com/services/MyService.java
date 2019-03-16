package existv2.com.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
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


public class MyService extends Service {
    static final int NOTIFICATION_ID = 543;
    public static Runnable runnable = null;
    public static Handler handler = new Handler();
    SharedPreference sharedPreference;
    private static final String NOTIFICATION_CHANNEL_ID ="notification_channel_id";
    private static final String NOTIFICATION_Service_CHANNEL_ID = "service_channel";
    String TAG="MyService";

    int flag=0;
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreference = new SharedPreference();
    //    callEvent();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }

//    public class MyBinder extends Binder {
//        public MyService getService()
//        {
//            return MyService.this;
//        }
//    }

    private Notification updateNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        return new NotificationCompat.Builder(this)
                .setContentTitle("NYECoin")
                .setTicker("")
                .setContentText("Running in background...")
                .setSmallIcon(R.drawable.logo_icon)
                .setLargeIcon(BitmapFactory.decodeResource(MyService.this.getResources(), R.drawable.logo_icon))
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
    }

    private void doCallReceiveBitalgo() {
        if (AppGlobal.isNetwork(this)) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", sharedPreference.getValue(MyService.this, WsConstant.register_id));
            optioMap.put("ValidData", sharedPreference.getValue(MyService.this,WsConstant.valid_data));
            optioMap.put("Type", "1");

            new RestClient(MyService.this).getInstance().get().receivebitalgo(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    Log.d(TAG,"jigar the do service 1 call recieve nye coin transaction coin "+new Gson().toJson(response));

                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(MyService.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) { }
            });
            callEvent();
        } else {
            callEvent();
        }
    }

    private void doCallReceiveOtherTransactionCoin() {
        if (AppGlobal.isNetwork(this)) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", sharedPreference.getValue(MyService.this, WsConstant.register_id));
            optioMap.put("ValidData", sharedPreference.getValue(MyService.this,WsConstant.valid_data));
            optioMap.put("Type", "1");

            new RestClient(MyService.this).getInstance().get().receiveOtherTransactionCoin(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    if (response.body() != null) {
                        Log.d(TAG,"jigar the  service 2 do call other transaction coin "+new Gson().toJson(response));

                        if (response.body().getStatus() == 1) {
                            Toast.makeText(MyService.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    Log.d(TAG,"jigar the do call error in other transaction coin "+t.getMessage());

                }

            });
            callEvent();
        } else {
            callEvent();
        }
    }
    private void doReceiveOtherTransactionMultiple() {
        if (AppGlobal.isNetwork(this)) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", sharedPreference.getValue(MyService.this, WsConstant.register_id));
            optioMap.put("ValidData", sharedPreference.getValue(MyService.this,WsConstant.valid_data));
            optioMap.put("Type", "1");

            new RestClient(MyService.this).getInstance().get().receiveOtherTransactionMultiple(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    if (response.body() != null) {
                        Log.d(TAG,"jigar the  service 3 do call multiple transaction coin "+new Gson().toJson(response));

                        if (response.body().getStatus() == 1) {
                            Toast.makeText(MyService.this, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) { }
            });
            callEvent();
        } else {
            callEvent();
        }
    }
//    private void callEvent() {
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                doCallReceiveBitalgo();
//                doCallReceiveOtherTransactionCoin();
//                doReceiveOtherTransactionMultiple();
//            }
//        };
//        handler.postDelayed(runnable, 30 * 1000 );
//
//
//
//    }
    private void callEvent() {


            runnable = new Runnable() {
                @Override
                public void run() {
                    doCallReceiveBitalgo();
                }
            };
            handler.postDelayed(runnable, 60 * 1000);


            runnable = new Runnable() {
                @Override
                public void run() {
                    doCallReceiveOtherTransactionCoin();
                }
            };
            handler.postDelayed(runnable, 60 * 1000);

            runnable = new Runnable() {
                @Override
                public void run() {
                    doReceiveOtherTransactionMultiple();
                }
            };
            handler.postDelayed(runnable, 60 * 1000);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

            if(intent!=null) {
                if (intent.getAction().contains("start")) {
                   // callEvent();
                    startServiceWithNotification();
                } else {
                    stopForeground(true);
                    stopSelf();
                }
                return Service.START_STICKY;
            }
        }catch (Exception ex)
        {
            Log.d(TAG,"Exception in main service exception ");
        }
//        return Service.START_STICKY;
        return START_STICKY;

    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction().contains("start")) {
//            startServiceWithNotification();
//        } else {
//            callEvent();
//            stopForeground(true);
//            stopSelf();
//        }
//        return Service.START_STICKY;
//    }


    void startServiceWithNotification() {
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            callEvent();
            startInForeground();
        } else {
            callEvent();
            startForeground(101, updateNotification());
        }
    }

    private void startInForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_icon)
                .setLargeIcon(BitmapFactory.decodeResource(MyService.this.getResources(), R.drawable.logo_icon))
                .setContentTitle("NYECoin")
                .setContentText("Running in background...")
                .setTicker("")
                .setContentIntent(pendingIntent);
        Notification notification=builder.build();
        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_Service_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_Service_CHANNEL_ID);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, notification);
    }
}
