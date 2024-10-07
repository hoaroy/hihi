package com.example.Sachpee.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Sachpee.Model.Partner;
import com.example.Sachpee.R;

import java.util.Base64;
import java.util.List;

public class Partner_BookAdapter extends RecyclerView.Adapter<Partner_BookAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;
    List<Partner> list;
    public Partner_BookAdapter(List<Partner> list, ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner_book,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Partner partner = list.get(position);
        holder.namePartner_book.setText(partner.getNamePartner());
        holder.addressPartner_book.setText(partner.getAddressPartner());

        byte[] imgByte = Base64.getDecoder().decode(partner.getImgPartner());
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        holder.imgPartner_book.setImageBitmap(bitmap);

//        holder.cardView_partner_book.setOnClickListener(view -> {
////            Intent intent = new Intent(context, Food_Of_Partner_Activity.class);
////            context.startActivity(intent);
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView_partner_book;
        TextView namePartner_book, addressPartner_book;
        ImageView imgPartner_book;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView_partner_book = itemView.findViewById(R.id.cardView_partner_book);
            namePartner_book = itemView.findViewById(R.id.namePartner_book);
            addressPartner_book = itemView.findViewById(R.id.addressPartner_book);
            imgPartner_book = itemView.findViewById(R.id.imgPartner_book);



        }
    }
    public interface ItemClickListener{
        public void onItemClick(Partner partner);
    }

}
