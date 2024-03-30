package com.nextlevelprogrammers.medicognition

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sendbird.android.GroupChannel
import com.sendbird.android.BaseMessage
import com.sendbird.uikit.adapters.MessageListAdapter
import com.sendbird.uikit.interfaces.MessageListUIParams

class CardViewAdapter(
    channel: GroupChannel,
    uiParams: MessageListUIParams
) : MessageListAdapter(channel, uiParams) {
    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isCardView()) return CARD_VIEW_TYPE
        else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        if (viewType == CARD_VIEW_TYPE) {
            val inflater = LayoutInflater.from(parent.context)
            return CardViewHolder(
                CardViewMessageBinding.inflate(inflater, parent, false)
            )
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    private class CardViewHolder(
        val binding: CardViewMessageBinding
    ) : MessageViewHolder(binding.root) {
        ...
    }

    companion object {
        const val CARD_VIEW_TYPE = 1000
    }

    private fun BaseMessage.isCardView(): Boolean {
        return extendedMessage.containsKey("custom_view")
    }

}