package com.ar.azurewebservice.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.azurewebservice.R;
import com.ar.azurewebservice.model.TPromo;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromoDetailActivity extends BaseActivty {

    @BindView(R.id.img) ImageView img;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.desc) TextView desc;

    private TPromo tPromo;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_detail);

        extras = getIntent().getExtras();
        tPromo = (TPromo) extras.getSerializable("data");

        ButterKnife.bind(this);

        Glide.with(this).
                load(tPromo.getImage()).
                centerCrop().
                crossFade().
                into(img);

        title.setText(tPromo.getTitle());
        desc.setText(tPromo.getDescription());

    }

}
