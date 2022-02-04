package com.LosF.pasaleladepago;

import android.app.Activity;
import android.app.Application;

import retrofit2.Retrofit;
import sqip.CardEntry;

public class ExampleApplication extends Application {

  public static com.LosF.pasaleladepago.GooglePayChargeClient createGooglePayChargeClient(Activity activity) {
    ExampleApplication application = (ExampleApplication) activity.getApplication();
    return new com.LosF.pasaleladepago.GooglePayChargeClient(application.chargeCallFactory);
  }

  private com.LosF.pasaleladepago.ChargeCall.Factory chargeCallFactory;

  @Override
  public void onCreate() {
    super.onCreate();

    Retrofit retrofit = com.LosF.pasaleladepago.ConfigHelper.createRetrofitInstance();
    chargeCallFactory = new com.LosF.pasaleladepago.ChargeCall.Factory(retrofit);

    com.LosF.pasaleladepago.CardEntryBackgroundHandler cardHandler =
        new com.LosF.pasaleladepago.CardEntryBackgroundHandler(chargeCallFactory, getResources());
    CardEntry.setCardNonceBackgroundHandler(cardHandler);
  }
}
