package com.saratrak.sisschool.admin;

import java.util.ArrayList;

public class AlertEntry {
	
	//static ArrayList<AlertEntry> entries = new ArrayList<AlertEntry>();
	
	private Boolean isRead;
	private String text;
	private String time;
	private int alertid;
	private String student_id;
	public AlertEntry(int alertid,Boolean isRead, String time, String text) {
		this.setAlertid(alertid);
		this.setIsRead(isRead);
		this.setText(text);
		this.setTime(time);
	}
	public AlertEntry()
	{
		
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getAlertid() {
		return alertid;
	}
	public void setAlertid(int alertid) {
		this.alertid = alertid;
	}
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	
}
