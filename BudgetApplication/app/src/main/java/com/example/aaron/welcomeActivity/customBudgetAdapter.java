package com.example.aaron.welcomeActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aaron on 5/4/2015.
 */
public class customBudgetAdapter extends ArrayAdapter<Budget> {

        public customBudgetAdapter(Context context, ArrayList<Budget> items)
        {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data for the item at this positio
            // Check if an existing view is being reused, otherwise inflate the view
            Budget theBudget = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.budget_custom_view, parent, false);
            }
            // Set the data into the textViews
            TextView nameBox = (TextView) convertView.findViewById(R.id.nameField);
            TextView totalBudgetBox = (TextView) convertView.findViewById(R.id.totalBudgetField);
            //We are going to set the colors of these
            nameBox.setTextColor(Color.BLACK);
            totalBudgetBox.setTextColor(Color.BLACK);
            //Fill the boxes
            nameBox.setText(theBudget.getName());
            String price = String.format("%.2f", theBudget.getMaxValue());
            price = "$" + price;
            totalBudgetBox.setText(price);
            return convertView;
        }
}

