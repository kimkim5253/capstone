package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;




public class CameraActivity extends AppCompatActivity {

    private Button captureImageBtn, detectImageBtn;
    private ImageView imageView;
    private TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private FirebaseFunctions mFunctions;
    //private Object imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        captureImageBtn = findViewById(R.id.capture_button);
        detectImageBtn = findViewById(R.id.detected_button);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dispatchTakePictureIntent();
            }
        });

        detectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                detectTextFromImage();
            }
        });
    }




    private void dispatchTakePictureIntent ()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            imageBitmap = scaleBitmapDown(imageBitmap,640);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        }
    }

    //mFunctions = FirebaseFunctions.getInstance();

    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                    @Override
                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }

    private Bitmap scaleBitmapDown(Bitmap imageBitmap, int maxDimension) {
        int originalWidth = imageBitmap.getWidth();
        int originalHeight = imageBitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(imageBitmap, resizedWidth, resizedHeight, false);
    }
    private void detectTextFromImage()
    {
        //FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromMediaImage(mediaImage);

    }
}
