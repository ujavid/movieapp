package com.example.kotlin.movieapp.ui.main

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import com.example.kotlin.movieapp.ext.plusAssign
import com.example.kotlin.movieapp.manager.MovieManager
import com.example.kotlin.movieapp.model.Movie
import com.example.kotlin.movieapp.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(application: Application, movieManager: MovieManager) : BaseViewModel(application) {
    val title = "Movies"
    val data: ObservableField<String> = ObservableField()
    var disposables = CompositeDisposable()
    var initialized: Boolean = false

    init {
        disposables += movieManager.topRated(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onGetTopRatedSuccess, this::onGetTopRatedError)
    }

    private fun onGetTopRatedSuccess(movies: List<Movie>) {
        if (initialized) {
            //TODO: Notify user that new data is available, for now we immediately display it
            updateData(movies)
        } else {
            initialized = true
            if (movies.isEmpty()) {
                //TODO: Brand new state = show loading screen, for now we just show some text
                data.set("Loading Movies...")
            } else {
                updateData(movies)
            }
        }
    }

    private fun updateData(movies: List<Movie>) {
        data.set(
                StringBuilder().apply {
                    movies.forEach {
                        append("${it.id} - ${it.name}")
                        append(System.lineSeparator())
                    }
                }.toString()
        )
    }

    private fun onGetTopRatedError(error: Throwable) {
        data.set(error.toString())
    }

    override fun onCleared() {
        disposables.dispose()
    }

    class Factory(private val application: Application, private val movieManager: MovieManager) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(application, movieManager) as T
    }
}
