package com.graywolftechnoligies.tize0;

import java.util.ArrayList;

/**
 * Created by neil on 12/16/15.
 */
public class Guests {
    private ArrayList<String> userIDs;
    private ArrayList<String> usernames;
    private ArrayList<Integer> attendingStatus;
    private ArrayList<String> actualNames;
    private String event;

    public ArrayList<String> getGuestUsernames(){return usernames;}
    public void setGuestUsernames(ArrayList<String> usernames){this.usernames=usernames;}

    public ArrayList<Integer> getGuestAttendingStatus(){return attendingStatus;}
    public void setGuestAttendingStatus(ArrayList<Integer> attendingStatus){this.attendingStatus=attendingStatus;}

    public ArrayList<String> getGuestActualNames(){return actualNames;}
    public void setGuestActualNames(ArrayList<String> actualNames){this.actualNames=actualNames;}

    public String getEvent(){return event;}
    public void setEvent(String event){this.event=event;}

    public ArrayList<String> getUserIDs(){return userIDs;}
    public void setUserIDs(ArrayList<String> userIDs){this.userIDs=userIDs;}
}
