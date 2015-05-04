package com.example.aaron.welcomeActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customExpenseAdapter extends ArrayAdapter<Expense> {

    public customExpenseAdapter(Context context, ArrayList<Expense> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data for the item at this position
        Expense item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_view, parent, false);
        }
        // Set the data into the textViews
        TextView nameBox = (TextView) convertView.findViewById(R.id.name);
        TextView aisleBox = (TextView) convertView.findViewById(R.id.aisleBox);
        TextView priceBox = (TextView) convertView.findViewById(R.id.price);
        //We are going to set the colors of these
        nameBox.setTextColor(Color.BLACK);
        priceBox.setTextColor(Color.BLACK);
        nameBox.setText(item.getName());
        String price = String.format("%.2f", item.getCurrentExpense());
        price = "$" + price;
        priceBox.setText(price);
        return convertView;
    }
}
