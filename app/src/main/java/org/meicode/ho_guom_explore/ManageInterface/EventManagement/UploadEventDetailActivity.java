package org.meicode.ho_guom_explore.ManageInterface.EventManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.meicode.ho_guom_explore.ManageInterface.CuisineAndAccommodationManagement.UpdateActivity;
import org.meicode.ho_guom_explore.ManageInterface.CuisineAndAccommodationManagement.UploadCuisineAndAccommodation;
import org.meicode.ho_guom_explore.ManageInterface.CuisineAndAccommodationManagement.UploadDetailActivity;
import org.meicode.ho_guom_explore.R;

public class UploadEventDetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLocation;

    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key ="";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        detailLocation = findViewById(R.id.detailLocation);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLocation.setText(bundle.getString("Location"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(UploadEventDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), EventManagementActivity.class));
                        finish();
                    }
                });



            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadEventDetailActivity.this, UpdateEventActivity.class)
                        .putExtra("Title", detailTitle.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Location", detailLocation.getText().toString())
                        .putExtra("StartDate", bundle.getString("StartDate"))
                        .putExtra("EndDate", bundle.getString("EndDate"))
                        .putExtra("Image", imageUrl)
                        .putExtra("Key", key);

                startActivity(intent);
            }
        });
    }
}