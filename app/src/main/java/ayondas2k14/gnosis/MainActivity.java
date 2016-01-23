package ayondas2k14.gnosis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CAMERA_CODE=1;
    private static int PICK_GALLERY_CODE=2;
    TextView nameTextView;
    ImageView profileImage;
    SharedPreferences myPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //Initialising the SharedPreferences object
        myPreference= PreferenceManager.getDefaultSharedPreferences(this);

        //Setting username text view and profile image image view
        nameTextView=(TextView)findViewById(R.id.userNameTV);
        profileImage=(ImageView)findViewById(R.id.profileImage);

        //get String value mapped corresponding to our name key
        String nameString=myPreference.getString("namePreference","NAME");
        nameTextView.setText(nameString);

        //get String value mapped corresponding to our image key
        String imageString=myPreference.getString("imagePreference", "ayon");

        //Convert String to image by decoding
        Bitmap image=decodeBase64(imageString);
        if(image==null)
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.user_default));
        else
            profileImage.setImageBitmap(image);

        //If device has no camera, disable the clickability of image view
        if(!hasCamera())
            profileImage.setClickable(false);

    }

    //Method to check device has a camera or not
    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Method to set click listener for user name text view
    public void onUserNameClicked(View view){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Enter your name");

        //Create a editext to input user name
        final EditText name=new EditText(this);
        name.setHint("Name");

        alert.setView(name);


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String username = name.getText().toString();

                if(username.isEmpty())
                    Toast.makeText(getApplicationContext(),"Name can not be empty!!",Toast.LENGTH_SHORT).show();
                else {
                    nameTextView.setText(username);

                    //Save name to Sharedpreference
                    SharedPreferences.Editor editor = myPreference.edit();
                    editor.putString("namePreference", username);
                    editor.commit();
                }
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    //Method to take image from camera and store it in the database
    public void onImageClicked(View view){
        //Create popmenu to ask user how to take image
        PopupMenu popup=new PopupMenu(this,view);

        popup.inflate(R.menu.image_action_menu);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.take_photo:               //Take image through camera

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);

                        break;
                    case R.id.pick_from_gallery:        //Pick Image from gallery

                        Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        intent2.setType("image/*");     //Only choose Images from Gallery

                        startActivityForResult(intent2, PICK_GALLERY_CODE);

                        break;
                    case R.id.remove_image:             //Remove Image

                        profileImage.setImageDrawable(getResources().getDrawable(R.drawable.user_default));

                        break;
                }
                return true;
            }
        });

        popup.show();

    }



    //Overridng method to capture image clicked by camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap image = null;
        ByteArrayOutputStream stream=new ByteArrayOutputStream();

        if(requestCode==REQUEST_CAMERA_CODE && resultCode==RESULT_OK){

            Bundle bundle=data.getExtras();
            image=(Bitmap)bundle.get("data");

            SharedPreferences.Editor editor = myPreference.edit();

            editor.putString("imagePreference", encodeTobase64(image));
            editor.commit();

            //Set image
            profileImage.setImageBitmap(image);

        }

        if(requestCode==PICK_GALLERY_CODE && resultCode==RESULT_OK){

            Uri selectedImageUri=data.getData();

            //Getting Bitmap image from URI
            try {
                image=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


            SharedPreferences.Editor editor = myPreference.edit();

            editor.putString("imagePreference", encodeTobase64(image));
            editor.commit();

            profileImage.setImageBitmap(image);
        }
    }

    //Method to open category for practice mode
    public void onPracticeModeClicked(View view){
        Intent intent=new Intent(this,PracticeActivity.class);
        startActivity(intent);
    }

    public void onChallengeModeClicked(View view)
    {
        Intent intent=new Intent(this,ChallengeActivity.class);
        startActivity(intent);

    }


    // Overriding onBackPressed() method so as to exit from app
    @Override
    public void onBackPressed () {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Exit");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want quit");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
       byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
       return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
