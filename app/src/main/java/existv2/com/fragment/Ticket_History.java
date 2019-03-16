package existv2.com.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.adapter.TicketAdapter;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.TicketInfoa.Info;
import existv2.com.model.TicketInfoa.TicketInfo;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ticket_History extends Fragment implements SearchView.OnQueryTextListener {

    public static ListView list_ticket;
    private ArrayList<Info> contactList;
    private TicketAdapter adapter;
    public static TextView txt_norecord;
    FloatingActionButton floatingActionButton;
    private SharedPreference sharedPreference;
    SwipeRefreshLayout swipeToRefresh_ticket;

    public Ticket_History() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ticket_history, container, false);

        MainActivity.setToolbarname("Ticket History");

        sharedPreference = new SharedPreference();
        list_ticket = (ListView) rootView.findViewById(R.id.list_ticket);
        txt_norecord = (TextView) rootView.findViewById(R.id.txt_norecord);
        swipeToRefresh_ticket = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh_ticket);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);

        String regit = sharedPreference.getValue(getActivity(), WsConstant.register_id);
        String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
        ticket(regit,token);

        contactList = new ArrayList<>();

        list_ticket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //showDialog(getActivity(),i);
            }
        });

        swipeToRefresh_ticket.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String regit = sharedPreference.getValue(getActivity(), WsConstant.register_id);
                String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
                ticket(regit,token);
                swipeToRefresh_ticket.setRefreshing(false);
            }
        });

        list_ticket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ticketid = adapter.getItem(i).getTicketNumber();
                sharedPreference.save(getActivity(),ticketid,WsConstant.ticket_number);
                String oc = adapter.getItem(i).getTicketStatus();
                sharedPreference.save(getActivity(),oc, WsConstant.oc);
                //to ticket details
                MainActivity.setToolbarname("Ticket Details");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Ticket_details()).addToBackStack(null).commit();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.setToolbarname("Create Ticket");
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new Create_Ticket()).addToBackStack(null).commit();
            }
        });


        return rootView;
    }


    private void ticket(final String regid,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("RegisterId", regid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");

            new RestClient(getActivity()).getInstance().get().tickethistory(optioMap).enqueue(new Callback<TicketInfo>() {
                @Override
                public void onResponse(Call<TicketInfo> call, Response<TicketInfo> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {

                            contactList = (ArrayList<Info>) response.body().getInfo();

                            adapter = new TicketAdapter(getActivity(), contactList);

                            list_ticket.setAdapter(adapter);

                        } else {
                            list_ticket.setVisibility(View.GONE);
                            txt_norecord.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TicketInfo> call, Throwable t) {
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
        searchView.setQueryHint("Search Ticket");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
   }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (!newText.equalsIgnoreCase("")){
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void checkSearchRecord() {
        txt_norecord.setVisibility(View.VISIBLE);
        list_ticket.setVisibility(View.GONE);

    }

    public static void checkSearchRecord1() {
        txt_norecord.setVisibility(View.GONE);
        list_ticket.setVisibility(View.VISIBLE);
    }

}