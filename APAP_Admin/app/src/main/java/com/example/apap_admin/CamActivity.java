package com.example.apap_admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CamActivity extends Activity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imgDocument;
    private Button btnSavePage;
    private Button btnFinishScanning;
    private ArrayList<Bitmap> scannedDocument;
    File directory;

    private String pdfFilePath; // Added to store the PDF file path

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        directory = new File(getFilesDir(), "bitmap_data");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        imgDocument = findViewById(R.id.imgDocument);
        btnSavePage = findViewById(R.id.btnSavePage);
        btnFinishScanning = findViewById(R.id.btnFinishScanning);
        Button btnCapture = findViewById(R.id.btnCapture);
        scannedDocument = new ArrayList<>();

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        btnSavePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the current page and prepare for the next page
                Log.d("test1111", "convertBitmapsToPDF: "+ scannedDocument.size());
                saveCurrentPage();
            }
        });

        btnFinishScanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish scanning and save the entire document
                saveScannedDocument();
            }
        });
    }
    void persistBitmap() throws IOException {
        // Save each bitmap as a separate file
        Log.d("test2345", "persistBitmap: "+ scannedDocument.size());

        for (int i = 0; i < scannedDocument.size(); i++) {
            File file = new File(directory, "bitmap_" + i + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            scannedDocument.get(i).compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
    }

    void loadBitmap() {
        ArrayList<Bitmap> loadedBitmaps = new ArrayList<>();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                loadedBitmaps.add(bitmap);
            }
        }
        Log.d("test2345", "loadbitmap: "+ loadedBitmaps.size());

        scannedDocument = loadedBitmaps;
    }


    @Override
    protected void onPause() {
        super.onPause();
//        try {
////            persistBitmap();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadBitmap();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("bitmapArray", scannedDocument);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scannedDocument = savedInstanceState.getParcelableArrayList("bitmapArray");
    }


    private void dispatchTakePictureIntent() {
        Log.d("test2345", "before take picture: "+ scannedDocument.size());

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            // Reset UI for capturing a new photo
            imgDocument.setImageResource(android.R.color.transparent);
            btnSavePage.setVisibility(View.VISIBLE);
        }
    }

    private void saveCurrentPage() {

        // Save the current page (image) and prepare for the next page
        // Here, you can save the Bitmap in a list or array for later processing
        // For simplicity, we'll just display the current image as the scanned document
        Log.d("test222", "convertBitmapsToPDF: "+ scannedDocument.size());

        imgDocument.setImageResource(android.R.color.transparent); // Clear the ImageView
        btnSavePage.setVisibility(View.GONE);
        btnFinishScanning.setVisibility(View.VISIBLE);
    }

    private void saveScannedDocument() {
        // Generate a unique PDF file name based on the current timestamp
        String pdfFileName = "scanned_document_" + System.currentTimeMillis() + ".pdf";

        // Convert the scanned images to a PDF and get the PDF file URI
        Uri pdfUri = convertBitmapsToPDF(scannedDocument, pdfFileName);

        // Create an Intent to hold the result data
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pdfUri", pdfUri.toString());

        // Set the result code and data
        setResult(RESULT_OK, resultIntent);

        // Finish the CamActivity
        finish();
    }


    private Uri convertBitmapsToPDF(ArrayList<Bitmap> bitmaps, String pdfFileName) {
        PdfDocument pdfDocument = new PdfDocument();
        Log.d("test123", "convertBitmapsToPDF: "+ bitmaps.size());

        for (Bitmap bitmap : bitmaps) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
                    bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            canvas.drawBitmap(bitmap, 0, 0, paint);

            pdfDocument.finishPage(page);
        }

        try {
            File pdfFile = new File(getFilesDir(), pdfFileName); // Store the PDF file in internal storage
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            pdfDocument.close();

            // Display the PDF in an ImageView (you can modify this part as needed)
            Uri pdfUri = Uri.fromFile(pdfFile);
            imgDocument.setImageURI(pdfUri);
            btnSavePage.setVisibility(View.GONE);
            btnFinishScanning.setVisibility(View.GONE);

            Toast.makeText(this, "Scanned document saved as PDF", Toast.LENGTH_SHORT).show();

            // Return the URI of the generated PDF file
            return pdfUri;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    scannedDocument.add(imageBitmap);
                    // Display the captured image in the ImageView
                    imgDocument.setImageBitmap(imageBitmap);
                    btnSavePage.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
