package mazeApp

class Maze(val dimx: Int, val dimy: Int, val start: Pair<Int, Int>, val end: Pair<Int, Int>) {
    var grid: Array<Array<CharArray>> = Array(dimx) { Array(dimy) { "lrud|".toCharArray() } } // Maze extend gridType
    var path: MutableList<Pair<Int, Int>> = ArrayList()

    operator fun get(i: Int): Array<CharArray> {
        return grid[i]
    }

    operator fun get(coord: Pair<Int, Int>): CharArray {
        val i = coord.first
        val j = coord.second
        return grid[i][j]
    }

    operator fun iterator(): Iterator<Array<CharArray>> {
        return grid.iterator()
    }

    operator fun set(coord: Pair<Int, Int>, walls: CharArray) {
        val i = coord.first
        val j = coord.second
        grid[i][j] = walls
    }

    fun updateNeighbors(dir: Char, current: Pair<Int, Int>) {
        val x = current.first
        val y = current.second
        val off = dirToOff(dir)

        try {
            var i = 0
            when {
                x == -1 && y == 0 -> i == 0
                x == 1 && y == 0 -> i == 1
                x == 0 && y == -1 -> i == 2
                x == 0 && y == 1 -> i == 3
            }
            grid[x + off.first][y + off.second][i] = '_'
        } catch (e: ArrayIndexOutOfBoundsException) {}
    }

    fun available_neighbors(current: Pair<Int, Int>): List<CharArray> {
        val x = current.first
        val y = current.second
        var available = Array<CharArray>(4) { CharArray(4) }

        try {
            available[0] = grid[x + 1][y].filter { it != '_' }.toCharArray()
        } catch (e: ArrayIndexOutOfBoundsException) {}
        try {
            available[1] = grid[x - 1][y].filter { it != '_' }.toCharArray()
        } catch (e: ArrayIndexOutOfBoundsException) {}
        try {
            available[2] = grid[x][y + 1].filter { it != '_' }.toCharArray()
        } catch (e: ArrayIndexOutOfBoundsException) {}
        try {
            available[3] = grid[x][y - 1].filter { it != '_' }.toCharArray()
        } catch (e: ArrayIndexOutOfBoundsException) {}

        return available.filter { it.isNotEmpty() }.shuffled()
    }

    fun dirToOff(dir: Char): Pair<Int, Int> {
        var xo = 0
        var yo = 0
        if (dir == 'l') xo--
        else if (dir == 'r') xo++
        else if (dir == 'u') yo++
        else if (dir == 'd') yo--

        return Pair(xo, yo)
    }

    fun offToDir(off: Pair<Int, Int>): Char {
        val x = off.first
        val y = off.second
        var dir: Char ?= null

        if (x == -1) dir = 'l'
        else if (x == 1) dir = 'r'
        else if (y == 1) dir = 'u'
        else if (y == -1) dir = 'd'

        return dir!!
    }
}