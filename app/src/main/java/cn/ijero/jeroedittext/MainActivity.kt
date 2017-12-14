package cn.ijero.jeroedittext

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cn.ijero.edittext.JeroSingleLineEditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
           if (isChecked){
               jeroSingleLineEditText.inputType = JeroSingleLineEditText.TYPE_NUMBER
           }
        }

        textRadioButton.setOnCheckedChangeListener { buttonView, isChecked ->
           if (isChecked){
               jeroSingleLineEditText.inputType = JeroSingleLineEditText.TYPE_TEXT
           }
        }

    }
}
