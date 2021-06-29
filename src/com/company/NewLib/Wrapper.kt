package com.company.NewLib

class Wrapper<T>(data: T?) {
    var instance: T? = null
    var next: Wrapper<T>? = null
    var prev: Wrapper<T>? = null
    override fun toString(): String {
        return instance.toString()
    }

    init {
        if (data != null) {
            instance = data
        }
    }
}