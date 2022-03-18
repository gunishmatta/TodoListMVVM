package com.gunish.todolistmvvm.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunish.todolistmvvm.data.Todo
import com.gunish.todolistmvvm.data.TodoRepository
import com.gunish.todolistmvvm.util.Routes
import com.gunish.todolistmvvm.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class TodoListViewModel @Inject constructor(private val respository: TodoRepository) : ViewModel() {
    val todos = respository.getTodos()
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private var recentlyDeletedTodo:Todo?=null

    fun onEvent(event: TodoListEvent)
    {
        when(event)
        {
            is TodoListEvent.onTodoClick->{
                sendUIEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO+"?todoId=${event.todo.id}"))
            }
            is TodoListEvent.onAddTodoClick->{
                sendUIEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is TodoListEvent.onUndoDeleteClick->{
                recentlyDeletedTodo?.let {todo->
                    viewModelScope.launch {
                        respository.insertTodo(todo)
                    }
                }
            }
            is TodoListEvent.onDoneChange->{
                viewModelScope.launch {
                    respository.insertTodo(event.todo.copy(isDone = event.isDone))
                }
            }
            is TodoListEvent.onDeleteTodoClick->{
                viewModelScope.launch {
                    recentlyDeletedTodo=event.todo
                    respository.deleteTodo(event.todo)
                    sendUIEvent(UiEvent.ShowSnackbar(message = "Todo Deleted", action = "Undo"))
                }
            }
        }
    }
private fun sendUIEvent(event: UiEvent)
{
    viewModelScope.launch {
        _uiEvent.send(event)
    }

}

}