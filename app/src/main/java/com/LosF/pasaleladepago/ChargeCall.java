package com.LosF.pasaleladepago;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import sqip.Call;

public class ChargeCall implements Call<com.LosF.pasaleladepago.ChargeResult> {

  public static class Factory {
    private final com.LosF.pasaleladepago.ChargeService service;
    private final Converter<ResponseBody, com.LosF.pasaleladepago.ChargeService.ChargeErrorResponse> errorConverter;

    public Factory(Retrofit retrofit) {
      service = retrofit.create(com.LosF.pasaleladepago.ChargeService.class);
      Annotation[] noAnnotations = {};
      Type errorResponseType = com.LosF.pasaleladepago.ChargeService.ChargeErrorResponse.class;
      errorConverter = retrofit.responseBodyConverter(errorResponseType, noAnnotations);
    }

    public Call<com.LosF.pasaleladepago.ChargeResult> create(String nonce) {
      return new ChargeCall(this, nonce);
    }
  }

  private final Factory factory;
  private final String nonce;
  private final retrofit2.Call<Void> call;

  private ChargeCall(Factory factory,
                     String nonce) {
    this.factory = factory;
    this.nonce = nonce;
    call = factory.service.charge(new com.LosF.pasaleladepago.ChargeService.ChargeRequest(nonce));
  }

  @Override
  public com.LosF.pasaleladepago.ChargeResult execute() {
    Response<Void> response;
    try {
      response = call.execute();
    } catch (IOException e) {
      return com.LosF.pasaleladepago.ChargeResult.networkError();
    }
    return responseToResult(response);
  }

  @Override
  public void enqueue(sqip.Callback<com.LosF.pasaleladepago.ChargeResult> callback) {
    call.enqueue(new Callback<Void>() {
      @Override
      public void onResponse(@NonNull retrofit2.Call<Void> call, @NonNull Response<Void> response) {
        callback.onResult(responseToResult(response));
      }

      @Override
      public void onFailure(@NonNull retrofit2.Call<Void> call, Throwable throwable) {
        if (throwable instanceof IOException) {
          callback.onResult(com.LosF.pasaleladepago.ChargeResult.networkError());
        } else {
          throw new RuntimeException("Unexpected exception", throwable);
        }
      }
    });
  }

  private com.LosF.pasaleladepago.ChargeResult responseToResult(Response<Void> response) {
    if (response.isSuccessful()) {
      return com.LosF.pasaleladepago.ChargeResult.success();
    }
    try {
      //noinspection ConstantConditions
      ResponseBody errorBody = response.errorBody();
      com.LosF.pasaleladepago.ChargeService.ChargeErrorResponse errorResponse = factory.errorConverter.convert(errorBody);
      return com.LosF.pasaleladepago.ChargeResult.error(errorResponse.errorMessage);
    } catch (IOException exception) {
      if (BuildConfig.DEBUG) {
        Log.d("ChargeCall", "Error while parsing error response: " + response.toString(),
            exception);
      }
      return com.LosF.pasaleladepago.ChargeResult.networkError();
    }
  }

  @Override
  public boolean isExecuted() {
    return call.isExecuted();
  }

  @Override
  public void cancel() {
    call.cancel();
  }

  @Override
  public boolean isCanceled() {
    return call.isCanceled();
  }

  @NonNull
  @Override
  public Call<com.LosF.pasaleladepago.ChargeResult> clone() {
    return factory.create(nonce);
  }
}
