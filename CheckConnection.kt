import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CheckConnection {

    var connectionType by mutableStateOf("")

    private suspend fun run(context: Context) = withContext(Dispatchers.IO) {
        // Invoking the Connectivity Manager
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        while (true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkConOne(connectivityManager)
            } else {
                checkConTwo(connectivityManager)
            }
            delay(1000)
        }
    }

    private fun checkConOne(connectivityManager:ConnectivityManager){
        val nw = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(nw)
        connectionType = if (actNw != null) {
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Conexão WIFI"
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Conexão Dados Celular"
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Conexão Ethernet"
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Conexão Bluetooth"
                else -> "Sem conexão"

            }
        } else {
            "Sem conexão"
        }
    }

    private fun checkConTwo(connectivityManager:ConnectivityManager){
        val netInfo = connectivityManager.allNetworkInfo

        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected) connectionType = "Conexão WIFI"
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                connectionType = if (ni.isConnected) "Conexão de Dados Celular"
                else {
                    "Sem conexão"
                }
        }
    }
}
