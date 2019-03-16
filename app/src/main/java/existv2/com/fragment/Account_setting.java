package existv2.com.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.R;


public class Account_setting extends Fragment {

    LinearLayout lin_main1,lin_main2,lin_main3;
//    TextView lin_main1,lin_main2,lin_main3;

    View view_main1,view_main2,view_main3;
    TextView lin_txt1,lin_txt2,lin_txt3;

    public Account_setting() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_account_setting, container, false);

        MainActivity.setToolbarname("Account Setting");

        lin_main1 =  rootview.findViewById(R.id.lin_main1);
        lin_main2 = rootview.findViewById(R.id.lin_main2);
        lin_main3 =  rootview.findViewById(R.id.lin_main3);

        view_main1 = (View) rootview.findViewById(R.id.view_lin1);
        view_main2 = (View) rootview.findViewById(R.id.view_lin2);
        view_main3 = (View) rootview.findViewById(R.id.view_lin3);

        lin_txt1 = (TextView) rootview.findViewById(R.id.lin_txt1);
        lin_txt2 = (TextView) rootview.findViewById(R.id.lin_txt2);
        lin_txt3 = (TextView) rootview.findViewById(R.id.lin_txt3);

        view_main1.setVisibility(View.VISIBLE);
        view_main2.setVisibility(View.INVISIBLE);
        view_main3.setVisibility(View.INVISIBLE);
        lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
        lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));

        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame_setting, new Setting_personal()).commit();

        lin_main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_main1.setVisibility(View.VISIBLE);
                view_main2.setVisibility(View.INVISIBLE);
                view_main3.setVisibility(View.INVISIBLE);
                lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
//                lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.white));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame_setting, new Setting_personal()).commit();
            }
        });

        lin_main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_main1.setVisibility(View.INVISIBLE);
                view_main2.setVisibility(View.VISIBLE);
                view_main3.setVisibility(View.INVISIBLE);
                lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
                lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame_setting, new Setting_contact()).commit();
            }
        });

        lin_main3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_main1.setVisibility(View.INVISIBLE);
                view_main2.setVisibility(View.INVISIBLE);
                view_main3.setVisibility(View.VISIBLE);
                lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.white));
                lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.white));
                lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame_setting, new Setting_api_keys()).commit();
            }
        });

        return rootview;
    }


}