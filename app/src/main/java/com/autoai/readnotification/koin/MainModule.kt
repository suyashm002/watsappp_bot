package com.autoai.readnotification.koin

import com.autoai.readnotification.viewModel.MessageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        MessageViewModel()
    }
}