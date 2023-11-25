package com.example.travelers_talks

class PostsProvider {
    companion object{
        var PostList: MutableList<Posts> = mutableListOf()

        private var localPostList: MutableList<Posts> = mutableListOf()

        // Método para actualizar la copia local de PostList
        fun updateLocalPostList() {
            localPostList.clear()
            localPostList.addAll(PostList)
        }

        // Método para obtener la copia local de PostList
        fun getLocalPostList(): List<Posts> {
            return localPostList.toList()
        }

    }
}
