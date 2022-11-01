package com.example.cs315_a9_ncm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private FirebaseListAdapter<ChatMessage> adapter;

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        if (currentUser == null)
        {
            Toast.makeText(getApplicationContext(), "WHO IS THIS?!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Welcome back, " + userDisplayName(), Toast.LENGTH_LONG).show();

            displayChatMessages();
        }

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(view ->
        {
            EditText input = findViewById(R.id.input);

            if(input.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Cannot send an empty message", Toast.LENGTH_LONG).show();
            }
            else
            {
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(), userDisplayName()));
                input.setText("");
            }
        });
    }

    private void displayChatMessages()
    {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>().setQuery(FirebaseDatabase.getInstance().getReference(), ChatMessage.class).setLayout(R.layout.message).build();

        adapter = new FirebaseListAdapter<ChatMessage>(options)
        {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position)
            {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(currentUser != null)
        {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(currentUser != null)
        {
            adapter.stopListening();
        }
    }

    public void performLogout(View v)
    {
        FirebaseAuth.getInstance().signOut();

        Toast.makeText(this, "User logged out", Toast.LENGTH_LONG).show();

        recreate();
    }

    private String userDisplayName()
    {
        String displayThisName;

        if (currentUser == null)
        {
            displayThisName = "ERROR";
        }
        else if (!Objects.requireNonNull(currentUser.getDisplayName()).isEmpty())
        {
            displayThisName = currentUser.getDisplayName();
        }
        else if (!Objects.requireNonNull(currentUser.getEmail()).isEmpty())
        {
            displayThisName = currentUser.getEmail();
        }
        else
        {
            displayThisName = "Data exists but is blank";
        }

        return displayThisName;
    }
}