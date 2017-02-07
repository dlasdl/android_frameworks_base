/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.app;

import android.annotation.NonNull;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;

/**
 * Represents a remote action that can be called from another process.  The action can have an
 * associated visualization including metadata like an icon or title.
 */
public final class RemoteAction implements Parcelable {

    private static final String TAG = "RemoteAction";

    private final Icon mIcon;
    private final CharSequence mTitle;
    private final CharSequence mContentDescription;
    private final PendingIntent mActionIntent;

    RemoteAction(Parcel in) {
        mIcon = Icon.CREATOR.createFromParcel(in);
        mTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mContentDescription = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mActionIntent = PendingIntent.CREATOR.createFromParcel(in);
    }

    public RemoteAction(@NonNull Icon icon, @NonNull CharSequence title,
            @NonNull CharSequence contentDescription, @NonNull PendingIntent intent) {
        if (icon == null || title == null || contentDescription == null || intent == null) {
            throw new IllegalArgumentException("Expected icon, title, content description and " +
                    "action callback");
        }
        mIcon = icon;
        mTitle = title;
        mContentDescription = contentDescription;
        mActionIntent = intent;
    }

    /**
     * Return an icon representing the action.
     */
    public @NonNull Icon getIcon() {
        return mIcon;
    }

    /**
     * Return an title representing the action.
     */
    public @NonNull CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Return a content description representing the action.
     */
    public @NonNull CharSequence getContentDescription() {
        return mContentDescription;
    }

    /**
     * Return the action intent.
     */
    public @NonNull PendingIntent getActionIntent() {
        return mActionIntent;
    }

    @Override
    public RemoteAction clone() {
        return new RemoteAction(mIcon, mTitle, mContentDescription, mActionIntent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        mIcon.writeToParcel(out, 0);
        TextUtils.writeToParcel(mTitle, out, flags);
        TextUtils.writeToParcel(mContentDescription, out, flags);
        mActionIntent.writeToParcel(out, flags);
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("title=" + mTitle);
        pw.print(" contentDescription=" + mContentDescription);
        pw.print(" icon=" + mIcon);
        pw.print(" action=" + mActionIntent.getIntent());
        pw.println();
    }

    public static final Parcelable.Creator<RemoteAction> CREATOR =
            new Parcelable.Creator<RemoteAction>() {
                public RemoteAction createFromParcel(Parcel in) {
                    return new RemoteAction(in);
                }
                public RemoteAction[] newArray(int size) {
                    return new RemoteAction[size];
                }
            };
}