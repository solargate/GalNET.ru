package ru.solargateteam.galnetru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

import ru.solargateteam.galnetru.db.DBItem;
import ru.solargateteam.galnetru.pref.PrefEngine;
import ru.solargateteam.galnetru.util.Util;

public class PostFragment extends Fragment {

    public static final String PARAM_ITEM = "ru.solargateteam.galnetru.param.PARAM_ITEM";

    TextView tvTitle;
    TextView tvPubDate;
    ImageView ivImage;
    TextView tvDescription;

    /*
    DBItem item;

    public void setItem (DBItem item) {
        this.item = item;
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_fragment, null);

        Bundle bundle = this.getArguments();

        DBItem item = (DBItem) bundle.getSerializable(PARAM_ITEM);

        if (item != null) {

            tvTitle = (TextView) v.findViewById(R.id.tv_post_title);
            tvPubDate = (TextView) v.findViewById(R.id.tv_post_pubdate);
            ivImage = (ImageView) v.findViewById(R.id.iv_post_image);
            tvDescription = (TextView) v.findViewById(R.id.tv_post_description);

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

            PrefEngine pe = new PrefEngine(getActivity());
            if (pe.useGalNETFont()) {
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(), Global.FONT_JURA_BOLD);
                tvTitle.setTypeface(face);
                tvDescription.setTypeface(face);
            }
        }

        return v;
    }
}
