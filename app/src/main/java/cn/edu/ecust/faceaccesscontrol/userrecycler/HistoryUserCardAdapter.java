package cn.edu.ecust.faceaccesscontrol.userrecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.activity.AdminApproveDetailActivity;

/**
 * Created by Administrator on 2017/5/7.
 */

public class HistoryUserCardAdapter extends RecyclerView.Adapter<HistoryUserCardAdapter.ViewHolder> {
    private Context mContext;
    private List<UserCard> mUserCardList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView faceImage;
        TextView userNo;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            faceImage=(ImageView)view.findViewById(R.id.usercarditem_imageview_face);
            userNo=(TextView)view.findViewById(R.id.usercarditem_textview_userno);
        }
    }

    public HistoryUserCardAdapter(List<UserCard> userCardList){
        mUserCardList=userCardList;
    }

    @Override
    public HistoryUserCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.usercard_item,parent,false);
        final HistoryUserCardAdapter.ViewHolder holder=new HistoryUserCardAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int position=holder.getAdapterPosition();
//                UserCard userCard=mUserCardList.get(position);
//                //Toast.makeText(v.getContext(),userCard.getStringUserNo(),Toast.LENGTH_SHORT).show();
//                Intent intentAdminApproveDetailActivity=new Intent(v.getContext(), AdminApproveDetailActivity.class);
//                intentAdminApproveDetailActivity.putExtra("userNo",userCard.getStringUserNo());
//                mContext.startActivity(intentAdminApproveDetailActivity);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryUserCardAdapter.ViewHolder holder, int position) {
        UserCard userCard=mUserCardList.get(position);
        holder.userNo.setText(userCard.getStringUserNo());
        Glide.with(mContext).load(userCard.getStringUserFacePath()).into(holder.faceImage);
    }

    @Override
    public int getItemCount() {
        return mUserCardList.size();
    }
}
