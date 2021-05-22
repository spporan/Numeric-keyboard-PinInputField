package com.poran.numeric_keyboard_pininputfield.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.poran.numeric_keyboard_pininputfield.R
import kotlin.math.min


class PinInputFields : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttributes(context, attrs, -1)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttributes(context, attrs, defStyleAttr)
    }

    /**
     * Reference to the [EditText] which will take user input.
     */

    private var fields: ArrayList<CustomizedEditText> = ArrayList()

    private var pinFieldList: ArrayList<CustomizedEditText> = ArrayList()
        get() = field

    /**
     * number of field
     */
    private var fieldLength: Int = 0

    /**
     * Input Field height
     */
    private var fieldHeight: Int = 0

    /**
     * Input Field height
     */
    private var fieldCornerRadius: Int = 0

    /**
     * Input Field width
     */
    private var fieldWidth: Int = 0

    /**
     * Input Field stroke
     */
    var fieldStroke: Int = 0

    /**
     * Text size of a pin field.
     */
    var fieldTextSize: Float = 0F

    /**
     * default color of a pin field.
     */
    private var defColorStateList: ColorStateList? = null

    private var defColorAttr: Int = 0

    private var activeColorAttr: Int = 0

    private var errorColorAttr: Int = 0
        set(value) {
            field = value
            if (childCount > 0) {
               // updateView(getChildAt(0))
            }
        }

    /**
     * activated color of a pin field
     */
    private var activeColorStateList: ColorStateList? = null


    /**
     * error color of a pin field
     */
    private var errorColorStateList: ColorStateList? = null



    /**
     * Initialize all attributes
     */

    private fun initAttributes(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.PinField, defStyleAttr, 0)
        val defaultTextSize = context.resources.getDimensionPixelSize(R.dimen.input_field_text_size)
        val defaultWidth = context.resources.getDimensionPixelSize(R.dimen.input_field_width)
        val defaultHeight = context.resources.getDimensionPixelSize(R.dimen.input_field_height)
        val defaultCornerRadius = context.resources.getDimensionPixelSize(R.dimen.margin_5)
        val defaultStroke = context.resources.getDimensionPixelSize(R.dimen.input_field_stroke)

        fieldLength = attributes.getInteger(R.styleable.PinField_numberOfField, DEF_FIELD_LENGTH)
        fieldWidth = attributes.getDimensionPixelSize(R.styleable.PinField_fieldWidth, defaultWidth)
        fieldHeight = attributes.getDimensionPixelSize(R.styleable.PinField_fieldHeight, defaultHeight)
        fieldTextSize = attributes.getDimensionPixelSize(R.styleable.PinField_fieldTextSize, defaultTextSize).toFloat()
        defColorStateList = attributes.getColorStateList(R.styleable.PinField_defColor)
        activeColorStateList = attributes.getColorStateList(R.styleable.PinField_activeColor)
        errorColorStateList = attributes.getColorStateList(R.styleable.PinField_defColor)
        defColorAttr = attributes.getColor(R.styleable.PinField_defColor, Color.GRAY)
        activeColorAttr = attributes.getColor(R.styleable.PinField_activeColor,Color.BLACK)
        errorColorAttr = attributes.getColor(R.styleable.PinField_defColor,Color.RED)
        fieldCornerRadius = attributes.getDimensionPixelSize(R.styleable.PinField_fieldCornerRadius, defaultCornerRadius)
        fieldStroke = attributes.getDimensionPixelSize(R.styleable.PinField_fieldStroke,defaultStroke)
        post { initViews() }
    }

    private fun initViews() {
        addFieldIntoArray()
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.pin_input_field_layout, this, false)
        Log.d("EEE", "create instance")
        addView(layout)
    }

    private fun addFieldIntoArray(){
        val pinField1 = rootView.findViewById<CustomizedEditText>(R.id.inputPin1)
        val pinField2 = rootView.findViewById<CustomizedEditText>(R.id.inputPin2)
        val pinField3 = rootView.findViewById<CustomizedEditText>(R.id.inputPin3)
        val pinField4 = rootView.findViewById<CustomizedEditText>(R.id.inputPin4)
        val pinField5 = rootView.findViewById<CustomizedEditText>(R.id.inputPin5)
        val pinField6 = rootView.findViewById<CustomizedEditText>(R.id.inputPin6)
        fields.apply {
            add(pinField1)
            add(pinField2)
            add(pinField3)
            add(pinField4)
            add(pinField5)
            add(pinField6)
        }
        initPinField()
    }
   private fun CustomizedEditText.addAttr(){
        activeColorStateList?.let {
            activeColor = it
        }
        defColorStateList?.let {
            defColor = it
        }
        errorColorStateList?.let {
            errorColor = it
        }
        layoutParams = LinearLayout.LayoutParams(fieldWidth, fieldHeight)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, fieldTextSize)

        val drawableContainerState = background.constantState as DrawableContainerState?
        drawableContainerState?.let {
            val children = it.children
            val defaultItem = children[0] as LayerDrawable
            val activeItem = children[1] as LayerDrawable
            val errorItem = children[2] as LayerDrawable

            val defaultDrawable = defaultItem.getDrawable(0) as GradientDrawable
            val activeDrawable = activeItem.getDrawable(0) as GradientDrawable
            val errorDrawable = errorItem.getDrawable(0) as GradientDrawable
            activeDrawable.cornerRadius = fieldCornerRadius.toFloat()
            errorDrawable.cornerRadius = fieldCornerRadius.toFloat()
            defaultDrawable.cornerRadius = fieldCornerRadius.toFloat()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                defaultDrawable.setStroke(fieldStroke, defColorStateList)
                activeDrawable.setStroke(fieldStroke, activeColorStateList)
                errorDrawable.setStroke(fieldStroke, errorColorStateList)
            }else{
                defaultDrawable.setStroke(fieldStroke, defColorAttr)
                activeDrawable.setStroke(fieldStroke, activeColorAttr)
                errorDrawable.setStroke(fieldStroke, errorColorAttr)
            }

        }


    }
    private fun initPinField(){
        val totalField = min(fieldLength, MAX_FIELD_LENGTH)
        for(i in 1..fields.size){
            if(i > totalField){
                fields[i - 1].visibility = GONE
                continue
            }
            fields[i - 1].addAttr()
            pinFieldList.add(fields[i - 1])
        }

    }

    // set error in all input field
    fun setError(){
        pinFieldList.forEach {
            it.mIsError = true
        }
    }

    companion object{
        private val MAX_FIELD_LENGTH = 6
        private val DEF_FIELD_LENGTH = 6


    }
}