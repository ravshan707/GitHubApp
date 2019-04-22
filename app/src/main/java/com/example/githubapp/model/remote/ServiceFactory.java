package com.example.githubapp.model.remote;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

//        client.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//                HttpUrl originalUrl = original.url();
//
//                HttpUrl url = originalUrl.newBuilder()
//                        .addQueryParameter("api_key", "brXMsD0cTFgrd7yQh6u17ilSMIhDz2t9")
//                        .build();
//
//                Request request = original.newBuilder().url(url).build();
//
//                return chain.proceed(request);
//            }
//        });

        return client.build();
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static <T> T getApi(Class<T> serviceClass) {
        return ServiceFactory.getRetrofit().create(serviceClass);
    }
}
