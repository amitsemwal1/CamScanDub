package com.example.semwal.camscandub;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.semwal.camscandub.RecyclerItemTouchListener.ClickListener;
import com.example.semwal.camscandub.RecyclerItemTouchListener.RecyclerTouchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PDFViewActivity extends AppCompatActivity  {

    int position = -1;
    AdapterRecyclerView adapterRecyclerView;
    RecyclerView recyclerView;
    ArrayList<String> pdfName=new ArrayList<>();
    String fileName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        init();
    }

    private void init() {
        // pdfView= (PDFView)findViewById(R.id.pdfview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        position = getIntent().getIntExtra("position", -1);
        //   displayFromSdcard();
        setAdapter();
    }

    Context context;
    Activity activity;

    private void setAdapter() {
        pdfName=new ArrayList<>();
        getFiles();

        //Log.d("AraySize",pdfName.get(2));
        Log.d("FileNames: ", "ArrayAdd");
        adapterRecyclerView = new AdapterRecyclerView(pdfName);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterRecyclerView);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String fileName=pdfName.get(position);

               Log.d("PdfFLIENAme",fileName);

                Intent intent=new Intent(PDFViewActivity.this,FullPdfView.class);
                Bundle bundle=new Bundle();
                bundle.putString("fileName",fileName);
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void longClick(View view,  int positon11) {
                fileName=pdfName.get(positon11);
                Log.d("FILLENAME",fileName);
                Log.d("Position", String.valueOf(positon11));

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PDFViewActivity.this);
                alertDialogBuilder.setMessage("Are you sure delete file").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String targetPdf = "/sdcard/CamScanDub/"+fileName;

                        Log.d("FILLENAME11",targetPdf);
                        File filePath = new File(targetPdf);
                        deleteRecursive(filePath);
                        implementRecyclerView();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();


            }
        }));

    }


    public static void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isFile()){
            fileOrDirectory.delete();
        }else {
            Log.d("nofile","noFile");
        }


    }
    private void implementRecyclerView(){
        pdfName=new ArrayList<>();
        getFiles();

        //Log.d("AraySize",pdfName.get(2));
        Log.d("FileNames: ", "ArrayAdd");
        adapterRecyclerView = new AdapterRecyclerView(pdfName);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterRecyclerView);
    }

  //Get All PDF file from folder
    private void getFiles() {
        String path = Environment.getExternalStorageDirectory().toString() + "/CamScanDub";
        Log.d("Files", "Path: " + path);
        File f = new File(path);

        File file[] = f.listFiles();
        Log.d("TotalFiles", String.valueOf(file.length));
        for (int i = 0; i < file.length; i++) {
            Log.d("FileNames: ", file[i].getName());

            pdfName.add(file[i].getName());
        }
    }



}