package com.talk2us.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.talk2us.R
import com.talk2us.models.Client
import com.talk2us.models.Message
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.absoluteValue

class ChatFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    lateinit var progress: ProgressBar

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.chat_fragment, container, false)
        recyclerView = v.findViewById(R.id.rv_message_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val msg = v.findViewById<EditText>(R.id.et_message)
        progress = v.findViewById(R.id.progress)
        v.findViewById<Button>(R.id.bt_send).setOnClickListener {
            viewModel.sendMessage(
                Message(
                    msg.text.toString(),
                    Utils.getTime(),
                    false,
                    false,
                    "Client"
                )
            )
            msg.setText("")
        }
        msg.setOnClickListener {
            recyclerView.smoothScrollToPosition(chatAdapter.itemCount)
        }
        var verticalScrollOffset = AtomicInteger(0)
        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            val y = oldBottom - bottom
            if (y.absoluteValue > 0) {
                // if y is positive the keyboard is up else it's down
                recyclerView.post {
                    if (y > 0 || verticalScrollOffset.get().absoluteValue >= y.absoluteValue) {
                        recyclerView.scrollBy(0, y)
                    } else {
                        recyclerView.scrollBy(0, verticalScrollOffset.get())
                    }
                }
            }
        }
        val mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser?.uid != null && PrefManager.getBoolean(
                R.string.not_registered,
                true
            )
        ) {

            val firebaseUser = mAuth.currentUser
            if (firebaseUser != null) {
                val userId = firebaseUser.uid
                PrefManager.putString(R.string.client_id, userId)
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("client").child(userId)
                myRef.setValue(
                    Client(
                        PrefManager.getString(
                            R.string.phone_number,
                            "Not available"
                        )
                    )
                ).addOnSuccessListener {
                    PrefManager.putBoolean(R.string.not_registered, false)
                    Utils.toast("User registered")
                }
            }
        }

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(ChatViewModel::class.java)
        chatAdapter = ChatAdapter(requireContext())
        viewModel.allWords.observe(requireActivity(), Observer {
            it?.let {
                chatAdapter.update(it)
                recyclerView.smoothScrollToPosition(chatAdapter.itemCount)
            }
        })
        viewModel.progress.observe(requireActivity(), Observer {
            if (it) {
                progress.visibility = View.VISIBLE
            } else {
                progress.visibility = View.INVISIBLE
            }
        })
        recyclerView.adapter = chatAdapter
        FirebaseDatabase.getInstance().getReference("chatMessages").child(
            PrefManager.getString(
                R.string.counsellor_id,
                "not available"
            ) + PrefManager.getString(R.string.client_id, "Not_Available")
        ).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (message in p0.children){
                    val messag=message.getValue(Message::class.java)
                    viewModel.insertLocally(messag as Message)
                }
            }

        })
    }
}
