package com.saaryogev.financeapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val host: NavHostFragment = supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
    private val amountText get() = findViewById<EditText>(R.id.amountText)
    private val paymentTypeText get() = findViewById<EditText>(R.id.paymentTypeText)
    private val paymentMethodText get() = findViewById<EditText>(R.id.paymentMethodText)
    private val paymentDateText get() = findViewById<EditText>(R.id.paymentDateText)
    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        account = intent.getParcelableExtra("account")
        paymentDateText.text.append(SimpleDateFormat("d/M/yy h:mm:ss", Locale.ENGLISH).format(Date()))
    }

    @KtorExperimentalAPI
    fun saveExpenseForm(view: View) {
        view.isEnabled = false
        val corsAnywhereUrl = "cors-anywhere.herokuapp.com"
        val backendUrl = "finance-app-be.herokuapp.com"
        val fullUrl = URLBuilder().apply {
            protocol = URLProtocol.HTTP
            host = corsAnywhereUrl
            encodedPath = "$backendUrl/expenses"
//            host = "10.0.2.2"
//            port = 8080
//            encodedPath = "/expenses"
        }.build()
        val expense = Expense(
            amountText.text.toString().toInt(),
            paymentTypeText.text.toString(),
            paymentMethodText.text.toString(),
            SimpleDateFormat("d/M/yy h:mm:ss", Locale.ENGLISH).parse(paymentDateText.text.toString()),
            account?.idToken ?: throw GoogleAuthException("Not signed in")
        )
        val client = HttpClient(CIO) {
            install(HttpTimeout){
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
        val response = runBlocking {
            try {
                return@runBlocking client.post<String>(fullUrl)
                {
                    contentType(ContentType.Application.Json)
                    header("Origin", "whatever")
                    body = expense
                }
            } catch (e: Exception) {
                return@runBlocking e.toString()
            }
        }
        AlertDialog.Builder(view.context).setTitle("Expense Processed").setMessage(response).setPositiveButton(android.R.string.yes
        ) { _, _ -> }.show()
        view.isEnabled = true
    }
}
