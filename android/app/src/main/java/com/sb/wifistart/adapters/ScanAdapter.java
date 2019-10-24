package com.sb.wifistart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sb.wifistart.R;
import com.sb.wifistart.dto.Scan;

import java.util.List;

public class ScanAdapter extends RecyclerView.Adapter<ScanAdapter.ScanHolder> {

    Context context;
    List<Scan> scans;

    public ScanAdapter(Context context, List<Scan> scans) {
        this.context = context;
        this.scans = scans;
    }

    @Override
    public int getItemCount() {
        return scans.size();
    }

    @Override
    public ScanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.scan_item,null);
        return new ScanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanHolder holder, int position) {
        Scan scan = scans.get(position);

        holder.scanInfoLayout.findViewById().setText(scan.hashCode());
        holder.button.setOnClickListener(view -> {
            if(holder.visible) {
                holder.scanPoints.setVisibility(View.GONE);
            } else {
                holder.scanPoints.setVisibility(View.VISIBLE);
            }
            holder.visible = !holder.visible;
        });

        holder.scanPoints.setLayoutManager(new LinearLayoutManager(context));
        holder.scanPoints.setAdapter(new PointAdapter(scan.getPoints(), context));
//        Log.i("dataaa",nameDetails.getName());
    }

    class ScanHolder extends RecyclerView.ViewHolder
    {
        LinearLayout scanInfoLayout;
        RecyclerView scanPoints;
        boolean visible;

        public ScanHolder(View itemView) {
            super(itemView);
            this.scanInfoLayout = (LinearLayout) itemView.findViewById(R.id.scanItem);
            this.scanPoints = (RecyclerView) itemView.findViewById(R.id.scanPoints);
        }
    }
}
