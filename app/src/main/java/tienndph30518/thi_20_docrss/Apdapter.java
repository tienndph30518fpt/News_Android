package tienndph30518.thi_20_docrss;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Apdapter extends RecyclerView.Adapter<Apdapter.MyViewHover> {
    Context context;
    ArrayList<Item> list ;


    public Apdapter(Context context, ArrayList<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHover onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.item_hienthi, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_hienthi2, parent, false);
        }

        return new MyViewHover(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHover holder, @SuppressLint("RecyclerView") int position) {

        Item item = list.get(position);
        if (position%2==1){
            holder.mainView.setCardBackgroundColor(Color.GREEN);
        }else {
            holder.mainView.setCardBackgroundColor(Color.WHITE);
        }
        holder.idTitlle.setText(list.get(position).getTitle());
        holder.idNoidung.setText(list.get(position).getDescription());
        holder.idthoigian.setText(list.get(position).getPubData());
        Picasso.get().load(list.get(position).getImgAvata()).into(holder.imgAvata);
        holder.imgTrangthai.setImageResource(R.drawable.traitimm);
        if (position %2==0){
            holder.idTitlle.setTextColor(Color.GREEN);
        }else {
            holder.idTitlle.setTextColor(Color.BLACK);
        }



        if (isCheckMang()) {
            Picasso.get().load(list.get(position).getImgAvata()).into(holder.imgAvata);
        } else {
            holder.imgAvata.setImageResource(R.drawable.ic_frag_news);
        }
        if (item.getTrangThai() == 0) {
            holder.imgTrangthai.setImageResource(R.drawable.heart_icon);
        } else {
            holder.imgTrangthai.setImageResource(R.drawable.heart_icon1);
        }


        holder.imgTrangthai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int trangthai = item.getTrangThai();
                if (trangthai == 0) {
                    trangthai = 1;
                } else {
                    trangthai = 0;
                }


                item.setTrangThai(trangthai);
                DB_XML db_xml = new DB_XML(context);
                db_xml.updata(item.getId(), trangthai);
//                if (trangthai == 0) {
//                    holder.imgTrangthai.setImageResource(R.drawable.traitimm);
//
//                } else {
//                    holder.imgTrangthai.setImageResource(R.drawable.heart_icon1);
//
//                }

                 list = db_xml.getAllDS();
                notifyDataSetChanged();
                db_xml.close();


                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isConnected()){
                }else {
                    Collections.sort(list, new Comparator<Item>() {
                        @Override
                        public int compare(Item item1, Item item2) {
                            return Integer.compare(item2.getTrangThai(), item1.getTrangThai());
                        }
                    });
                }
            }
        });
        String link = item.getLink();

        holder.idTitlle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckMang()){
                    openWebView(link);
                }else {
                    DialogIntenet();
                }

            }
        });
        holder.idNoidung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckMang()){
                    openWebView(link);
                }else {
                    DialogIntenet();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }
    public int getItemViewType(int position) {
        return position % 2; // Trả về 0 cho vị trí lẻ và 1 cho vị trí chẵn
    }
    class MyViewHover extends RecyclerView.ViewHolder {
        TextView idTitlle, idNoidung, idthoigian;
        ImageView imgAvata, imgTrangthai;
        CardView mainView;

        public MyViewHover(@NonNull View itemView) {
            super(itemView);
            idTitlle = itemView.findViewById(R.id.idTitlle);
            idNoidung = itemView.findViewById(R.id.idNoidung);
            idthoigian = itemView.findViewById(R.id.idngaythang);
            imgAvata = itemView.findViewById(R.id.imgView);
            imgTrangthai = itemView.findViewById(R.id.imgTrangThai);
            mainView = itemView.findViewById(R.id.main_id);

        }
    }


    // check mạng
    public boolean isCheckMang() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    // Dialog để cho người dùng bật mạng
    public void DialogIntenet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông Báo");
        builder.setMessage("Bạn Đang Trong Chế Độ off like. Vui lòng Bật Mạng");
        builder.setPositiveButton("Bật Mạng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                batMangWiFi();
            }
        }).setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }


    // Di chuyển đến nơi bật mạng
    public void batMangWiFi(){
        Intent intent  =new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    // chuyển sang web
    public void openWebView(String url){
        Intent intent = new Intent(context,MainActivity_Web.class);
        intent.putExtra(MainActivity_Web.EXTRA_URL,url);
        context.startActivity(intent);
    }

}
