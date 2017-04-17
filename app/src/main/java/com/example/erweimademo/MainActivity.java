package com.example.erweimademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate;
    private Button btnScan;
    private ImageView mImageView;
    private TextView tvResult;
    private EditText edtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = edtContent.getText().toString().trim();
                if(str.length()>0){
                    try {
                        str = new String(str.getBytes("utf-8"),"ISO-8859-1");
                    }catch (Exception e){

                    }
                    Bitmap bitmap = encodeAsBitmap(str);
                    mImageView.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(MainActivity.this,"请输入内容",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this)
                .setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class)
                .initiateScan();

            }
        });
    }

    private void initViews() {
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnScan = (Button) findViewById(R.id.btnScan);
        mImageView = (ImageView) findViewById(R.id.imgErWeiMa);
        tvResult = (TextView) findViewById(R.id.tvResult);
        edtContent = (EditText) findViewById(R.id.edtContent);

    }

    Bitmap encodeAsBitmap(String str){
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae){ // ?
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"扫描成功",Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                tvResult.setText(ScanResult);
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }

    }
}
