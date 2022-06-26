package com.sml.cowematest.activitie.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.sml.cowematest.R;
import com.sml.cowematest.databinding.ActivityDetailBinding;
import com.sml.cowematest.util.Fkey;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initToolbar();
    }
    void initView(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String title=bundle.getString(Fkey.TITLE);
            String url=bundle.getString(Fkey.URL);
            Picasso.with(this)
                    .load(url)
                    .placeholder(androidx.cardview.R.color.cardview_dark_background)
                    .into(binding.image);
            binding.title.setText(title);
        }
    }
    void initToolbar(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}