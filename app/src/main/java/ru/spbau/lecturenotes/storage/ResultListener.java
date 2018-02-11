package ru.spbau.lecturenotes.storage;

public interface ResultListener <T> {
  public void onResult(T result);
  public void onError(Throwable error);
}
