package com.example.cameraapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    Button cameraBtn,galleryBtn;
    Button uploadBtn;
    String currentPhotoPath;
    //StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        uploadBtn = findViewById(R.id.uploadBtn);



        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Before Upload Photo", Toast.LENGTH_SHORT).show();

               // uploadImageToServer();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Before Ask Camera Permissions.", Toast.LENGTH_SHORT).show();
                askCameraPermissions();
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Before Ask Camera Permissions.", Toast.LENGTH_SHORT).show();
                askCameraPermissions();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }



    private void askCameraPermissions() {
        Toast.makeText(MainActivity.this, "Ask Camera Permissions Inside Line 80", Toast.LENGTH_SHORT).show();

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);

            Toast.makeText(MainActivity.this, "Ask Camera Permissions Inside Line 83", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Ask Camera Permissions Inside Line 85 Before DispatchTakePictureIntent", Toast.LENGTH_SHORT).show();
            dispatchTakePictureIntent();
            Toast.makeText(MainActivity.this, "Ask Camera Permissions Inside Line 87 After DispatchTakePictureIntent", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "onRequestPermissionsResult Inside Line 93.", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
                Toast.makeText(this, "onRequestPermissionsResult Inside Line 95.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "onRequestPermissionsResult Inside Line 97.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "onActivityResult Inside Line 109.", Toast.LENGTH_SHORT).show();
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Toast.makeText(this, "onActivityResult Inside Line 112.", Toast.LENGTH_SHORT).show();
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToServer(f.getName(),contentUri);


            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);

                uploadImageToServer(imageFileName, contentUri);


            }

        }


    }

    private void uploadImageToServer(String name, Uri contentUri) {
        Toast.makeText(this, "uploadImageToServer Inside Line 146.", Toast.LENGTH_SHORT).show();
        CmdObj cmdObj = new CmdObj("/usr/local/share/java/opencv4/",
                "/var/www/rug-counter/models/load_model.onnx",
                "rugcounter.jar",
                "/var/www/rug-counter/inputImages/img_unprocessed.jpeg",
                "/var/www/rug-counter/outputImages/img_processed.jpeg",
                "/usr/local/share/java/opencv4/");

        Toast.makeText(this, "uploadImageToServer Inside Line 154.", Toast.LENGTH_SHORT).show();
        AppCredential credential = new AppCredential("165.22.3.2",
                "root",
                "GkRcGH6FeWdvyXQW5MDC",
                "https://rug-counter.boutiquerugs.com/",
                "/var/www/rug-counter/",
                "22");
        RunnerHelper runnerHelper = new RunnerHelper();

        try {

            /**Burdaki pathi degistirmek gerek***/
            runnerHelper.runner(credential, cmdObj,
                    "jarRunner/src/main/images/img_unprocessed.jpg",
                    "/var/www/rug-counter/inputImages/img_unprocessed.jpeg",
                    "/var/www/rug-counter/outputImages/img_processed.jpeg");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


       /* final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });

                Toast.makeText(MainActivity.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });*/

    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside 196 ", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside 200 ", Toast.LENGTH_SHORT).show();
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside IOException Line  206", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.cameraapplication.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}