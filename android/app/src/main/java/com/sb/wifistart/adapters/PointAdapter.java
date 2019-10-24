package com.sb.wifistart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sb.wifistart.R;
import com.sb.wifistart.dto.Point;

import java.util.List;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.PointHolder> {

    List<Point> points;
    Context context;

    public PointAdapter(List<Point> points, Context context) {
        this.points = points;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    @NonNull
    @Override
    public PointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.point_item,null);
        return new PointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointHolder holder, int position) {
        Point point = points.get(position);

        holder.name.setText(point.getName());
        holder.begin.setText(point.getBegin());
        holder.end.setText(point.getEnd());
    }

    class PointHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView begin;
        TextView end;

        public PointHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.pointName);
            this.begin = itemView.findViewById(R.id.pointBegin);
            this.end = itemView.findViewById(R.id.pointEnd);
        }
    }
}
