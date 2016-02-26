package com.graywolftechnoligies.tize0;

/**
 * Created by neil on 8/19/15.
 */

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

class EventsAdapter extends BaseAdapter {
    public static final String upcomingEventsHeader="Upcoming Events";
    public static final String myEventsHeader="My Events";
    public static final String pastEventsHeader="Past Events";

    ArrayList<Event> upcomingEventsList=new ArrayList<>();
    ArrayList<Event> myEventsList=new ArrayList<>();
    ArrayList<Event> myPastEventsList=new ArrayList<>();

    ArrayList<String> headersAndEventNames=new ArrayList<>();
    ArrayList<String> hostNames=new ArrayList<>();
    ArrayList<String> icons=new ArrayList<>();
    ArrayList<Integer> iconsSource=new ArrayList<>();

    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;

    int[] headerPositions=new int[3];

    int HDR_POS1;
    int HDR_POS2;
    int HDR_POS3;

    private ArrayList<Event> EventsList;
    private final Context mContext;

    ParseUser currentUser= ParseUser.getCurrentUser();
    Date currentDate=new Date();

    public EventsAdapter(Context context, ArrayList<Event> EventsList) {
        mContext = context;
        this.EventsList=EventsList;
        sortEvents();
    }

    public void sortEvents(){

        for(int i=0;i<EventsList.size();i++){
            if(EventsList.get(i).getHostUser().equals(currentUser) && EventsList.get(i).getEndDate().compareTo(currentDate)<0){
                myPastEventsList.add(EventsList.get(i));

            }
            else if(EventsList.get(i).getHostUser().equals(currentUser) && EventsList.get(i).getEndDate().compareTo(currentDate)>0){
                //Log.d("My Event Added", Integer.toString(i));
                myEventsList.add(EventsList.get(i));
            }
            else{
                upcomingEventsList.add(EventsList.get(i));
                //Log.d("Upcoming Event Added",Integer.toString(i));
            }
        }
        Collections.sort(myPastEventsList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getStartDate().compareTo(e2.getStartDate());
            }
        });
        Collections.sort(myEventsList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getStartDate().compareTo(e2.getStartDate());
            }
        });
        Collections.sort(upcomingEventsList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getStartDate().compareTo(e2.getStartDate());
            }
        });
        headersAndEventNames.add(upcomingEventsHeader);
        hostNames.add(null);
        for(int j=0;j<upcomingEventsList.size();j++){
            headersAndEventNames.add(upcomingEventsList.get(j).getEventName());
            hostNames.add(upcomingEventsList.get(j).getHostName());
            icons.add(upcomingEventsList.get(j).getIcon());
        }
        headersAndEventNames.add(myEventsHeader);
        hostNames.add(null);
        for(int j=0;j<myEventsList.size();j++){
            headersAndEventNames.add(myEventsList.get(j).getEventName());
            hostNames.add(myEventsList.get(j).getHostName());
            icons.add(myEventsList.get(j).getIcon());
        }
        headersAndEventNames.add(pastEventsHeader);
        hostNames.add(null);
        for(int j=0;j<myPastEventsList.size();j++){
            headersAndEventNames.add(myPastEventsList.get(j).getEventName());
            hostNames.add(myPastEventsList.get(j).getHostName());
            icons.add(myPastEventsList.get(j).getIcon());
        }
        //Create icon resource IDs from Parse icon names
        for (int i=0;i<icons.size();i++){
            iconsSource.add(mContext.getResources().getIdentifier(icons.get(i) + "_icon","drawable","com.graywolftechnoligies.tize0"));
            Log.d("iconsSource ID "+Integer.toString(i),Integer.toString(iconsSource.get(i)));
        }
        int l=0;
        for(int k=0;k<hostNames.size();k++){
            if (hostNames.get(k)==null){
                headerPositions[l]=k;
                l++;
            }
        }
        HDR_POS1=0;
        HDR_POS2=headerPositions[1];
        HDR_POS3=headerPositions[2];

    }

    @Override
    public int getCount() {
        return headersAndEventNames.size();
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
                    R.layout.lv_layout, parent, false);
            item.setTag(LIST_ITEM);
        }

        TextView header = (TextView)item.findViewById(R.id.lv_item_header);
        header.setText(headersAndEventNames.get(position % headersAndEventNames.size()));

        TextView hostName = (TextView)item.findViewById(R.id.lv_item_subtext);
        hostName.setText(hostNames.get(position % hostNames.size()));

        ImageView image=(ImageView)item.findViewById(R.id.icon_image);
        image.setImageResource(iconsSource.get(position % iconsSource.size()));

        return item;
    }

    private String getHeader(int position) {

        if(position == HDR_POS1  || position == HDR_POS2 || position ==HDR_POS3) {
            return headersAndEventNames.get(position);
        }

        return null;
    }
}
