package com.example.albert.pestormix_apk.utils;

/**
 * Created by Albert on 23/01/2016.
 */
public abstract class Constants {

    //Preferences keys
    public static final String PREFERENCES_KEY = "pestormixapp";
    public static final String PREFERENCES_TUTORIAL_KEY = "tutorial";
    public static final String PREFERENCES_INIT_DATA_KEY = "init_data";
    public static final String PREFERENCES_SENT_TOKEN_TO_SERVER_KEY = "sent_token_to_server";
    public static final String PREFERENCES_USER_LOGGED = "user_logged";
    public static final String PREFERENCES_USER_NAME = "user_name";
    public static final String PREFERENCES_USER_IMAGE = "user_image";
    public static final String PREFERENCES_USER_GOOGLE_ID = "user_id";
    public static final String PREFERENCES_USER_GCM_ID = "user_gcm_id";
    public static final String PREFERENCES_NEED_TO_PUSH = "need_to_push";

    //Network
    public static final String NETWORK_RASPBERRY_IP = "192.168.42.1";
    public static final String NETWORK_RASPBERRY_PORT = "1110";

    //Intent extras
    public static final String EXTRA_IS_TUTORIAL = "is_tutorial";
    public static final String EXTRA_COCKTAIL = "cocktail";
    public static final String EXTRA_COCKTAIL_NAME = "cocktail_name";
    public static final String EXTRA_COCKTAIL_DESCRIPTION = "cocktail_description";
    public static final String EXTRA_COCKTAIL_DRINKS = "cocktail_drinks";
    public static final String EXTRA_CREATE_COCKTAIL_TYPE = "create_coctail_type";
    public static final String EXTRA_QUESTION_ID = "question_id";
    public final static String EXTRA_DRINK_NAME = "drink_name";

    //Broadcast actions
    public static final String ACTION_REGISTRATION_COMPLETE = "registrationComplete";
    public static final String ACTION_START_SYNC_WITH_REMOTE = "start_sync_with_remote";
    public static final String ACTION_START_SYNC_TO_REMOTE = "start_sync_to_remote";
    public static final String ACTION_START_SYNC_FROM_REMOTE = "start_sync_from_remote";
    public static final String ACTION_ASYNC_FAILED = "async_failed";
    public static final String ACTION_PULL_COMPLETED = "pull_completed";
}
