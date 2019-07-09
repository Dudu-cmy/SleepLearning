package com.example.sleeplearning.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sleeplearning.R;

public class ViewPagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    Context context;

    public ViewPagerAdapter(Activity activity, int[] img) {
        this.context = activity;

    }

    public int image[] =
            {
                    R.drawable.power,
                    R.drawable.wifi,
                    R.drawable.speaker,

            };
    public String messages[] =
            {
              "Please make sure your phone is plugged in and charging",
                    "Check to see that you're connected to WiFi",
                    "Make sure your earbuds are plugged in and the volume is at a comfortable level"
            };
    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView trailing;
        inflater = (LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.layout_image, container,false);
        trailing = itemView.findViewById(R.id.image);
        trailing.setImageResource(image[position]);
        TextView txt = itemView.findViewById(R.id.txtmsg);
        txt.setText(messages[position]);
        ((ViewPager)container).addView(itemView);

        return itemView;
        //return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((RelativeLayout)object);

    }
}
