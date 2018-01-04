package com.ryuta46.nemnfcinvoice

data class InvoiceData(
        val name: String,
        val addr: String,
        val amount: Long,
        val msg: String
)

data class InvoiceContainer(
        val v: Int = 2,
        val type: Int = 2,
        val data: InvoiceData
)