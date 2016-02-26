package com.graywolftechnoligies.tize0;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by neil on 12/15/15.
 */
public class GuestsAdapter extends BaseAdapter{
    public static final String attendingHeader="Attending";
    public static final String maybeHeader="Maybe";
    public static final String notAttendingHeader="Not Attending";
    public static final String noResponseHeader = "Not Responded";

    ArrayList<String> attendingList=new ArrayList<>();
    ArrayList<String> maybeList=new ArrayList<>();
    ArrayList<String> notAttendingList=new ArrayList<>();
    ArrayList<String> noResponseList=new ArrayList<>();

    ArrayList<String> headersAndGuestNames=new ArrayList<>();


    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;


    int HDR_POS1;
    int HDR_POS2;
    int HDR_POS3;
    int HDR_POS4;

    private Guests guests;
    private final Context mContext;


    public GuestsAdapter(Context context, Guests guests) {
        mContext = context;
        this.guests=guests;
        sortGuests();
    }

    public void sortGuests(){

        for(int i=0;i<guests.getUserIDs().size();i++){
            if(guests.getGuestAttendingStatus().get(i)==0){
                attendingList.add(guests.getGuestUsernames().get(i));
            }
            else if(guests.getGuestAttendingStatus().get(i)==1){
                maybeList.add(guests.getGuestUsernames().get(i));
            }
            else if(guests.getGuestAttendingStatus().get(i)==2){
                notAttendingList.add(guests.getGuestUsernames().get(i));
            }
            else{
                noResponseList.add(guests.getGuestUsernames().get(i));
            }
        }
        Collections.sort(attendingList);
        Collections.sort(maybeList);
        Collections.sort(notAttendingList);
        Collections.sort(noResponseList);
        headersAndGuestNames.add(attendingHeader);
        headersAndGuestNames.addAll(attendingList);
        headersAndGuestNames.add(maybeHeader);
        headersAndGuestNames.addAll(maybeList);
        headersAndGuestNames.add(notAttendingHeader);
        headersAndGuestNames.addAll(notAttendingList);
        headersAndGuestNames.add(noResponseHeader);
        headersAndGuestNames.addAll(noResponseList);

        HDR_POS1=0;
        HDR_POS2=headersAndGuestNames.indexOf(maybeHeader);
        HDR_POS3=headersAndGuestNames.indexOf(notAttendingHeader);
        HDR_POS4=headersAndGuestNames.indexOf(noResponseHeader);

    }

    @Override
    public int getCount() {
        return headersAndGuestNames.size();
    }

    @Override
    public boolean areAllItemsEnabled(){
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String headerText = getHeader(position);
        if(headerText != null) {

            View item = convertView;
            if(convertView == null || convertView.getTag() == LIST_ITEM) {

                item = LayoutInflater.from(mContext).inflate(
                        R.layout.lv_header_layout, parent, false);
                item.setTag(LIST_HEADER);

            }

            TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
            headerTextView.setText(headerText);
            return item;
        }

        View item = convertView;
        if(convertView == null || convertView.getTag() == LIST_HEADER) {
            item = LayoutInflater.from(mContext).inflate(
                    R.layout.guest_list_layout, parent, false);
            item.setTag(LIST_ITEM);
        }

        TextView header = (TextView)item.findViewById(R.id.guestListEntry);
        header.setText(headersAndGuestNames.get(position % headersAndGuestNames.size()));

        return item;
    }

    private String getHeader(int position) {

        if(position == HDR_POS1  || position == HDR_POS2 || position ==HDR_POS3 || position==HDR_POS4) {
            return headersAndGuestNames.get(position);
        }

        return null;
    }
}

