package existv2.com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import existv2.com.fragment.Ticket_History;
import existv2.com.model.TicketInfoa.Info;


public class TicketAdapter extends ArrayAdapter<Info> {

    private List<Info> origPlanetList;
    private List<Info> oricoininfo;
    private Context context;
    private Filter planetFilter;
    private TicketAdapter adapter;
    private LayoutInflater mInflater;

    // Constructors
    public TicketAdapter(Context context, ArrayList<Info> objects) {
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
            View view = mInflater.inflate(R.layout.ticket_list_item, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Info item = getItem(position);

        assert item != null;
        String subject = item.getSubject();
        String department = item.getDepartment();
        String status = item.getStatus();
        String date = item.getLastUpdate();

        vh.txt_ti_subject.setText(subject);
        vh.txt_ti_department.setText(department);
        vh.txt_ti_status.setText(status);
        vh.txt_ti_date.setText(date);

        String oc = item.getTicketStatus();

        if (oc.equalsIgnoreCase("Open Ticket")){
            vh.txt_closed.setVisibility(View.GONE);
        } else if (oc.equalsIgnoreCase("Close Ticket")){
            vh.txt_closed.setVisibility(View.VISIBLE);
        }

        return vh.rootView;
    }

    public void resetData() {
        oricoininfo = origPlanetList;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_ti_subject;
        public final TextView txt_ti_department;
        public final TextView txt_ti_status;
        public final TextView txt_ti_date;
        public final TextView txt_closed;

        private ViewHolder(LinearLayout rootView, TextView txt_ti_subject, TextView txt_ti_department, TextView txt_ti_status, TextView txt_ti_date, TextView txt_closed) {
            this.rootView = rootView;
            this.txt_ti_subject = txt_ti_subject;
            this.txt_ti_department = txt_ti_department;
            this.txt_ti_status = txt_ti_status;
            this.txt_ti_date = txt_ti_date;
            this.txt_closed = txt_closed;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_ti_subject = (TextView) rootView.findViewById(R.id.txt_ti_subject);
            TextView txt_ti_department = (TextView) rootView.findViewById(R.id.txt_ti_department);
            TextView txt_ti_status = (TextView) rootView.findViewById(R.id.txt_ti_status);
            TextView txt_ti_date = (TextView) rootView.findViewById(R.id.txt_ti_date);
            TextView txt_closed = (TextView) rootView.findViewById(R.id.txt_closed);
            return new ViewHolder(rootView, txt_ti_subject, txt_ti_department, txt_ti_status, txt_ti_date, txt_closed);
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
                    if (p.getSubject().toUpperCase().startsWith(constraint.toString().toUpperCase()))
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
                Ticket_History.checkSearchRecord();
            } else {
                oricoininfo = (List<Info>) results.values;
                notifyDataSetChanged();
                Ticket_History.checkSearchRecord1();
            }

        }

    }
}