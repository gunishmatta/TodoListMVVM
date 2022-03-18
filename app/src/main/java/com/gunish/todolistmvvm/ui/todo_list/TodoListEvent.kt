package com.gunish.todolistmvvm.ui.todo_list

import com.gunish.todolistmvvm.data.Todo

sealed class TodoListEvent {
    data class onDeleteTodoClick(val todo:Todo):TodoListEvent()
    data class onDoneChange(val todo: Todo,val isDone:Boolean):TodoListEvent()
    object onUndoDeleteClick:TodoListEvent()
    data class onTodoClick(val todo: Todo):TodoListEvent()
    object onAddTodoClick:TodoListEvent()
}