package com.testing.foodmanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewBranchesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBranches;
    private BranchAdapter branchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_branches);

        recyclerViewBranches = findViewById(R.id.recyclerViewBranches);
        recyclerViewBranches.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(this);
        List<Branch> branchList = dbHelper.getAllBranches();

        if (branchList != null && !branchList.isEmpty()) {
            branchAdapter = new BranchAdapter(branchList);
            recyclerViewBranches.setAdapter(branchAdapter);
        } else {
            Toast.makeText(this, "No branches found", Toast.LENGTH_SHORT).show();
        }
    }
}
