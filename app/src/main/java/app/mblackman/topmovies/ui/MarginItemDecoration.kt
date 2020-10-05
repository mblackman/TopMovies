package app.mblackman.topmovies.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * A [RecyclerView.ItemDecoration] for adding pixel margins around items in the list.
 */
class MarginItemDecoration(
    private val topMargin: Int? = null,
    private val bottomMargin: Int? = null,
    private val leftMargin: Int? = null,
    private val rightMargin: Int? = null
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (topMargin == null && bottomMargin == null && leftMargin == null && rightMargin == null) {
            return
        }

        val itemPosition: Int = parent.getChildAdapterPosition(view)
        val totalCount: Int = parent.adapter?.itemCount ?: 0

        if (itemPosition in 0 until totalCount) {
            topMargin?.let { outRect.top = it }
            bottomMargin?.let { outRect.bottom = it }
            rightMargin?.let { outRect.right = it }
            leftMargin?.let { outRect.left = it }
        }
    }

}