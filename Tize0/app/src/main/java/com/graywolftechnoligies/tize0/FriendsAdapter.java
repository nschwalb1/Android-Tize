package com.graywolftechnoligies.tize0;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by neil on 9/16/15.
 */
public class FriendsAdapter extends ArrayAdapter<String> {

    Context mContext;
    int layoutResourceId;
    ArrayList<String> users=new ArrayList<>();
    public FriendsAdapter(Context mContext, int layoutResourceId, ArrayList<String> users){
        super(mContext, layoutResourceId, users);
        this.layoutResourceId=layoutResourceId;
        this.mContext=mContext;
        this.users=users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            LayoutInflater inflater=((Activity) mContext).getLayoutInflater();
            convertView=inflater.inflate(layoutResourceId,parent,false);
        }
        String userTemp=users.get(position);

/*        TextView usernameListItem = (TextView)convertView.findViewById(R.id.usernameListItem);
        usernameListItem.setText(userTemp);
        usernameListItem.setTag(userTemp+"Tag");*/

        return convertView;
    }
}
