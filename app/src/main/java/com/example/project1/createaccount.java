package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class createaccount extends AppCompatActivity {
    TextView txt_signin;
    ImageFilterView add_picture;
    ShapeableImageView img;
    int video_source =3;
    private String currentphotopath;
    Button button_create;
    StorageReference storageRef ;
    DatabaseReference ref ;
    Uri dat;
    EditText txt_nom;
    EditText txt_cin;
    EditText txt_tele;
    EditText txt_email;
    EditText txt_password;
    EditText txt_password2;
    private StorageTask mUploadTask;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createaccount);
        txt_nom=findViewById(R.id.txt_nom);
        txt_cin=findViewById(R.id.txt_cin);
        txt_tele=findViewById(R.id.txt_tele);
        txt_email=findViewById(R.id.txt_email);
        txt_password=findViewById(R.id.txt_password);
        txt_password2=findViewById(R.id.txt_password2);

        storageRef = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference("Comptes");

        txt_signin=findViewById(R.id.txt_signin);
        add_picture=findViewById(R.id.add_picture);
        button_create=findViewById(R.id.button_create);
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(txt_nom.getText().toString())){txt_nom.setError("required nom");}
                else txt_nom.setError(null);
                if(TextUtils.isEmpty(txt_cin.getText().toString())) txt_cin.setError("required cin");
                else txt_cin.setError(null);
                if(!(txt_tele.length() == 14)) txt_tele.setError("format indefini");
                else txt_tele.setError(null);
                if(!Patterns.EMAIL_ADDRESS.matcher(txt_email.getText().toString()).matches()){txt_email.setError("Email badly formated");}
                else txt_email.setError(null);
                if(TextUtils.isEmpty(txt_password.getText().toString())) txt_password.setError("required password");
                else txt_password.setError(null);
                if(!txt_password2.getText().toString().equalsIgnoreCase(txt_password.getText().toString())) txt_password2.setError("corriger le mot de passe");

                if(!TextUtils.isEmpty(txt_password2.getText().toString())) ;
                else txt_password2.setError("doen't match with origin password");

                if(!TextUtils.isEmpty(txt_nom.getText().toString())&&
                        !TextUtils.isEmpty(txt_cin.getText().toString())&&
                        !TextUtils.isEmpty(txt_password.getText().toString())&&
                        !TextUtils.isEmpty(txt_password2.getText().toString())&&
                        txt_password2.getText().toString().equalsIgnoreCase(txt_password.getText().toString())&&
                        txt_tele.length() == 14&&
                        Patterns.EMAIL_ADDRESS.matcher(txt_email.getText().toString()).matches()){
                    if(dat!=null){
                        upload_img(dat);
                    }
                    else{
                        AlertDialog al = new AlertDialog.Builder(createaccount.this).setTitle("Picture").setMessage("Do you want to continu without picture").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dat = Uri.parse("android.resource://com.example.project1/drawable/profil_img");
                                upload_img(dat);
                            }
                        }).show();
                    }
                }


            }
        });
        img=findViewById(R.id.img);
        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setType("image/*");
                it.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(it,1);
            }
        });
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final EditText phone_number = (EditText) findViewById(R.id.txt_tele);

        phone_number.setText("+212 ");
        Selection.setSelection(phone_number.getText(), phone_number.getText().length());
        phone_number.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+212 ")){
                    phone_number.setText("+212 ");
                    Selection.setSelection(phone_number.getText(), phone_number.getText().length());

                }

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){
            if(requestCode ==1 && data!=null && data.getData()!=null){
                dat =data.getData();
                img.setImageURI(dat);

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void upload_img(Uri dat) {
        StorageReference fileReference = storageRef.child(txt_cin.getText().toString()+"." + getFileExtension(dat));
        Dialog dialog = new Dialog(this);
        mUploadTask =fileReference.putFile(dat).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        others(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(createaccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(createaccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_image_upload);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();


    }
    private void others(String s) {
        String tele ="";
        for (int i =0 ; i <= txt_tele.getText().length()-1;i++){
            if(i>=5){
                tele+=txt_tele.getText().charAt(i);
            }
        }
        ref.child(String.valueOf(txt_cin.getText())).child("email").setValue(txt_email.getText().toString());
        ref.child(String.valueOf(txt_cin.getText())).child("password").setValue(txt_password.getText().toString());
        ref.child(String.valueOf(txt_cin.getText())).child("nom").setValue(txt_nom.getText().toString());
        ref.child(String.valueOf(txt_cin.getText())).child("tele").setValue(tele.toString());
        ref.child(String.valueOf(txt_cin.getText())).child("ref").setValue(s.toString());
        ref.child(String.valueOf(txt_cin.getText())).child("anniv").setValue("");
        ref.child(String.valueOf(txt_cin.getText())).child("adresse").setValue("");
        ref.child(String.valueOf(txt_cin.getText())).child("sexe").setValue("");
        ref.child(String.valueOf(txt_cin.getText())).child("ville").setValue("");
        Dialog dialog = new Dialog(createaccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.created);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btn_ok = dialog.findViewById(R.id.button_ok_created);
        dialog.show();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}