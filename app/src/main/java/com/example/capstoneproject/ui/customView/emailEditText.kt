package com.example.capstoneproject.ui.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class emailEditText : AppCompatEditText {

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
                if (text.isNullOrEmpty()) {
                    error = null
                } else {
                    validateEmail(text.toString())
                }
            }

            override fun afterTextChanged(text: Editable?) {}
        })
    }

    private fun validateEmail(email: String) {
        error = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid e-mail address format"
        } else {
            null
        }
    }
}
