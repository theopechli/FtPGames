package com.theopechli.ftpgames.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.theopechli.ftpgames.FtPGamesApplication
import com.theopechli.ftpgames.data.FtPGamesRepository
import com.theopechli.ftpgames.model.Game
import com.theopechli.ftpgames.model.GameDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GamesUiState {
    data class Success(val games: List<Game>) : GamesUiState
    object Error : GamesUiState
    object Loading : GamesUiState
}

class GamesViewModel(
    private val ftpGamesRepository: FtPGamesRepository,
    private val gameDao: GameDao
) : ViewModel() {
    var gamesUiState: GamesUiState by mutableStateOf(GamesUiState.Loading)
        private set
    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> get() = _refreshing.asStateFlow()

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            Log.i("GAME_GET", "Hello")
            gamesUiState = GamesUiState.Loading
            val games = gameDao.getAll()
            /* TODO be smart about it */
            if (games.size < 5) {
                getGamesFromRemote()
            } else {
                gamesUiState = GamesUiState.Success(games)
            }
            Log.i("GAME_GET", "Bye")
        }
    }

    private fun getGamesFromRemote() {
        viewModelScope.launch {
            Log.i("GAME_GET_REMOTE", "Hello")
            val games: List<Game>
            gamesUiState = try {
                games = ftpGamesRepository.getGames()
                gameDao.insertAll(games)
                GamesUiState.Success(games)
            } catch (e: IOException) {
                GamesUiState.Error
            } catch (e: HttpException) {
                GamesUiState.Error
            }
            Log.i("GAME_GET_REMOTE", "Bye")
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _refreshing.emit(true)
            gamesUiState = GamesUiState.Loading
            getGamesFromRemote()
            _refreshing.emit(false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FtPGamesApplication)
                val gamesRepository = application.container.ftpGamesRepository
                val gameDatabase = application.container.gameDatabase
                val gameDao = gameDatabase.gameDao()
                GamesViewModel(ftpGamesRepository = gamesRepository, gameDao = gameDao)
            }
        }
    }
}