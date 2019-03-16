package existv2.com.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Button;
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
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import existv2.com.R;
import existv2.com.constant.AppGlobal;
import existv2.com.constant.SharedPreference;
import existv2.com.constant.WsConstant;
import existv2.com.model.CommanResponse;
import existv2.com.webservice.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.kbeanie.imagechooser.api.ChooserType.REQUEST_CAPTURE_PICTURE;


public class Create_Ticket extends Fragment implements ImageChooserListener {

    com.rengwuxian.materialedittext.MaterialEditText edi_ti_username,edi_ti_subject,edi_ti_message;
    RelativeLayout rel_image;
    Button btn_edit_ti_save;
    TextView ti_text;
    SharedPreference sharedPreference;
    ImageView img_ti_image;
    private String filePath = "";
    private MultipartBody.Part body;
    private int chooserType;
    private ImageChooserManager imageChooserManager;

    public Create_Ticket() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_create_ticket, container, false);

        sharedPreference = new SharedPreference();

        String usernamne = sharedPreference.getValue(getActivity(),WsConstant.username);

        edi_ti_username = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_ti_username);
        edi_ti_subject = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_ti_subject);
        edi_ti_message = (com.rengwuxian.materialedittext.MaterialEditText) rootview.findViewById(R.id.edi_ti_message);
        rel_image = (RelativeLayout) rootview.findViewById(R.id.rel_image);
        btn_edit_ti_save = (Button) rootview.findViewById(R.id.btn_edit_ti_save);
        ti_text = (TextView) rootview.findViewById(R.id.ti_text);
        img_ti_image = (ImageView) rootview.findViewById(R.id.img_ti_image);

        edi_ti_username.setText(usernamne);

        rel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogForChosseImage();
            }
        });

        btn_edit_ti_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edi_ti_subject.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter subject", Toast.LENGTH_SHORT).show();
                } else if (edi_ti_message.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    String subject = edi_ti_subject.getText().toString();
                    String msg = edi_ti_message.getText().toString();
                    String regid = sharedPreference.getValue(getActivity(), WsConstant.register_id);
                    String token = sharedPreference.getValue(getActivity(),WsConstant.valid_data);
                    create_ticket(regid,subject,msg,token);
                }
            }
        });

        return rootview;
    }

    public void create_ticket(String regId, String subject, final String msg, String token) {
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
            RequestBody asubject = RequestBody.create(MediaType.parse("text/plain"), subject);
            RequestBody amsg = RequestBody.create(MediaType.parse("text/plain"), msg);
            RequestBody atoken = RequestBody.create(MediaType.parse("text/plain"), token);
            RequestBody atype = RequestBody.create(MediaType.parse("text/plain"), "1");

            new RestClient(getActivity()).getInstance().get()
                    .create_ticket(body,asubject,aregid,amsg,atoken,atype).enqueue(new Callback<CommanResponse>() {
                @Override
                public void onResponse(Call<CommanResponse> call, Response<CommanResponse> response) {
                    AppGlobal.hideProgressDialog(getActivity());
                    if (response.body() != null) {
                        if (response.body().getStatus() == 1) {
                            edi_ti_message.setText("");
                            edi_ti_subject.setText("");
                            img_ti_image.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dott));
                            ti_text.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
                        }).into(img_ti_image);
                ti_text.setVisibility(View.GONE);
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
                        }).into(img_ti_image);
                ti_text.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onError(String s) {
        Log.e("Error", ":" + s);
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
                img_ti_image.setImageBitmap(thumbnail);
                ti_text.setVisibility(View.GONE);
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