# -*- coding: utf-8 -*-
import json
import nfc
import nfc.snep
import threading


class NfcInvoiceServer:
    def __init__(self, device_id):
        self.clf = nfc.ContactlessFrontend(device_id)
        self.invoice_container = {}

    def serve(self, address, amount, message):
        invoice_data = {
            "name": "Invoice Data from NFC",
            "addr": address,
            "amount": amount * 1000000,
            "msg": message
        }

        self.invoice_container = {
            "v": 2,
            "type": 2,
            "data": invoice_data
        }
        self.clf.connect(llcp={'on-connect': self.connected})

    def send_ndef_message(self, llc):
        snep = nfc.snep.SnepClient(llc)
        text = nfc.ndef.TextRecord(json.dumps(self.invoice_container))
        aar = nfc.ndef.Record('urn:nfc:ext:android.com:pkg', data='com.ryuta46.nemnfcinvoice')
        snep.put(nfc.ndef.Message(text, aar))

    def connected(self, llc):
        print('connected')
        threading.Thread(target=self.send_ndef_message, args=(llc,)).start()
        return True



