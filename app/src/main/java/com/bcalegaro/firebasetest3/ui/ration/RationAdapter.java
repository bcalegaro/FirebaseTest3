package com.bcalegaro.firebasetest3.ui.ration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcalegaro.firebasetest3.R;
import com.bcalegaro.firebasetest3.data.Ration;

import java.util.List;

public class RationAdapter  extends RecyclerView.Adapter<RationAdapter.RationViewHolder> {
    public class RationViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameItemView;
        private final TextView quantityItemView;
        private final TextView timestampItemView;


        private RationViewHolder(View itemView) {
            super(itemView);
            nameItemView = itemView.findViewById(R.id.nameTextView);
            quantityItemView = itemView.findViewById(R.id.quantityTextView);
            timestampItemView = itemView.findViewById(R.id.timestampTextView);
        }
    }

    private List<Ration> mRations; //cached copy

    //construtor
    public RationAdapter(){}

    @NonNull
    @Override
    public RationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //infla o layout custom
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ration_item, parent, false);
        return new RationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RationViewHolder holder, int position) {
        if (mRations != null){
            Ration current = mRations.get(position);
            holder.nameItemView.setText(current.name);
            holder.quantityItemView.setText(String.valueOf(current.quantity));
            holder.timestampItemView.setText(current.timestamp);
        }
    }

    public void setmRations(List<Ration> rations){
        mRations = rations;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRations != null)
            return mRations.size();
        else return 0;
    }
}
