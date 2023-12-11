package com.example.batteryfinder

import android.app.AlarmManager
import android.app.PendingIntent
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import cn.huolala.battery.api.hookstub.cpu.CpuManager


class BatteryTestActivity : AppCompatActivity() {
    var alarmFlag = false
    var blueToothFlag = false
    var cpuFlag = false
    var locationFlag = false
    var sensorFlag = false
    var powerWakeLockFlag = false
    var wifiWakeLockFlag = false
    lateinit var manager: LocationManager
    val listener = LocationListener { location -> Log.e("hello", location.provider) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            Intent(this, SecondActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // alarm 相关
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmBtn = this.findViewById<Button>(R.id.alarm)
        alarmBtn.setOnClickListener {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10000,
                pendingIntent
            )
        }

        // bluetooth 相关
        val blueToothBtn = this.findViewById<Button>(R.id.blue_tooth_text)
        val bluetooth = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val scanSettings: ScanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // 设置连续扫描
            .build()

        val scanner = bluetooth.adapter.bluetoothLeScanner

        val callback = object : ScanCallback() {
            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
            }

            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
            }

        }



        blueToothBtn.setOnClickListener {
            if (scanner == null) {
                Log.e("battery", "没有打开蓝牙")
                return@setOnClickListener
            }
            blueToothFlag = !blueToothFlag
            if (blueToothFlag) {
                blueToothBtn.text = "bluetooth 使用中"
                scanner.startScan(null, scanSettings, callback)
            } else {
                blueToothBtn.text = "bluetooth 已暂停"
                scanner.stopScan(callback)
            }
        }


        // cpu 相关
        val cpuBtn = this.findViewById<Button>(R.id.cpu)
        cpuBtn.setOnClickListener {
            CpuManager.recordCpuStat()
        }


        // location 相关
        // 启动一个

        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val contract = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }
        contract.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        contract.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val locationBtn = this.findViewById<Button>(R.id.location)
        val locationListener = LocationListener {
            Log.e("hello", "get location ${it.provider}")
        }
        val locationListener2 = LocationListener {
            Log.e("hello", "get location ${it.provider}")
        }

        locationBtn.setOnClickListener {
            locationFlag = !locationFlag
            if (locationFlag) {
                locationBtn.text = "定位请求中"
                manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10L,
                    10f,
                    locationListener
                )


            } else {
                locationBtn.text = "定位请求已暂停"
                manager.removeUpdates(locationListener)
            }
        }

        // 多进程定位
        val ipcLocationBtn = this.findViewById<Button>(R.id.ipc_location)
        ipcLocationBtn.setOnClickListener {
            startService(Intent(this, IPCService::class.java))
        }

        // sensor 相关
        val sensorBtn = this.findViewById<Button>(R.id.sensor)
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }

        val sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorBtn.setOnClickListener {
            sensorFlag = !sensorFlag
            if (sensorFlag) {
                sensorBtn.text = "sensor 请求中"
                sensorManager.registerListener(sensorListener, sensor, 0)
            } else {
                sensorBtn.text = "sensor 已取消"
                sensorManager.unregisterListener(sensorListener)
            }
        }

        //power wakelock
        val powerBtn = this.findViewById<Button>(R.id.power_wl)
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        val mWakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            this.javaClass.canonicalName
        )
        mWakeLock.setReferenceCounted(false)
        powerBtn.setOnClickListener {
            powerWakeLockFlag = !powerWakeLockFlag
            if (powerWakeLockFlag) {
                mWakeLock.acquire()
                mWakeLock.acquire()
                powerBtn.text = "power wake lock acquire"
            } else {
                powerBtn.text = "power wake lock release"
                mWakeLock.release()
            }

        }

        // wifi wakelock
        val wifiBtn = this.findViewById<Button>(R.id.wifi_wl)
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiLock: WifiManager.WifiLock =
            wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock")
        wifiLock.setReferenceCounted(false)
        wifiBtn.setOnClickListener {
            wifiWakeLockFlag = !wifiWakeLockFlag
            if (wifiWakeLockFlag) {
                wifiBtn.text = "wifi wake lock acquire"
                wifiLock.acquire()
            } else {
                wifiBtn.text = "wifi wake lock release"
                wifiLock.release()
            }
        }
    }


}