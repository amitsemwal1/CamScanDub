package com.example.semwal.camscandub;

import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by uu on 30-10-2017.
 */

public class FullPdfView extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private PDFView pdfview;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG = "PDFViewActivity";
    int position = -1;
    Button btn_addpage;

    String pdfFile_Name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_pdf_view);
        pdfview = (PDFView) findViewById(R.id.pdfview);
        btn_addpage = (Button) findViewById(R.id.btn_addpage);


        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                pdfFile_Name = bundle.getString("fileName");
                Log.d("pdfFileName", pdfFile_Name);
                displayFromSdcard(bundle.getString("fileName"));
            } else {
                Log.d("Else",
                        "ElsePart");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_addpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FullPdfView.this, "Clicked", Toast.LENGTH_SHORT).show();
               // AddPageToPdf(pdfFile_Name);
                newPagePDF(pdfFileName);
            }
        });
    }

    PdfWriter writer;
    PdfReader reader;
    PdfStamper stamper;

    //Add a Page to Pdf
    private void AddPageToPdf(String fileName) {
        String pdfFilePath = "/sdcard/CamScanDub/" + fileName;
        Log.d("PdfFilePath", pdfFilePath);

        try {
            File file = new File(pdfFilePath);
            reader = new PdfReader(String.valueOf(file));
            int pagNO = reader.getNumberOfPages();
            Log.d("TotalPageNo", String.valueOf(pagNO));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //stamper.insertPage(reader.getNumberOfPages()+1,reader.getPageSize(1));
            stamper = new PdfStamper(reader, new FileOutputStream(pdfFilePath));
            PdfContentByte content = stamper.getOverContent(1);
            Image image = Image.getInstance(Image.getInstance(pdfFilePath));
            image.scaleAbsoluteHeight(50);
            image.scaleAbsoluteWidth((image.getWidth()*50)/image.getHeight());
            image.setAbsolutePosition(70,140);

            stamper.close();

            /*
            Document document=new Document();
            writer=PdfWriter.getInstance(document,new FileOutputStream(pdfFilePath));
            writer.setPageEmpty(false);
            document.close();
            */

        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  try {
            Document document = new Document();

           /* try {

                writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

                writer.setPageEmpty(false);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //writer.setPageEmpty(false);
            document.open();
            document.add(new Paragraph("This page will not followed by blank page!"));
            document.newPage();
            //we donotadd anything to this page:newPage() will be ignored
            document.newPage();
            document.add(new Paragraph("This page will follwed by blank page!"));
            document.newPage();
            writer.setPageEmpty(false);
            document.newPage();
            document.add(new Paragraph("The previous page was a blank page!"));
            document.close();


        } catch (DocumentException e) {
            e.printStackTrace();
        }*/
    }

    private void newPagePDF(String fileName) {
        String out="/sdcard/CamScanDub/" + fileName;
        File oldFile=new File(out);
        File newFile=new File(out);
        try{
            Document document=new Document();
            PdfCopy filePdfCopy=new PdfCopy(document,new FileOutputStream(oldFile,true));
            document.open();
            PdfReader pdfReader1=new PdfReader(newFile.getAbsolutePath());
            PdfReader reader_old=new PdfReader(oldFile.getAbsolutePath());
            filePdfCopy.addDocument(pdfReader1);
            filePdfCopy.addDocument(reader_old);
            filePdfCopy.close();
            pdfReader1.close();
            reader_old.close();
            document.close();

        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException: ", String.valueOf(e));
            //stats.addError();
        } catch (DocumentException e) {
            Log.e("DocumentException: ", String.valueOf(e));
            //stats.addError();
        } catch (IOException e) {
            Log.e("IOException: ", String.valueOf(e));
            //stats.addError();
        }

    }

    private void displayFromSdcard(String name) {
        pdfFileName = "/sdcard/CamScanDub/" + name;
        Log.d("filePath", pdfFileName);
        File file = new File(pdfFileName);


        Log.e("File path", file.getAbsolutePath());

        pdfview.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void loadComplete(int nbPages) {

        PdfDocument.Meta meta = pdfview.getDocumentMeta();
        printBookmarksTree(pdfview.getTableOfContents(), "-");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
