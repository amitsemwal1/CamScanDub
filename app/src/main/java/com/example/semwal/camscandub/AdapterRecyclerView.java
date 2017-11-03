package com.example.semwal.camscandub;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by uu on 28-10-2017.
 */

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.MyViewHolder> {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG="PDFViewActivity";
    int position=-1;

    Context context;
    Activity activity;

    String uName[];
    File AdapterFile[];
    ArrayList<String> fileName;
    public AdapterRecyclerView( ArrayList<String> pdfName){
        this.fileName=pdfName;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        PDFView pdfview;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.tv);
           // pdfview=(PDFView)itemView.findViewById(R.id.pdfview);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String nems=fileName.get(position);
        holder.tv.setText(nems);

        //String path= Environment.getExternalStorageDirectory().toString()+"/CamScanDub";
        //File file= new File(path);

    }

    @Override
    public int getItemCount() {
       // Log.d("AdaoterLength", String.valueOf(AdapterFile.length));





        Log.d("pdfSixe", String.valueOf(fileName.size()));
        return fileName.size();
    }



}
