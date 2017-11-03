package com.example.semwal.camscandub;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;

/**
 * Created by Semwal on 10/27/2017.
 */

public class CamToPdfFragment extends Fragment {

    public static final int CAMERAPIC = 1;
    Button btn_select, btn_convert;
    ImageView iv_image;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    public static final int REQUEST_PERMISSIONS = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cam_to_pdf, container, false);
        btn_convert = (Button) view.findViewById(R.id.btn_convert);
        btn_select = (Button) view.findViewById(R.id.btn_select);
        iv_image=(ImageView)view.findViewById(R.id.iv_image);

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_PICTURE);*/
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERAPIC);
            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boolean_save) {

                    Intent intent1 = new Intent(getActivity(), PDFViewActivity.class);
                    startActivity(intent1);

                } else {
                    //createPdf();
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERAPIC) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            iv_image.setImageBitmap(image);

            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] byteArray=stream.toByteArray();

        }
    }
    private void converImage(){

        //DocumentsContract.Document

    }
}
