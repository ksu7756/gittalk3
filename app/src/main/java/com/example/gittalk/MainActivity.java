package com.example.gittalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.rahul.githuboauth.GithubAuthenticator;
import com.github.rahul.githuboauth.SuccessCallback;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.internal.FederatedSignInActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder> mFirebaseAdapter;

    public static final String MESSAGES_CHILD = "messages";
    private DatabaseReference mFIrebaseDatebaseReference;
    private EditText mMessageEditText;

    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 객체
    private FirebaseUser mFirebaseUser; //파이어베이스 유저 인증되면 객체획득
    private GoogleApiClient mGoogleApiClients;

    private String mUsername; // 이름저장 변수
    private String mphotoUrl; // 프로필사진저장 변수

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        ImageView messageImageView;
        TextView messageTextView;
        CircleImageView photoImageView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageImageView = itemView.findViewById(R.id.messageImageview);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            photoImageView = itemView.findViewById(R.id.photoImageview);
        }//생성자
    }
    private RecyclerView mMessageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFIrebaseDatebaseReference = FirebaseDatabase.getInstance().getReference();
        mMessageEditText = findViewById(R.id.message_edit);
        mMessageRecyclerView = findViewById(R.id.message_recycler_view);

        findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChatMessage chatMessage = new ChatMessage(mMessageEditText.getText().toString(),
                        mUsername, mphotoUrl, null);
                mFIrebaseDatebaseReference.child(MESSAGES_CHILD)
                        .push()
                        .setValue(chatMessage);
                mMessageEditText.setText("");
            }
        });

        mGoogleApiClients = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser(); //로그인시 유저객체획득
        if(mFirebaseUser == null) {
            startActivity(new Intent(this, signin.class));
            finish(); //현재화면 닫음
            return;
        }else{
            mUsername = mFirebaseUser.getDisplayName();
            if(mFirebaseUser.getPhotoUrl() != null){
                mphotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        Query query = mFIrebaseDatebaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query,ChatMessage.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, ChatMessage model) {
                    holder.messageTextView.setText(model.getText());
                    holder.nameTextView.setText(model.getName());
                    if(model.getPhtoUrl() == null){
                        holder.photoImageView.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,
                                R.drawable.ic_account_circle_black_24dp));
                    }else{
                        Glide.with(MainActivity.this)
                                .load(model.getPhtoUrl())
                                .into(holder.photoImageView);
                    }
            }

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message,parent,false);

                return new MessageViewHolder(view);
            }
        };
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClients);
                mUsername = "";
                startActivity(new Intent(this,signin.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
