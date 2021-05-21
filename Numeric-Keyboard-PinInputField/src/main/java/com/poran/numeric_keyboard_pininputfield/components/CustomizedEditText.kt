package com.poran.numeric_keyboard_pininputfield.components

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import com.poran.numeric_keyboard_pininputfield.R

@SuppressLint("AppCompatCustomView")
class PinInputField : EditText {
    var mIsError = false
        set(value) {
            value.also { field = it }
            refreshDrawableState()
        }

    var isActive = false
        set(value) {
            value.also { field = it }
            refreshDrawableState()
        }
    var defColor: ColorStateList? = null
    var activeColor: ColorStateList? = null
    var errorColor: ColorStateList? = null



    constructor(context: Context) : this(context, null, 0) {
        loadAttr(context,null,0)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        loadAttr(context,attrs,0)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        loadAttr(context,attrs,defStyleAttr)
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        loadAttr(context,attrs,defStyleAttr)
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error = null
                isActive = text.isNotEmpty() || isCursorVisible
                if(mIsError && text.isNotEmpty()){
                    mIsError = false
                }
                Log.d("EEE","focused onTextChanged <> $isActive")

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        })


    }

    override fun setError(error: CharSequence?) {
        super.setError(error)
        refreshDrawableState()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        isActive = focused || text.isNotEmpty()
        if(focused){
            Log.d("EEE","focused changed <>")
            mIsError = false
        }
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        super.setError(error, icon)
        refreshDrawableState()
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        Log.d("EEE","onCreateDrawableState <>")
        if (mIsError) {
            mergeDrawableStates(drawableState, STATE_ERROR)
            post {
                setTextColor(errorColor)
            }
        }else if(isActive){
            mergeDrawableStates(drawableState, STATE_ACTIVE)
            post {
                setTextColor(activeColor)
            }
        }
        return drawableState
    }



    override fun setTextColor(color: Int) {
        super.setTextColor(color)
    }


    companion object {
        private val STATE_ERROR = intArrayOf(R.attr.state_error)
        private val STATE_ACTIVE = intArrayOf(R.attr.state_active_field)
    }

    private fun loadAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.error, defStyleAttr, 0)
        defColor = attributes.getColorStateList(R.styleable.error_defColor)
        activeColor = attributes.getColorStateList(R.styleable.error_activeColor)
        errorColor = attributes.getColorStateList(R.styleable.error_fieldErrorColor)

    }
}