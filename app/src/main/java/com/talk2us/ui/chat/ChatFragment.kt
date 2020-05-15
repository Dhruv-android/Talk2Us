package com.talk2us.ui.chat

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.talk2us.R
import com.talk2us.models.Message
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.absoluteValue

class ChatFragment : Fragment() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var chatAdapter: ChatAdapter
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
        v.findViewById<Button>(R.id.bt_send).setOnClickListener {
            viewModel.sendMessage(
                Message(
                    msg.text.toString(),
                    "Utils.getCurrentTime()",
                    false,
                    false
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
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(ChatViewModel::class.java)
        chatAdapter= ChatAdapter(requireContext())
        viewModel.allWords.observe(requireActivity(), Observer {
            it?.let {
                chatAdapter.update(it)
                recyclerView.smoothScrollToPosition(chatAdapter.itemCount)
            }
        })
        recyclerView.adapter=chatAdapter
    }
}
