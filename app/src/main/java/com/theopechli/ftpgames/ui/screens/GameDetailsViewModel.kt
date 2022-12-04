package com.theopechli.ftpgames.ui.screens

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface GameDetailsUiState {
    data class Success(val gameDetails: GameDetails) : GameDetailsUiState
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

    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> get() = _refreshing.asStateFlow()

    init {
        getGameDetails(id)
    }

    private fun getGameDetails(id: Long) {
        viewModelScope.launch {
            gameDetailsUiState = GameDetailsUiState.Loading
            val gameDetails = gameDetailsDao.getGameDetails(id)
            if (gameDetails != null) {
                gameDetailsUiState = GameDetailsUiState.Success(gameDetails)
            } else {
                getGameDetailsFromRemote(id)
            }
        }
    }

    private fun getGameDetailsFromRemote(id: Long) {
        viewModelScope.launch {
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
        }
    }

    fun refresh(id: Long) {
        viewModelScope.launch {
            _refreshing.emit(true)
            gameDetailsUiState = GameDetailsUiState.Loading
            getGameDetailsFromRemote(id)
            _refreshing.emit(false)
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