package com.example.cameraapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    ImageView selectedImage;
    Button cameraBtn,galleryBtn;
    Button uploadBtn;
    String currentPhotoPath;
    Camera mCamera;
    SurfaceView mPreview;

    private ProgressDialog processDialog;
    //StorageReference storageReference;

    public String serverLink = "https://rug-counter.boutiquerugs.com/backend/imageupload.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);
        uploadBtn = findViewById(R.id.uploadBtn);


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Before Ask Camera Permissions.", Toast.LENGTH_SHORT).show();
                try {
                    askCameraPermissions();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });






       uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = null;
                Activity activity = null;
                Toast.makeText(MainActivity.this, "Before Upload Photo", Toast.LENGTH_SHORT).show();


                selectedImage.buildDrawingCache();
                Bitmap bitmap = selectedImage.getDrawingCache();
                String encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                uploadImageToServer(bitmap,encodedImageData);
            }
        });



        //async task to upload image
        class Upload extends AsyncTask<Void,Void,String> {
            private Context mContext;
            private Activity mActivity;
            private Bitmap image;
            private String name;


           /* public ServiceStubAsyncTask(Context context, Activity activity) {
                mContext = context;
                mActivity = activity;
            }*/
            public Upload(Context context, Activity activity,Bitmap image,String name){
                this.image = image;
                this.name = name;
            }

            @Override
            protected String doInBackground(Void... params) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //compress the image to jpg format
                image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                /*
                 * encode image to base64 so that it can be picked by saveImage.php file
                 * */
                String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

                //generate hashMap to store encodedImage and the name
                HashMap<String,String> detail = new HashMap<>();
                detail.put("imageName", name);
                detail.put("imgData", encodeImage);
                try{
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to saveImage.php file

                    String response = Request.post(serverLink,dataToSend);
                    try {

                        JSONObject resultJsonObject = new JSONObject(response);
                        JSONObject console= resultJsonObject.getJSONObject("console");
                        JSONObject decodeImageData= resultJsonObject.getJSONObject("imageData");
                        @SuppressLint("ResourceType") LinearLayout progressBarLayout = (LinearLayout)findViewById(R.layout.progress_bar);
                        progressBarLayout.setTooltipText(console.toString());
                        TextView textView = (TextView) progressBarLayout.findViewById(R.id.textView);
                        ImageView imageView = progressBarLayout.findViewById(R.id.imageView);
                        textView.setText(console.toString());
                        imageView.setImageBitmap();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //return the response
                    return response;

                }catch (Exception e){
                    e.printStackTrace();
                    //Log.e(TAG,"ERROR  "+e);
                    return null;
                }


            }



            @Override
            protected void onPostExecute(String s) {

                    super.onPostExecute(s);

                    if (processDialog.isShowing()) {
                        processDialog.dismiss();
                    }
                //show image uploaded
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                processDialog = new ProgressDialog(mContext);
                processDialog.setMessage("Please  Wait ...");
                processDialog.setCancelable(false);
                processDialog.show();
            }

        }



        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {

            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }


    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();

        // Get the Base64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    private void uploadImageToServer(Bitmap bitmap, String encodedImageData) {

        Upload(bitmap,encodedImageData);
    }

    private void Upload(Bitmap bitmap, String encodedImageData) {

    }

    //// Check if the permission is already granted
    //if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
    //    // If not, request the permission
    //    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    //}
    private void askCameraPermissions() throws IOException {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "onRequestPermissionsResult Inside Line 251.", Toast.LENGTH_SHORT).show();
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                Toast.makeText(this, "onActivityResult Inside Line 122.", Toast.LENGTH_SHORT).show();
                File file = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(file));
                Toast.makeText(this, "onActivityResult Inside Line 125.", Toast.LENGTH_SHORT).show();

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                //uploadImageToServer(file.getName(),contentUri.getEncodedUserInfo());


            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                //Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                //selectedImage.setImageURI(contentUri);

                //uploadImageToServer(imageFileName, contentUri);


            }

        }


    }












/******************Check Permission Before Using the Camera:
                    Before you access the camera in your app, always check if the permission has been granted:
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
        // You have permission, access the camera
        } else {
        // You don't have permission, request it or inform the user
        }
 **********************************************************/

    private void dispatchTakePictureIntent()throws IOException {
        Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside 234 ", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // You have permission, access the camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        // Ensure that there's a camera activity to handle the intent------burda problem var
        //if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
        Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside 238 ", Toast.LENGTH_SHORT).show();
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside IOException ", Toast.LENGTH_SHORT).show();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.cameraapplication.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }else{
            Toast.makeText(MainActivity.this, "DispatchTakePictureIntent Inside You don't have permission to access the camera ", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
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
}
