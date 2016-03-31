package ru.solargateteam.galnetru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

import ru.solargateteam.galnetru.db.DBItem;
import ru.solargateteam.galnetru.pref.PrefEngine;
import ru.solargateteam.galnetru.util.Util;

public class PostActivity extends AppCompatActivity {

    public static final String PARAM_ITEM = "ru.solargateteam.galnetru.param.PARAM_ITEM";

    TextView tvTitle;
    TextView tvPubDate;
    ImageView ivImage;
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        DBItem item = (DBItem) getIntent().getSerializableExtra(PARAM_ITEM);

        tvTitle = (TextView) findViewById(R.id.tv_post_title);
        tvPubDate = (TextView) findViewById(R.id.tv_post_pubdate);
        ivImage = (ImageView) findViewById(R.id.iv_post_image);
        tvDescription = (TextView) findViewById(R.id.tv_post_description);

        tvTitle.setText(Util.strProcessHTML(item.getTitle()));

        tvPubDate.setText(Util.makeDateStringFromUnixTime(item.getPubDate()));

        if (item.getImagePath() != null) {
            try {
                File imageFile = new File(item.getImagePath(), item.getId() + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                ivImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ivImage.setImageResource(R.drawable.image_no_banner);
        }

        tvDescription.setText(Util.strProcessHTML(item.getDescription()));

        PrefEngine pe = new PrefEngine(getApplicationContext());
        if (pe.useGalNETFont()) {
            Typeface face = Typeface.createFromAsset(getAssets(), Global.FONT_JURA_BOLD);
            tvTitle.setTypeface(face);
            tvDescription.setTypeface(face);
        }
    }
}
