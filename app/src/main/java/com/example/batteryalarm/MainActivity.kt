package com.example.batteryalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.DeterminBatteryCharger
import com.example.batteryalarm.ui.theme.BatteryAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    notificationChannel(LocalContext.current)
                    BatteryAlarm()
                }
            }
        }
        val batteryReceiver = DeterminBatteryCharger { batteryLevel ->
        }
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, intentFilter)
    }
}


@Composable
fun BatteryAlarm() {
    val context = LocalContext.current
    val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
    val batLevel: Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    if (batLevel <= 20) {
        SetNotification(
            smallIcon = R.drawable.icon_batterylow,
            textTitle = "Battery Low",
            textContent = "The mobile must be placed on the charger"
        )
        ImageBattery(
            imageBattery = R.drawable.battery_low, descriptionText = "Battery Low"
        )
    }
    if (batLevel>20) {
        ImageBattery(
            imageBattery = R.drawable.battery_full, descriptionText = "Battery Full"
        )
    }

}

@Composable
fun ImageBattery(
    @DrawableRes imageBattery: Int,
    descriptionText: String,
    modifier: Modifier = Modifier
) {

    Image(
        painter = painterResource(id = imageBattery),
        contentDescription = descriptionText,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 50.dp)
    )
}

@Composable
fun SetNotification(
    @DrawableRes smallIcon: Int, textTitle: String, textContent: String
) {
    val context = LocalContext.current

    var bulder = NotificationCompat.Builder(context, "1")
        .setSmallIcon(smallIcon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    NotificationManagerCompat.from(context).notify(99,bulder.build())
}


private fun notificationChannel(context: Context) {
    val name = "Battery Low"
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel("1", name, importance)
    channel.description = "Battery is Low"
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BatteryAlarmPreview() {
        BatteryAlarm()

}

