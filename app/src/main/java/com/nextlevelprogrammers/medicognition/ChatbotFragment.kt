package com.nextlevelprogrammers.medicognition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sendbird.android.GroupChannel
import com.sendbird.android.MessageListUIParams
import com.sendbird.uikit.adapters.MessageListAdapter

class ChatbotFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_chatbot, container, false)

        val recyclerViewChatbot = rootView.findViewById<RecyclerView>(R.id.recyclerViewChatbot)
        recyclerViewChatbot.layoutManager = LinearLayoutManager(requireContext())

        // Initialize CardViewAdapter instead of ChatbotAdapter
        val cardViewAdapter = CardViewAdapter(generativeModel.groupChannel, MessageListUIParams())
        recyclerViewChatbot.adapter = cardViewAdapter

        rootView.findViewById<ImageButton>(R.id.buttonSend).setOnClickListener {
            val userInput = rootView.findViewById<EditText>(R.id.editTextUserInput).text.toString().trim()
            if (userInput.isNotEmpty()) {
                // Add user input to the chat adapter
                cardViewAdapter.addMessage(userInput)
                rootView.findViewById<EditText>(R.id.editTextUserInput).text.clear()
            } else {
                Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }

    override fun provideMessageListAdapter(channel: GroupChannel, uiParams: MessageListUIParams): MessageListAdapter {
        return CardViewAdapter(channel, uiParams)
    }
}

open class MessageListAdapterProvider {

}
