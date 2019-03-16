package existv2.com.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.adapter.TransactionAdapter;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.TransactionInfoa.Info;
import existv2.com.model.TransactionInfoa.TransactionInfo;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Transaction_history extends Fragment implements SearchView.OnQueryTextListener {

    public static ListView list_transaction;
    private ArrayList<Info> contactList;
    public static TransactionAdapter adapter;
    public static TextView txt_norecord;
    private SharedPreference sharedPreference;
    SwipeRefreshLayout swipeToRefresh_transaction;
    AlertDialog alertDialog;
    Animation startAnimation;
    String TAG="Transaction_history";

    public Transaction_history() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_transaction_history, container, false);

        MainActivity.setToolbarname("Transaction History");

        sharedPreference = new SharedPreference();
        list_transaction = (ListView) rootView.findViewById(R.id.list_transaction);
        txt_norecord = (TextView) rootView.findViewById(R.id.txt_norecord);
        swipeToRefresh_transaction = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh_transaction);


        String regit = sharedPreference.getValue(getActivity(), WsConstant.register_id);
        String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
        transacton(regit, token);

        contactList = new ArrayList<>();

        list_transaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(getActivity(), i);
            }
        });

        swipeToRefresh_transaction.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String regit = sharedPreference.getValue(getActivity(), WsConstant.register_id);
                String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
                transacton(regit, token);
                swipeToRefresh_transaction.setRefreshing(false);
            }
        });

        return rootView;
    }

    public void showDialog(Context mContext, Integer i) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = (getActivity()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog_transaction_details, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        startAnimation = AnimationUtils.loadAnimation(mContext, R.anim.blinking_animation);

        alertDialog = alertDialogBuilder.create();

        ImageView close = (ImageView) dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        TextView txt_de_date = (TextView) dialogView.findViewById(R.id.txt_de_date);
        TextView txt_de_time = (TextView) dialogView.findViewById(R.id.txt_de_time);
        TextView txt_de_token = (TextView) dialogView.findViewById(R.id.txt_de_token);
        TextView txt_de_orignal_amount = (TextView) dialogView.findViewById(R.id.txt_de_orignal_amount);
        TextView txt_de_fees = (TextView) dialogView.findViewById(R.id.txt_de_fees);
        TextView txt_de_se = (TextView) dialogView.findViewById(R.id.txt_de_se);
        TextView txt_de_se_email = (TextView) dialogView.findViewById(R.id.txt_de_se_email);
        TextView txt_de_se_email_address = (TextView) dialogView.findViewById(R.id.txt_de_se_email_address);
        TextView txt_de_tx = (TextView) dialogView.findViewById(R.id.txt_de_tx);
        TextView txt_de_txid = (TextView) dialogView.findViewById(R.id.txt_de_txid);
        txt_de_txid.setVisibility(View.GONE);
        TextView txt_de_alg_amount = (TextView) dialogView.findViewById(R.id.txt_de_alg_amount);
        TextView txt_de_send_receive = (TextView) dialogView.findViewById(R.id.txt_de_send_receive);
        TextView txt_de_send_receive_id = (TextView) dialogView.findViewById(R.id.txt_de_send_receive_id);
        TextView txt_de_transaction_hash = (TextView) dialogView.findViewById(R.id.txt_de_transaction_hash);
        TextView txt_de_transaction_hash_id = (TextView) dialogView.findViewById(R.id.txt_de_transaction_hash_id);
        TextView txt_de_time_completed = (TextView) dialogView.findViewById(R.id.txt_de_time_completed);
        TextView txt_de_time_completed_time = (TextView) dialogView.findViewById(R.id.txt_de_time_completed_time);
        TextView txt_de_status = (TextView) dialogView.findViewById(R.id.txt_de_status);

        txt_de_orignal_amount.setPaintFlags(txt_de_orignal_amount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_fees.setPaintFlags(txt_de_fees.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_se.setPaintFlags(txt_de_se.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_se_email.setPaintFlags(txt_de_se_email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_tx.setPaintFlags(txt_de_tx.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_alg_amount.setPaintFlags(txt_de_alg_amount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_send_receive.setPaintFlags(txt_de_send_receive.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_transaction_hash.setPaintFlags(txt_de_transaction_hash.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_de_time_completed.setPaintFlags(txt_de_time_completed.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        String dta = adapter.getItem(i).getTransactionDate();
        Date datee = null;
        try {
            datee = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dta);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String ext_time = new SimpleDateFormat("H:mm:ss").format(datee);
        String ext_date = new SimpleDateFormat("dd-MM-yyyy").format(datee);
        String token = adapter.getItem(i).getTransactionType();
        String alg_amount = adapter.getItem(i).getAmount();
        String amount = adapter.getItem(i).getAmount();
        String str = amount;
        str = str.replaceAll("[^\\d.]", "");
        double intFinalAmount=Double.parseDouble(str);
        double buy_price =intFinalAmount;
        double total = intFinalAmount * buy_price;

        //decimal format
        DecimalFormat df = new DecimalFormat("#.##########");
        String usd_amount = df.format(total);

        String rece = adapter.getItem(i).getToAddress();
//        String txid = adapter.getItem(i).getTransaction;
        String fess = adapter.getItem(i).getFee();


        String mastatus = adapter.getItem(i).getTransactionStatus();

        String txhash = null;
        try {
            txhash = adapter.getItem(i).getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (txhash == null) {
            txhash = "-----";
        }

        String trimecompleted = adapter.getItem(i).getTransactionDate();
        String status = adapter.getItem(i).getTransactionStatus();
        txt_de_send_receive_id.setText(adapter.getItem(i).getToAddress());
//
//        if (rece.contains("Receive")) {
//            String newRe = deleteAll(rece, "Receive To : ");
//            txt_de_send_receive_id.setText(adapter.getItem(i).getToAddress());
//            txt_de_send_receive.setText("Receive To");
//        } else if (rece.contains("Send")) {
//            String newRe = deleteAll(rece, "Send To : ");
//            txt_de_send_receive_id.setText(adapter.getItem(i).getToAddress());
//            txt_de_send_receive.setText("Send To");
//        }

        if (mastatus.equalsIgnoreCase("0")) {
            String de_se = "Seller : " + adapter.getItem(i).getToAddress();
            String de_se_email = "Seller Email";
            String de_se_address = adapter.getItem(i).getToAddress();
            txt_de_se.setText(de_se);
            txt_de_se_email.setText(de_se_email);
            txt_de_se_email_address.setText(de_se_address);
        } else if (mastatus.equalsIgnoreCase("1")) {
            String de_se = "Sender : " + adapter.getItem(i).getToAddress();
            String de_se_email = "Sender Email";
            String de_se_address = adapter.getItem(i).getToAddress();
            txt_de_se.setText(de_se);
            txt_de_se_email.setText(de_se_email);
            txt_de_se_email_address.setText(de_se_address);
        }

        txt_de_status.startAnimation(startAnimation);

       // txt_de_txid.setText(txid);
        txt_de_date.setText(ext_date);
        txt_de_time.setText(ext_time);
        txt_de_token.setText(token);
        txt_de_orignal_amount.setText("Original Amount : " + usd_amount + " USD");
        Spanned Hashlink = Html.fromHtml("<a href=\""+txhash+"\">"+txhash+"</a>");
        txt_de_transaction_hash_id.setText(Hashlink);
        txt_de_transaction_hash_id.setMovementMethod(LinkMovementMethod.getInstance());

        txt_de_time_completed_time.setText(trimecompleted);
        txt_de_status.setText("Status : " + status);
 //       txt_de_fees.setText("Fees : " + fess + "%");
        txt_de_fees.setText("Fees : " + fess);
//        txt_de_alg_amount.setText("Amount : " + alg_amount +" " + R.string.coin_code);
        txt_de_alg_amount.setText("Amount : " + alg_amount );


        alertDialog.show();
    }

    private static String deleteAll(String strValue, String charToRemove) {
        return strValue.replaceAll(charToRemove, "");

    }


    private void transacton(final String regid, final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");

            new RestClient(getActivity()).getInstance().get().transactionhistory(optioMap).enqueue(new Callback<TransactionInfo>() {
                @Override
                public void onResponse(Call<TransactionInfo> call, Response<TransactionInfo> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Log.d(TAG,"jigar the transaction we getting is => "+new Gson().toJson(response));
//                            for (int i=0;i<response.body().getInfo().size();i++) {
//                                Log.d(TAG,"jigar the transaction we getting is "+response.body().getInfo().get(i).getTransactionType());
//                            }
                            contactList = (ArrayList<Info>) response.body().getInfo();

                            adapter = new TransactionAdapter(getActivity(), contactList);

                            list_transaction.setAdapter(adapter);

                        } else {
                            list_transaction.setVisibility(View.GONE);
                            txt_norecord.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TransactionInfo> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Transaction");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equalsIgnoreCase("")) {
            try {
                adapter.getFilter().filter(newText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                adapter.resetData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void checkSearchRecord() {
        txt_norecord.setVisibility(View.VISIBLE);
        list_transaction.setVisibility(View.GONE);

    }

    public static void checkSearchRecord1() {
        txt_norecord.setVisibility(View.GONE);
        list_transaction.setVisibility(View.VISIBLE);
    }

}