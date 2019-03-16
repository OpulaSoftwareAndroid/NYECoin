package existv2.com.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import existv2.com.MainActivity;
import existv2.com.MoneyValueFilter;
import existv2.com.My_App_Controller;
import existv2.com.R;
import existv2.com.activity.BarCodeScanActivity;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.CoinAdapter;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CommanResponse;
import existv2.com.model.POJOCoin;
import existv2.com.model.Senda.Send;
import existv2.com.model.UserDetails.UserDetailsPojo;
import existv2.com.model.Wallatea.Wallate;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Fragment {

    SharedPreference sharedPreference;
    String regid,validdata;
    TextView txt_coin,txt_card_coin,txt_total,txt_pending;
    LinearLayout lin_send,lin_receive,lin_transaction_history,lin_pending_ticket,lin_total_ticket;
    com.rengwuxian.materialedittext.MaterialEditText et_paxex_address,et_paxex_amount,et_login_password
            ,editTextRemarks,editTextYourOTP;
    AlertDialog alertDialog;
    float amountt;
    private List<POJOCoin> coinList = new ArrayList<>();
    private CoinAdapter coinAdapter;
    String TAG="Dashboard";
    RecyclerView recyclerViewCoinList;
    double total = 0;
    String strStoredOTP;
    public Dashboard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        MainActivity.setToolbarname("Wallet");

        sharedPreference = new SharedPreference();
        validdata = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
        regid = sharedPreference.getValue(getActivity(), WsConstant.register_id);
        txt_coin = rootView.findViewById(R.id.coin);
        txt_total = rootView.findViewById(R.id.txt_total);
        txt_pending = rootView.findViewById(R.id.txt_pending);
        txt_card_coin = rootView.findViewById(R.id.txt_card_coin);
        lin_send = rootView.findViewById(R.id.lin_send);
        lin_receive = rootView.findViewById(R.id.lin_receive);
        lin_transaction_history = rootView.findViewById(R.id.lin_transaction_history);
        lin_pending_ticket = rootView.findViewById(R.id.lin_pending_ticket);
        lin_total_ticket = rootView.findViewById(R.id.lin_total_ticket);
        recyclerViewCoinList=rootView.findViewById(R.id.recylerViewCoinList);
        coinAdapter = new CoinAdapter(coinList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewCoinList.setLayoutManager(mLayoutManager);
        recyclerViewCoinList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCoinList.setAdapter(coinAdapter);

        lin_transaction_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setToolbarname("Transaction History");
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Transaction_history()).addToBackStack(null).commit();
            }
        });

        lin_total_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setToolbarname("Ticket History");
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Ticket_History()).addToBackStack(null).commit();
            }
        });

        lin_pending_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setToolbarname("Ticket History");
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Ticket_History()).addToBackStack(null).commit();
            }
        });

        lin_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSend(getActivity());
            }
        });

        lin_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogReceive(getActivity());
            }
        });

     //   wallate(regid,validdata);
        getUserByIDRetro(regid,validdata);
        setCoinListInDashBoard();
   //     getUserDetailByID();
        return rootView;
    }
    public void setCoinListInDashBoard()
    {

        StringRequest req = new StringRequest(Request.Method.POST, WsConstant.API_COIN_LIST,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        try {
                            Log.v(TAG, "jigar the response user login " + res);


                            JSONObject response = new JSONObject(res);

//                            {"status":1,"msg":"Successfully Registered.","info":21933002}
                            String strMessage = response.getString(WsConstant.TAG_MSG);

                            String strStatus=response.getString(WsConstant.TAG_STATUS);
                            if(strStatus.equals("1")) {
                                JSONArray jsonArrayInfo=response.getJSONArray(WsConstant.TAG_INFO);
//                                "short_name": "NYE",
//                                        "fullname": "NYE",
//                                        "image": "nye.svg",
//                                        "current_price": "4.00",
//                                        "chnage_per": "-0.040",
//                                        "img": "https://nyecoin.io/TestginCoin/img/nye.svg"
                                for (int i=0;i<jsonArrayInfo.length();i++)
                                {

                                    JSONObject jsonObjectMainCoin=jsonArrayInfo.getJSONObject(i);
                                    String strCoinFullName=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_FULL_NAME);
                                    String strCoinShortName=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_SHORT_NAME);
                                    String strCoinImage=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_IMAGE_URL);
                                    String strCoinCurrentPrice=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_CURRENT_PRICE);
                                    String strCoinChangePricePer=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_CHANGE_PRICE_PER);
//    public POJOCoin(String strShortName, String strFullName, String strCurrentPrice
//                                        ,String strCoinImageUrl,String strCoinPriceChangePer) {

                                    POJOCoin  pojoCoin=new POJOCoin(strCoinShortName,strCoinFullName,strCoinCurrentPrice
                                            ,strCoinImage,strCoinChangePricePer);

                                    coinList.add(pojoCoin);
                                }
                                coinAdapter.notifyDataSetChanged();
                            }else
                            {
                                Toast.makeText(getContext(),strMessage,Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            Log.v(TAG, "jigar the json error user login " + e);

                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            Log.v(TAG, "jigar the null pointer user register " + e);

                            e.printStackTrace();
                        } catch (Exception e) {
                            Log.v(TAG, "jigar the exception in user register " + e);

                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.v(TAG, "jigar the volley error user login " + error);

            }


        }) {


//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> map = new HashMap<String, String>();
////                strDOB = editTextDOB.getText().toString();
////                strMobileNumber = editTextMobileNumber.getText().toString();
////                strUserName = editTextMobileNumber.getText().toString();
////                strPassword = editTextPassword.getText().toString();
////                strConfirmPassword = editTextConfirmPassword.getText().toString();
////                strEmailID = editTextEmailID.getText().toString();
////                //    strUserWeight=  editTextUserWeight.getText().toString();
////                //      strEmailID=  editTextEmailID.getText().toString();
////                strUserWeight = editTextUserWeight.getText().toString();
////                strUserHeight = editTextUserHeight.getText().toString();
////                strMedicalCondition = editTextMedicalCondition.getText().toString();
////                strDailyActivity = editTextDailyActivity.getText().toString();
//                map.put(Constants.TAG_NAME, strUserName);
//                map.put(Constants.TAG_EMAIL_ID, strEmailID);
//                map.put(Constants.TAG_GENDER, strGender);
//                map.put(Constants.TAG_MOBILE_NO, "" + strMobileNumber);
//                map.put(Constants.TAG_DOB, "" + strDOB);
//                map.put(Constants.TAG_PASSWORD, "" + strPassword);
//                map.put(Constants.TAG_WEIGHT, "" + strUserWeight);
//                map.put(Constants.TAG_HEIGHT, "" + strUserHeight);
//                map.put(Constants.TAG_WORKOUT_START_TIME, "" + strWorkoutStartTime);
//                map.put(Constants.TAG_WORKOUT_END_TIME, "" + strWorkoutEndTime);
//                map.put(Constants.TAG_REST_START_TIME, "" + strRestStartTime);
//                map.put(Constants.TAG_REST_END_TIME, "" + strRestEndTime);
//                map.put(Constants.TAG_MEDICAL_CONDITION, "" + strMedicalCondition);
//                map.put(Constants.TAG_DAILY_ACTIVITY, "" + strDailyActivity);
//                map.put(Constants.TAG_VALID_DATA, "AjsEFhsjcnshsuj@kjmjkmrtghy8rr");
//                map.put(Constants.TAG_TYPE, "1");
//                Log.v(TAG, "jigar the set user register params=" + map);
//                return map;
////                "Name" : "Kevin Dhimmar",
////                        "Gender" : "Male",
////                        "MobileNo" : "123456789111",
////                        "DOB" : "06-02-1996",
////                        "Password" : "12345678",
////                        "Weigth" : "50",
////                        "EmailID"
////                "Heigth" : "5",
////                        "WorkoutStartTime" : "05:02:02",
////                        "WorkoutEndTime" : "05:02:02",
////                        "StartRestTime" : "05:02:02",
////                        "EndRestTime" : "05:02:02",
////                        "MedicalCondition" : "No",
////                        "DailyActivity" : "Travelling",
////                        "ValidData":"AjsEFhsjcnshsuj@kjmjkmrtghy8rr",
////                        "Type":"1"
//            }

//            @Override
//            public byte[] getBody() throws com.android.volley.AuthFailureError {
//                String strValidData="AjsEFhsjcnshsuj@kjmjkmrtghy8rr";
//                String str = "{" +
//                        "\""+WsConstant.TAG_MOBILE_NO+"\" : \""+strMobileNumber+"\",\"" +
//                        ""+WsConstant.TAG_PASSWORD+"\" : \""+strPassword+"\",\"" +
//                        ""+WsConstant.TAG_VALID_DATA+"\" : \""+strValidData+"\",\"" +
//                        ""+WsConstant.TAG_TYPE+"\" : \"1\"" +"}";
//                Log.v(TAG, "jigar the user login params=" + str);
//
//                return str.getBytes();
//            };
//
//            public String getBodyContentType()
//            {
//                return "application/json; charset=utf-8";
//            }


        };

        My_App_Controller.getInstance().addToRequestQueue(req, TAG);

    }

    public void getUserDetailByID()
    {

        StringRequest req = new StringRequest(Request.Method.POST, WsConstant.API_USER_DETAIL_BY_ID,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        try {
                            Log.v(TAG, "jigar the response user detail by login " + res);


                            JSONObject response = new JSONObject(res);

//                            {"status":1,"msg":"Successfully Registered.","info":21933002}
                            String strMessage = response.getString(WsConstant.TAG_MSG);

                            String strStatus=response.getString(WsConstant.TAG_STATUS);
                            if(strStatus.equals("1")) {
                                JSONArray jsonArrayInfo=response.getJSONArray(WsConstant.TAG_INFO);

//                                "short_name": "NYE",
//                                        "fullname": "NYE",
//                                        "image": "nye.svg",
//                                        "current_price": "4.00",
//                                        "chnage_per": "-0.040",
//                                        "img": "https://nyecoin.io/TestginCoin/img/nye.svg"

                                for (int i=0;i<jsonArrayInfo.length();i++)
                                {

                                    JSONObject jsonObjectMainCoin=jsonArrayInfo.getJSONObject(i);
                                    String strCoinFullName=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_FULL_NAME);
                                    String strCoinShortName=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_SHORT_NAME);
                                    String strCoinImage=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_IMAGE_URL);
                                    String strCoinCurrentPrice=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_CURRENT_PRICE);
                                    String strCoinChangePricePer=jsonObjectMainCoin.getString(WsConstant.TAG_COIN_CHANGE_PRICE_PER);
//    public POJOCoin(String strShortName, String strFullName, String strCurrentPrice
//                                        ,String strCoinImageUrl,String strCoinPriceChangePer) {

//                                    POJOCoin  pojoCoin=new POJOCoin(strCoinShortName,strCoinFullName,strCoinCurrentPrice
//                                            ,strCoinImage,strCoinChangePricePer);
//
//                                    coinList.add(pojoCoin);
                                }
//                                coinAdapter.notifyDataSetChanged();
                            }else
                            {
                                Toast.makeText(getContext(),strMessage,Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            Log.v(TAG, "jigar the json error user  user detail by login " + e);

                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            Log.v(TAG, "jigar the null pointer user user detail by  " + e);

                            e.printStackTrace();
                        } catch (Exception e) {
                            Log.v(TAG, "jigar the exception in  user detail by  " + e);

                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.v(TAG, "jigar the volley error user login " + error);

            }


        }) {


//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> map = new HashMap<String, String>();
////                strDOB = editTextDOB.getText().toString();
////                strMobileNumber = editTextMobileNumber.getText().toString();
////                strUserName = editTextMobileNumber.getText().toString();
////                strPassword = editTextPassword.getText().toString();
////                strConfirmPassword = editTextConfirmPassword.getText().toString();
////                strEmailID = editTextEmailID.getText().toString();
////                //    strUserWeight=  editTextUserWeight.getText().toString();
////                //      strEmailID=  editTextEmailID.getText().toString();
////                strUserWeight = editTextUserWeight.getText().toString();
////                strUserHeight = editTextUserHeight.getText().toString();
////                strMedicalCondition = editTextMedicalCondition.getText().toString();
////                strDailyActivity = editTextDailyActivity.getText().toString();
//                map.put(Constants.TAG_NAME, strUserName);
//                map.put(Constants.TAG_EMAIL_ID, strEmailID);
//                map.put(Constants.TAG_GENDER, strGender);
//                map.put(Constants.TAG_MOBILE_NO, "" + strMobileNumber);
//                map.put(Constants.TAG_DOB, "" + strDOB);
//                map.put(Constants.TAG_PASSWORD, "" + strPassword);
//                map.put(Constants.TAG_WEIGHT, "" + strUserWeight);
//                map.put(Constants.TAG_HEIGHT, "" + strUserHeight);
//                map.put(Constants.TAG_WORKOUT_START_TIME, "" + strWorkoutStartTime);
//                map.put(Constants.TAG_WORKOUT_END_TIME, "" + strWorkoutEndTime);
//                map.put(Constants.TAG_REST_START_TIME, "" + strRestStartTime);
//                map.put(Constants.TAG_REST_END_TIME, "" + strRestEndTime);
//                map.put(Constants.TAG_MEDICAL_CONDITION, "" + strMedicalCondition);
//                map.put(Constants.TAG_DAILY_ACTIVITY, "" + strDailyActivity);
//                map.put(Constants.TAG_VALID_DATA, "AjsEFhsjcnshsuj@kjmjkmrtghy8rr");
//                map.put(Constants.TAG_TYPE, "1");
//                Log.v(TAG, "jigar the set user register params=" + map);
//                return map;
////                "Name" : "Kevin Dhimmar",
////                        "Gender" : "Male",
////                        "MobileNo" : "123456789111",
////                        "DOB" : "06-02-1996",
////                        "Password" : "12345678",
////                        "Weigth" : "50",
////                        "EmailID"
////                "Heigth" : "5",
////                        "WorkoutStartTime" : "05:02:02",
////                        "WorkoutEndTime" : "05:02:02",
////                        "StartRestTime" : "05:02:02",
////                        "EndRestTime" : "05:02:02",
////                        "MedicalCondition" : "No",
////                        "DailyActivity" : "Travelling",
////                        "ValidData":"AjsEFhsjcnshsuj@kjmjkmrtghy8rr",
////                        "Type":"1"
//            }

            @Override
            public byte[] getBody() throws com.android.volley.AuthFailureError {
                String str = "{" +

                        "\""+WsConstant.TAG_REGISTERED_ID+"\" : \""+regid+"\",\"" +
                        ""+WsConstant.TAG_VALID_DATA+"\" : \""+WsConstant.TAG_VALID_DATA_STATIC+"\",\"" +
                        ""+WsConstant.TAG_TYPE+"\" : \"1\"" +"}";
                Log.v(TAG, "jigar the user login params=" + str);

                return str.getBytes();
            };

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }


        };

        My_App_Controller.getInstance().addToRequestQueue(req, TAG);

    }

    @SuppressLint("DefaultLocale")
    public void showDialogSend(Context mContext) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = (getActivity()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog_send, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        final TextView tv_txt = dialogView.findViewById(R.id.tv_txt);
        final TextView edFees = dialogView.findViewById(R.id.tv_Send_TRC_Fees);
        final TextView tvTotalAmount = dialogView.findViewById(R.id.tv_Send_TRC_TotalAmount);
        final TextView textViewNyeWalletBalance = dialogView.findViewById(R.id.textViewNyeWalletBalance);

        textViewNyeWalletBalance.setText(txt_coin.getText().toString());
        et_paxex_address = dialogView.findViewById(R.id.et_paxex_address);
        et_paxex_amount = dialogView.findViewById(R.id.et_paxex_amount);
        et_login_password = dialogView.findViewById(R.id.et_login_password);
        editTextRemarks = dialogView.findViewById(R.id.editTextRemarks);
        editTextYourOTP = dialogView.findViewById(R.id.editTextOTP);
        Button btn_paxex_send = dialogView.findViewById(R.id.btn_paxex_send);
        ImageView btn_paste = dialogView.findViewById(R.id.btn_paste);
        LinearLayout linear_Send_Paxex_QrCode = dialogView.findViewById(R.id.linear_Send_Paxex_QrCode);

        editTextYourOTP.setVisibility(View.GONE);
        alertDialog = alertDialogBuilder.create();

        String fees = sharedPreference.getValue(getActivity(),WsConstant.Fees);
        tv_txt.setText("Fees : " + fees + "%");

        et_paxex_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_paxex_amount.getText().toString().trim().equalsIgnoreCase("") ||
                        et_paxex_amount.getText().toString().trim().equalsIgnoreCase("0") ||
                        et_paxex_amount.getText().toString().equalsIgnoreCase(".")) {
                    tvTotalAmount.setText("0");
                    edFees.setText("0");
                } else {
                    double mainFee = Double.parseDouble(sharedPreference.getValue(getActivity(), WsConstant.Fees));
                    double mainAmount = Double.parseDouble(et_paxex_amount.getText().toString());
                    double fees = (mainAmount * mainFee) / 100;

                    total = mainAmount - fees;

                    edFees.setText("Fees : " + String.format("%.8f", fees));
                    tvTotalAmount.setText("Total Amount : " + String.format("%.8f", total));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int cursorPosition = et_paxex_amount.getSelectionEnd();
                String originalStr = et_paxex_amount.getText().toString();

                //To restrict only two digits after decimal place
                et_paxex_amount.setFilters(new InputFilter[]{new MoneyValueFilter(Integer.parseInt("8"))});

                try {
                    et_paxex_amount.removeTextChangedListener(this);
                    String value = et_paxex_amount.getText().toString();

                    if (!value.equals("")) {
                        if (value.startsWith(".")) {
                            et_paxex_amount.setText("0.");
                        }
                        String str = et_paxex_amount.getText().toString().replaceAll(",", "");
                        if (!value.equals("")){
                            et_paxex_amount.setText(getDecimalFormattedString(str));
                        }

                        int diff = et_paxex_amount.getText().toString().length() - originalStr.length();
                        et_paxex_amount.setSelection(cursorPosition + diff);
                    }
                    et_paxex_amount.addTextChangedListener(this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    et_paxex_amount.addTextChangedListener(this);
                }
            }
        });

        linear_Send_Paxex_QrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BarCodeScanActivity.class);
                startActivityForResult(i, 101);
            }
        });

        btn_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager _clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
                assert _clipboard != null;
                CharSequence textToPaste = "";
                try {
                    textToPaste = _clipboard.getPrimaryClip().getItemAt(0).getText();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                et_paxex_address.setText(textToPaste);
            }
        });

        btn_paxex_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(et_paxex_amount.getText().toString().equalsIgnoreCase(""))){
                    amountt = Float.parseFloat(et_paxex_amount.getText().toString());
                }
                if (et_paxex_address.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter " + getResources().getString(R.string.app_name) + " address", Toast.LENGTH_SHORT).show();
                } else if (et_paxex_amount.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
                } else if (amountt<=0){
                    Toast.makeText(getActivity(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                } else if (et_login_password.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    String add = et_paxex_address.getText().toString();
                    String amount = et_paxex_amount.getText().toString();
                    String pass = et_login_password.getText().toString();
                    String regid = sharedPreference.getValue(getActivity(),WsConstant.register_id);
                    String token = sharedPreference.getValue(getActivity(),WsConstant.valid_data);
                    String remarks = editTextRemarks.getText().toString();

                    strStoredOTP= sharedPreference.getValue(getActivity(), WsConstant.OTP_CODE);
                    Log.d(TAG,"jigar the otp before passing to transaction is "+strStoredOTP);
                    if(editTextYourOTP.getText().toString().equals(""))
                    {
                        sendNYECoinForOTP(regid, amount, add, pass, token, remarks);
                    }else
                    {
                        if(editTextYourOTP.getText().toString().equals(strStoredOTP)) {
                            sendNYECoin(regid, amount, add, pass, token, remarks, editTextYourOTP.getText().toString());
                        }else
                        {
                            Toast.makeText(getActivity(), "OTP Mismatch! Please Enter Valid OTP ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //                    if(strStoredOTP!=null ) {
//                        if (strStoredOTP.equals("")) {
//                            sendNYECoinForOTP(regid, amount, add, pass, token, remarks);
//
//                        } else {
//                            sendNYECoin(regid, amount, add, pass, token, remarks,strStoredOTP);
//                        }
//                    }else
//                    {
//                        sendNYECoinForOTP(regid, amount, add, pass, token, remarks);
//
//                    }
                }
            }
        });
        alertDialog.show();
    }

    public static String getDecimalFormattedString(String value) {
        if (value != null && !value.equalsIgnoreCase("")) {
            StringTokenizer lst = new StringTokenizer(value, ".");
            String str1 = value;
            String str2 = "";
            if (lst.countTokens() > 1) {
                str1 = lst.nextToken();
                str2 = lst.nextToken();
            }
            String str3 = "";
            int i = 0;
            int j = -1 + str1.length();
            if (str1.charAt(-1 + str1.length()) == '.') {
                j--;
                str3 = ".";
            }
            for (int k = j; ; k--) {
                if (k < 0) {
                    if (str2.length() > 0)
                        str3 = str3 + "." + str2;
                    return str3;
                }
                str3 = str1.charAt(k) + str3;
                i++;
            }
        }
        return "";
    }

    public void showDialogReceive(Context mContext) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog_receive, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        final TextView txt_paxex_address = dialogView.findViewById(R.id.txt_paxex_address);
        ImageView btn_copy = dialogView.findViewById(R.id.btn_copy);
        Button btnCopy=dialogView.findViewById(R.id.btnCopy);

        ImageView btn_share = dialogView.findViewById(R.id.btn_share);
        ImageView img_barcode = dialogView.findViewById(R.id.img_barcode);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        if(sharedPreference.getValue(getActivity(),WsConstant.coin_address)!=null) {
            txt_paxex_address.setText(sharedPreference.getValue(getActivity(), WsConstant.coin_address));
        }else
        {
            txt_paxex_address.setText("");
        }
        final String add = txt_paxex_address.getText().toString();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(add, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            img_barcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager _clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
                assert _clipboard != null;
                _clipboard.setText(txt_paxex_address.getText().toString());
                Toast.makeText(getActivity(), "Address Copied to Clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager _clipboard = (ClipboardManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CLIPBOARD_SERVICE);
                assert _clipboard != null;
                _clipboard.setText(txt_paxex_address.getText().toString());
                Toast.makeText(getActivity(), "Address Copied to Clipboard", Toast.LENGTH_SHORT).show();

            }
        });


        final String finalAdd = add;
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, finalAdd);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });




        alertDialog.show();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_sync, menu);
        MenuItem searchItem = menu.findItem(R.id.sync);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
             //   wallate(regid,validdata);
                getUserByIDRetro(regid,validdata);
                doCallReceiveNYECoin();
                return false;

            }
        });
    }


    private void doCallReceiveNYECoin() {
        if (AppGlobal.isNetwork(getActivity())) {
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", sharedPreference.getValue(getActivity(), WsConstant.register_id));
            optioMap.put("ValidData", sharedPreference.getValue(getActivity(), WsConstant.valid_data));
            optioMap.put("Type", "1");

            new RestClient(getActivity()).getInstance().get().receivebitalgo(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {

                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                }
            });
        }
    }

    private void sendNYECoin(final String regid, String amount, String address, String password, String token, String remarks, final String strOTP)
    {
        if (AppGlobal.isNetwork(getActivity())) {
            AppGlobal.showProgressDialog(getActivity());
         //  address= "QVkcbsK5J9xzShTSe1N7RqyQLaX1Pgd7";
           Log.d(TAG,"jigar the otp we have after service call is "+strOTP);
            Map<String, String> optioMap = new HashMap<>();
                    optioMap.put("RegisterId", regid);
                    optioMap.put("OtherAmount", amount);
                    optioMap.put("ToAddress", address);
                    optioMap.put("OtpCode", strOTP);
                    optioMap.put("remark", remarks);
                    optioMap.put("TransactionPassword", password);
                    optioMap.put("ValidData", token);
                    optioMap.put("Type", "1");
//            "RegisterId" : "55859374",
//                    "OtherAmount" : "1",
//                    "ToAddress" : "QVkcbsK5J9xzShTSe1N7RqyQLaX1Pgd7",
//                    "TransactionPassword" : "
//                    "OtpCode" : "",
//                    "remark" : "test",
//                  12345678",  "ValidData" : "AjsEFhsjcnshsuj@kjmjkmrtghy8rr",
//                    "Type" : "1"

            new RestClient(getActivity()).getInstance().get().sendNYECoin(optioMap).enqueue(new Callback<Send>() {
                @Override
                public void onResponse(Call<Send> call, Response<Send> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    Log.d(TAG,"jigar the send amount we getting is "+new Gson().toJson(response));
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();
                        }
                        else if(response.body().getStatus()==2)
                        {
                            alertDialog.dismiss();

                        }
                        else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();

                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<Send> call, Throwable t) {
                    try {
                        Log.d(TAG,"jigar the error send on failure is  "+t.getMessage());

                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
        editTextYourOTP.setVisibility(View.GONE);
        sharedPreference.removeValue(getContext(),strOTP);
    }
    private void sendNYECoinForOTP(final String regid, final String amount,String address, final String password, final String token, final String remarks) {
        if (AppGlobal.isNetwork(getActivity())) {
            AppGlobal.showProgressDialog(getActivity());
          //  address= "QVkcbsK5J9xzShTSe1N7RqyQLaX1Pgd7";
            Map<String, String> optioMap = new HashMap<>();

                    optioMap.put("RegisterId", regid);
                    optioMap.put("OtherAmount", amount);
                    optioMap.put("ToAddress", address);
                    optioMap.put("OtpCode", "");
                    optioMap.put("remark", remarks);
                    optioMap.put("TransactionPassword", password);
                    optioMap.put("ValidData", token);
                    optioMap.put("Type", "1");

//            "RegisterId" : "55859374",
//                    "OtherAmount" : "1",
//                    "ToAddress" : "QVkcbsK5J9xzShTSe1N7RqyQLaX1Pgd7",
//                    "TransactionPassword" : "12345678",
//                    "OtpCode" : "",
//                    "remark" : "test",
//                    "ValidData" : "AjsEFhsjcnshsuj@kjmjkmrtghy8rr",
//                    "Type" : "1"

            final String finalAddress2 = address;
            new RestClient(getActivity()).getInstance().get().sendNYECoin(optioMap).enqueue(new Callback<Send>() {
                @Override
                public void onResponse(Call<Send> call, Response<Send> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    Log.d(TAG,"jigar the send amount we getting is "+new Gson().toJson(response));
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                        else if(response.body().getStatus()==2)
                        {
                            strStoredOTP=response.body().getInfo();
                            Toast.makeText(getActivity(), "OTP is " + response.body().getInfo(), Toast.LENGTH_SHORT).show();
                            sharedPreference.save(getContext(),strStoredOTP,WsConstant.OTP_CODE);
                            editTextYourOTP.setVisibility(View.VISIBLE);
                          //  sendNYECoin(regid, amount, finalAddress2,password, token, remarks,strStoredOTP);

                        }
                        else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Send> call, Throwable t) {
                    try {
                        Log.d(TAG,"jigar the error send on failure is  "+t.getMessage());

                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void wallate(final String regid,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");

            new RestClient(getActivity()).getInstance().get().wallate(optioMap).enqueue(new Callback<Wallate>() {
                @Override
                public void onResponse(Call<Wallate> call, Response<Wallate> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            String amount = response.body().getInfo().getAmount();
                            String total_ticket = String.valueOf(response.body().getInfo().getTotalTicket());
                            String pending_ticket = String.valueOf(response.body().getInfo().getPendingTicket());
                            txt_total.setText(total_ticket);
                            txt_pending.setText(pending_ticket);
                            txt_coin.setText(amount);
                            txt_card_coin.setText(amount);
                            sharedPreference.save(getActivity(),response.body().getInfo().getFees(),WsConstant.Fees);
                            sharedPreference.save(getActivity(),response.body().getInfo().getCoinAddress(),WsConstant.coin_address);
                            sharedPreference.save(getActivity(),response.body().getInfo().getPublicKey(),WsConstant.public_key);
                            sharedPreference.save(getActivity(),response.body().getInfo().getPrivateKey(),WsConstant.private_key);
                            sharedPreference.save(getActivity(),response.body().getInfo().getMerchantKey(),WsConstant.merchant_key);
                            sharedPreference.save(getActivity(),response.body().getInfo().getProfile(),WsConstant.Profile);
                            sharedPreference.save(getActivity(),response.body().getInfo().getName(),WsConstant.name);
                            sharedPreference.save(getActivity(),response.body().getInfo().getMobileNo(),WsConstant.mobile_no);
                            sharedPreference.save(getActivity(),response.body().getInfo().getResidentAddress(),WsConstant.resident_address);
                            sharedPreference.save(getActivity(),response.body().getInfo().getCity(),WsConstant.city_id);
                            sharedPreference.save(getActivity(),response.body().getInfo().getState(),WsConstant.state_id);
                            sharedPreference.save(getActivity(),response.body().getInfo().getPincode(),WsConstant.pincode);
                            sharedPreference.save(getActivity(),response.body().getInfo().getCityname(),WsConstant.city_name);
                            sharedPreference.save(getActivity(),response.body().getInfo().getStatename(),WsConstant.state_name);

                            MainActivity.headername(getActivity());

                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Wallate> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(),getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }
    private void getUserByIDRetro(final String regid,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");
            Log.d(TAG,"jigar the user register id we getting is "+regid);

            new RestClient(getActivity()).getInstance().get().getUserDetailByID(optioMap).
                    enqueue(new Callback<UserDetailsPojo>() {
                @Override
                public void onResponse(Call<UserDetailsPojo> call, Response<UserDetailsPojo> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {

                            Log.d(TAG,"jigar the user details by id we getting is "+new Gson().toJson(response));
                            String amount = response.body().getInfo().getUserWallet().getAmount();
//                            String total_ticket = String.valueOf(response.body().getInfo().getTotalTicket());
//                            String pending_ticket = String.valueOf(response.body().getInfo().getPendingTicket());
                             txt_coin.setText(amount);
                            txt_card_coin.setText(amount);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserWallet().getAmount(),WsConstant.Fees);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserWallet().getCoinAddress(),WsConstant.coin_address);
//                            sharedPreference.save(getActivity(),response.body().getInfo().getUserWallet().getUserApiKey(),WsConstant.public_key);
//                            sharedPreference.save(getActivity(),response.body().getInfo().getPrivateKey(),WsConstant.private_key);
//                            sharedPreference.save(getActivity(),response.body().getInfo().getMerchantKey(),WsConstant.merchant_key);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getProfileImage(),WsConstant.Profile);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getName(),WsConstant.name);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getMobileNo(),WsConstant.mobile_no);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getResidentAddress(),WsConstant.resident_address);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getResidentStatus(),WsConstant.resident_status);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentNumber(),WsConstant.document_number);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentExpireDate(),WsConstant.document_expiry_date);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentType(),WsConstant.document_type);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getDocumentName(),WsConstant.document_name);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getCity(),WsConstant.city_id);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getState(),WsConstant.state_id);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getPincode(),WsConstant.pincode);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getCity(),WsConstant.city_name);
                            sharedPreference.save(getActivity(),response.body().getInfo().getUserInfo().getState(),WsConstant.state_name);
                            sharedPreference.save(getActivity(),response.body().getInfo().getFeeDetail().getPercentage(),WsConstant.Fees);

                            MainActivity.headername(getActivity());


                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDetailsPojo> call, Throwable t) {

                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(),getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == 101) {
            if (data != null) {
                String contents = data.getStringExtra("Barcode");

                if (contents.contains("bitcoins:")) {
                    contents = contents.replace("bitcoins:", "");
                }
                if (contents.contains("?")) {
                    //   contents=contents.split("\\?")[0];
                    et_paxex_address.setText(contents.split("\\?")[0]);
                } else {
                    et_paxex_address.setText(contents);

                }
                et_paxex_address.setSelection(et_paxex_address.getText().length());

            }
        }
    }

}