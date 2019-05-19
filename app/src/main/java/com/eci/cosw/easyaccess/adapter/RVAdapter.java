package com.eci.cosw.easyaccess.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.Access;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.AccessViewHolder> {

    public List<Access> accesses;

    public RVAdapter(List<Access> accesses)  {
        this.accesses = accesses;
    }

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
        accessViewHolder.getTextView().setText(accesses.get(i).getTime());
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class AccessViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        AccessViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.accessRow);


        }

        public TextView getTextView() {
            return textView;
        }
    }
}
