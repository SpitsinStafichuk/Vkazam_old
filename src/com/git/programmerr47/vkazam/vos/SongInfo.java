package com.git.programmerr47.vkazam.vos;

import java.util.Date;

public class SongInfo {

	private final int id;
	private GracenoteSongInfo gracenoteSongInfo;
	private Date date;
	private String songPosition;
	private boolean favourite;
	private boolean deleted;

	public SongInfo(int id, GracenoteSongInfo gracenoteSongInfo, Date date,
			String songPosition, boolean favourite, boolean deleted) {
		super();
		this.id = id;
		this.gracenoteSongInfo = gracenoteSongInfo;
		this.date = date;
		this.songPosition = songPosition;
		this.favourite = favourite;
		this.deleted = deleted;
	}

	public SongInfo(GracenoteSongInfo gracenoteSongInfo, Date date,
			String songPosition, boolean favourite, boolean deleted) {
		super();
		id = -1;
		this.gracenoteSongInfo = gracenoteSongInfo;
		this.date = date;
		this.songPosition = songPosition;
		this.favourite = favourite;
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public GracenoteSongInfo getGracenoteSongInfo() {
		return gracenoteSongInfo;
	}

	public void setGracenoteSongInfo(GracenoteSongInfo gracenoteSongInfo) {
		this.gracenoteSongInfo = gracenoteSongInfo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSongPosition() {
		return songPosition;
	}

	public void setSongPosition(String songPosition) {
		this.songPosition = songPosition;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
