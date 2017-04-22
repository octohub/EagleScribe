package com.richardlucasapps.eaglescribe.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class InstanceIdService extends FirebaseInstanceIdService {

  public static String getToken() {
    return FirebaseInstanceId.getInstance().getToken();
  }
}
