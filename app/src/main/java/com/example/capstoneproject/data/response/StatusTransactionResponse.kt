package com.example.capstoneproject.data.response

import com.google.gson.annotations.SerializedName

data class StatusTransactionResponse(

	@field:SerializedName("status_message")
	val statusMessage: String? = null,

	@field:SerializedName("transaction_id")
	val transactionId: String? = null,

	@field:SerializedName("fraud_status")
	val fraudStatus: String? = null,

	@field:SerializedName("approval_code")
	val approvalCode: String? = null,

	@field:SerializedName("transaction_status")
	val transactionStatus: String? = null,

	@field:SerializedName("status_code")
	val statusCode: String? = null,

	@field:SerializedName("reference_id")
	val referenceId: String? = null,

	@field:SerializedName("signature_key")
	val signatureKey: String? = null,

	@field:SerializedName("payment_option_type")
	val paymentOptionType: String? = null,

	@field:SerializedName("gross_amount")
	val grossAmount: String? = null,

	@field:SerializedName("card_type")
	val cardType: String? = null,

	@field:SerializedName("shopeepay_reference_number")
	val shopeepayReferenceNumber: String? = null,

	@field:SerializedName("payment_type")
	val paymentType: String? = null,

	@field:SerializedName("bank")
	val bank: String? = null,

	@field:SerializedName("masked_card")
	val maskedCard: String? = null,

	@field:SerializedName("transaction_time")
	val transactionTime: String? = null,

	@field:SerializedName("channel_response_code")
	val channelResponseCode: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("channel_response_message")
	val channelResponseMessage: String? = null
)
