package com.pronunciation;

public interface AsyncResult<T, V> {
  void onSuccess(T result);
  void onFail(V error);
}
