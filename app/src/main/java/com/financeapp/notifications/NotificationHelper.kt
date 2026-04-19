package com.financeapp.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.financeapp.R
import com.financeapp.data.models.Category
import java.text.NumberFormat
import java.util.Locale

object NotificationHelper {

    private const val CHANNEL_ID = "finance_alerts"
    private const val CHANNEL_NAME = "Alertas Financeiros"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Alertas de limite de gastos por categoria" }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun notifyLimiteCategoria(context: Context, category: Category, gastoAtual: Double) {
        val fmt = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val percent = ((gastoAtual / category.budgetLimit) * 100).toInt()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle("⚠️ Limite de ${category.name} em ${percent}%")
            .setContentText("Você gastou ${fmt.format(gastoAtual)} de ${fmt.format(category.budgetLimit)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            NotificationManagerCompat.from(context).notify(category.id, notification)
        }
    }
}
