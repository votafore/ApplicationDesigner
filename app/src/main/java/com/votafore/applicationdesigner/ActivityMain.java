package com.votafore.applicationdesigner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.votafore.applicationdesigner.controller.ProjectListManager;
import com.votafore.applicationdesigner.support.RecycleAdapter;

public class ActivityMain extends AppCompatActivity {

    RecyclerView mProjectList;
    ProjectListManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = ProjectListManager.getInstance(getApplicationContext());
        manager.getAdapter().setOnClickListener(new RecycleAdapter.OnItemClickListener() {
            @Override
            public void onClick(int id) {
                Intent i = new Intent(getApplicationContext(), ActivityProject.class);
                i.putExtra("ProjectID",id);
                startActivity(i);
            }

            @Override
            public void onLongClick(int position) {
                manager.deleteProject(position);
            }
        });

        mProjectList = (RecyclerView)findViewById(R.id.project_list);

        mProjectList.setAdapter(manager.getAdapter());
        mProjectList.setLayoutManager(new LinearLayoutManager(this));
        mProjectList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.create_project:
                manager.createProject("new project");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
