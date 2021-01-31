package com.chugunova.tinkofffinteh.mainpage

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.chugunova.tinkofffinteh.R
import com.chugunova.tinkofffinteh.api.*
import com.chugunova.tinkofffinteh.model.Result
import com.chugunova.tinkofffinteh.model.SavedPosts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random


class MainPageFragment : Fragment() {

    private lateinit var mImageView: ImageView
    private lateinit var mView: View
    private lateinit var mForward: Button
    lateinit var mBack: Button
    private lateinit var mDescription: TextView
    lateinit var mPosts: Result
    lateinit var mUserApi: UserAPI
    lateinit var mRepository: PostRepository
    lateinit var mProgressBar: ProgressBar
    lateinit var savedPosts: ArrayList<SavedPosts>
    lateinit var url: String
    var mCurrentItem: Int = 0
    lateinit var mError: ImageView
    lateinit var mSection: String
    lateinit var mLatest: Button
    lateinit var mTop: Button
    lateinit var mHot: Button

    enum class Section(val section: String) {
        Latest("latest"),
        Top("top"),
        Hot("hot")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSection = Section.Latest.section

        mUserApi = UserAPI.create()
        mRepository = PostRepositoryProvider.providePostRepository(mUserApi)
        savedPosts = ArrayList()

        downloadImage()
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
        mBack = view.findViewById(R.id.back)
        mDescription = view.findViewById(R.id.description)
        mProgressBar = view.findViewById(R.id.progress)
        mError = view.findViewById(R.id.error)
        mLatest = view.findViewById(R.id.latest)
        mTop = view.findViewById(R.id.top)
        mHot = view.findViewById(R.id.hot)

        mForward.setOnClickListener { forwardPressed() }
        mBack.setOnClickListener { backPressed() }

        mLatest.setOnClickListener { selectedLatest() }
        mTop.setOnClickListener { selectedTop() }
        mHot.setOnClickListener { selectedHot() }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getRandomPage(): Int {
        return Random.nextInt(0, 2350)
    }

    private fun getRandomPost(): Int {
        return Random.nextInt(0, 4)
    }

    private fun forwardPressed() {
        mDescription.text = ""
        if (savedPosts.size != 0) {
            mBack.visibility = View.VISIBLE
        }
        if (mCurrentItem < savedPosts.size - 1) {
            mCurrentItem++
            setImageAndDescription()
        } else {
            mProgressBar.visibility = View.VISIBLE
            mImageView.visibility = View.GONE
            mError.visibility = View.GONE
            downloadImage()
        }
    }

    private fun backPressed() {
        mDescription.text = ""
        if (mCurrentItem > 0) {
            mCurrentItem--
        }

        if (mCurrentItem == 0) {
            mBack.visibility = View.GONE
        }

        setImageAndDescription()
    }

    private fun setImageAndDescription() {
        mProgressBar.visibility = View.GONE
        mError.visibility = View.GONE
        mImageView.visibility = View.VISIBLE
        if (savedPosts.size != 0) {
            Glide.with(this)
                .asGif()
                .load(savedPosts[mCurrentItem].url)
                .into(mImageView)
            mDescription.text = savedPosts[mCurrentItem].description
        }
    }

    private fun downloadImage() {
        mRepository.searchUsers(mSection, getRandomPage())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                mPosts = result
                val randomPost = getRandomPost()
                val description: String = mPosts.result[randomPost].description

                url = mPosts.result[randomPost].gifURL
                val test = SavedPosts(url, description)
                savedPosts.add(test)
                mCurrentItem = savedPosts.indexOf(test)
                Glide.with(this)
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            mProgressBar.visibility = View.GONE
                            mError.visibility = View.VISIBLE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            mError.visibility = View.GONE
                            mProgressBar.visibility = View.GONE
                            mImageView.visibility = View.VISIBLE
                            mDescription.text = description
                            return false
                        }
                    })
                    .into(mImageView)
            }, { error ->
                mProgressBar.visibility = View.GONE
                mError.visibility = View.VISIBLE
                error.printStackTrace()
            })
    }


    private fun selectedLatest() {
        mSection = Section.Latest.section
        mLatest.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGreen) }
        mTop.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGray) }
        mHot.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGray) }
    }

    private fun selectedTop() {
        mSection = Section.Top.section
        mLatest.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGray) }
        mTop.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGreen) }
        mHot.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGray) }
    }

    private fun selectedHot() {
        mSection = Section.Hot.section
        mLatest.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGray) }
        mTop.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGray) }
        mHot.background = activity?.let { ContextCompat.getDrawable(it, R.color.colorGreen) }
    }
}