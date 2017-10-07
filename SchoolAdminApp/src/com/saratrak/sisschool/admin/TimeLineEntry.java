package com.saratrak.sisschool.admin;

public class TimeLineEntry {
	private String status;
	private String time;
	private long loc;
	private String speed;
	private int enginestatus;
	public TimeLineEntry(String status, String time, Long loc, String speed,int enginestatus) {
		this.status = status;
		this.time = time;
		this.loc = loc;
		this.speed = speed;
		this.enginestatus = enginestatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getLoc() {
		return loc;
	}
	public void setLoc(Long loc) {
		this.loc = loc;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public int getEnginestatus() {
		return enginestatus;
	}
	public void setEnginestatus(int enginestatus) {
		this.enginestatus = enginestatus;
	}

}
