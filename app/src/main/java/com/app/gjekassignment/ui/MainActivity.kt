package com.app.gjekassignment.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.DiffUtil
import com.app.gjekassignment.GjekApp
import com.app.gjekassignment.data.User
import com.app.gjekassignment.databinding.ActivityMainBinding
import com.app.gjekassignment.ui.liked_users.LikedUsersActivity
import com.yuyakaido.android.cardstackview.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class MainActivity : AppCompatActivity(), CardStackListener {
    
    private lateinit var viewModel: UsersViewModel
    private lateinit var binding: ActivityMainBinding
    private val pb = PublishSubject.create<List<User>>()
    
    private val adapter by lazy { CardStackAdapter(this) }
    private val cardStackLayoutManager by lazy { CardStackLayoutManager(this, this) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        cardStackLayoutManager.cardStackSetting.visibleCount = 4
        binding.cardStackView.layoutManager = cardStackLayoutManager
        binding.cardStackView.adapter = adapter
        
        val likeSwipeSetting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
    
        val skipSwipeSetting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        
        binding.btnLike.setOnClickListener {
            if (adapter.itemCount > 0 && cardStackLayoutManager.topPosition <= adapter.itemCount) {
                cardStackLayoutManager.setSwipeAnimationSetting(likeSwipeSetting)
                binding.cardStackView.swipe()
            }
        }
        
        binding.btnSkip.setOnClickListener {
            if (adapter.itemCount > 0 && cardStackLayoutManager.topPosition <= adapter.itemCount) {
                cardStackLayoutManager.setSwipeAnimationSetting(skipSwipeSetting)
                binding.cardStackView.swipe()
            }
        }
        
        binding.btnFav.setOnClickListener {
            startActivity(Intent(this, LikedUsersActivity::class.java))
        }
        
        viewModel = ViewModelProvider(this, UsersViewModel.Factory((application as GjekApp).usersRepository)).get()
        viewModel.getUsersLiveData().observe(this, Observer {
            pb.onNext(it)
        })
        viewModel.getShowLoadingLiveData().observe(this, Observer { visible ->
            if (visible != null && cardStackLayoutManager.topPosition == adapter.itemCount)
                binding.progressBar.isVisible = visible
        })
        viewModel.init()
    
        pb.switchMap { new ->
            Observable.fromArray(adapter.getUsers())
                .map { old -> DiffResult(new, DiffUtil.calculateDiff(UsersDiffCallback(old, new))) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
        }.subscribe { diffResult ->
            adapter.setUsers(diffResult.new)
            diffResult.result.dispatchUpdatesTo(adapter)
    
            binding.progressBar.isVisible = false
        }
    }
    
    override fun onCardDisappeared(view: View?, position: Int) {
    }
    
    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }
    
    override fun onCardSwiped(direction: Direction?) {
        if (cardStackLayoutManager.topPosition == adapter.itemCount - 5)
            viewModel.loadMore()
    
        val user = adapter.getUserAtPos(cardStackLayoutManager.topPosition - 1)
        user ?: return
    
        when (direction) {
            Direction.Left -> viewModel.skipUser(user)
            Direction.Right -> viewModel.likeUser(user)
            else -> {}
        }
    }
    
    override fun onCardCanceled() {
    }
    
    override fun onCardAppeared(view: View?, position: Int) {
    }
    
    override fun onCardRewound() {
    }
    
    class UsersDiffCallback(private val old: List<User>, private val new: List<User>) : DiffUtil.Callback() {
        
        override fun getOldListSize() = old.size
        
        override fun getNewListSize() = new.size
        
        override fun areItemsTheSame(oldPosition: Int, newPosition: Int)
              = old[oldPosition].email == new[newPosition].email
        
        override fun areContentsTheSame(oldPosition: Int, newPosition: Int)
              = old[oldPosition] == new[newPosition]
    }
    
    data class DiffResult(val new: List<User>, val result: DiffUtil.DiffResult)
}