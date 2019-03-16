package existv2.com.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.AllTicketInfoa.Info;


public class AllTicketAdapter extends ArrayAdapter<Info> {

    List<Info> transactionInfo;
    private Context context;
    private LayoutInflater mInflater;
    private SharedPreference sharedPreference;
    String TAG="AllTicketAdapter";
    // Constructors
    public AllTicketAdapter(Context context, ArrayList<Info> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        transactionInfo = objects;
    }

    @Override
    public Info getItem(int position) {
        return transactionInfo.get(position);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        String sub = null;
        sharedPreference = new SharedPreference();
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.allticket_list_item, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Info item = getItem(position);

        String date = item.getDate();

        Date datee = null;
        try {
            datee = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String ext_time = new SimpleDateFormat("H:mm:ss").format(datee);
        String ext_date = new SimpleDateFormat("dd-MM-yyyy").format(datee);
        final String attech = item.getTicket();
        String tvstatusnumber = (item.getReplyName());



        if (tvstatusnumber.equalsIgnoreCase("")){
            String usrename = sharedPreference.getValue(context,WsConstant.name);
            sub = usrename + "(" + item.getPerson() + ")";
        } else {
            String usrename = item.getReplyName();
            sub = item.getPerson();
        }
        Log.d(TAG,"jigar the url we have as image source is "+item.getAttachments());
        if (item.getAttachments().equals("")){
            vh.txt_atteach.setVisibility(View.GONE);
        } else {
            vh.txt_atteach.setVisibility(View.VISIBLE);
        }

        String msg = item.getMessage();

        String m = Html.fromHtml(msg).toString();

        vh.txt_subject.setText(sub);
        vh.txt_message.setText(m);
        vh.txt_time.setText(ext_time);
        vh.txt_date.setText(ext_date);
        vh.txt_atteach.setPaintFlags(vh.txt_atteach.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        vh.txt_atteach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(context,attech);
            }
        });

        return vh.rootView;
    }

    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView txt_subject;
        public final TextView txt_message;
        public final TextView txt_atteach;
        public final TextView txt_time;
        public final TextView txt_date;

        private ViewHolder(LinearLayout rootView, TextView txt_subject, TextView txt_message, TextView txt_atteach, TextView txt_time, TextView txt_date) {
            this.rootView = rootView;
            this.txt_subject = txt_subject;
            this.txt_message = txt_message;
            this.txt_atteach = txt_atteach;
            this.txt_time = txt_time;
            this.txt_date = txt_date;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView txt_subject = (TextView) rootView.findViewById(R.id.txt_subject);
            TextView txt_message = (TextView) rootView.findViewById(R.id.txt_message);
            TextView txt_atteach = (TextView) rootView.findViewById(R.id.txt_atteach);
            TextView txt_time = (TextView) rootView.findViewById(R.id.txt_time);
            TextView txt_date = (TextView) rootView.findViewById(R.id.txt_date);
            return new ViewHolder(rootView, txt_subject, txt_message, txt_atteach, txt_time,txt_date);
        }
    }

    public void showDialog(Context mContext,String add) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailog_show_image, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);

        ImageView image = (ImageView) dialogView.findViewById(R.id.image);

        AppGlobal.showProgressDialog(context);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        Glide.with(context).load(add)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        AppGlobal.hideProgressDialog(context);
                        Toast.makeText(context, "No Image Found!" , Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        AppGlobal.hideProgressDialog(context);
                        alertDialog.show();
                        return false;
                    }
                })
                .into(image);

    }
}