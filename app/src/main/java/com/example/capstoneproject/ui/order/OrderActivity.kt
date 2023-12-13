package com.example.capstoneproject.ui.order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.BuildConfig
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityOrderBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlin.properties.Delegates

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var viewModel: OrderViewModel
    private var itemDetails = mutableListOf<ItemDetails>()
    private val db = Firebase.firestore
    private lateinit var orderId: String
    private lateinit var plan: String
    private val userID = FirebaseAuth.getInstance().currentUser!!.uid
    private var id_plan by Delegates.notNull<Int>()
    private var price by Delegates.notNull<Double>()
    private lateinit var customerDetails: CustomerDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        setupToolbar()
        buildUiKit()

        getCustomerDetail()

        plan = intent.getStringExtra(EXTRA_PACKAGE).toString()
        if (plan != null) {
            getPlanFromDb(userID)
            setPackageTo(plan)
        }

        binding.btnPayment.setOnClickListener {
            viewModel.checkLastTransaction(userID)
            viewModel.isTransactionAllowed.observe(this) { isTrasactionAllowed ->
                if (isTrasactionAllowed) {
                    itemDetails.clear()
                    itemDetails.add(
                        ItemDetails(
                            id_plan.toString(),
                            price,
                            1,
                            "$plan Package")
                    )
                    orderId = System.currentTimeMillis().toString()
                    UiKitApi.getDefaultInstance().startPaymentUiFlow(
                        activity = this,
                        launcher = launcher,
                        transactionDetails = transactionDetails(price),
                        customerDetails = customerDetails,
                        itemDetails = itemDetails
                    )
                } else {
                    Toast.makeText(this, "Please finish current transaction", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getCustomerDetail() {
        db.collection("user")
            .whereEqualTo("uid", userID)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    val firstname = userDocument.get("firstname").toString()
                    val lastname = userDocument.get("lastname").toString()
                    val email = userDocument.get("email").toString()
                    val mobile = userDocument.get("mobile").toString()
                    customerDetails = CustomerDetails(
                        customerIdentifier = userID,
                        firstName = firstname,
                        lastName = lastname,
                        email = email,
                        phone = mobile
                    )
                } else {
                    Log.d("FullnameProfile", "Something went wrong")
                }
            }
    }

    private fun getPlanFromDb(idUser: String) {
        db.collection("user")
            .whereEqualTo("uid", idUser)
            .limit(1)
            .get()
            .addOnSuccessListener { data ->
                Log.d("HomeFragment", data.size().toString())
                if (!data.isEmpty) {
                    val userDocument = data.documents[0]
                    val plan = userDocument.get("plan").toString()
                    setPackageFrom(plan)
                } else {
                    Log.d("OrderActivity", "Something went wrong")
                }
            }
    }

    private fun setPackageFrom(plan: String) {
        when (plan) {
            "Gold" -> {
                val goldColor = ContextCompat.getColor(this, R.color.gold)
                binding.tvPackageFrom.setTextColor(goldColor)
                binding.tvSpeedFrom.setTextColor(goldColor)
                binding.tvServiceDateFrom.setTextColor(goldColor)
                binding.tvLocationFrom.setTextColor(goldColor)
                binding.cardPackageFrom.setBackgroundResource(R.drawable.plan_gold)
                binding.tvPackageFrom.text = "Gold"
                binding.tvSpeedFrom.text = "Speed up to 50mb/s"
            }
            "Silver" -> {
                val silverColor = ContextCompat.getColor(this, R.color.silver)
                binding.tvPackageFrom.setTextColor(silverColor)
                binding.tvSpeedFrom.setTextColor(silverColor)
                binding.tvServiceDateFrom.setTextColor(silverColor)
                binding.tvLocationFrom.setTextColor(silverColor)
                binding.cardPackageFrom.setBackgroundResource(R.drawable.plan_silver)
                binding.tvPackageFrom.text = "Silver"
                binding.tvSpeedFrom.text = "Speed up to 30mb/s"
            }
            "Bronze" -> {
                val bronzeColor = ContextCompat.getColor(this, R.color.bronze)
                binding.tvPackageFrom.setTextColor(bronzeColor)
                binding.tvSpeedFrom.setTextColor(bronzeColor)
                binding.tvServiceDateFrom.setTextColor(bronzeColor)
                binding.tvLocationFrom.setTextColor(bronzeColor)
                binding.cardPackageFrom.setBackgroundResource(R.drawable.plan_bronze)
                binding.tvPackageFrom.text = "Bronze"
                binding.tvSpeedFrom.text = "Speed up to 15mb/s"
            }
            else -> {
                binding.cardPackageFrom.visibility = View.GONE
                binding.containerPackageTo.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = 0
                }
                binding.tvChangeTo.visibility = View.GONE
            }
        }
    }

    private fun setPackageTo(plan: String) {
        when (plan) {
            "Gold" -> {
                id_plan = 1
                price = 949000.0
                val goldColor = ContextCompat.getColor(this, R.color.gold)
                binding.tvPackageTo.setTextColor(goldColor)
                binding.tvSpeedTo.setTextColor(goldColor)
                binding.tvServiceDateTo.setTextColor(goldColor)
                binding.tvLocationTo.setTextColor(goldColor)
                binding.cardPackageTo.setBackgroundResource(R.drawable.plan_gold)
                binding.tvPackageTo.text = "Gold"
                binding.tvSpeedTo.text = "Speed up to 50mb/s"
                binding.tvPrice.text = "Rp 949.000"
            }
            "Silver" -> {
                id_plan = 2
                price = 549000.0
                val silverColor = ContextCompat.getColor(this, R.color.silver)
                binding.tvPackageTo.setTextColor(silverColor)
                binding.tvSpeedTo.setTextColor(silverColor)
                binding.tvServiceDateTo.setTextColor(silverColor)
                binding.tvLocationTo.setTextColor(silverColor)
                binding.cardPackageTo.setBackgroundResource(R.drawable.plan_silver)
                binding.tvPackageTo.text = "Silver"
                binding.tvSpeedTo.text = "Speed up to 30mb/s"
                binding.tvPrice.text = "Rp 549.000"
            }
            "Bronze" -> {
                id_plan = 3
                price = 249000.0
                val bronzeColor = ContextCompat.getColor(this, R.color.bronze)
                binding.tvPackageTo.setTextColor(bronzeColor)
                binding.tvSpeedTo.setTextColor(bronzeColor)
                binding.tvServiceDateTo.setTextColor(bronzeColor)
                binding.tvLocationTo.setTextColor(bronzeColor)
                binding.cardPackageTo.setBackgroundResource(R.drawable.plan_bronze)
                binding.tvPackageTo.text = "Bronze"
                binding.tvSpeedTo.text = "Speed up to 15mb/s"
                binding.tvPrice.text = "Rp 249.000"
            }
            else -> {
                Log.e("Err","404")
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
//                    Toast.makeText(this, "${transactionResult?.status}", Toast.LENGTH_LONG).show()
                    if (transactionResult != null) {
                        val transaction = hashMapOf(
                            "idOrder" to orderId,
                            "idUser" to userID,
                            "idService" to id_plan,
                            "status" to when(transactionResult.status) {
                                com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS -> "Success"
                                com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING -> "Pending"
                                else -> "Invalid"
                            },
                            "timestamp" to Timestamp.now()
                        )

                        when (transactionResult.status) {
                            com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS -> {
                                Toast.makeText(this, "Transaction Finished. ID: " + transactionResult.transactionId, Toast.LENGTH_SHORT).show()
                                db.collection("transaction")
                                    .add(transaction)
                                    .addOnSuccessListener {
                                        db.collection("user")
                                            .whereEqualTo("uid", userID)
                                            .get()
                                            .addOnSuccessListener { querySnapshot ->
                                                for (document in querySnapshot) {
                                                    val typePackage = when(id_plan) {
                                                        1 -> "Gold"
                                                        2 -> "Silver"
                                                        3 -> "Bronze"
                                                        else -> "None"
                                                    }
                                                    db.collection("user").document(document.id).update("plan", typePackage)
                                                        .addOnSuccessListener {
                                                            Log.d("Services", id_plan.toString())
                                                            Toast.makeText(this, "Successfully Update to Firestore", Toast.LENGTH_LONG).show()
                                                        }
                                                        .addOnFailureListener {
                                                            Log.d("Services", id_plan.toString())
                                                            Toast.makeText(this, "Failure Update to Firestore", Toast.LENGTH_LONG).show()
                                                        }
                                                }
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failure Add to Firestore", Toast.LENGTH_LONG).show()
                                    }
                            }
                            com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING -> {
                                Toast.makeText(this, "Transaction Pending. ID: " + transactionResult.transactionId, Toast.LENGTH_SHORT).show()
                                db.collection("transaction")
                                    .add(transaction)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Successfully Add to Firestore", Toast.LENGTH_LONG).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failure Add to Firestore", Toast.LENGTH_LONG).show()
                                    }
                            }
                            UiKitConstants.STATUS_CANCELED -> {
                                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_SHORT).show()
                            }
                            com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED -> {
                                Toast.makeText(this, "Transaction FAILED" + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_SHORT).show()
                            }
                            com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID -> {
                                Toast.makeText(this, "Transaction INVALID" + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this, "Transaction INVALID" + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_SHORT).show()
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }

    private fun transactionDetails(totalPrice: Double) : SnapTransactionDetail {
        return SnapTransactionDetail(
            orderId = orderId,
            grossAmount = totalPrice
        )
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(BuildConfig.MERCHANT_SERVER)
            .withMerchantClientKey(BuildConfig.CLIENT_KEY)
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FF1E1E1E", "#FF01A1DD", "#FF1E1E1E"))
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    companion object {
        const val EXTRA_PACKAGE = "extra_package"
    }
}