package com.example.todolist2025;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btnNewTask, btLogout;
    private ListView lvTasks;

    private ArrayList<Task> tasks;
    private TaskAdapter adapter; // task adapter
    private ActivityResultLauncher<Intent> launcher;
    public static final int ADD = 1;
    public static final int EDIT = 2;
    private int selectedTask; // editing task

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnNewTask = findViewById(R.id.btNewTask);
        btLogout = findViewById(R.id.btLogout);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Sign Out successful", Toast.LENGTH_LONG).show();
            }
        });
        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                //startActivity(intent);
                intent.putExtra("key_action", ADD);
                launcher.launch(intent);
            }
        });
        lvTasks = findViewById(R.id.lvTasks);

        tasks = new ArrayList<>();
        //initData();

        adapter = new TaskAdapter(tasks, MainActivity.this);
        lvTasks.setAdapter(adapter);


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if( result.getResultCode()==RESULT_OK)
                {
                    Intent data = result.getData();
                    String strContent = data.getStringExtra("key_content");
                    String strDeadline = data.getStringExtra("key_deadline");
                    boolean hiPriority = data.getBooleanExtra("key_hi", false);

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    int action = data.getIntExtra("key_action", -1);
                    if(action == ADD) {
                        if( strDeadline == null || strDeadline.length()==0)
                            strDeadline = " ";
                        Task t = new Task(strContent, strDeadline, hiPriority, "");
                        // Write a message to the database

                        DatabaseReference myRef = db.getReference(currentUser.getUid()).child("tasks").push();
                        t.setId(myRef.getKey());
                        myRef.setValue( t);

                        //tasks.add(t);
                    }
                    else if ( action == EDIT)
                    {
                        Task t = tasks.get(selectedTask);
                        t.setContent(strContent);
                        t.setPriority(hiPriority);
                        t.setDeadLine(strDeadline);


                        DatabaseReference myRef = db.getReference(currentUser.getUid()).child("tasks").child(t.getId());
                        myRef.setValue(t);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTask = position;
                Task t = tasks.get(position);
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("key_content", t.getContent() );

                intent.putExtra("key_hi", t.isPriority());
                intent.putExtra("key_deadline", t.getDeadLine());
                intent.putExtra("key_action", MainActivity.EDIT);

                launcher.launch(intent);
            }
        });
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference myRef = db.getReference(currentUser.getUid()).child("tasks").child(tasks.get(position).getId());
                myRef.removeValue();
                return true;
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference(currentUser.getUid()).child("tasks");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasks.clear();
                for(DataSnapshot snap: snapshot.getChildren())
                {
                    Task t = snap.getValue(Task.class);
                    tasks.add(t);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
//    // use for testing only
//    private void initData()
//    {
//        tasks.add(new Task("Buy milk", "", true));
//        tasks.add(new Task("call mother", "12/02/2025", false));
//        tasks.add(new Task("prepare to test", "25/02/2025", true));
//        tasks.add(new Task("stam", "", false));
//    }
}