# -*- coding: utf-8 -*-
from kivy.app import App
from kivy.core.window import Window
from kivy.uix.widget import Widget
from kivy.properties import StringProperty, NumericProperty
from send_invoice_server import NfcInvoiceServer


Window.size = (500, 200)
server = NfcInvoiceServer("usb:054c:06c3")


class MainWidget(Widget):
    address = StringProperty()
    amount = StringProperty()
    message = StringProperty()

    def __init__(self, **kwargs):
        super(MainWidget, self).__init__(**kwargs)
        self.address = ''
        self.amount = '0.0'
        self.message = 'NFC Invoice Data'

    def button_clicked(self):
        server.serve(self.address, float(self.amount), self.message)


class WindowApp(App):
    pass


if __name__ == '__main__':
    WindowApp().run()
