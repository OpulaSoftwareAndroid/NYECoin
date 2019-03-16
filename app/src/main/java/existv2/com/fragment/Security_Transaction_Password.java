package existv2.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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


public class Security_Transaction_Password extends Fragment {

    com.rengwuxian.materialedittext.MaterialEditText edi_current,edi_new,edi_confirm;
    Button btn_edit_password_save;
    SharedPreference sharedPreference;

    public Security_Transaction_Password() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_transaction_password, container, false);

        MainActivity.setToolbarname("Transaction Password");

        sharedPreference = new SharedPreference();

        edi_current = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_current);
        edi_new = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_new);
        edi_confirm = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_confirm);
        btn_edit_password_save = (Button) rootview.findViewById(R.id.btn_edit_password_save);
        if(sharedPreference.getValue(getContext(),WsConstant.TAG_TRANSACTION_PASSWORD)!=null) {
            edi_current.setVisibility(View.VISIBLE);
        }else
        {
            edi_current.setVisibility(View.GONE);

        }


        btn_edit_password_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edi_current.getVisibility()==View.VISIBLE) {
                    if (edi_current.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Please enter current password", Toast.LENGTH_SHORT).show();
                    }
                }

                if (edi_new.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter new password", Toast.LENGTH_SHORT).show();
                }  else if (edi_confirm.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
                } else if (!(edi_new.getText().toString().equalsIgnoreCase(edi_confirm.getText().toString()))){
                    Toast.makeText(getActivity(), "Password is not matching", Toast.LENGTH_SHORT).show();
                } else {
                    String newp = edi_new.getText().toString();
                    String currentp = edi_current.getText().toString();
                    String regid = sharedPreference.getValue(getActivity(),WsConstant.register_id);
                    String token = sharedPreference.getValue(getActivity(),WsConstant.valid_data);
                    edit_password(regid,currentp,newp,token);
                }
            }
        });
        sharedPreference.save(getContext(),"12345678",WsConstant.TAG_TRANSACTION_PASSWORD);
        return rootview;
    }

    private void edit_password(final String regid,final String currentp,final String newp,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("OldPassword", currentp);
            optioMap.put("NewPassword", newp);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "2");

            new RestClient(getActivity()).getInstance().get().edit_transaction_password(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            sharedPreference.save(getContext(),newp,WsConstant.TAG_TRANSACTION_PASSWORD);
                            edi_confirm.setText("");
                            edi_new.setText("");
                            edi_current.setText("");

                        } else {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
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


}