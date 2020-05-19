package com.example.ushopping.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.ushopping.LoginActivity;
import com.example.ushopping.MainActivity;
import com.example.ushopping.data.SessionGetData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIContext {

    private static Retrofit api;
    private static final String BASE_URL = "http://10.0.2.2:5000";
    private static UUID session;

    public static Retrofit getContext() {
        if (api == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        }
        return api;
    }

    public static <T> APICall makeCall(View view, Call<T> restCall, APICallback<T> call) {
        return new APICall(view, restCall, call);
    }

    public static <T> T createAPI(Class<T> api) {
        return getContext().create(api);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    public static boolean isLogin(Context context) {
        return (getSession(context) != null);
    }

    public static UUID getSession(Context context) {
        if (session != null) return session;
        File file = new File(context.getFilesDir(), "session");
        if (!file.exists()) return null;

        try {
            FileReader reader = new FileReader(file);
            char[] data = new char[(int) file.length()];
            reader.read(data);
            session = UUID.fromString(new String(data));
            reader.close();
            return session;

        } catch (Exception e) {
            Log.wtf("EXCEPTION", e);
        }

        return null;
    }

    public static void login(Context context, UUID session) {
        APIContext.session = session;
        File file = new File(context.getFilesDir(), "session");

        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(session.toString());
            writer.close();

        } catch (Exception e) {
            Log.wtf("EXCEPTION", e);
        }
    }

    public static void logout(Context context) {
        APIContext.session = null;
        File file = new File(context.getFilesDir(), "session");
        if (file.exists()) file.delete();
    }

    public static void updateSession(Context context) {
        UUID sess = getSession(context);
        if (sess == null) {
            spawnActivity(context, LoginActivity.class);
            return;
        }

        Call<SessionGetData> call = createAPI(SessionAPI.class).get(sess);
        makeCall(null, call, data -> {
            if (!data.active) {
                spawnActivity(context, LoginActivity.class);
            }
        }).call();
    }

    public static void spawnActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
