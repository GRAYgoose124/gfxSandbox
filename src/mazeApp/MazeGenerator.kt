package mazeApp

import kotlin.collections.shuffled

class MazeGenerator {
    companion object {
        fun dfs(dimx: Int, dimy: Int): Maze {
            var start: Pair<Int, Int> = Pair(50, 50)
            var current: Pair<Int, Int> = start
            var goal: Pair<Int, Int> = Pair(100, 100)
            var maze = Maze(dimx, dimy, start, goal)
            maze[current] = "lrud_".toCharArray()

            var dir = 'd'
            var steps = 0
            while (steps < 10000) {
                var x = current.first
                var y = current.second

                if (possibleMoves(maze, current) > 1) {
                    when (dir) {
                        'l' -> if (maze.dimx >= x + 2 && maze[x + 1][y][4] != '_') x++
                        'r' -> if (0 <= x - 2 && maze[x - 1][y][4] != '_') x--
                        'u' -> if (0 <= y - 2 && maze[x][y - 1][4] != '_') y--
                        'd' -> if (maze.dimy >= y + 2 && maze[x][y + 1][4] != '_') y++
                    }

                    current = Pair(x, y)
                    maze[current][4] = '_'
                } else {
                    for (i in 0 until maze.path.size) {
                        try {
                            val moves = maze.available_neighbors(current)
                            if (maze.path[maze.path.size - 2] == current) {
                                current = maze.path[maze.path.size - (i + 2)]
                            } else {
                                newDir(moves[0])
                                break
                            }
                        }
                        catch (e: IndexOutOfBoundsException) { break }
                    }
                }

                maze.updateNeighbors(dir, current)
                maze.path.add(current)

                steps++
            }

            return maze
        }

        val newDir = { w: CharArray ->
            val indices = w.toList().filter {
                (it != '_' || it != '|')
            }.shuffled()

            w[w.indexOf(indices[0])]
        }

        val possibleMoves = { maze: Maze, c: Pair<Int, Int> ->
            var sum = 0
            maze.available_neighbors(c).forEach { a: CharArray ->
                sum += a.count { (it != '_' || it != '|') }
            }
            sum
        }
    }

}