package com.fehaja.connect4;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by fguo on 1/2/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    int mPixel;

    public ImageAdapter(Context c, int nPixel) {

        mContext = c;
        mPixel = nPixel;

    }

    public int getCount() {
        return 42;
        /*return mThumbIds.length;*/
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(148, 148));
            imageView.setLayoutParams(new GridView.LayoutParams(mPixel, mPixel));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
/*
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
*/

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        char c = cf.positionAt(position);
        //int nResourceId = R.drawable.aqua_square;

        imageView.setImageResource(R.drawable.biglight);

        switch (c) {
            case 'O':
            case 'o':
                //nResourceId = R.drawable.bigblue;
                Picasso.with(mContext)
                        .load(R.drawable.hanson)
                        .transform(new CropCircleTransformation())
                        .transform(new ColorFilterTransformation(Color.argb(128, 0, 0,255)))
                        .into(imageView);
                if ('o'==c)
                { YoYo.with(Techniques.Tada)
                        .duration(5000)
                        .playOn(imageView);}
                break;
            case 'X':case 'x':
                //nResourceId = R.drawable.bigred;
                Picasso.with(mContext)
                        .load(R.drawable.bella)
                        .transform(new CropCircleTransformation())
                        .transform(new ColorFilterTransformation(Color.argb(128, 255, 0,0)))
                        .into(imageView);
                if ('x'==c)
                { YoYo.with(Techniques.Tada)
                        .duration(5000)
                        .playOn(imageView);}

                break;
            case '-':
                //nResourceId = R.drawable.biglight;
                break;

        }

        //imageView.setImageResource(nResourceId);
//        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
    void reset (boolean bBlue)
    {
        cf = new ConnectFour(bBlue);
    }

    // references to our images

        ConnectFour cf = new ConnectFour();
    /*
    private Integer[] mThumbIds = {
            R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.red, R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.red,R.drawable.aqua_square,
            R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.blue, R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.blue,R.drawable.aqua_square,
            R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.red, R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.red,R.drawable.aqua_square,
            R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.blue, R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.blue,R.drawable.aqua_square,
            R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.red, R.drawable.aqua_square, R.drawable.aqua_square, R.drawable.red,R.drawable.aqua_square,
            R.drawable.aqua_square, R.drawable.red, R.drawable.blue, R.drawable.blue, R.drawable.red, R.drawable.blue,R.drawable.aqua_square,

    */        /*
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };*/
}
