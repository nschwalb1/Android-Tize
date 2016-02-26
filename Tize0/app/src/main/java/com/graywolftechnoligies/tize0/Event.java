package com.graywolftechnoligies.tize0;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Date;


/**
 * Created by neil on 9/2/15.
 */
public class Event {
    private String eventName;
    private String host;
    private String hostName;
    private ParseUser hostUser;
    private String locationName;
    private ParseGeoPoint location;
    private String icon;
    private Date startDate;
    private Date endDate;
    private String eventDetails;
    private String eventID;

    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName){
        this.eventName=eventName;
    }

    public String getHost(){
        return host;
    }

    public void setHost(String host){
        this.host=host;
    }

    public String getHostName(){
        return hostName;
    }

    public void setHostName(String hostName){
        this.hostName=hostName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName){
        this.locationName=locationName;
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
        this.icon=icon;
    }

    public String getEventDetails(){
        return eventDetails;
    }

    public void setEventDetails(String eventDetails){
        this.eventDetails=eventDetails;
    }

    public ParseUser getHostUser(){
        return hostUser;
    }

    public void setHostUser(ParseUser hostUser){
        this.hostUser=hostUser;
    }

    public ParseGeoPoint getLocation() {
        return location;
    }

    public void setLocation(ParseGeoPoint location){
        this.location=location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEventID() {return eventID;}

    public void setEventID(String eventID) {this.eventID = eventID;}
}
