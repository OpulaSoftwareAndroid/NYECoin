package existv2.com.fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CityLista.CityList;
import existv2.com.model.CityLista.Info;
import existv2.com.model.CommanResponse;
import existv2.com.model.StateLista.StateList;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Setting_contact extends Fragment {

    private ArrayList<existv2.com.model.StateLista.Info> stateList;
    private ArrayList<existv2.com.model.CityLista.Info> cityList;
    com.rengwuxian.materialedittext.MaterialEditText edi_address,edi_country,edi_pincode,editTextState,editTextCity;
    Button btn_edit_contact_save;
    SharedPreference sharedPreference;
    Spinner spinner_state,spinner_city;
    String state_name,city_name,state_iddd,city_iddd;
    ArrayAdapter<String> adapter_city;
    ArrayAdapter<String> adapter_state;
    int a = 0;
    String TAG="Setting_contact";

    public Setting_contact() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_setting_contact, container, false);

        sharedPreference = new SharedPreference();
        spinner_state = (Spinner) rootview.findViewById(R.id.spinner_state);
        spinner_city = (Spinner) rootview.findViewById(R.id.spinner_city);
        edi_address = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_address);
        edi_country = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_country);
        edi_pincode = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_pincode);
        btn_edit_contact_save = (Button) rootview.findViewById(R.id.btn_edit_contact_save);
        editTextCity=rootview.findViewById(R.id.editTextCity);
        editTextState=rootview.findViewById(R.id.editTextState);

        final String address = sharedPreference.getValue(getActivity(), WsConstant.resident_address);
        String country = sharedPreference.getValue(getActivity(), WsConstant.country_name);
        final String pincode = sharedPreference.getValue(getActivity(), WsConstant.pincode);

        edi_address.setText(address);
        edi_country.setText(country);
        edi_pincode.setText(pincode);

        btn_edit_contact_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edi_address.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter Address", Toast.LENGTH_LONG).show();
                } else if (edi_pincode.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select Pincode", Toast.LENGTH_LONG).show();
                } else if (editTextState.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select State", Toast.LENGTH_LONG).show();
                } else if (editTextCity.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please select City", Toast.LENGTH_LONG).show();
                }
                else if (spinner_state.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(), "Please select State", Toast.LENGTH_LONG).show();
                } else if (spinner_city.getSelectedItemPosition()==0){
                    Toast.makeText(getActivity(), "Please select City", Toast.LENGTH_LONG).show();
                }
                else {
                    String add = edi_address.getText().toString();
                    String pin = edi_pincode.getText().toString();
                    String regid = sharedPreference.getValue(getActivity(),WsConstant.register_id);
                    String token = sharedPreference.getValue(getActivity(),WsConstant.valid_data);
                    String strState=editTextState.getText().toString();
                    String strCity=editTextCity.getText().toString();
                    edit_contact(regid,add,pin,token,strCity,strState);
                }
            }
        });

        stateList = new ArrayList<>();
        cityList = new ArrayList<>();

        String country_id = sharedPreference.getValue(getActivity(),WsConstant.country_id);
        String state_id = sharedPreference.getValue(getActivity(),WsConstant.state_id);
        String city_id = sharedPreference.getValue(getActivity(),WsConstant.city_id);

        state_name = sharedPreference.getValue(getActivity(),WsConstant.state_name);
        city_name = sharedPreference.getValue(getActivity(),WsConstant.city_name);

        editTextCity.setText(state_name);
        editTextState.setText(city_name);

        //        if (state_id.equalsIgnoreCase("0") || city_id.equalsIgnoreCase("0")){
//            getState(country_id);
//            a=0;
//        } else {
//            String s = sharedPreference.getValue(getActivity(),WsConstant.state_name);
//            //getAlreadyState(country_id,s);
//            a=1;
//        }

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int i = spinner_state.getSelectedItemPosition();
                if (i>0){
                    int j = i-1;
                    //state_name = stateList.get(j).getName();
                    state_iddd = stateList.get(j).getId();
                    if (a==0){
                        getCity(state_iddd);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }

        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int i = spinner_city.getSelectedItemPosition();
                if (i>0){
                    int j = i-1;
                    city_iddd = cityList.get(j).getId();
                    //city_name = cityList.get(j).getName();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });

        return rootview;
    }

    private void getAlreadyState(final String id,final String s) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("country_id", id);
            new RestClient(getActivity()).getInstance().get().getState(optioMap).enqueue(new Callback<StateList>() {
                @Override
                public void onResponse(Call<StateList> call, Response<StateList> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            stateList = (ArrayList<existv2.com.model.StateLista.Info>) response.body().getInfo();
                            String[] items = new String[stateList.size()+1];
                            items[0] = "Choose State";
                            for (int i = 0; i < stateList.size(); i++) {
                                items[i+1] = stateList.get(i).getName();
                            }
                            adapter_state = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, items) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_state.setAdapter(adapter_state);
                            spinner_state.setSelection(adapter_state.getPosition(s));
                            String state_id = sharedPreference.getValue(getActivity(),WsConstant.state_id);
                            String c = sharedPreference.getValue(getActivity(),WsConstant.city_name);
                            getAlreadyCity(state_id,c);
                        } else {
                            Toast.makeText(getActivity(), "Unable to load", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StateList> call, Throwable t) {
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
//
    private void getAlreadyCity(final String id,final String c) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("state_id", id);
            new RestClient(getActivity()).getInstance().get().getCity(optioMap).enqueue(new Callback<CityList>() {
                @Override
                public void onResponse(Call<CityList> call, Response<CityList> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            cityList = (ArrayList<Info>) response.body().getInfo();
                            String[] items = new String[cityList.size()+1];
                            items[0] = "Choose City";
                            for (int i = 0; i < cityList.size(); i++) {
                                items[i+1] = cityList.get(i).getName();
                            }
                            adapter_city = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, items) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_city.setAdapter(adapter_city);
                            spinner_city.setSelection(adapter_city.getPosition(c));
                            a=0;
                        } else {
                            String[] items = new String[1];
                            items[0] = "No Record Found.";
                            ArrayAdapter<String> adapter;
                            adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, items) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_city.setAdapter(adapter);

                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CityList> call, Throwable t) {
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

    private void getCity(final String id) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("state_id", id);
            new RestClient(getActivity()).getInstance().get().getCity(optioMap).enqueue(new Callback<CityList>() {
                @Override
                public void onResponse(Call<CityList> call, Response<CityList> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            cityList = (ArrayList<Info>) response.body().getInfo();
                            String[] items = new String[cityList.size()+1];
                            items[0] = "Choose City";
                            for (int i = 0; i < cityList.size(); i++) {
                                items[i+1] = cityList.get(i).getName();
                            }
                            adapter_city = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, items) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_city.setAdapter(adapter_city);
                        } else {
                            String[] items = new String[1];
                            items[0] = "No Record Found.";
                            ArrayAdapter<String> adapter;
                            adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, items) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_city.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CityList> call, Throwable t) {
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

    private void getState(final String id) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("country_id", id);
            new RestClient(getActivity()).getInstance().get().getState(optioMap).enqueue(new Callback<StateList>() {
                @Override
                public void onResponse(Call<StateList> call, Response<StateList> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            stateList = (ArrayList<existv2.com.model.StateLista.Info>) response.body().getInfo();
                            String[] items = new String[stateList.size()+1];
                            items[0] = "Choose State";
                            for (int i = 0; i < stateList.size(); i++) {
                                items[i+1] = stateList.get(i).getName();
                            }
                            adapter_state = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, items) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_state.setAdapter(adapter_state);

                            getCity(state_iddd);
                            ///////////////////////////////////////////////////////

                            String[] itemss = new String[1];
                            itemss[0] = "Select State First!";
                            ArrayAdapter<String> adapterr;
                            adapterr = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_real_black, itemss) {
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
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
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
                            spinner_city.setAdapter(adapterr);

                        } else {
                            Toast.makeText(getActivity(), "Unable to load", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<StateList> call, Throwable t) {
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

    private void edit_contact(final String regid, final String address, final String pincode, final String token
            , final String strState, final String strCity) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("Address", address);
            optioMap.put("State", strState);
            optioMap.put("City", strCity);
            optioMap.put("PinCode", pincode);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");
//            Perameter
//            {
//                "RegisterId" : "55859374",
//                    "Address" : "Surat Gujarat India",
//                    "State" : "Gujarat",
//                    "City" : "Surat",
//                    "PinCode" : "395006",
//                    "ValidData" : "AjsEFhsjcnshsuj@kjmjkmrtghy8rr",
//                    "Type" : "1"
//            }

            new RestClient(getActivity()).getInstance().get().edit_contact(optioMap).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    Log.d(TAG,"jigar the set contact info we getting is "+new Gson().toJson(response));
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            sharedPreference.save(getActivity(),state_iddd,WsConstant.state_id);
                            sharedPreference.save(getActivity(),strState,WsConstant.state_name);
                            sharedPreference.save(getActivity(),address,WsConstant.resident_address);
                            sharedPreference.save(getActivity(),strCity,WsConstant.city_name);
                            sharedPreference.save(getActivity(),city_iddd,WsConstant.city_id);
                            sharedPreference.save(getActivity(),pincode,WsConstant.pincode);
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