package com.example.orange_toolz_url;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class MainActivity extends AppCompatActivity {

    ProgressBar progress;
    TextView output;
    EditText link;
    TextView enter;


    public void Execute(String mylink){

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(mylink))
                .setDomainUriPrefix("https://orangetoolz.page.link")
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            String URL = shortLink.toString();
                            output.setText(URL);
                            progress.setVisibility(View.INVISIBLE);
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("URL Copied", URL);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(MainActivity.this, "Shorted URL copied on clipboard", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Enter a valid or new URL", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
        output = findViewById(R.id.output);
        link = findViewById(R.id.link);
        enter = findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                String linked = link.getText().toString();

                if(linked.equals("")){
                    Toast.makeText(MainActivity.this, "Entar a link", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                }else {

                    Execute(linked);

                }
            }
        });
    }
}
