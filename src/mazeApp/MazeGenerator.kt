package mazeApp

import kotlin.collections.shuffled
import kotlin.random.Random

class MazeGenerator {
    companion object {
        fun dfs(dimx: Int, dimy: Int): Maze {
            var start: Pair<Int, Int> = Pair(50, 50)
            var current: Pair<Int, Int> = start
            var goal: Pair<Int, Int> = Pair(100, 100)
            var maze = Maze(dimx, dimy, start, goal)
            maze[current] = "lrud_".toCharArray()
            var dir = 'd'

            val backStep = {
                for (i in 1 until maze.path.size) {
                    val moves = maze.available_neighbors(current)
                    try {
                        val prevStep = maze.path[i - 1]
                        if (prevStep == current) {
                            current = maze.path[i - 1]
                        } else {
                            dir = newDir(moves[0].filter { (it != dir) }.toCharArray())
                            break
                        }
                    }
                    catch (e: IndexOutOfBoundsException) {}
                }
                Pair(current, dir)
            }

            var steps = 0
            while (steps < 10000) {
                var x = current.first
                var y = current.second

                when (dir) {
                    'l' -> if (maze.dimx >= x + 2 && maze[x + 1][y][4] != '_') {
                        x++
                        current = Pair(x, y)
                    } else {
                        val bs = backStep()
                        current = bs.first
                        dir = bs.second
                    }
                    'r' -> if (0 <= x - 2 && maze[x - 1][y][4] != '_') {
                        x--
                        current = Pair(x, y)
                    } else {
                        val bs = backStep()
                        current = bs.first
                        dir = bs.second
                    }
                    'u' -> if (0 <= y - 2 && maze[x][y - 1][4] != '_') {
                        y--
                        current = Pair(x, y)
                    } else {
                        val bs = backStep()
                        current = bs.first
                        dir = bs.second
                    }
                    'd' -> if (maze.dimy >= y + 2 && maze[x][y + 1][4] != '_') {
                        y++
                        current = Pair(x, y)
                    } else {
                        val bs = backStep()
                        current = bs.first
                        dir = bs.second
                    }

                }

                maze[current][4] = '_'

                maze.updateNeighbors(dir, current)
                maze.path.add(current)

                steps++
            }

            return maze
        }

        val newDir = { w: CharArray ->
            val dirs = w.toList().filter { (it != '_') }.shuffled()

             w[w.indexOf(dirs[0])]
        }


        val countPossibleMoves = { maze: Maze, c: Pair<Int, Int> ->
            var sum = 0
            maze.available_neighbors(c).forEach { a: CharArray ->
                sum += a.count { (it != '_' && it != '|') }
            }
            sum
        }
    }
}