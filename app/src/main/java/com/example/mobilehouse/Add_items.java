package com.example.mobilehouse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_items extends AppCompatActivity {

    EditText itemName;
    EditText itemPrice;
    EditText itemNote;
    ImageButton imageButton;
    Button addItem;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStotage;

    private static final  int Gallery_Code=1;
    Uri imageUrl=null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        getSupportActionBar().hide();


        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("items");
        mStotage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        itemName = (EditText) findViewById(R.id.et_additem_name);
        itemPrice = (EditText) findViewById(R.id.et_additem_Price);
        itemNote = (EditText) findViewById(R.id.et_additem_about);
        addItem = (Button) findViewById(R.id.bt_additem_confirm);
        imageButton = (ImageButton) findViewById(R.id.addimage);


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Code && requestCode == RESULT_OK){
            imageUrl= data.getData();
            imageButton.setImageURI(imageUrl);
        }
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = itemName.getText().toString().trim();
                String price = itemPrice.getText().toString().trim();
                String note = itemNote.getText().toString().trim();

                if (!name.isEmpty()|| !price.isEmpty() || !note.isEmpty()  || imageUrl!=null){
                    progressDialog.setTitle("Uploading....");
                    progressDialog.show();
                    imageUrl= data.getData();

                    StorageReference filepath=mStotage.getReference().child("images").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t=task.getResult().toString();
                                    DatabaseReference newPost=mRef.push();

                                    newPost.child("itemName").setValue(name);
                                    newPost.child("itemPrice").setValue(price);
                                    newPost.child("itemNote").setValue(note);
                                    newPost.child("image").setValue(task.getResult().toString());
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), View_Additems.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    });
                }


            }

        });

    }
}



