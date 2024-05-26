package com.example.eigthpracpart2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkInfo;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            OneTimeWorkRequest downloadWorkRequest = new OneTimeWorkRequest.Builder(DownloadImageWorker.class).build();
            WorkManager.getInstance(this).enqueue(downloadWorkRequest);

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadWorkRequest.getId())
                    .observe(this, workInfo -> {
                        if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            String imageUrl = workInfo.getOutputData().getString(DownloadImageWorker.IMAGE_URL_KEY);
                            if (imageUrl != null) {
                                Glide.with(this).load(imageUrl).into(imageView);
                            }
                        }
                    });
        });
    }
}
