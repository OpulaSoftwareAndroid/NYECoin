package existv2.com.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import existv2.com.MainActivity;
import existv2.com.R;
import existv2.com.adapter.AllTicketAdapter;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.AllTicketInfoa.AllTicketInfo;
import existv2.com.model.AllTicketInfoa.Info;
import existv2.com.model.CommanResponse;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.kbeanie.imagechooser.api.ChooserType.REQUEST_CAPTURE_PICTURE;


public class Ticket_details extends Fragment implements ImageChooserListener {

    private ListView list_transaction;
    private ArrayList<Info> contactList;
    private AllTicketAdapter adapter;
    TextView txt_norecord;
    FloatingActionButton btn_create_ticket;
    String stMessage = "", stSubject = "";
    EditText edFileName;
    private String filePath = "";
    private MultipartBody.Part body;
    private int chooserType;
    private ImageChooserManager imageChooserManager;
    private SharedPreference sharedPreference;
    com.rengwuxian.materialedittext.MaterialEditText edi_re_message;
    SwipeRefreshLayout swipeToRefresh_ticket;
    TextView re_text;
    ImageView img_re_image;
    AlertDialog alertDialog;

    public Ticket_details() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ticket_details, container, false);

        MainActivity.setToolbarname("Ticket Details");

        sharedPreference = new SharedPreference();
        list_transaction = (ListView) rootView.findViewById(R.id.list_tickett);
        txt_norecord = (TextView) rootView.findViewById(R.id.txt_norecord);
        btn_create_ticket = (FloatingActionButton) rootView.findViewById(R.id.btn_create_tickett);
        swipeToRefresh_ticket = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh_ticket);

        btn_create_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForTicket(getActivity());
            }
        });

        String ticketid = sharedPreference.getValue(getActivity(), WsConstant.ticket_number);
        String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
        String oc = sharedPreference.getValue(getActivity(), WsConstant.oc);

        if (oc.equalsIgnoreCase("Open Ticket")){
            btn_create_ticket.setVisibility(View.VISIBLE);
        } else if (oc.equalsIgnoreCase("Close Ticket")){
            btn_create_ticket.setVisibility(View.GONE);
        }

        swipeToRefresh_ticket.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String ticketid = sharedPreference.getValue(getActivity(), WsConstant.ticket_number);
                String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
                ticket(ticketid,token);
                swipeToRefresh_ticket.setRefreshing(false);
            }
        });

        ticket(ticketid,token);

        contactList = new ArrayList<>();

        return rootView;
    }

    private void openDialogForTicket(Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (getActivity()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_ticket, null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();

        edi_re_message = (com.rengwuxian.materialedittext.MaterialEditText) dialogView.findViewById(R.id.edi_re_message);
        final RelativeLayout rel_image = (RelativeLayout) dialogView.findViewById(R.id.rel_imagee);
        re_text = (TextView) dialogView.findViewById(R.id.re_text);
        final TextView btn_edit_re_save = (Button) dialogView.findViewById(R.id.btn_edit_re_save);
        img_re_image = (ImageView) dialogView.findViewById(R.id.img_re_image);

        rel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChosseImage();
            }
        });

        btn_edit_re_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (edi_re_message.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter message.", Toast.LENGTH_SHORT).show();
                } else {
                    stMessage = edi_re_message.getText().toString();
                    String regid = sharedPreference.getValue(getActivity(), WsConstant.register_id);
                    String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
                    String ticketnumber = sharedPreference.getValue(getActivity(), WsConstant.ticket_number);
                    doCreateTicket(regid,stMessage,token,ticketnumber);
                }
            }
        });
        alertDialog.show();
    }

    public void doCreateTicket(String regId,final String msg,String token,String ticketnumber) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            String fileName = "";
            if (!filePath.equalsIgnoreCase("")) {
                File file = new File(filePath);

                if (!file.exists()) {
                    Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestBody reqFile = RequestBody.create(MediaType.parse("ProfilePicture/*"), file);
                body = MultipartBody.Part.createFormData("ProfilePicture", file.getName(), reqFile);
            }

            RequestBody aregid = RequestBody.create(MediaType.parse("text/plain"), regId);
            RequestBody amsg = RequestBody.create(MediaType.parse("text/plain"), msg);
            RequestBody atoken = RequestBody.create(MediaType.parse("text/plain"), token);
            RequestBody anumber = RequestBody.create(MediaType.parse("text/plain"), ticketnumber);
            RequestBody atype = RequestBody.create(MediaType.parse("text/plain"), "1");

            new RestClient(getActivity()).getInstance().get()
                    .reply_ticket(body,aregid,amsg,atoken,anumber,atype).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            alertDialog.dismiss();
                            filePath="";
                            body=null;
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            String regit = sharedPreference.getValue(getActivity(), WsConstant.ticket_number);
                            String token = sharedPreference.getValue(getActivity(), WsConstant.valid_data);
                            ticket(regit,token);
                        } else {
                            Toast.makeText(getActivity(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Data Null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommanResponse> call, Throwable t) {
                    try {
                        AppGlobal.hideProgressDialog(getActivity());
                        Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void ticket(final String ticketid,final String token) {
        if (AppGlobal.isNetwork(Objects.requireNonNull(getActivity()))) {
            AppGlobal.showProgressDialog(getActivity());
            Map<String, String> optioMap = new HashMap<>();
            optioMap.put("TicketNumber", ticketid);
            optioMap.put("ValidData", token);
            optioMap.put("Type", "1");

            new RestClient(getActivity()).getInstance().get().alltickethistory(optioMap).enqueue(new Callback<AllTicketInfo>() {
                @Override
                public void onResponse(Call<AllTicketInfo> call, Response<AllTicketInfo> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {

                            contactList = (ArrayList<Info>) response.body().getInfo();

                            adapter = new AllTicketAdapter(getActivity(), contactList);

                            list_transaction.setAdapter(adapter);

                            if (response.body().getTicketStatus().equalsIgnoreCase("1")){
                                btn_create_ticket.setVisibility(View.GONE);
                            } else if (response.body().getTicketStatus().equalsIgnoreCase("0")){
                                btn_create_ticket.setVisibility(View.VISIBLE);
                            }

                        } else {
                            list_transaction.setVisibility(View.GONE);
                            txt_norecord.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.network_time_out_error), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AllTicketInfo> call, Throwable t) {
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

    private void openDialogForChosseImage() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        View vi = getActivity().getLayoutInflater().inflate(R.layout.dialog_open_image, null, false);
        dialog.setContentView(vi);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();

        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setAttributes(lp);


        final TextView closeAppNo = dialog.findViewById(R.id.image_camera);
        closeAppNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                dialog.dismiss();
            }
        });
        final TextView closeAppYes = dialog.findViewById(R.id.image_gallary);
        closeAppYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("CheckResult")
    private void takePicture() {
        RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA).subscribe(new io.reactivex.functions.Consumer<Uri>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Uri uri) throws Exception {
                File finalFile = new File(getRealPathFromURI(uri));
                filePath = finalFile.getAbsolutePath();
                Glide.with(getActivity()).load(filePath)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).into(img_re_image);
                re_text.setVisibility(View.GONE);
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filePath = image.getFilePathOriginal();
                Glide.with(getActivity()).load(image.getFilePathOriginal())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        }).into(img_re_image);
                re_text.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onError(String s) {
        Log.e("Error", "::" + s);
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "File Path : " + filePath + "\nChooser Type: " + chooserType);

        if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CAPTURE_PICTURE) {
            try {
                Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Objects.requireNonNull(thumbnail).compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
                filePath = destination.getAbsolutePath();
                img_re_image.setImageBitmap(thumbnail);
                re_text.setVisibility(View.GONE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(Objects.requireNonNull(getActivity()), chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }
}