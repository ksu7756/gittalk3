package com.example.gittalk;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahul.githuboauth.GithubAuthenticator;
import com.github.rahul.githuboauth.SuccessCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.internal.FederatedSignInActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 객체
    private FirebaseUser mFirebaseUser; //파이어베이스 유저 인증되면 객체획득

    private String mUsername; // 이름저장 변수
    private String mphotoUrl; // 프로필사진저장 변수

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        ImageView messageImageView;
        TextView messageTextView;
        CircleImageView photoTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            messageImageView = itemView.findViewById(R.id.messageImageview);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            photoTextView = itemView.findViewById(R.id.photoiImageview);
        }//생성자
    }
    private RecyclerView mMessageRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageRecyclerView = findViewById(R.id.message_recycler_view);

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
    }
}
