package com.ryuta46.nemnfcinvoice

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import java.util.*


class InvoiceActivity : AppCompatActivity() {
    companion object {
        private val TAG = InvoiceActivity::class.java.simpleName

        private val TARGE_CLASS_NAME = "org.nem.nac.ui.activities.NewTransactionActivity"
        private val EXTRA_STR_ADDRESS = TARGE_CLASS_NAME + ".e-address"
        private val EXTRA_DOUBLE_AMOUNT = TARGE_CLASS_NAME + ".e-amount"
        private val EXTRA_STR_MESSAGE = TARGE_CLASS_NAME + ".e-message"
        private val EXTRA_BOOL_ENCRYPTED = TARGE_CLASS_NAME + ".e-encrypted"

    }

    private fun launchInvoiceActivity(invoiceData: InvoiceData) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName("org.nem.nac.mainnet", "org.nem.nac.ui.activities.NewTransactionActivity")

        intent.putExtra(EXTRA_STR_ADDRESS, invoiceData.addr)
                .putExtra(EXTRA_STR_MESSAGE, invoiceData.msg)
                .putExtra(EXTRA_DOUBLE_AMOUNT, invoiceData.amount.toDouble() / Math.pow(10.0, 6.0))
                .putExtra(EXTRA_BOOL_ENCRYPTED, true)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)

        // インテントの取得
        // NDEF対応カードの検出かチェック
        val action = intent.action
        if (action != NfcAdapter.ACTION_NDEF_DISCOVERED) {
            return
        }
        // Ndefメッセージの取得
        val messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES).map {
            it as NdefMessage
        }

        messages.forEach { message ->
            message.records.forEach { record ->
                record.payload?.let { payload ->
                    if (payload.isNotEmpty() and Arrays.equals(NdefRecord.RTD_TEXT, record.type)) {
                        val langBytes = payload[0].toInt() + 1 // +1 is langBytes itself
                        val invoiceText = String(payload.drop(langBytes).toByteArray())

                        val invoiceContainer = Gson().fromJson(invoiceText, InvoiceContainer::class.java)
                        launchInvoiceActivity(invoiceContainer.data)
                    }
                }
            }
        }
    }
}
