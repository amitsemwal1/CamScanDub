package com.example.semwal.camscandub;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final int GALLERY_PICTURE = 1;
    Button btn_select, btn_convert,btnViewAll;
    ImageView iv_image;
    boolean boolean_permission;
    boolean boolean_save;
    Bitmap bitmap;
    public static final int REQUEST_PERMISSIONS = 1;
    String imageName;


    ContentValues values;
    Uri imageUri;

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cam_to_pdf);

        btn_select = (Button) findViewById(R.id.btn_select);
        btn_convert = (Button) findViewById(R.id.btn_convert);
        btnViewAll = (Button) findViewById(R.id.btnViewAll);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        //for gallery permission

        fn_permission();

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                values=new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, GALLERY_PICTURE);



            }
        });

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
              /*  pd=new ProgressDialog(MainActivity.this);
                pd.setMessage("Converting File..");
                pd.show();
*/
                createPdf(imageName);
                getFiles();

            }
        });
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(MainActivity.this, PDFViewActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {

            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                //Create Folder in Sdcard
                createDirectory();

                File file=new File(String.valueOf(imageUri));
                String fileName=file.getName();
                Log.d("ImageName",fileName);
                imageName=fileName;
                Log.d("ImageNameUri",imageName);

                iv_image.setImageBitmap(bitmap);
                btn_convert.setClickable(true);
                btn_convert.setVisibility(View.VISIBLE);


            } catch (IOException e) {
                e.printStackTrace();
            }

            }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private boolean status;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    private void createPdf(String imageName) {
        Log.d("ImagefullName",imageName);
        //Start Progress Dialog


        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/CamScanDub/"+imageName+".pdf";
        File filePath = new File(targetPdf);
        Log.d("PdfFile", new String(String.valueOf(filePath)));

        try {
            document.writeTo(new FileOutputStream(filePath));
            //btn_convert.setText("Check PDF");
            //boolean_save = true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Convert Successfully", Toast.LENGTH_SHORT).show();
        //StopProgressDialog
        //stopPd();
        btn_convert.setVisibility(View.GONE);
        iv_image.setImageResource(0);
        // close the document
        document.close();

    }

    private void startPD(){
        pd=new ProgressDialog(this);
        pd.setMessage("Converting File..");
        pd.show();
    }
    private void stopPd(){
        pd.dismiss();
    }
    private void createDirectory(){
        File myDirectory=new File(Environment.getExternalStorageDirectory(),"CamScanDub");
        if (!myDirectory.exists()){
            myDirectory.mkdirs();
        }
    }
    //Get All PDF file from folder
    private void getFiles(){
        String path= Environment.getExternalStorageDirectory().toString()+"/CamScanDub";
        Log.d("Files","Path: "+path);
        File f=new File(path);

        File file[]=f.listFiles();
        Log.d("TotalFiles", String.valueOf(file.length));

        for (int i=0;i<file.length;i++){
            Log.d("FileNames: ",file[i].getName());
        }

    }

    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){

            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;


            } else {
                Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }


}


