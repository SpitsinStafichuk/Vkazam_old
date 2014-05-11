package com.github.spitsinstafichuk.vkazam.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;

import com.github.spitsinstafichuk.vkazam.model.exceptions.SongNotFoundException;
import com.perm.kate.api.Api;
import com.perm.kate.api.Audio;
import com.perm.kate.api.KException;

import android.util.Log;

public class SongData {

    public static final String NO_COVER_ART = "no_cover_art";

    protected String artist;

    protected String album;

    protected String title;

    protected String trackId;

    protected String pleercomUrl;

    protected String coverArtUrl;

    protected long lyricsId = -1;

    protected Date date;

    protected String vkArtist;

    protected String vkTitle;

    protected String ppArtist;

    protected String ppTitle;

    protected String contributorImageUrl;

    protected String artistBiographyURL;

    protected String songPosition;

    protected String albumReviewUrl;

    protected String albumReleaseYear;

    protected String albumArtist;

    protected String vkAudioId;

    @Override
    public String toString() {
        return "artist: " + artist +
                "\nalbum: " + album +
                "\ntitle: " + title +
                "\ntrackId" + trackId +
                "\ndate: " + date +
                "\npleercomUrl: " + pleercomUrl +
                "\nvkAudioId: " + vkAudioId +
                "\ncoverArtUrl: " + coverArtUrl +
                "\ncontributorImageUrl" + contributorImageUrl +
                "\nartistBiographyURL: " + artistBiographyURL +
                "\nsongPosition: " + songPosition +
                "\nalbumReviewUrl: " + albumReviewUrl +
                "\nalbumReleaseYear" + albumReleaseYear +
                "\nalbumArtist: " + albumArtist;
    }

    public String getFullTitle() {
        return artist + " - " + title;
    }

    public SongData(SongData songData) {
        this(songData.ppArtist, songData.ppTitle, songData.trackId, songData.artist, songData.album,
                songData.title, songData.pleercomUrl, songData.vkAudioId,
                songData.coverArtUrl, songData.date, songData.contributorImageUrl,
                songData.artistBiographyURL,
                songData.songPosition, songData.albumReviewUrl, songData.albumReleaseYear,
                songData.albumArtist);
    }

    public SongData(String trackId, String artist, String title, Date date, String coverArtUrl) {
        this(null, null, trackId, artist, null, title, null, null, coverArtUrl, date, null, null,
                null, null, null, null);
        Log.v("SongData", "Creating data with no pleercomurl is success");
        Log.v("SongData", "pleercomurl = " + pleercomUrl);
        Log.v("SongData", "coverarturl = " + this.coverArtUrl);
    }

    public SongData(String trackId, String artist, String album, String title, Date date) {
        this(null, null, trackId, artist, album, title, null, null, null, date, null, null, null,
                null, null, null);
    }

    public SongData(String ppArtist, String ppTitle, String trackId, String artist, String album,
            String title, String pleercomUrl, String vkAudioId, String coverArtUrl, Date date,
            String contributorImageUrl,
            String artistBiographyURL, String songPosition, String albumReviewUrl,
            String albumReleaseYear,
            String albumArtist) {

        this.ppArtist = ppArtist;
        this.ppTitle = ppTitle;
        this.artist = artist;
        this.setAlbum(album);
        this.title = title;
        this.trackId = trackId;
        this.pleercomUrl = pleercomUrl;
        this.coverArtUrl = coverArtUrl;
        this.date = date;
        this.setContributorImageUrl(contributorImageUrl);
        this.setArtistBiographyURL(artistBiographyURL);
        this.setSongPosition(songPosition);
        this.setAlbumReviewUrl(albumReviewUrl);
        this.setAlbumReleaseYear(albumReleaseYear);
        this.setAlbumArtist(albumArtist);
        Log.v("SongData", "Creating: pleercomUrl is " + this.pleercomUrl);
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getVkArtist() {
        return vkArtist;
    }

    public String getVkTitle() {
        return vkTitle;
    }

    public String getPpArtist() {
        return ppArtist;
    }

    public void setPpArtist(String artist) {
        ppArtist = artist;
    }

    public String getPpTitle() {
        return ppTitle;
    }

    public void setPpTitle(String title) {
        ppTitle = title;
    }

    public String getTrackId() {
        return trackId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPleercomUrl() {
        return pleercomUrl;
    }

    public String getCoverArtUrl() {
        return coverArtUrl;
    }

    public void setCoverArtUrl(String coverArtURL) {
        this.coverArtUrl = coverArtURL;
    }

    public void setPleercomUrl(String pleercomURL) {
        this.pleercomUrl = pleercomURL;
    }

    public void setVkAudioId(String vkAudioId) {
        this.vkAudioId = vkAudioId;
    }

    public long getLirycsId() {
        return lyricsId;
    }

    public String getVkAudioId() {
        return vkAudioId;
    }

    public void setNullFields(SongData songData) {
        if (trackId.equals(songData.trackId)) {
            if (pleercomUrl == null) {
                pleercomUrl = songData.pleercomUrl;
            }
            if (coverArtUrl == null) {
                coverArtUrl = songData.coverArtUrl;
            }
            if (contributorImageUrl == null) {
                contributorImageUrl = songData.contributorImageUrl;
            }
            if (artistBiographyURL == null) {
                artistBiographyURL = songData.artistBiographyURL;
            }
            if (songPosition == null) {
                songPosition = songData.songPosition;
            }
            if (albumReviewUrl == null) {
                albumReviewUrl = songData.albumReviewUrl;
            }
            if (albumArtist == null) {
                albumArtist = songData.albumArtist;
            }
            if (albumReleaseYear == null) {
                albumReleaseYear = songData.albumReleaseYear;
            }
        } else {
            throw new IllegalArgumentException("TrackIds must be equals");
        }
    }

    public String findVkAudio(Api vkApi)
            throws MalformedURLException, IOException, JSONException, KException,
            SongNotFoundException {
        if (vkApi != null) {
            List<Audio> audioList = vkApi
                    .searchAudio(getArtist() + " " + getTitle(), "2", "0", 1l, 0l, null, null);
            if (audioList.isEmpty()) {
                if ((getAlbumArtist() != null) &&
                        (!getAlbumArtist().equals(getArtist()))) {
                    audioList = vkApi
                            .searchAudio(getAlbumArtist() + " " + getTitle(), "2", "0", 1l, 0l,
                                    null, null);
                }
                if (audioList.isEmpty()) {
                    throw new SongNotFoundException();
                }
            }
            Audio audio = audioList.get(0);
            vkAudioId = audio.owner_id + "_" + audio.aid;
            Log.v("Lyrics", "" + audio + " --- " + audio.lyrics_id);
            vkArtist = audio.artist;
            vkTitle = audio.title;
            if (audio.lyrics_id != null) {
                lyricsId = audio.lyrics_id;
            } else {
                lyricsId = -1;
            }
            return audio.url;
        } else {
            return null;
        }
    }

    public String getLyrics(Api vkApi)
            throws MalformedURLException, IOException, JSONException, KException,
            SongNotFoundException {
        if (lyricsId != -1) {
            return vkApi.getLyrics(lyricsId);
        } else {
            if (vkAudioId == null) {
                findVkAudio(vkApi);
                if (lyricsId != -1) {
                    return vkApi.getLyrics(lyricsId);
                }
            }
        }
        return null;
    }

    public void setLyricsId(long lyricsId) {
        this.lyricsId = lyricsId;
    }

    public void findPPAudio() throws MalformedURLException, IOException, JSONException,
            com.github.spitsinstafichuk.vkazam.model.pleer.api.KException, SongNotFoundException {
        List<com.github.spitsinstafichuk.vkazam.model.pleer.api.Audio> audioList
                = com.github.spitsinstafichuk.vkazam.model.pleer.api.Api
                .searchAudio(getArtist() + " " + getTitle(), 1, 1);
        if (audioList.isEmpty()) {
            if ((getAlbumArtist() != null) &&
                    (!getAlbumArtist().equals(getArtist()))) {
                audioList = com.github.spitsinstafichuk.vkazam.model.pleer.api.Api
                        .searchAudio(getAlbumArtist() + " " + getTitle(), 1, 1);
            }
            if (audioList.isEmpty()) {
                throw new SongNotFoundException();
            }
        }
        pleercomUrl = audioList.get(0).url;
        ppArtist = audioList.get(0).artist;
        ppTitle = audioList.get(0).title;
    }

    public String getContributorImageUrl() {
        return contributorImageUrl;
    }

    public void setContributorImageUrl(String contributorImageUrl) {
        this.contributorImageUrl = contributorImageUrl;
    }

    public String getArtistBiographyURL() {
        return artistBiographyURL;
    }

    public void setArtistBiographyURL(String artistBiographyURL) {
        this.artistBiographyURL = artistBiographyURL;
    }

    public String getSongPosition() {
        return songPosition;
    }

    public void setSongPosition(String songPosition) {
        this.songPosition = songPosition;
    }

    public String getAlbumReviewUrl() {
        return albumReviewUrl;
    }

    public void setAlbumReviewUrl(String albumReviewUrl) {
        this.albumReviewUrl = albumReviewUrl;
    }

    public String getAlbumReleaseYear() {
        return albumReleaseYear;
    }

    public void setAlbumReleaseYear(String albumReleaseYear) {
        this.albumReleaseYear = albumReleaseYear;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
