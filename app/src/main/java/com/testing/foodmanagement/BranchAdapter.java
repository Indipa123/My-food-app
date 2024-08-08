package com.testing.foodmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ViewHolder> {

    private List<Branch> branchList;

    public BranchAdapter(List<Branch> branchList) {
        this.branchList = branchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_branch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Branch branch = branchList.get(position);
        holder.branchNameTextView.setText(branch.getBranchName());
        holder.phoneTextView.setText(branch.getPhone());
        holder.emailTextView.setText(branch.getEmail());
        holder.openHoursTextView.setText(branch.getOpenHours());
        holder.locationTextView.setText(branch.getLocation());
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView branchNameTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView openHoursTextView;
        TextView locationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            branchNameTextView = itemView.findViewById(R.id.textBranchName);
            phoneTextView = itemView.findViewById(R.id.textPhone);
            emailTextView = itemView.findViewById(R.id.textEmail);
            openHoursTextView = itemView.findViewById(R.id.textOpenHours);
            locationTextView = itemView.findViewById(R.id.textLocation);
        }
    }
}
