package com.example.chucknorrisjokes

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class JokeTouchHelper(private val onJokeRemoved: (position: Int) -> Unit, private val onItemMoved:(ini: Int, final : Int) -> Unit ):
    ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(UP or DOWN, RIGHT)
    {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
        {
            val initialPos = viewHolder.adapterPosition
            val targetPos = target.adapterPosition
            onItemMoved(initialPos, targetPos)

            return true
        }



        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int)
        {
            val pos = viewHolder.adapterPosition
            onJokeRemoved(pos)
        }

    }


    )