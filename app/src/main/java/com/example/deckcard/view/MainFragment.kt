package com.example.deckcard.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.deckcard.R
import com.example.deckcard.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var vm = MainViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.main_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataInit()
        initView()
    }

    private fun dataInit() {

        vm.cardList.observe(viewLifecycleOwner, Observer {

        })

        vm.compareResult.observe(viewLifecycleOwner, Observer {
            setView(it)
        })

        vm.shuffle()
    }

    private fun initView() {

    }

    private fun setView(result: Boolean) {

    }
}