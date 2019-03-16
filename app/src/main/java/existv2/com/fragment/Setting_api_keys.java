package existv2.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import existv2.com.R;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;


public class Setting_api_keys extends Fragment {

    com.rengwuxian.materialedittext.MaterialEditText edit_marchant,edit_public,edit_private;
    SharedPreference sharedPreference;

    public Setting_api_keys() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_setting_api_keys, container, false);

        sharedPreference = new SharedPreference();
        edit_marchant = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_marchant);
        edit_public = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_public);
        edit_private = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edit_private);

        String marcahant = sharedPreference.getValue(getActivity(), WsConstant.merchant_key);
        String publica = sharedPreference.getValue(getActivity(), WsConstant.public_key);
        String privatea = sharedPreference.getValue(getActivity(), WsConstant.private_key);

        edit_marchant.setText(marcahant);
        edit_public.setText(publica);
        edit_private.setText(privatea);

        return rootview;
    }


}