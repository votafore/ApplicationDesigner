package com.votafore.applicationdesigner.support;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.votafore.applicationdesigner.R;
import com.votafore.applicationdesigner.model.Project;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    List<Project> mProjects;
    Context mContext;

    OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClick(int id);
        void onLongClick(int position);
    }

    public RecycleAdapter(Context ctx, List<Project> proj){

        mProjects = proj;
        mContext = ctx;
    }

    public void setList(List<Project> list){
        mProjects = list;
    }

    public void setOnClickListener(RecycleAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tmp_projectlist_item, parent, false);
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // TODO: тут работаем с вьюхами из холдера
        holder.mtitle.setText(mProjects.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public List<Project> getProjects(){
        return mProjects;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        // TODO: тут определяются переменные-ссылки на View, с котороыми потом надо будет работать при выводе элементов списка
        TextView mtitle;
        RecycleAdapter.OnItemClickListener mListener;

        public ViewHolder(View itemView, RecycleAdapter.OnItemClickListener listener){
            super(itemView);

            // TODO: тут этим переменным присваиваются ссылки на View из itemView
            mtitle = (TextView)itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(mProjects.get(getAdapterPosition()).getId());
        }

        @Override
        public boolean onLongClick(View v) {

            mListener.onLongClick(getAdapterPosition());
            return false;
        }
    }
}
