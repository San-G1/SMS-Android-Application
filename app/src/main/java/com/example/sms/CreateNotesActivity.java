package com.example.sms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;

import org.w3c.dom.Document;

import java.time.LocalDate; // import the LocalDate class
import java.util.HashMap;
import java.util.Map;

public class CreateNotesActivity extends AppCompatActivity {

    EditText mcreatetitleofnote,mcreatecontentofnote;
    FloatingActionButton msavenote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private String uname;

    ImageView back_btn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        Intent i = getIntent();
        uname = i.getStringExtra("uname");

        msavenote = findViewById(R.id.savenote);
        mcreatecontentofnote = findViewById(R.id.createcontentofnote);
        mcreatetitleofnote = findViewById(R.id.createtitleofnote);
        back_btn = (ImageView) findViewById(R.id.addNote_back);

        progressBar = (ProgressBar) findViewById(R.id.progressBarOfCreateNote);

//        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatetitleofnote.getText().toString();
                String content = mcreatecontentofnote.getText().toString();
                if (title.isEmpty() || content.isEmpty())
                {
                    TastyToast.makeText(CreateNotesActivity.this, "Both fields are required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

//                    Toast.makeText(CreateNotesActivity.this, "Both fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(uname).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content",content );

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aUnused) {
                            TastyToast.makeText(CreateNotesActivity.this, "Note Created Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            TastyToast.makeText(CreateNotesActivity.this, "Failed to Create Note", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                        }
                    });
                }
            }
        });

    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId()==android.R.id.home)
//        {
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}