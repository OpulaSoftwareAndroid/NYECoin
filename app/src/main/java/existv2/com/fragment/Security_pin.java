package existv2.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;


public class Security_pin extends Fragment {

    com.rengwuxian.materialedittext.MaterialEditText edi_current_pin,edi_new_pin,edi_confirm_pin;
    Button btn_edit_pin_save;
    SharedPreference sharedPreference;
    String TAG="Security_pin";
    public Security_pin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_security_pin, container, false);

        MainActivity.setToolbarname("Login Pin");

        sharedPreference = new SharedPreference();

        edi_current_pin = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_current_pin);
        edi_new_pin = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_new_pin);
        edi_confirm_pin = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_confirm_pin);
        btn_edit_pin_save = (Button) rootview.findViewById(R.id.btn_edit_pin_save);
        if(AppGlobal.getStringPreference(getContext(),WsConstant.SP_LOGIN_SET_PIN)==null)
        {
            edi_current_pin.setVisibility(View.GONE);

        }else
        {
            if(!AppGlobal.getStringPreference(getContext(),WsConstant.SP_LOGIN_SET_PIN).equals("")) {
                edi_current_pin.setVisibility(View.VISIBLE);
            }else
            {
                edi_current_pin.setVisibility(View.GONE);

            }
        }
            btn_edit_pin_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edi_current_pin.getVisibility()==View.VISIBLE) {
                    if (edi_current_pin.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Please enter current pin", Toast.LENGTH_SHORT).show();
                    }
                }
                 if (edi_new_pin.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter new pin", Toast.LENGTH_SHORT).show();
                } else if (edi_confirm_pin.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter confirm pin", Toast.LENGTH_SHORT).show();
                } else if (!(edi_new_pin.getText().toString().equalsIgnoreCase(edi_confirm_pin.getText().toString()))){
                    Toast.makeText(getActivity(), "pin is not matching", Toast.LENGTH_SHORT).show();
                } else {
                    String currentp = edi_current_pin.getText().toString();
                    String newp = edi_new_pin.getText().toString();
                    String oldpin = AppGlobal.getStringPreference(Objects.requireNonNull(getActivity()), WsConstant.SP_LOGIN_SET_PIN);
                     Log.d(TAG,"jigar the old pin was "+oldpin);
                    if (oldpin.equalsIgnoreCase(currentp)){
                        edi_new_pin.setText("");
                        edi_current_pin.setText("");
                        edi_confirm_pin.setText("");
                        Toast.makeText(getActivity(), "Pin Changed !!", Toast.LENGTH_SHORT).show();
                        AppGlobal.setStringPreference(getActivity(),newp, WsConstant.SP_LOGIN_SET_PIN);
                        sharedPreference.save(getContext(), "true", WsConstant.TAG_STORED_PIN_SWITCH);
                        edi_current_pin.setVisibility(View.VISIBLE);


                    } else {
                        Toast.makeText(getActivity(), "Wrong pin!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return rootview;
    }
}