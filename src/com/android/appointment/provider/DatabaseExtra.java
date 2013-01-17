
package com.android.appointment.provider;

import android.net.Uri;

public class DatabaseExtra {

    public static final String DATABASE_NAME = "appointment.db";

    public static final String VND_ANDROID_DIR_APPOINTMENT = "vnd.android-dir/appointment";

    public static final int DATABASE_VERSION = 1;

    public static class CONVERSATION_TABLE {
        public static final Uri CONTENT_URI = Uri.parse(
                "content://appointment-conversation/conversation");

        public static final int CONVERSATION_BOX_SENT    = 0;
        public static final int CONVERSATION_BOX_RECEIVE  = 1;
        public static final int CONVERSATION_BOX_DRAFT =2;

        public static final String AUTHORITY = "appointment-conversation";

        public static final String NAME = "conversation";

        public static final String _ID = "_id";

        public static final String CONVERSATION_ID = "conversation_id";

        public static final String DESCRIPTION = "description";

        public static final String DATE = "date";

        public static final String LONGITUDE = "longitude";

        public static final String LATITUDE = "latitude";

        public static final String LOCATION_DESCRIPTION = "location_description";

        public static final String BOX = "box";
    }

    public static class RECIPIENT_TABLE {
        public static final Uri CONTENT_URI = Uri.parse(
                "content://appointment-recipient/recipients");

        public static final int SHARE_LOCATION_WITH_OTHERS = 0;
        public static final int NO_SHARE_LOCATION_WITH_OTHERS = 1;

        public static final String AUTHORITY = "appointment-recipient";

        public static final String NAME = "recipients";

        public static final String _ID = "_id";

        public static final String PARTICIPANT = "participant";

        public static final String SHARE_WITH_OTHERS = "is_share";

        public static final String CONVERSATION_ID = "conversation_id";
    }

    public static class FAVORITE_ADDRESS_TABLE {
        public static final Uri CONTENT_URI = Uri.parse(
                "content://appointment-favorite-address/favorite_address");

        public static final String AUTHORITY = "appointment-favorite-address";

        public static final String NAME = "favorite_address";

        public static final String _ID = "_id";

        public static final String LONGITUDE = "longitude";

        public static final String LATITUDE = "latitude";

        public static final String LOCATION_DESCRIPTION = "location_description";
    }
}
