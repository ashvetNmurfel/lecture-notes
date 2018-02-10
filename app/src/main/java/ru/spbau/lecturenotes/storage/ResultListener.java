package ru.spbau.lecturenotes.storage;

/**
 * Created by denspb on 10.02.2018.
 */

public interface ResultListener <T> {
  public void onResult(T result);
  public void onError(Throwable error);
}
