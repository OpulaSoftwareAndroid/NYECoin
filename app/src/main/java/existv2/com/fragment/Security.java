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


public class Security extends Fragment {

    LinearLayout lin_main1,lin_main2,lin_main3;
    View view_main1,view_main2,view_main3;
    TextView lin_txt1,lin_txt2,lin_txt3;

    public Security() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_security, container, false);

        MainActivity.setToolbarname("Security");

        lin_main1 = (LinearLayout) rootview.findViewById(R.id.lin_main1);
        lin_main2 = (LinearLayout) rootview.findViewById(R.id.lin_main2);
        lin_main3 = (LinearLayout) rootview.findViewById(R.id.lin_main3);

        view_main1 = (View) rootview.findViewById(R.id.view_lin11);
        view_main2 = (View) rootview.findViewById(R.id.view_lin21);
        view_main2 = (View) rootview.findViewById(R.id.view_lin21);

        lin_txt1 = (TextView) rootview.findViewById(R.id.lin_txt1);
        lin_txt2 = (TextView) rootview.findViewById(R.id.lin_txt2);
        lin_txt3 = (TextView) rootview.findViewById(R.id.lin_txt3);

        view_main1.setVisibility(View.VISIBLE);
        view_main2.setVisibility(View.INVISIBLE);
        lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
        lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));

        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame_security, new Security_pin()).commit();

        lin_main1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_main1.setVisibility(View.VISIBLE);
                view_main2.setVisibility(View.INVISIBLE);
                lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
                lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame_security, new Security_pin()).commit();

            }
        });

        lin_main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_main1.setVisibility(View.INVISIBLE);
                view_main2.setVisibility(View.VISIBLE);
                view_main2.setVisibility(View.VISIBLE);

                lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
                lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame_security, new Security_password()).commit();

            }
        });

        lin_main3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_main1.setVisibility(View.INVISIBLE);
                view_main2.setVisibility(View.VISIBLE);
                lin_txt1.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
                lin_txt2.setTextColor(getActivity().getResources().getColor(R.color.colorGrey));
                lin_txt3.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame_security, new Security_Transaction_Password()).commit();
            }
        });

        return rootview;
    }


}