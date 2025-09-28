package com.example.todolist2025;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {
    private ArrayList<Task> objects;
    private Context context;

    public TaskAdapter(ArrayList<Task> objects, Context context) {
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        if( v == null)
            v = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item,parent, false );
        TextView tvContent = v.findViewById(R.id.tvContent);
        TextView tvDeadLine = v.findViewById(R.id.tvDeadline);
        TextView tvPriority = v.findViewById(R.id.tvPriority);
        Task t = objects.get(position);
        tvContent.setText(t.getContent());
        tvDeadLine.setText(t.getDeadLine());
        if( t.isPriority())
        {
            tvPriority.setBackgroundColor(Color.RED);
            tvPriority.setText("!");
        }
        else {
            tvPriority.setBackgroundColor(Color.parseColor("#9DC3E1"));
            tvPriority.setText("");
        }

        return v;
    }
}
