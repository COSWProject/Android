package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.google.firebase.codelab.mlkit.TextRecognition;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class PostFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Post p;
    private View v;

    private RetrofitHttp retrofitHttp;
    private UserService userService;
    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);




    public PostFragment() { }

    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public Post getP() {
        return p;
    }

    public void setP(Post p) {
        this.p = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_post, container, false);

        ImageView imageView = v.findViewById(R.id.imageView4);
        Bitmap b = this.p.getImageUri();
        imageView.setImageBitmap(b);

        TextRecognition textRecognition = new TextRecognition();

        textRecognition.runTextRecognition(b);
        
        TextView textView = v.findViewById(R.id.textView4);
        final String name =textRecognition.getName();
        textView.setText(name);

        TextView textView_one = v.findViewById(R.id.textView5);
        final String ide =textRecognition.getIdentification();
        textView_one.setText(ide);

        TextView textView_two = v.findViewById(R.id.textView3);
        final String mail =this.p.getEmail();
        textView_two.setText(mail);

        TextView textView_three = v.findViewById(R.id.textView6);
        final String phone =this.p.getPhone();
        textView_three.setText(phone);

        TextView textView_four = v.findViewById(R.id.textView7);
        final String country =this.p.getCountry();
        textView_four.setText(country);

        final String pass = this.p.getPassword();

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                User user;

                user = new User(ide,name,mail, pass, Integer.parseInt(phone), country, "User");

                try {
                    Response<ResponseBody> userResponse = userService.createUser(user).execute();

                } catch (IOException e) {

                }
            }
        });


        return this.v;
    }


}
