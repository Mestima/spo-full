package com.company.NewLib


import java.lang.StringBuilder

class NewHashSet {
    var n = 4
    var set: Array<NewList?>
    private fun needRehash(): Boolean {
        var amount = 0
        for (list in set) {
            if (list != null && list.size() >= 2) {
                amount++
            }
        }
        return amount / n >= 0.75
    }

    private fun rehash() {
        val newCount = n * 2
        val newContent = arrayOfNulls<NewList>(newCount)
        for (list in set) {
            if (list != null) {
                for (i in 0 until list.size()) {
                    val element = list[i]!!
                    val newIndex = element.hashCode() % newCount
                    if (newContent[newIndex] == null) {
                        newContent[newIndex] = NewList()
                    }
                    newContent[newIndex]!!.add(element)
                }
            }
        }
        set = newContent
        n = newCount
    }

    private fun h(`object`: Any): Int {
        return `object`.hashCode() % n
    }

    fun add(`object`: Any) {
        if (needRehash()) {
            rehash()
        }
        val h = h(`object`)
        if (!set[h]!!.contains(`object`)) {
            set[h]!!.add(`object`)
        }
    }

    operator fun contains(`object`: Any): Boolean {
        val h = h(`object`)
        return set[h] != null && set[h]!!.contains(`object`)
    }

    fun remove(`object`: Any) {
        val h = h(`object`)
        val objectString = `object`.toString()
        val list = set[h]
        if (list != null && list.contains(objectString)) {
            for (i in 0 until list.size()) {
                if (list[i] == objectString) {
                    list.remove(i)
                }
            }
        }
    }

    override fun equals(`object`: Any?): Boolean {
        if (this === `object`) {
            return true
        }
        if (javaClass != `object`!!.javaClass) {
            return false
        }
        val set = `object` as NewHashSet?
        return hashCode() == set.hashCode()
    }

    override fun toString(): String {
        var s = StringBuilder("{")
        for (list in set) {
            if (list != null) {
                for (i in 0 until list.size()) {
                    s.append(list[i]).append(", ")
                }
            }
        }
        if (s.length > 1) s = StringBuilder(s.substring(0, s.length - 2))
        s.append("}")
        return s.toString()
    }

    override fun hashCode(): Int {
        var result = n
        result = 31 * result + set.contentHashCode()
        return result
    }

    init {
        set = arrayOfNulls(n)
        for (i in 0 until n) {
            set[i] = NewList()
        }
    }
}