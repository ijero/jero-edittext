package cn.ijero.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
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
    : RelativeLayout(ctx, attrs), AnkoLogger, TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (showClear) {
            autoShowClear = !s.isNullOrEmpty()
        }
    }

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_NUMBER = 1
        const val NO_ID = 0
    }

    private val rootLayout = LayoutInflater.from(ctx)
            .inflate(R.layout.layout_single_line_edit_text, this, true).apply {
        setListeners()
    }.singleLineRootLayout

    private var eyeResourceId: Int = NO_ID
    private var eyeCloseResourceId: Int = NO_ID

    private fun setListeners() {
        singleLineClearImageView.setOnClickListener {
            clearContent()
        }
        singleLineEyeImageView.apply {
            setOnClickListener {
                showContent = !showContent
            }
        }
        singleLineEditText.addTextChangedListener(this)
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
    private var autoShowClear = false
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

    var showClear = false
        set(value) {
            if (field != value) {
                field = value
                singleLineClearImageView.visibility = if (field && !singleLineEditText.text.isNullOrEmpty()) {
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
                eyeResourceId = NO_ID
                singleLineEyeImageView.setImageDrawable(field)
            }
        }

    var eyeCloseImage: Drawable? = null
        set(value) {
            if (field != value) {
                field = value
                eyeCloseResourceId = NO_ID
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

    var showContent = true
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

    private fun showContent() {
        when (inputType) {
            TYPE_TEXT -> {
                singleLineEditText.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_NORMAL
            }
            else -> {
                singleLineEditText.inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_NORMAL
            }
        }
        when {
            eyeCloseImage != null -> singleLineEyeImageView.setImageDrawable(eyeCloseImage)
            eyeCloseResourceId != NO_ID -> singleLineEyeImageView.setImageResource(eyeCloseResourceId)
            else -> singleLineEyeImageView.setImageResource(R.mipmap.eye_blocked)
        }
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
        clearImage = ta.getDrawable(R.styleable.JeroSingleLineEditText_clearImage)
        eyeImage = ta.getDrawable(R.styleable.JeroSingleLineEditText_eyeImage)
        eyeCloseImage = ta.getDrawable(R.styleable.JeroSingleLineEditText_eyeCloseImage)
        inputType = ta.getInt(R.styleable.JeroSingleLineEditText_inputType, inputType)
        showContent = ta.getBoolean(R.styleable.JeroSingleLineEditText_showContent, showContent)

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