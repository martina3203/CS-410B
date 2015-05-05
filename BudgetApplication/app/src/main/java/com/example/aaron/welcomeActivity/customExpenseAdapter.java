package com.example.aaron.welcomeActivity;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_custom_view, parent, false);
        }
        // Set the data into the textViews
        TextView nameBox = (TextView) convertView.findViewById(R.id.name);
        TextView aisleBox = (TextView) convertView.findViewById(R.id.aisle);
        TextView priceBox = (TextView) convertView.findViewById(R.id.price);
        //We are going to set the colors of these
        nameBox.setTextColor(Color.BLACK);
        priceBox.setTextColor(Color.BLACK);
        aisleBox.setTextColor(Color.BLACK);
        //Fill the boxes
        nameBox.setText(item.getName());
        if (item.getAisle() != 0)
        {
            aisleBox.setText("Aisle " + item.getAisle());
        }
        else
        {
            aisleBox.setText("No Aisle Given");
        }
        String price = String.format("%.2f", item.getCurrentExpense());
        price = "$" + price;
        priceBox.setText(price);
        return convertView;
    }
}
