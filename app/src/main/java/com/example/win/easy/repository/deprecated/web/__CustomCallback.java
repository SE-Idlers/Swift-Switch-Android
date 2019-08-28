package com.example.win.easy.repository.deprecated.web;

import androidx.annotation.NonNull;

import com.example.win.easy.repository.web.dto.Response;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class __CustomCallback implements Callback<Response> {

    @Override
    public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
        if(response.code()==200){
            Response customResponse=response.body();
            if (customResponse!=null&&customResponse.getStatus()==Response.Status.Success)
                process(customResponse.getBody());
        }
    }

    protected abstract void process(Object data);
    @Override
    public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) { }
}

