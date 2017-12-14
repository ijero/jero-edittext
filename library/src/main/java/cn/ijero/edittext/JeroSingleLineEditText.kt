package cn.ijero.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.layout_single_line_edit_text.view.*
import org.jetbrains.anko.AnkoLogger

/**
 * @author Jero . Created on 2017/12/14.
 */
class JeroSingleLineEditText
@JvmOverloads
constructor(ctx: Context, attrs: AttributeSet? = null)
    : RelativeLayout(ctx, attrs), AnkoLogger {

    private val rootLayout = LayoutInflater.from(ctx)
            .inflate(R.layout.layout_single_line_edit_text, this, true).apply {
        setListeners()
    }.singleLineRootLayout

    private fun setListeners() {
        singleLineClearImageView.setOnClickListener {
            clearContent()
        }
        singleLineEyeImageView.apply {
            setOnClickListener {
                showContent = !showContent
            }
        }
    }

    private fun clearContent() {
        singleLineEditText.setText("")
    }

    var text: CharSequence? = null
        set(value) {
            if (field != value) {
                field = value
            }
            singleLineEditText.setText(field)
            singleLineEditText.setSelection(text?.length ?: 0)
        }
        get() = singleLineEditText.text.toString()

    var hint: CharSequence? = null
        set(value) {
            if (field != value) {
                field = value
            }
            singleLineEditText.hint = field
        }

    var textColorHint: ColorStateList? = null
        set(value) {
            if (field != value) {
                field = value
                singleLineEditText.setHintTextColor(field)
            }
        }
    var textColor: ColorStateList? = null
        set(value) {
            if (field != value) {
                field = value
                singleLineEditText.setTextColor(field)
            }
        }
    var textSize = 0F
        set(value) {
            if (field != value) {
                field = value
                singleLineEditText.paint.textSize = field
            }
        }
    var showClear = false
        set(value) {
            if (field != value) {
                field = value
                singleLineClearImageView.visibility = if (field) {
                    VISIBLE
                } else {
                    GONE
                }
            }
        }
    var showEye = false
        set(value) {
            if (field != value) {
                field = value
                singleLineEyeImageView.visibility = if (field) {
                    VISIBLE
                } else {
                    GONE
                }
            }
        }

    var clearImage: Drawable? = null
        set(value) {
            if (field != value) {
                field = value
                singleLineClearImageView.setImageDrawable(field)
            }
        }

    var eyeImage: Drawable? = null
        set(value) {
            if (field != value) {
                field = value
                eyeResourceId = 0
                singleLineEyeImageView.setImageDrawable(field)
            }
        }

    var eyeCloseImage: Drawable? = null
        set(value) {
            if (field != value) {
                field = value
                eyeCloseResourceId = 0
                singleLineEyeImageView.setImageDrawable(field)
            }
        }

    var inputType = TYPE_TEXT
        set(value) {
            if (field != value) {
                field = value
                clearContent()
                changeInputType()
            }
        }

    var showContent = false
        set(value) {
            if (field != value) {
                field = value
                changeInputType()
            }
        }

    private fun changeInputType() {
        when (showContent) {
            true -> {
                showContent()
            }
            else -> {
                hideContent()
            }
        }
        singleLineEditText.setSelection(text?.length ?: 0)
    }

    private fun hideContent() {
        when (inputType) {
            TYPE_TEXT -> {
                singleLineEditText.inputType = EditorInfo.TYPE_CLASS_TEXT
            }
            else -> {
                singleLineEditText.inputType = EditorInfo.TYPE_CLASS_NUMBER
            }
        }
        when {
            eyeCloseImage != null -> singleLineEyeImageView.setImageDrawable(eyeCloseImage)
            eyeCloseResourceId != NO_ID -> singleLineEyeImageView.setImageResource(eyeCloseResourceId)
            else -> singleLineEyeImageView.setImageResource(R.mipmap.eye_blocked)
        }
    }

    private fun showContent() {
        when (inputType) {
            TYPE_TEXT -> {
                singleLineEditText.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            }
            else -> {
                singleLineEditText.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD
            }
        }
        when {
            eyeImage != null -> singleLineEyeImageView.setImageDrawable(eyeImage)
            eyeResourceId != NO_ID -> singleLineEyeImageView.setImageResource(eyeResourceId)
            else -> singleLineEyeImageView.setImageResource(R.mipmap.eye)
        }
    }

    private var eyeResourceId: Int = NO_ID
    private var eyeCloseResourceId: Int = NO_ID

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_NUMBER = 1
        const val NO_ID = 0
    }

    init {
        applyStyle(attrs)
    }

    private fun applyStyle(attrs: AttributeSet?) {
        attrs ?: return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.JeroSingleLineEditText)
        text = ta.getText(R.styleable.JeroSingleLineEditText_android_text)
        hint = ta.getText(R.styleable.JeroSingleLineEditText_android_hint)
        textColorHint = ta.getColorStateList(R.styleable.JeroSingleLineEditText_android_textColorHint)
        textColor = ta.getColorStateList(R.styleable.JeroSingleLineEditText_android_textColor)
        textSize = ta.getDimension(R.styleable.JeroSingleLineEditText_android_textSize, textSize)
        showClear = ta.getBoolean(R.styleable.JeroSingleLineEditText_showClear, showClear)
        showEye = ta.getBoolean(R.styleable.JeroSingleLineEditText_showEye, showEye)
        showContent = ta.getBoolean(R.styleable.JeroSingleLineEditText_showContent, showContent)
        clearImage = ta.getDrawable(R.styleable.JeroSingleLineEditText_clearImage)
        eyeImage = ta.getDrawable(R.styleable.JeroSingleLineEditText_eyeImage)
        eyeCloseImage = ta.getDrawable(R.styleable.JeroSingleLineEditText_eyeCloseImage)
        inputType = ta.getInt(R.styleable.JeroSingleLineEditText_inputType, inputType)

        ta.recycle()
    }

    fun eyeResource(eyeResourceId: Int, eyeCloseResourceId: Int) {
        if (eyeCloseResourceId != NO_ID && this.eyeResourceId != eyeResourceId) {
            eyeImage = null
            singleLineEyeImageView.setImageResource(eyeResourceId)
        }

        if (eyeCloseResourceId != NO_ID && this.eyeCloseResourceId != eyeCloseResourceId) {
            eyeCloseImage = null
            singleLineEyeImageView.setImageResource(eyeCloseResourceId)
        }
    }

}