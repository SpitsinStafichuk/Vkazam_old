
package com.git.programmerr47.vkazam.vos;

import java.util.Date;

public class SongInfo {

    private final long id;
    private GracenoteSongInfo gracenoteSongInfo;
    private Date date;
    private String songPosition;
    private boolean favourite;
    private Date deletionDate;

    public SongInfo(long id, GracenoteSongInfo gracenoteSongInfo, Date date,
            String songPosition, boolean favourite, Date deletionDate) {
        super();
        this.id = id;
        this.gracenoteSongInfo = gracenoteSongInfo;
        this.date = date;
        this.songPosition = songPosition;
        this.favourite = favourite;
        this.deletionDate = deletionDate;
    }

    public SongInfo(GracenoteSongInfo gracenoteSongInfo, Date date,
            String songPosition, boolean favourite, Date deletionDate) {
        super();
        id = -1;
        this.gracenoteSongInfo = gracenoteSongInfo;
        this.date = date;
        this.songPosition = songPosition;
        this.favourite = favourite;
        this.deletionDate = deletionDate;
    }

    public long getId() {
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

    public Date getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }

}
