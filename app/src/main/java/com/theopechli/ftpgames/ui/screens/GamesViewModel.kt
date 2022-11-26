package com.theopechli.ftpgames.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.theopechli.ftpgames.GamesApplication
import com.theopechli.ftpgames.data.GamesRepository
import com.theopechli.ftpgames.model.Game
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GamesUiState {
    data class Success(val games: List<Game>) : GamesUiState
    object Error : GamesUiState
    object Loading : GamesUiState
}

class GamesViewModel(private val gamesRepository: GamesRepository) : ViewModel() {
    var gamesUiState: GamesUiState by mutableStateOf(GamesUiState.Loading)
        private set

    init {
        getGames()
    }

    fun getGames() {
        viewModelScope.launch {
            gamesUiState = GamesUiState.Loading
            gamesUiState = try {
                GamesUiState.Success(gamesRepository.getGames())
            } catch (e: IOException) {
                GamesUiState.Error
            } catch (e: HttpException) {
                GamesUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GamesApplication)
                val gamesRepository = application.container.gamesRepository
                GamesViewModel(gamesRepository = gamesRepository)
            }
        }
    }
}