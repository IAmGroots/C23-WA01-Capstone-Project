package com.example.capstoneproject.ui.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class mobileEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttributeSet: Int) : super(context, attributeSet, defStyleAttributeSet) {
        init()
    }

    private fun init() {
        addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isPhoneNumberValid(text.toString()) || text.toString().length < 10) {
                    error = "Invalid mobile number format"
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(text: Editable?) {}
        })
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val pattern = Regex("^(8)\\d{9,}$")
        return pattern.matches(phoneNumber)
    }
}
