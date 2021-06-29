package com.company.NewLib

import kotlin.Throws
import java.lang.IndexOutOfBoundsException
import java.util.Objects
import java.lang.StringBuilder

class NewList {
    private var first: Wrapper<Any>? = null
    private var last: Wrapper<Any>? = null
    private var count = 0
    fun add(`object`: Any?) {
        if (first == null) {
            first = Wrapper(`object`)
            last = first
        } else {
            val wrapper = Wrapper(`object`)
            wrapper.prev = last
            last!!.next = wrapper
            last = wrapper
        }
        count++
    }

    @Throws(IndexOutOfBoundsException::class)
    private fun getNode(index: Int): Wrapper<Any>? {
        if (index >= count) {
            throw IndexOutOfBoundsException()
        }
        var i: Int
        var wrapper: Wrapper<Any>?
        if (count == 1) {
            wrapper = first
        } else if (count == 2) {
            wrapper = if (index == 0) first else last
        } else if (index < count / 2) {
            i = 0
            wrapper = first
            while (i < index) {
                assert(wrapper != null)
                wrapper = wrapper!!.next
                i++
            }
        } else {
            i = count - 1
            wrapper = last
            while (i > index) {
                assert(wrapper != null)
                wrapper = wrapper!!.prev
                i--
            }
        }
        return wrapper
    }

    operator fun get(index: Int): Any? {
        if (index >= count) {
            throw IndexOutOfBoundsException()
        }
        return getNode(index)!!.instance
    }

    @Throws(IndexOutOfBoundsException::class)
    fun remove(index: Int) {
        if (index >= count) {
            throw IndexOutOfBoundsException()
        }
        val wrapper = getNode(index)
        val prev = wrapper!!.prev
        val next = wrapper.next
        if (prev != null) {
            prev.next = next
        } else {
            first = next
        }
        if (next != null) {
            next.prev = prev
        } else {
            last = prev
        }
        count--
    }

    operator fun contains(`object`: Any): Boolean {
        var current = first
        while (current != null) {
            if (Objects.requireNonNull(current.instance).hashCode() == `object`.hashCode()) {
                if (current.instance == `object`) {
                    return true
                }
            }
            current = current.next
        }
        return false
    }

    fun size(): Int {
        return count
    }

    override fun toString(): String {
        if (first == null) {
            return "[]"
        }
        val string = StringBuilder("[")
        var current = first
        while (current != last) {
            string.append(current).append(", ")
            current = Objects.requireNonNull(current)!!.next
        }
        string.append(last.toString()).append("]")
        return string.toString()
    }
}