package br.eti.roberto.consultorio

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Person
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import br.eti.roberto.consultorio.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Tratar erro de permissão
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                Log.d("loccc", location.toString())
                val toast = Toast.makeText(this, "latitude:"
                        +location!!.latitude.toString()+" longitude:"
                        +location.longitude!!.toString(), Toast.LENGTH_LONG)
                toast.show()
            }


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            //snackbar ou toast não é notifications
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()


            //notification push
            val CHANNEL_ID = "1710"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = getString(R.string.NotTitle)
                val descriptionText = getString(R.string.lorem_ipsum)
                val importance = NotificationManager.
                IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID,
                    name, importance).apply {
                    description = descriptionText
                }


                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as
                            NotificationManager
                notificationManager.createNotificationChannel(channel)


                val notification = NotificationCompat.Builder(this,
                    CHANNEL_ID)
                    .setContentTitle("Minha primeira notificação")
                    .setContentText("Minha notificaçãozinha")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(1710,notification)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}