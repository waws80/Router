package pw.androidthanatos.router;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Reuest router的请求体
 * @author liuxiongfei
 */

public final class Request {

    private Context mContext;
    private String mPath ="";
    private String mAction ="";
    private Bundle mParams = new Bundle();
    private @AnimatorRes int mEnter = -1;
    private @AnimatorRes int mExit = -1;
    private Bundle mOptions = new Bundle();
    private int mRequestCode = -1;
    private int mFlag = -1;
    private ResultCallBack mCallback;

    private Request (Builder builder){
        this.mContext = builder.mContext;
        this.mPath = builder.mPath;
        this.mAction = builder.mAction;
        this.mParams = builder.mBundle;
        this.mEnter = builder.mEnter;
        this.mExit = builder.mExit;
        this.mOptions = builder.mOptions;
        this.mRequestCode = builder.mRequestCode;
        this.mFlag = builder.mFlag;
        this.mCallback = builder.callBack;
    }


    public Context context() {
        return mContext;
    }

    public String path() {
        return mPath;
    }

    public String action(){
        return mAction;
    }

    public Bundle params(){
        return mParams;
    }

    public int[] animator(){
        return new int[]{mEnter,mExit};
    }

    public int requestCode(){
        return mRequestCode;
    }

    public int flag(){
        return mFlag;
    }

    public ResultCallBack callBack(){
        return mCallback;
    }

    public Bundle option(){
        return mOptions;
    }

    public static class Builder{

        private @NonNull Context mContext;
        private @NonNull String mPath = "";
        private @NonNull String mAction = "";
        private Bundle mBundle = new Bundle();
        private @AnimatorRes int mEnter = -1;
        private @AnimatorRes int mExit = -1;
        private Bundle mOptions;
        private int mRequestCode = -1;
        private int mFlag = -1;
        private ResultCallBack callBack;

        public Builder(@NonNull String path, @NonNull String action ,@NonNull Context context){
            this.mPath = path;
            this.mAction = action;
            this.mContext = context;
        }

        public Builder(@NonNull String action, @NonNull Context context){
            this.mAction = action;
            this.mContext = context;
        }

        public Builder(@NonNull Context context){
            this.mContext = context;
        }

        public Builder path(@NonNull String path){
            this.mPath = path;
            return this;
        }

        public Builder flag(int flag) {
            this.mFlag = flag;
            return this;
        }

        public Builder action(@NonNull String action){
            this.mAction = action;
            return this;
        }

        public Builder responseCode(int requestCode){
            this.mRequestCode = requestCode;
            return this;
        }

        public Builder resultCallBack(ResultCallBack callBack){
            this.callBack = callBack;
            return this;
        }

        public Builder addInt(String key, int value){
            this.mBundle.putInt(key, value);
            return this;
        }

        public Builder addIntArr(String key, int[] values){
            this.mBundle.putIntArray(key, values);
            return this;
        }

        public Builder addLong(String key, long value){
            this.mBundle.putLong(key, value);
            return this;
        }

        public Builder addLongArr(String key, long[] values){
            this.mBundle.putLongArray(key, values);
            return this;
        }

        public Builder addFloat(String key, float value){
            this.mBundle.putFloat(key, value);
            return this;
        }

        public Builder addFloaArr(String key, float[] values){
            this.mBundle.putFloatArray(key, values);
            return this;
        }

        public Builder addDouble(String key, double value){
            this.mBundle.putDouble(key, value);
            return this;
        }

        public Builder addDoubleArr(String key, double[] values){
            this.mBundle.putDoubleArray(key, values);
            return this;
        }
        public Builder addBoolean(String key, boolean value){
            this.mBundle.putBoolean(key, value);
            return this;
        }

        public Builder addBooleanArr(String key, boolean[] values){
            this.mBundle.putBooleanArray(key, values);
            return this;
        }

        public Builder addString(String key, String value){
            this.mBundle.putString(key, value);
            return this;
        }

        public Builder addStringArr(String key, String[] values){
            this.mBundle.putStringArray(key, values);
            return this;
        }

        public Builder addStringList(String key, ArrayList<String> stringList){
            this.mBundle.putStringArrayList(key,stringList);
            return this;
        }

        public Builder addByte(String key, byte value){
            this.mBundle.putByte(key, value);
            return this;
        }

        public Builder addByteArray(String key, byte[] values){
            this.mBundle.putByteArray(key, values);
            return this;
        }

        public Builder addChar(String key, char value){
            this.mBundle.putChar(key, value);
            return this;
        }

        public Builder addCharArr(String key, char[] values){
            this.mBundle.putCharArray(key, values);
            return this;
        }

        public Builder addAll(Bundle bundle){
            this.mBundle.putAll(bundle);
            return this;
        }

        public Builder addObject(String key, Object value){
            if (value instanceof Serializable){
                this.mBundle.putSerializable(key, (Serializable) value);
            }else if (value instanceof Parcelable){
                this.mBundle.putParcelable(key, (Parcelable) value);
            }else {
                throw new IllegalArgumentException("请确保value 为 Serializable 或 Parcelable");
            }
            return this;
        }

        public Builder addObjects(String key, Object[] values){
            if (values instanceof Parcelable[]){
                this.mBundle.putParcelableArray(key, (Parcelable[]) values);
            }else {
                throw new IllegalArgumentException("请确保value 为 Parcelable[]");
            }
            return this;
        }

        public Builder addObjects(String key, ArrayList<Parcelable> values){
            this.mBundle.putParcelableArrayList(key,values);
            return this;
        }

        public Builder addAnimate(@AnimatorRes int enter, @AnimatorRes int exit){
            this.mEnter = enter;
            this.mExit = exit;
            return this;
        }

        public Builder addOption(@NonNull Bundle option){
            this.mOptions = option;
            return this;
        }

        public Request build(){
            if (this.mPath.isEmpty() && this.mAction.isEmpty()){
                RouterLog.d("path isEmpty  or  action isEmpty");
                return null;
            }
            return new Request(this);
        }


    }

}
