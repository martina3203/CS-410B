package com.example.aaron.welcomeActivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jeremy on 3/6/2015.
 */
public class AdapterBudget extends ArrayAdapter<budget>
{
    private Activity activity;
    private ArrayList<budget> lBudget;
    private static LayoutInflater inflater = null;

    public AdapterBudget(Activity activity, int textViewResourceId, ArrayList<budget> _lBudget)
    {
        super(activity, textViewResourceId, _lProducts);
        try{
            this.activity = activity;
            this.lBudget = _lBudget;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e)
        {

        }
    }

    public int getCount() {
        return lBudget.size();
    }

    public Product getItem(Product position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        final ViewHolder holder;
        try{
            if(convertView == null){
                vi = inflater.inflate(R.layout.yourlayout, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.display_name);
                holder.display_number = (TextView) vi.findViewById(R.id.display_number);
            }catch (Exception e){

            }
            return vi;

        }
    }
}


