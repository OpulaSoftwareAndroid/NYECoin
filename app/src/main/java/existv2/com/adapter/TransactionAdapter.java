package existv2.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import existv2.com.R;
import existv2.com.fragment.Transaction_history;
import existv2.com.model.TransactionInfoa.Info;

public class TransactionAdapter extends ArrayAdapter<Info> {

    private List<Info> origPlanetList;
    private List<Info> oricoininfo;
    private Context context;
    private Filter planetFilter;
    private TransactionAdapter adapter;
    private LayoutInflater mInflater;
    private String TAG="TransactionAdapter";
    // Constructors
    public TransactionAdapter(Context context, ArrayList<Info> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.adapter = this;
        this.origPlanetList = objects;
        this.oricoininfo = objects;
    }

    public int getCount() {
        return oricoininfo.size();
    }

    @Override
    public Info getItem(int position) {
        return oricoininfo.get(position);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.transaction_list_item, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Info item = getItem(position);

        assert item != null;
        Log.d(TAG,"jigar the item transaction history have is "+item.getTransactionType());
        String transaction_type = item.getTransactionType();
        String status = item.getTransactionStatus();
        String date = item.getTransactionDate();
        String fees = item.getFee();
        String amount = item.getAmount();

        vh.txt_tra_date.setText(date);
        vh.txt_tra_status.setText(Html.fromHtml("<b> Status: </b> " +status));
        Log.d(TAG,"jigar the transaction history have is "+transaction_type);
        vh.textViewAddressTo.setText(Html.fromHtml("<b> To: </b> "+item.getToAddress()));
        if (transaction_type.equalsIgnoreCase("Deposit")){
            vh.txt_tra_amount.setText(Html.fromHtml(" <b> Amount: </b>"+amount));
            vh.textViewAddressTo.setText(Html.fromHtml("<b>Receive From: </b> "+item.getToAddress()));

            vh.txt_tra_department.setText(transaction_type);
            vh.txt_tra_department.setTextColor(Color.parseColor("#FF28A645"));
            vh.txt_tra_fee.setVisibility(View.GONE);
        } else if (transaction_type.equalsIgnoreCase("Withdraw")){
            vh.txt_tra_amount.setText(Html.fromHtml("<b> Amount: </b> "+amount));
            vh.txt_tra_department.setText(transaction_type);
            vh.textViewAddressTo.setText(Html.fromHtml("<b>Send To: </b> "+item.getToAddress()));
            vh.txt_tra_department.setTextColor(Color.parseColor("#FFDB3545"));
            vh.txt_tra_fee.setText("Fee:"+fees);
            vh.txt_tra_fee.setTextColor(Color.parseColor("#FFDB3545"));
            vh.txt_tra_fee.setVisibility(View.VISIBLE);
        }

        return vh.rootView;
    }

    public void resetData() {
        oricoininfo = origPlanetList;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_tra_date;
        public final TextView txt_tra_amount;
        public final TextView textViewAddressTo;
        public final TextView txt_tra_status;
        public final TextView txt_tra_department;
        public final TextView txt_tra_fee;

        private ViewHolder(LinearLayout rootView, TextView txt_tra_date, TextView txt_tra_amount, TextView txt_tra_status, TextView txt_tra_department,TextView txt_tra_fee,TextView textViewAddressTo) {
            this.rootView = rootView;
            this.txt_tra_date = txt_tra_date;
            this.txt_tra_amount = txt_tra_amount;
            this.txt_tra_status = txt_tra_status;
            this.txt_tra_department = txt_tra_department;
            this.txt_tra_fee = txt_tra_fee;
            this.textViewAddressTo=textViewAddressTo;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_tra_date = (TextView) rootView.findViewById(R.id.txt_tra_date);
            TextView txt_tra_amount = (TextView) rootView.findViewById(R.id.txt_tra_amount);
            TextView txt_tra_status = (TextView) rootView.findViewById(R.id.txt_tra_status);
            TextView txt_tra_department = (TextView) rootView.findViewById(R.id.txt_tra_department);
            TextView txt_tra_fee = (TextView) rootView.findViewById(R.id.txt_tra_fee);
            TextView textViewAddressTo = (TextView) rootView.findViewById(R.id.textViewAddressTo);

            return new ViewHolder(rootView, txt_tra_date, txt_tra_amount, txt_tra_status, txt_tra_department,txt_tra_fee,textViewAddressTo);
        }
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null)
            planetFilter = new PlanetFilter();

        return planetFilter;
    }

    private class PlanetFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origPlanetList;
                results.count = origPlanetList.size();
            }
            else {
                // We perform filtering operation
                List<Info> nPlanetList = new ArrayList<Info>();

                for (Info p : oricoininfo) {
                    if (p.getType().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0) {
                notifyDataSetInvalidated();
                Transaction_history.checkSearchRecord();
                Log.d("datachanged","null");
            }
            else {
                oricoininfo = (List<Info>) results.values;
                notifyDataSetChanged();
                Transaction_history.checkSearchRecord1();
                Log.d("datachanged","found");
            }

        }

    }
}