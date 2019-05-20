package com.eci.cosw.easyaccess.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.activity.CodeGeneratorActivity;
import com.eci.cosw.easyaccess.model.Access;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.AccessViewHolder> {

    public List<Access> accesses;
    private String rol;
    private Context context;

    @Override
    public int getItemCount() {
        return accesses.size();
    }

    @NonNull
    @Override
    public AccessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AccessViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.access_row, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull AccessViewHolder accessViewHolder, int i) {
        String access = accesses.get(i).getQr();
        String[] details = access.split("-");
        accessViewHolder.name.setText(accesses.get(i).getTime());
        if (rol=="Company"){
            accessViewHolder.date.setText(accesses.get(i).getQr().split("-")[2]);
        }else{
            accessViewHolder.date.setText(accesses.get(i).getQr().split("-")[0]);
        }
        accessViewHolder.hour.setText(accesses.get(i).getDate());
        accessViewHolder.setQr(accesses.get(i).getQr());
    }

    public void updateAccesses(List<Access> accesses, String rol) {
        this.accesses = accesses;
        this.rol = rol;
        notifyDataSetChanged();
    }

    public RVAdapter(Context context){
        this.context = context;
    }



    class AccessViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        TextView name;
        TextView date;
        TextView hour;
        String qr;

        AccessViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.accessRow);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            hour = (TextView) itemView.findViewById(R.id.hour);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CodeGeneratorActivity.class);
                    intent.putExtra("QR", qr);
                    context.startActivity(intent);
                }
            });
        }

        public String getQr() {
            return qr;
        }

        public void setQr(String qr) {
            this.qr = qr;
        }

        public CardView getCardView() {
            return cardView;
        }
    }
}
