package com.example.first_project_1_1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity
{


  /**
   * {@inheritDoc}
   * <p>
   * Perform initialization of all fragments.
   *
   * @param savedInstanceState
   */
  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
  }
}
