package com.example.repo.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.repo.R
import com.example.repo.extensions.load
import com.example.repo.githubapi.TrendingRepo
import kotlinx.android.synthetic.main.rv_repo_item.view.*

class RepoVH ( view: View) : RecyclerView.ViewHolder(view) {

    val languageDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_bg_language_indicator)

    fun bind(trendingRepo: TrendingRepo,selectedItem: Int, onRepoClicked : () -> Unit) {
        with(itemView) {
            ivAvatar.load(trendingRepo.avatar!!)
            tvAuthor.text = trendingRepo.author
            tvRepoName.text = trendingRepo.name

            tvDesc.text = trendingRepo.description
            if (trendingRepo.language != null) {
                tvLanguage.visibility = View.VISIBLE
                tvLanguage.text = trendingRepo.language
                if(trendingRepo.languageColor != null) {
                    languageDrawable?.setTint(trendingRepo.languageColor!!)
                }

            } else {
                tvLanguage.visibility = View.GONE
            }


            tvStars.text = "${trendingRepo.stars}"
            tvForks.text = "${trendingRepo.forks}"

            rlDetails.visibility = if(selectedItem == adapterPosition) View.VISIBLE else View.GONE

            setOnClickListener {
                onRepoClicked()
            }

        }
    }

}

fun Drawable.setTint(color: String) {
    try {
        (this as GradientDrawable).setColor(Color.parseColor(color))
    } catch (ex: IllegalAccessException) {
        ex.printStackTrace()
    }

}