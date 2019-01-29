package com.example.oskin.lesson9_data_storage.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;
import java.util.UUID;

public class Note implements Parcelable {

    private String mId;

    private String mName;
    private String mText;
    private int mTime;

    public Note(){
    }

    public Note(String name, String text) {
        mId = UUID.randomUUID().toString();
        mName = name;
        mText = text;
        //TODO uncomment this and add time in signature
        //mTime = time;
    }


    public void setId(String id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTime(int time) {
        mTime = time;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getText() {
        return mText;
    }

    public int getTime() {
        return mTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return mTime == note.mTime &&
                Objects.equals(mId, note.mId) &&
                Objects.equals(mName, note.mName) &&
                Objects.equals(mText, note.mText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mName, mText, mTime);
    }

    protected Note(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mText = in.readString();
        mTime = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mText);
        dest.writeInt(mTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}