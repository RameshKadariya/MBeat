package com.example.mbeat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutMe extends AppCompatActivity {
    ImageView imgInsta,imgTwitter,imgYoutube,imgFacebook;
    TextView txtEmail,txtWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_me);

        imgYoutube=findViewById(R.id.imgYoutube);
        imgFacebook=findViewById(R.id.imgFacebook);
        imgInsta=findViewById(R.id.imgInsta);
        imgTwitter=findViewById(R.id.imgTwitter);
        txtEmail=findViewById(R.id.txtEmail);
        txtWeb=findViewById(R.id.txtWeb);

        imgInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(android.net.Uri.parse("https://www.instagram.com/ramesh.kadariya/"));
                startActivity(intent);
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(android.net.Uri.parse("mailto:contact@rameshkadariya.com.np"));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        txtWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(android.net.Uri.parse("https://rameshkadariya.com.np"));
                startActivity(intent);
            }
        });

        imgYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(android.net.Uri.parse("https://www.youtube.com/@rameshkadariya4444"));
                startActivity(intent);
            }
        });

        imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(android.net.Uri.parse("https://www.facebook.com/rameshkadariya"));
                startActivity(intent);
            }
        });

        imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(android.net.Uri.parse("https://twitter.com/rameshkadariya"));
                startActivity(intent);
            }
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}