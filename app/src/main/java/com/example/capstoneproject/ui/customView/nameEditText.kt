package com.example.capstoneproject.ui.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class nameEditText : AppCompatEditText {

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
                error = if (text.toString().length <= 1) {
                    "First name must be a minimum of 2 characters"
                } else if(text.toString().length >= 150) {
                    "First name maximum 150 characters"
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
