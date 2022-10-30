package com.example.ihelpu.tools.communication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.ihelpu.MainActivity;
import com.google.common.util.concurrent.ListenableFuture;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ihelpu.R;

import java.util.concurrent.ExecutionException;

//NIEDOKOŃCZONE!
public class ReadSignLanguage extends AppCompatActivity {

    private static final int RC_CP = 200;

    TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sign_language);

        showResult = findViewById(R.id.showResult);

        if(RC_CP == 200) {
            cameraPermissions();
        }

        startCamera();
    }

    private void cameraPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, RC_CP);
        } else{
            toast("Wystąpił błąd! Spróbuj ponownie później.");
        }
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider>
                cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                            bindPreview(cameraProvider);
                        } catch (ExecutionException | InterruptedException e) { }
                    }
                },
                ActivityCompat.getMainExecutor(this)
        );
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        PreviewView mPreviewView = findViewById(R.id.cameraImage);

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ActivityCompat.getMainExecutor(this),
                new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(@NonNull ImageProxy image) {
                        image.close();
                    }
                });

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK).build();
        cameraProvider.unbindAll();

        cameraProvider.bindToLifecycle(this, cameraSelector,
                preview, imageAnalysis);
    }

    void toast(String toastText){
        Toast.makeText(ReadSignLanguage.this,toastText, Toast.LENGTH_LONG).show();
    }
}