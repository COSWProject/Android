package com.eci.cosw.easyaccess.activity;

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

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PostFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Post p;
    private View v;



    private String ide;

    public PostFragment() {

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


        return this.v;
    }


}
