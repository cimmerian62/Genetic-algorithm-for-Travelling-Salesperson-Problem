# Genetic-algorithm-for-Travelling-Salesperson-Problem

Problem: obtain a solution to the travelling salesman problem through a genetic algorithm

the chromosomes consist initially of a list of numbers from 1 to 4 which are shuffled to randomize them

Crossover is performed by choosing 2 random numbers between 0 and 3, the the values at and between the addresses will be the pick.
If the first number is larger than the second wraparound is performed. The pick is take from two parents. Starting at the 
beginning of the other parent 2's pick and wrapping around back the the begginning of the pick, numbers are added to child 1 that are 
not in parent 1's pick, when the spot where the begginning of the pick in the parent is reached, parent 1's pick is added. From here 
the program continues adding numbers from parent 2. The opposite is done to create child 2.

Mutation is performed by choosing 2 random numbers between 0 and 3 as adresses, and simply reversing the values at and between
those addresses. If the first number is larger than the second wraparound is performed.


