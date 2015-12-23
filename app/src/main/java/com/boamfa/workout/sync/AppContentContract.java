package com.boamfa.workout.sync;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bogdan on 21/12/15.
 */
public final class AppContentContract {

    /**
     * The authority of the provider.
     */
    public static final String AUTHORITY =
            "workout.com";
    /**
     * The content URI for the top-level
     * lentitems authority.
     */
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    /**
     * Constants for the Tracks table
     * of the lentitems provider.
     */
    public static final class Tracks implements CommonColumns {
        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(
                        AppContentContract.CONTENT_URI,
                        "items");
        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/workout.com.items";
        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/workout.com.items";
        /**
         * A projection of all columns
         * in the items table.
         */
//        public static final String[] PROJECTION_ALL =
//                {_ID, NAME, BORROWER};
//        /**
//         * The default sort order for
//         * queries containing NAME fields.
//         */
//        public static final String SORT_ORDER_DEFAULT =
//                NAME + " ASC";
    }

    /**
     * Constants for the Photos table of the
     * lentitems provider. For each item there
     * is exactly one photo. You can
     * safely call insert with the an already
     * existing ITEMS_ID. You won’t get constraint
     * violations. The content provider takes care
     * of this.<br>
     * Note: The _ID of the new record in this case
     * differs from the _ID of the old record.
     */
    public static final class Photos
            implements BaseColumns { }

    /**
     * Constants for a joined view of Items and
     * Photos. The _id of this joined view is
     * the _id of the Items table.
     */
    public static final class ItemEntities
            implements CommonColumns { }

    /**
     * This interface defines common columns
     * found in multiple tables.
     */
    public static interface CommonColumns
            extends BaseColumns { }
}
