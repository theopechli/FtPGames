package com.theopechli.ftpgames.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.theopechli.ftpgames.FtPGamesApplication
import com.theopechli.ftpgames.data.FtPGamesRepository
import com.theopechli.ftpgames.model.GameDetails
import com.theopechli.ftpgames.model.GameDetailsDao
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GameDetailsUiState {
    data class Success(val game: GameDetails) : GameDetailsUiState
    object Error : GameDetailsUiState
    object Loading : GameDetailsUiState
}

class GameDetailsViewModel(
    id: Long,
    private val ftpGamesRepository: FtPGamesRepository,
    private val gameDetailsDao: GameDetailsDao
) : ViewModel() {
    var gameDetailsUiState: GameDetailsUiState by mutableStateOf(GameDetailsUiState.Loading)
        private set

    init {
        getGameDetails(id)
    }

    private fun getGameDetails(id: Long) {
        viewModelScope.launch {
            Log.i("GAME_GET_DETAILS", "Hello")
            gameDetailsUiState = GameDetailsUiState.Loading
            val gameDetails = gameDetailsDao.getGameDetails(id)
            if (gameDetails != null) {
                gameDetailsUiState = GameDetailsUiState.Success(gameDetails)
            } else {
                getGameDetailsFromRemote(id)
            }
            Log.i("GAME_GET_DETAILS", "Bye")
        }
    }

    private fun getGameDetailsFromRemote(id: Long) {
        viewModelScope.launch {
            Log.i("GAME_GET_DETAILS_REMOTE", "Hello")
            val gameDetails: GameDetails
            gameDetailsUiState = try {
                gameDetails = ftpGamesRepository.getGameById(id)
                gameDetailsDao.insertGameDetails(gameDetails)
                GameDetailsUiState.Success(gameDetails)
            } catch (e: IOException) {
                GameDetailsUiState.Error
            } catch (e: HttpException) {
                GameDetailsUiState.Error
            }
            Log.i("GAME_GET_DETAILS_REMOTE", "Bye")
        }
    }

    class Factory(private val id: Long) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application = checkNotNull(extras[APPLICATION_KEY]) as FtPGamesApplication
            val ftpGamesRepository = application.container.ftpGamesRepository
            val gameDatabase = application.container.gameDatabase
            val gameDetailsDao = gameDatabase.gameDetailsDao()

            return GameDetailsViewModel(
                ftpGamesRepository = ftpGamesRepository,
                gameDetailsDao = gameDetailsDao,
                id = id
            ) as T
        }
    }
}