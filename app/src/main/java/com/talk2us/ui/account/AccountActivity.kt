package com.talk2us.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.talk2us.R
import com.talk2us.databinding.ActivityAccountBinding
import com.talk2us.utils.Constants
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils
import kotlinx.android.synthetic.main.activity_welcome.*

class AccountActivity : AppCompatActivity() {
    public lateinit var  nam:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityAccountBinding=
            DataBindingUtil.setContentView(this, R.layout.activity_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner=this
        supportActionBar?.title="Change name"
        val viewModel:AccountViewModel= ViewModelProviders.of(this).get(AccountViewModel::class.java)
        binding.viewModel=viewModel
        nam=findViewById(R.id.name)
        nam.setText(PrefManager.getString(Constants.NAME,"Pallav"))
        nam.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString()==PrefManager.getString(Constants.NAME,"Pallav")){
                    viewModel.change.postValue(false)
                }
                else{
                    viewModel.change.postValue(true)
                }
            }

        })
    }
    fun onClick(v: View){
        Utils.toast("Name changed")
        PrefManager.putString(Constants.NAME,nam.text.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
