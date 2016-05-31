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
    public static final String PREFERENCES_NETWORK_CONNECTED = "network_connected";

    //Network
    public static final String NETWORK_RASPBERRY_IP = "192.168.1.100";
//    public static final String NETWORK_RASPBERRY_IP = "192.168.42.1";
    public static final String NETWORK_RASPBERRY_PORT = "1110";
    public static final String NETWORK_GLASS = "glass";
    public static final String NETWORK_USE = "use";
    public static final String NETWORK_ALCOHOL = "alcohol";
    public static final String NETWORK_VALVE = "v%s";

    //Cocktail fields
    public static final String COCKTAIL_NAME = "name";
    public static final String COCKTAIL_USER_ID = "userId";
    public static final String COCKTAIL_DESCRIPTION = "description";
    public static final String COCKTAIL_ALCOHOL = "alcohol";
    public static final String COCKTAIL_DRINKS = "drinks";

    //Drink fields
    public static final String DRINK_ID = "id";
    public static final String DRINK_NAME = "name";

    //Question fields
    public static final String QUESTION_ID = "id";

    //Valve fields
    public static final String VALVE_ID = "id";
    public static final String VALVE_DRINK_NAME = "drinkName";
    public static final String VALVE_DRINK_ALCOHOL = "drinkAlcohol";
    public static final String VALVE_USER_ID = "userId";

    //Glass fields
    public static final String GLASS_NAME = "name";
    public static final String GLASS_CAPACITY = "capacity";

    //Backend type
    public static final String BACKEND_HEROKU = "Heroku";
    public static final String BACKEND_GOOGLE = "Google";

    //Intent extras
    public static final String EXTRA_IS_TUTORIAL = "is_tutorial";
    public static final String EXTRA_COCKTAIL = "cocktail";
    public static final String EXTRA_COCKTAIL_NAME = "cocktail_name";
    public static final String EXTRA_COCKTAIL_DESCRIPTION = "cocktail_description";
    public static final String EXTRA_COCKTAIL_DRINKS = "cocktail_drinks";
    public static final String EXTRA_CREATE_COCKTAIL_TYPE = "create_coctail_type";
    public static final String EXTRA_QUESTION_ID = "question_id";
    public final static String EXTRA_DRINK_ID = "drink_name";

    //Broadcast actions
    public static final String ACTION_REGISTRATION_COMPLETE = "registrationComplete";
    public static final String ACTION_START_SYNC_WITH_REMOTE = "start_sync_with_remote";
    public static final String ACTION_START_SYNC_TO_REMOTE = "start_sync_to_remote";
    public static final String ACTION_START_SYNC_FROM_REMOTE = "start_sync_from_remote";
    public static final String ACTION_START_SYNC_VALVES_REMOTE = "start_sync_valves_remote";
    public static final String ACTION_ASYNC_FAILED = "async_failed";
    public static final String ACTION_PULL_COMPLETED = "pull_completed";
    //Others
    public static final String DEFAULT_USER_ID = "1";
    public static final String DEFAULT_USER_NAME = "Default";
}
