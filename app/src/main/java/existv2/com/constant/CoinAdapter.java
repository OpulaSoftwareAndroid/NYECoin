package existv2.com.constant;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.model.POJOCoin;
import existv2.com.model.SvgDecoder;
import existv2.com.model.Wallatea.SvgDrawableTranscoder;
import existv2.com.model.Wallatea.SvgSoftwareLayerSetter;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.MyViewHolder> {

    String TAG="CoinAdapter";
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    private List<POJOCoin> coinList;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewCoinFullName, textViewCoinShortName, textViewCoinRate,textViewCoinRateChange;

        public ImageView imageViewCoin,imageViewMarketTrend;
        public MyViewHolder(View view) {
            super(view);
            textViewCoinFullName = (TextView) view.findViewById(R.id.textViewCoinFullName);
            textViewCoinShortName = (TextView) view.findViewById(R.id.textViewCoinShortName);
            textViewCoinRate = (TextView) view.findViewById(R.id.textViewCoinRate);
            textViewCoinRateChange = (TextView) view.findViewById(R.id.textViewCoinRateChange);
            imageViewCoin =  view.findViewById(R.id.imageViewCoin);
            imageViewMarketTrend =  view.findViewById(R.id.imageViewMarketTrend);

        }
    }


    public CoinAdapter(List<POJOCoin> coinList, Context context) {
        this.coinList = coinList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        POJOCoin pojoCoin = coinList.get(position);
        holder.textViewCoinFullName.setText(pojoCoin.getCoinFullName());
        holder.textViewCoinShortName.setText("("+pojoCoin.getCoinShortName()+")");
        holder.textViewCoinRate.setText(pojoCoin.getCoinCurrentPrice());
        holder.textViewCoinRateChange.setText("("+pojoCoin.getCoinPriceChangePer()+")");
        Log.v(TAG, "jigar the image of coin " + pojoCoin.getCoinImageUrl());

        Glide.with(context).load(pojoCoin.getCoinImageUrl()).into(holder.imageViewCoin);

//        try {
//            requestBuilder = Glide
//                    .with(context)
//                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
//                    .from(Uri.class)
//                    .as(SVG.class)
//                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
//                    .sourceEncoder(new StreamEncoder())
//                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
//                    .decoder(new SvgDecoder())
////                .placeholder(R.drawable.ic_facebook)
////                .error(R.drawable.ic_web)
//                    .animate(android.R.anim.fade_in)
//                    .listener(new SvgSoftwareLayerSetter<Uri>());
//            Uri uri = Uri.parse(pojoCoin.getCoinImageUrl());
//            requestBuilder
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    // SVG cannot be serialized so it's not worth to cache it
//                    .load(uri)
//                    .into(holder.imageViewCoin);
//        }catch (Exception ex)
//        {
//            Log.d(TAG,"jigar the error in exception is "+ex);
//        }
        if(Double.parseDouble(pojoCoin.getCoinPriceChangePer())<0)
        {
            holder.imageViewMarketTrend.setImageResource(R.drawable.down_trend);
            holder.textViewCoinRateChange.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.textViewCoinRate.setTextColor(context.getResources().getColor(R.color.colorRed));

        }else
        {
            holder.imageViewMarketTrend.setImageResource(R.drawable.up_trend);
            holder.textViewCoinRateChange.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.textViewCoinRate.setTextColor(context.getResources().getColor(R.color.colorGreen));

        }



    }

    @Override
    public int getItemCount() {
        return coinList.size();
    }
}