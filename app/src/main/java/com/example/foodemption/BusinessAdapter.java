package com.example.foodemption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BusinessAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Business> mDataSource;

    public BusinessAdapter(Context context, ArrayList<Business> businessList) {
        mContext = context;
        mDataSource = businessList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1 Lets ListView know how many items to display, or in other words, it returns the size of your data source.
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2 Returns an item to be placed in a given position from the data source, specifically, Business objects
    // obtained from mDataSource.
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3 defines a unique ID for each row in the list. For simplicity, you just use the position of the item as its ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4 creates a view to be used as a row in the list. Here you define what information shows and where it sits
    // within the ListView. You also inflate a custom view from the XML layout defined in res/layout/list_item_business.xml
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_business, parent, false);

        // Get business name
        TextView tvAddress = rowView.findViewById(R.id.tvAddress);
        //TextView tvTitle = rowView.findViewById(com.example.foodemption.R.id.business_list_title);

        // Get discount start hour element
        TextView tvDiscountStartHour = rowView.findViewById(R.id.tvDiscountStartHour);

        // Get thumbnail element
        ImageView ivLogoThumbnail = rowView.findViewById(R.id.ivLogoThumbnail);

        // Get distance from user
        TextView tvDistanceFromUser = rowView.findViewById(R.id.tvDistanceFromUser);
        //tvDistanceFromUser.setVisibility(View.INVISIBLE);

        // 1
        Business business = (Business) getItem(position);

        // 2
        tvAddress.setText(business.getAddress() + "\n" + business.getCity());
        tvDiscountStartHour.setText("החל מ"+ "\n" +business.getDiscountStartHour());
        double d = business.getDistanceFromUserLocation()/1000.0;
        tvDistanceFromUser.setText(new DecimalFormat("#.#").format(d) + " ק\"מ");
        //new DecimalFormat("#.#").format(getDistanceFromUserLocation);

        // 3
        Picasso.get().load(business.getLogoURL()).into(ivLogoThumbnail);
        //Picasso.get().load("https://commondatastorage.googleapis.com/easy/images/UserPics/8824411_1524484858199.jpg").into(ivLogoThumbnail);

        return rowView;
    }
}
