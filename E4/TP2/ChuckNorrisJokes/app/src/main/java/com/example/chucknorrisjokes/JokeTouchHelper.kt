package com.example.chucknorrisjokes

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class JokeTouchHelper(private val onJokeRemoved: (position: Int) -> Unit, private val onItemMoved:(ini: Int, final : Int) -> Unit ):
    ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(UP or DOWN,LEFT or RIGHT)
    {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean
        {
            val initial_pos = viewHolder.adapterPosition
            val target_pos = target.adapterPosition
            onItemMoved(initial_pos, target_pos)

            return true
        }// TODO("use it to reorder items")



        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int)
        {
            val pos = viewHolder.adapterPosition
            onJokeRemoved(pos)
        }//TODO("use it to delete items")

    }


    )