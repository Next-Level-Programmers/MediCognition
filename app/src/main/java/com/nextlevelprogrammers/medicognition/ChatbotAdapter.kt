package com.nextlevelprogrammers.medicognition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatbotAdapter(private val chatMessages: MutableList<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENDER = 1
    private val VIEW_TYPE_CHATBOT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutResourceId = R.layout.item_chat_message
        val view = LayoutInflater.from(parent.context).inflate(layoutResourceId, parent, false)

        return when (viewType) {
            VIEW_TYPE_SENDER -> SenderViewHolder(view)
            VIEW_TYPE_CHATBOT -> ChatbotViewHolder(view)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        when (holder) {
            is SenderViewHolder -> holder.bind(chatMessage)
            is ChatbotViewHolder -> holder.bind(chatMessage)
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].sender == "User") VIEW_TYPE_SENDER else VIEW_TYPE_CHATBOT
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderLayout: RelativeLayout = itemView.findViewById(R.id.layoutSender)
        private val chatbotLayout: RelativeLayout = itemView.findViewById(R.id.layoutChatbot)
        private val messageText: TextView = itemView.findViewById(R.id.textMessageSender)

        fun bind(chatMessage: ChatMessage) {
            senderLayout.visibility = View.VISIBLE
            chatbotLayout.visibility = View.GONE
            messageText.text = chatMessage.text
        }
    }

    inner class ChatbotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderLayout: RelativeLayout = itemView.findViewById(R.id.layoutSender)
        private val chatbotLayout: RelativeLayout = itemView.findViewById(R.id.layoutChatbot)
        private val messageText: TextView = itemView.findViewById(R.id.textMessageChatbot)

        fun bind(chatMessage: ChatMessage) {
            senderLayout.visibility = View.GONE
            chatbotLayout.visibility = View.VISIBLE
            messageText.text = chatMessage.text
        }
    }

    fun addMessage(userMessage: String?, chatbotResponse: String?) {
        userMessage?.let {
            val currentTimeMillis = System.currentTimeMillis()
            chatMessages.add(ChatMessage(sender = "User", text = userMessage))
            notifyItemInserted(chatMessages.size - 1)
        }
        chatbotResponse?.let {
            val currentTimeMillis = System.currentTimeMillis()
            chatMessages.add(ChatMessage(sender = "Chatbot", text = chatbotResponse))
            notifyItemInserted(chatMessages.size - 1)
        }
    }
}