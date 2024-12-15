package com.example.gamerland;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private Context context;
    private int[] avatars;
    private OnAvatarClickListener listener;

    public AvatarAdapter(Context context, int[] avatars, OnAvatarClickListener listener) {
        this.context = context;
        this.avatars = avatars;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        holder.imageView.setImageResource(avatars[position]);
    }

    @Override
    public int getItemCount() {
        return avatars.length;
    }

    class AvatarViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imvAvatar);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAvatarClick(avatars[getAdapterPosition()]);
                }
            });
        }
    }

    public interface OnAvatarClickListener {
        void onAvatarClick(int avatarResId);
    }
}
