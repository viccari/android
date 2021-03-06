package com.elifut.services;

import com.elifut.models.GoogleApiConnectionResult;

import rx.Single;

public interface GoogleApiConnectionHandler {
  Single<GoogleApiConnectionResult> result();
  void connect();
  void onStart();
  void onStop();
  void onActivityResult(int requestCode, int responseCode);
}
