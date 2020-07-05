package com.talk2us.ui.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseError
import com.talk2us.R
import com.talk2us.models.Counsellor
import com.talk2us.ui.chat.ChatViewModel
import com.talk2us.utils.FirebaseUtils
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils

class SessionEndFragment : Fragment(),FirebaseUtils.FirebaseStateListener<Counsellor> {

    companion object {
        fun newInstance() = SessionEndFragment()
    }

    private lateinit var viewModel: SessionEndViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.session_end_fragment, container, false)
        val button=v.findViewById<Button>(R.id.end_session)
        button.setOnClickListener {
            viewModel.endSession(this)
        }
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SessionEndViewModel::class.java)


    }

    override fun onSuccess(counsellor: Counsellor?) {
        PrefManager.sessionEnded()
        ViewModelProviders.of(this).get(ChatViewModel::class.java).deleteAll()
        Utils.toast("Session successfully ended")
        activity?.finish()
    }

    override fun onError(e: DatabaseError?) {
        TODO("Not yet implemented")
    }

}
