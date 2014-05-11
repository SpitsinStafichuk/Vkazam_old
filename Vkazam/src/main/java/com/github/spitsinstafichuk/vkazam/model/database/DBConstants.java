package com.github.spitsinstafichuk.vkazam.model.database;

public interface DBConstants {

    String DATABASE = "history_database.db";

    String ID = "_id";
    String DATE = "date";

    String MUSIC_HISTORY_TABLE = "history";
    String MUSIC_HISTORY_ARTIST = "artist";
    String MUSIC_HISTORY_ALBUM = "album";
    String MUSIC_HISTORY_TITLE = "title";
    String MUSIC_HISTORY_PP_ARTIST = "pp_artist";
    String MUSIC_HISTORY_PP_TITLE = "pp_title";
    String MUSIC_HISTORY_GRACENOTE_TRACK_ID = "song_data_link";
    String MUSIC_HISTORY_PLEERCOM_URL = "pleercom_url";
    String MUSIC_HISTORY_VK_AUDIO_ID = "vk_audio_id";
    String MUSIC_HISTORY_COVER_ART_URL = "cover_art_url";
    String MUSIC_HISTORY_BIOGRAPHY_URL = "biography_url";
    String MUSIC_HISTORY_CONTRIBUTOR_IMAGE_URL = "contributor_image_url";
    String MUSIC_HISTORY_ALBUM_REVIEW_URL = "album_review_url";
    String MUSIC_HISTORY_ALBUM_RELEASE_YEAR = "album_release_year";
    String MUSIC_HISTORY_ALBUM_ARTIST = "album_artist";

    String FINGERPRINTS_TABLE = "fingerprints";
    String FINGERPRINT_DATA = "fingerprint_data";

    String SQL_CREATE_MUSIC_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS "
            + MUSIC_HISTORY_TABLE + " (" + ID + " INTEGER PRIMARY KEY,"
            + MUSIC_HISTORY_ARTIST + " TEXT,"
            + MUSIC_HISTORY_ALBUM + " TEXT,"
            + MUSIC_HISTORY_TITLE + " TEXT,"
            + MUSIC_HISTORY_PP_ARTIST + " TEXT,"
            + MUSIC_HISTORY_PP_TITLE + " TEXT,"
            + DATE + " INTEGER,"
            + MUSIC_HISTORY_GRACENOTE_TRACK_ID + " TEXT,"
            + MUSIC_HISTORY_PLEERCOM_URL + " TEXT,"
            + MUSIC_HISTORY_VK_AUDIO_ID + " TEXT,"
            + MUSIC_HISTORY_COVER_ART_URL + " TEXT,"
            + MUSIC_HISTORY_BIOGRAPHY_URL + " TEXT,"
            + MUSIC_HISTORY_CONTRIBUTOR_IMAGE_URL + " TEXT,"
            + MUSIC_HISTORY_ALBUM_REVIEW_URL + " TEXT,"
            + MUSIC_HISTORY_ALBUM_RELEASE_YEAR + " VARCHAR(5),"
            + MUSIC_HISTORY_ALBUM_ARTIST + " TEXT"
            + ")";

    String SQL_CREATE_FINGERPRINTS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + FINGERPRINTS_TABLE + " (" + ID + " INTEGER PRIMARY KEY,"
            + FINGERPRINT_DATA + " TEXT,"
            + DATE + " INTEGER"
            + ")";

    String SQL_DROP_MUSIC_HISTORY_TABLE = "DROP TABLE IF EXISTS " + MUSIC_HISTORY_TABLE;

    String SQL_DROP_FINGERPRINTS_TABLE = "DROP TABLE IF EXISTS " + FINGERPRINTS_TABLE;
}
