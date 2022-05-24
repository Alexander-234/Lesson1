package com.example.first_project_1_1;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;


// Стартовый класс моего приложения
// Класс AppCompatActivity по сути представляет отдельный экран (страницу) приложения или его визуальный интерфейс.
// и MainActivity наследует весь этот функционал.
public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener
{
  int i_ReadFromDeviceLightingLevel, i_ReadFromDeviceHumidityLevel;
  TextView textViewSetLightingLevel, textPrintCurrentLightingLevel, textViewSetHumidityLevel, textPrintCurrentHumidityLevel;
  Button buttonConfirmSetNewLightingLevel, buttonConfirmSetNewHumidityLevel;
  ProgressBar progressBarWait1, progressBarWait2;
  SeekBar seekBarSetLightingLevel, seekBarSetHumidityLevel;

  private static final int REQUEST_ENABLE_BT = 1;

  BluetoothAdapter bluetoothAdapter;

  ArrayList<String> pairedDeviceArrayList;

  ListView listViewPairedDevice;
  FrameLayout ButPanel;

  ArrayAdapter<String> pairedDeviceAdapter;
  private UUID myUUID;

//  ThreadConnectBTdevice myThreadConnectBTdevice;
//  ThreadConnected myThreadConnected;


  @SuppressLint("SetTextI18n") @Override protected void onCreate(Bundle savedInstanceState)
  {

    final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";



//      По умолчанию MainActivity содержит только один метод onCreate(), в котором фактически и создается весь интерфейс приложения:
    super.onCreate(savedInstanceState);
//      При запуске приложения сначала запускается класс MainActivity,
//      который в качестве графического интерфейса устанавливает разметку из файла activity_main.xml
//      В метод setContentView() передается ресурс разметки графического интерфейса (activity_main.xml):
    setContentView(R.layout.activity_main);


    // Если Bluetooth не работает, выход из программы
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
      Toast.makeText(this, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
      finish();
      return;
    }

    myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    if (bluetoothAdapter == null) {
      Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
      finish();
      return;
    }

//    String stInfo = bluetoothAdapter.getName() + " " + bluetoothAdapter.getAddress();
//    textInfo.setText(String.format("Это устройство: %s", stInfo));



// ---------------------------------------------------------------------------------------------------------------------------------------------------

    // !!!!!!!!!!!!!!!!!!!!!!!!!!! ВЛАЖНОСТЬ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // читаем уровень влажности из устройства по Bluetooth (пока это ЗАГЛУШКА !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
    i_ReadFromDeviceHumidityLevel = 76;

    // находим указатель на текстовый блок "УСТАНОВКА ПОРОГА ВЛАЖНОСТИ"
    textViewSetHumidityLevel = (TextView) findViewById(R.id.textSetHumidityLevel);
    // выводим текст вместе с текущим порогом переключения по уровню влажности, прочитанным по Bluetooth
    textViewSetHumidityLevel.setText(getString(R.string.stringSetHumidityLevel) + " " + i_ReadFromDeviceHumidityLevel + "%");

    // Находим ползунок по идентификатору ID
    seekBarSetHumidityLevel = (SeekBar) findViewById(R.id.seekBarSetHumidityLevel);
    // заголовок класса MainActivity не забыть изменить на:
    //    public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener
//    seekBarSetHumidityLevel.setOnSeekBarChangeListener(this); // подключаем интерфейс "SeekBar.OnSeekBarChangeListener"

    // Обрабатываем активность слайдера
    seekBarSetHumidityLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
      {
        textViewSetHumidityLevel.setText(getString(R.string.stringSetHumidityLevel) + " " + String.valueOf(seekBarSetHumidityLevel.getProgress()) + "%");
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar)
      {
        buttonConfirmSetNewHumidityLevel.setVisibility(View.INVISIBLE); // кнопка исчезает, если мы решили подправить значение
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar)
      {
        buttonConfirmSetNewHumidityLevel.setVisibility(View.VISIBLE);
      }

    }); // -------------------------------------------------------------------------------------------------------------------------------------------

    // устанавливаем шкалу слайдера на прочтённый по каналу Bluetooth из устройства уровень
    seekBarSetHumidityLevel.setProgress(i_ReadFromDeviceHumidityLevel);

    // находим указатель на текстовый блок "ТЕКУЩИЙ УРОВЕНЬ ВЛАЖНОСТИ"
    textPrintCurrentHumidityLevel = (TextView) findViewById(R.id.textPrintCurrentHumidityLevel);
    // выводим текст вместе с текущим порогом переключения по уровню влажности, прочитанным по Bluetooth
    textPrintCurrentHumidityLevel.setText(getString(R.string.stringCurrentHumidityLevel) + " " + i_ReadFromDeviceHumidityLevel + "%");

    // находим указатель на кнопку "ЗАПИСАТЬ"
    buttonConfirmSetNewHumidityLevel = (Button) findViewById(R.id.buttonConfirmSetNewHumidityLevel);
    buttonConfirmSetNewHumidityLevel.setVisibility(View.INVISIBLE); // кнопка исчезает

    // находим крутящееся кольцо ожидания, оно в данный момент невидимо
    progressBarWait2 = (ProgressBar) findViewById(R.id.progressBarWait2);


    // !!!!!!!!!!!!!!!!!!!!!!!!!!! ОСВЕЩЁННОСТЬ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // читаем уровень освещённости из устройства по Bluetooth (пока это ЗАГЛУШКА !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
    i_ReadFromDeviceLightingLevel = 20;

    // находим указатель на текстовый блок "УСТАНОВКА ПОРОГА ОСВЕЩЁННОСТИ"
    textViewSetLightingLevel = (TextView) findViewById(R.id.textSetLightingLevel);
    // выводим текст вместе с текущим порогом переключения по освещённости, прочитанным по Bluetooth
    textViewSetLightingLevel.setText(getString(R.string.stringSetLightingLevel) + " " + i_ReadFromDeviceLightingLevel + "%");

    // Находим ползунок по идентификатору ID
    seekBarSetLightingLevel = (SeekBar) findViewById(R.id.seekBarSetLightingLevel);
    // заголовок класса MainActivity не забыть изменить на:
    //    public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener
    seekBarSetLightingLevel.setOnSeekBarChangeListener(this);

    // подключаем интерфейс "SeekBar.OnSeekBarChangeListener"
    // устанавливаем шкалу слайдера на прочтённый по каналу Bluetooth из устройства уровень
    seekBarSetLightingLevel.setProgress(i_ReadFromDeviceLightingLevel);

    // находим указатель на текстовый блок "ТЕКУЩИЙ УРОВЕНЬ ОСВЕЩЁННОСТИ"
    textPrintCurrentLightingLevel = (TextView) findViewById(R.id.textPrintCurrentLightingLevel);
    // выводим текст вместе с текущим порогом переключения по освещённости, прочитанным по Bluetooth
    textPrintCurrentLightingLevel.setText(getString(R.string.stringCurrentLightingLevel) + " " + i_ReadFromDeviceLightingLevel + "%");

    // находим указатель на кнопку "ЗАПИСАТЬ"
    buttonConfirmSetNewLightingLevel = (Button) findViewById(R.id.buttonConfirmSetNewLightingLevel);
    buttonConfirmSetNewLightingLevel.setVisibility(View.INVISIBLE); // кнопка исчезает

    // находим крутящееся кольцо ожидания, оно в данный момент невидимо
    progressBarWait1 = (ProgressBar) findViewById(R.id.progressBarWait1);

  }    //  @Override protected void onCreate --------------------------------------------------------------------------------------------------------------------------------


//  if (bluetooth.isEnabled()) {
//  // Bluetooth включен. Работаем.
//}
//else
//  {
//    // Bluetooth выключен. Предложим пользователю включить его.
//    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//  }
//
//  use registerForActivityResult(ActivityResultContract, ActivityResultCallback)


  // Процесс перехода из окна MainActivity в окно SecondActivity,
  // который будет происходить при нажатии на текст "Установка нового порога влажности":
  public void Click(View view)
  {
    //Создаем переход:
    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
    //Запускаем его при нажатии:
    startActivity(intent);
  }

  //  метод обработки нажатия на кнопку "ЗАПИСАТЬ" порог освещённости -----------------------------------------------------------------------------------------------------
  public void sendLightingParametersToDevice(View view)
  {
    buttonConfirmSetNewLightingLevel.setVisibility(View.INVISIBLE);  //  ВЫКЛючение изображения кнопки

    seekBarSetLightingLevel.setVisibility(View.INVISIBLE);  //  ВЫКЛючение изображения слайдера

    textPrintCurrentLightingLevel.setVisibility(View.INVISIBLE); //  ВЫКЛючение текста "Текущий порог освещённости"

    progressBarWait1.setVisibility(View.VISIBLE);  //  ВКЛючение вращающегося кольца ожидания

//  Вывод строки "ОТПРАВКА..."
//    buttonConfirmSetNewLightingLevel.setText(getString(R.string.stringAfterClick));

//  finish(); выход из программы
  } // -------------------------------------------------------------------------------------------------------------------------------------------------


  //  метод обработки нажатия на кнопку "ЗАПИСАТЬ" порог влажности -------------------------------------------------------------------------------------
  public void sendHumidityParametersToDevice(View view)
  {
    buttonConfirmSetNewHumidityLevel.setVisibility(View.INVISIBLE);  //  ВЫКЛючение изображения кнопки

    seekBarSetHumidityLevel.setVisibility(View.INVISIBLE);  //  ВЫКЛючение изображения слайдера

    textPrintCurrentHumidityLevel.setVisibility(View.INVISIBLE); //  ВЫКЛючение текста "Текущий порог освещённости"

    progressBarWait2.setVisibility(View.VISIBLE);  //  ВКЛючение вращающегося кольца ожидания

//  finish(); выход из программы
  } // -------------------------------------------------------------------------------------------------------------------------------------------------


  // Метод чтения положения движка слайдера в процентах (%) при его передвижении -----------------------------------------------------------------------
  @SuppressLint("SetTextI18n") @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
  {
    textViewSetLightingLevel.setText(getString(R.string.stringSetLightingLevel) + " " + String.valueOf(seekBar.getProgress()) + "%");
  } // -------------------------------------------------------------------------------------------------------------------------------------------------

  @Override public void onStartTrackingTouch(SeekBar seekBar)
  {
    buttonConfirmSetNewLightingLevel.setVisibility(View.INVISIBLE); // кнопка исчезает, если мы решили подправить значение
  }

  // Метод вызывается после установки шкалы слайдера ---------------------------------------------------------------------------------------------------
  @Override public void onStopTrackingTouch(SeekBar seekBar)
  {
    // кнопка "ЗАПИСАТЬ" появляется
    buttonConfirmSetNewLightingLevel.setVisibility(View.VISIBLE);
  } // -------------------------------------------------------------------------------------------------------------------------------------------------


} // public class MainActivity ----------------------------------------------------------------------------------------------------------------------------------

////    Включение вибрации в телефоне:
////    Вставить в файл AndroidManifest.xml строчку:
////       <uses-permission android:name="android.permission.VIBRATE"/>
//    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//// Vibrate for 500 milliseconds
//    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

