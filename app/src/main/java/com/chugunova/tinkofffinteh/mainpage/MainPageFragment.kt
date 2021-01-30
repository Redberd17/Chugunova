package com.chugunova.tinkofffinteh.mainpage

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.chugunova.tinkofffinteh.R
import com.chugunova.tinkofffinteh.api.PostRepositoryProvider
import com.chugunova.tinkofffinteh.api.UserAPI
import com.chugunova.tinkofffinteh.model.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_page.*
import kotlin.random.Random

class MainPageFragment : Fragment() {

    lateinit var mPresentr: MainPagePresenter
    lateinit var mImageView: ImageView
    lateinit var mView: View
    lateinit var mForward: Button
    lateinit var mDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresentr = MainPagePresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.main_page, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mImageView = view.findViewById(R.id.glide)
        mForward = view.findViewById(R.id.forward)
        mDescription = view.findViewById(R.id.description)

        mForward.setOnClickListener { forwardPressed() }
    }

    override fun onResume() {
        super.onResume()
        downloadImage()
    }

    fun getRandomPage(): Int {
        return Random.nextInt(0, 100)
    }

    fun getRandomPost(): Int {
        return Random.nextInt(0, 4)
    }

    fun forwardPressed() {
        downloadImage()
    }

    fun downloadImage() {
        var posts: Result? = null
        val userApi = UserAPI.create()
        val repository = PostRepositoryProvider.providePostRepository(userApi)
        repository.searchUsers("top", getRandomPage())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                posts = result
                val randomPost = getRandomPost()
                description.text = posts?.result?.get(randomPost)?.description
                Glide
                    .with(this)
                    .load(posts?.result?.get(randomPost)?.gifURL)
                    .into(mImageView);
            }, { error ->
                error.printStackTrace()
            })
    }
}