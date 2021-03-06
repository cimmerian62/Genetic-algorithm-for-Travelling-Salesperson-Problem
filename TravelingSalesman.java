import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/*
course: CSC 320
project: Assignment 1
date: Mar. 2
author: Josiah Luikham
purpose: Get a solution to the travelling salesman problem by a genetic algorithm
*/
class SalesmanGenome implements Comparable {
    List<Integer> genome;
    int[][] adjacency;
    int startingCity;
    int numberOfCities;
    int fitness;
    
    public SalesmanGenome(int numberOfCities, int[][] adjacency, int startingCity) {
        this.adjacency = adjacency;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;

        this.genome = randomSalesman();
        this.fitness = this.calculateFitness();
    }
    
    public int getFitness() {
        return fitness;
    }
    public  List<Integer> getGenome() {
        return genome;
    }
    public void printGenome() {
        for (int i = 0; i < genome.size(); i++)
            System.out.print((genome.get(i))+ "-");
        System.out.println();
    }
    
    public SalesmanGenome(List<Integer> permutationOfCities, int numberOfCities, int[][] adjacency, int startingCity) {
        this.genome = permutationOfCities;
        this.adjacency = adjacency;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;

        this.fitness = this.calculateFitness();
    }
    
    private List<Integer> randomSalesman() { //this function makes a random path by shuffling a list of integers from 1 to numberOfCities-1
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i < numberOfCities; i++) 
                result.add(i);
        Collections.shuffle(result);
        return result;
    } 
    
    public int calculateFitness() {
        int fitness = 0;
        int currentCity = startingCity;
    
        // Calculating path cost
        for (int i = 0; i < genome.size(); i++) {
            fitness += adjacency[currentCity][genome.get(i)];
            currentCity = genome.get(i);
        }
    
        // We have to add going back to the starting city to complete the circle
        // the genome is missing the starting city, and indexing starts at 0, which is why we subtract 2
        fitness += adjacency[currentCity][startingCity];
    
        return fitness;
    }
    
    public int compareTo(Object o) { //this allows the object to be compared to other objects
        SalesmanGenome genome = (SalesmanGenome) o;
        if(this.fitness > genome.getFitness())
            return 1;
        else if(this.fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }
    
}
    

class Trvsm {
    private int generationSize;
    private int genomeSize;
    private int numberOfCities;
    private int reproductionSize;
    private int maxIterations;
    private float mutationRate;
    private int[][] lengths;
    private int startingCity;
    private int targetFitness;
    private int tournamentSize;
    
    public Trvsm(int numberOfCities, int[][] lengths, int startingCity, int targetFitness){
        this.numberOfCities = numberOfCities;
        this.genomeSize = numberOfCities-1;
        this.lengths = lengths;
        this.startingCity = startingCity;
        this.targetFitness = targetFitness;

        generationSize = 500;
        reproductionSize = 100;
        maxIterations = 1000;
        mutationRate = 0.1f;
        tournamentSize = 10;
    }
    public SalesmanGenome tourneyPop(List<SalesmanGenome> population) {
        List<SalesmanGenome> tourney = new ArrayList<>();
        Random rand = new Random();
        for (int i= 0; i < tournamentSize; i++) {
            tourney.add(population.get(rand.nextInt(population.size()))); 
        }
        return Collections.min(tourney);
    }
    public List<SalesmanGenome> selection(List<SalesmanGenome> population) { //select which paths to pass on to future generations
        List<SalesmanGenome> selected = new ArrayList<>();
        
                
        for (int i=0; i < reproductionSize; i++) { 
            selected.add(tourneyPop(population));
        }
        
//        System.out.println();
//        System.out.println("The ones that will reproduce are: ");
//        for (int i = 0; i <selected.size(); i++) {
//            selected.get(i).printGenome();
//        }
        return selected;
    }
    
    public List<SalesmanGenome> crossover(List<SalesmanGenome> parents) {
        Random random = new Random();
        int breakpoint1 = random.nextInt(genomeSize);
        int breakpoint2 = random.nextInt(genomeSize); //choose two points
        
        List<SalesmanGenome> children = new ArrayList<>();

        // Copy parental genomes - we copy so we wouldn't modify in case they were
        // chosen to participate in crossover multiple times
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());
        
        
        List<Integer> child1 = new ArrayList<>();
        List<Integer> child2 = new ArrayList<>();
        
        if (breakpoint1 <= breakpoint2) { //if the first point is smaller than the second do this
            List<Integer> pick1 = new ArrayList<>();
            List<Integer> pick2 = new ArrayList<>();
            
                    
            for (int i = breakpoint1; i <= breakpoint2; i++) { //the picks are the numbers between the breakpoints
                pick1.add(parent1Genome.get(i));
                pick2.add(parent2Genome.get(i));
            }
            
            
        // Creating child 1
            for (int k = breakpoint1; k < parent2Genome.size(); k++){
                if (child1.size() == breakpoint1)
                    for (int j = 0; j < pick1.size(); j++)
                        child1.add(pick1.get(j));
                if (!pick1.contains(parent2Genome.get(k)))
                    child1.add(parent2Genome.get(k));
            }
            for (int i = 0; i < breakpoint1; i++) {
                if (child1.size() == breakpoint1)
                    for (int j = 0; j < pick1.size(); j++)
                        child1.add(pick1.get(j));
                if (!pick1.contains(parent2Genome.get(i)))
                    child1.add(parent2Genome.get(i));
            }
            if (child1.size() == breakpoint1)
                for (int j = 0; j < pick1.size(); j++)
                    child1.add(pick1.get(j));
                
                
        //create child 2
            for (int k = breakpoint1; k < parent1Genome.size(); k++){
                if (child2.size() == breakpoint1)
                    for (int j = 0; j < pick2.size(); j++)
                        child2.add(pick2.get(j));
                if (!pick2.contains(parent1Genome.get(k)))
                    child2.add(parent1Genome.get(k));
            }
            for (int i = 0; i < breakpoint1; i++) {
                if (child2.size() == breakpoint1)
                    for (int j = 0; j < pick2.size(); j++)
                        child2.add(pick2.get(j));
                if (!pick2.contains(parent1Genome.get(i)))
                    child2.add(parent1Genome.get(i));
            }
            if (child2.size() == breakpoint1)
                for (int j = 0; j < pick2.size(); j++)
                    child2.add(pick2.get(j));
        }
        else { //if breakpoint1 is larger than breakpoint 2 we hav to wraparound
            List<Integer> pick1a = new ArrayList<>();//pick1a is pick on right side of the array
            List<Integer> pick2a = new ArrayList<>();
            List<Integer> pick1b = new ArrayList<>();//pick1b is on left side
            List<Integer> pick2b = new ArrayList<>();
            //create picks if breakpoint 2 < breakpoint 1
            for (int i = breakpoint1; i < parent1Genome.size(); i++)
                pick1a.add(parent1Genome.get(i));
            for (int i = 0; i <= breakpoint2; i++)
                pick1b.add(parent1Genome.get(i));
            for (int i = breakpoint1; i < parent2Genome.size(); i++)
                pick2a.add(parent2Genome.get(i));
            for (int i = 0; i <= breakpoint2; i++)
                pick2b.add(parent2Genome.get(i));
            
            
            //child 1
            
            for (int i = 0; i < pick1b.size(); i++)
                child1.add(pick1b.get(i));
            for (int i = breakpoint1; i < parent2Genome.size(); i++)
                if (!pick1a.contains(parent2Genome.get(i)) && !pick1b.contains(parent2Genome.get(i)))
                    child1.add(parent2Genome.get(i));
            for (int i = 0; i < breakpoint1; i++)
                if (!pick1a.contains(parent2Genome.get(i)) && !pick1b.contains(parent2Genome.get(i)))
                    child1.add(parent2Genome.get(i));
            for (int i = 0; i < pick1a.size(); i++)
                child1.add(pick1a.get(i));
            //child 2
            for (int i = 0; i < pick2b.size(); i++)
                child2.add(pick2b.get(i));
            for (int i = breakpoint1; i < parent1Genome.size(); i++)
                if (!pick2a.contains(parent1Genome.get(i)) && !pick2b.contains(parent1Genome.get(i)))
                    child2.add(parent1Genome.get(i));
            for (int i = 0; i < breakpoint1; i++)
                if (!pick2a.contains(parent1Genome.get(i)) && !pick2b.contains(parent1Genome.get(i)))
                    child2.add(parent1Genome.get(i));
            for (int i = 0; i < pick2a.size(); i++)
                child2.add(pick2a.get(i));
        }
        
        children.add(new SalesmanGenome(child1, numberOfCities, lengths, startingCity));
    
        children.add(new SalesmanGenome(child2, numberOfCities, lengths, startingCity));

        return children;
    }
    
    public SalesmanGenome mutate(SalesmanGenome salesman) {
        Random random = new Random();
        int breakpoint1 = random.nextInt(genomeSize);
        int breakpoint2 = random.nextInt(genomeSize);
        
        float mutate = random.nextFloat();
        List<Integer> genome = salesman.getGenome();
        List<Integer> newGenome = new ArrayList<>();
        
        if (mutate < mutationRate) {
            if (breakpoint1 < breakpoint2) { //once again we've chosen two points, the numbers between them are reversed
                
                for (int i = 0; i < breakpoint1; i++)
                    newGenome.add(genome.get(i));
                for (int i = breakpoint2; i >= breakpoint1; i--)
                    newGenome.add(genome.get(i));
                for (int i = breakpoint2+1; i < genome.size(); i++)
                    newGenome.add(genome.get(i));
                return new SalesmanGenome(newGenome, numberOfCities, lengths, startingCity);
            }//we have to use wraparound here
            else if (breakpoint1 > breakpoint2) {
                for (int i = genome.size()-1; i >= breakpoint1; i--)
                    newGenome.add(genome.get(i));
                for (int i = breakpoint2+1; i < breakpoint1; i++)
                    newGenome.add(genome.get(i));
                for (int i = 0; i <= breakpoint2; i++)
                    newGenome.add(genome.get(i));
                return new SalesmanGenome(newGenome, numberOfCities, lengths, startingCity);
            }
        }
        return salesman; 
    }
    public List<SalesmanGenome> getParents(List<SalesmanGenome> selected) {
        Random random = new Random(); //pick two random parents out of the selected population
        List<SalesmanGenome> parents = new ArrayList<>();
        int p1 = random.nextInt(selected.size());
        int p2 = random.nextInt(selected.size());
        parents.add(selected.get(p1));
        parents.add(selected.get(p2));
        return parents;
    }
    
    public List<SalesmanGenome> createGeneration(List<SalesmanGenome> selected){
        List<SalesmanGenome> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        
        while(currentGenerationSize < generationSize){
            
            List<SalesmanGenome> parents = getParents(selected);
            List<SalesmanGenome> children = crossover(parents);
            
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            
            generation.add(children.get(0));
            generation.add(children.get(1));
            currentGenerationSize+=2;
        }
        return generation;
    }
    
    public List<SalesmanGenome> initialPopulation(){
        List<SalesmanGenome> population = new ArrayList<>(); //make an initial random population
        for(int i=0; i < generationSize; i++){
            population.add(new SalesmanGenome(numberOfCities, lengths, startingCity));
        }
        return population;
    }
    
    public SalesmanGenome optimize(){
        List<SalesmanGenome> population = initialPopulation();
        SalesmanGenome globalBestGenome = population.get(0);
        
//        System.out.println("The initial population is:");
//        for (int j = 0; j < population.size(); j++) {
//                population.get(j).printGenome();
//                System.out.println();
//        }
//        System.out.println("end initial population");
//        System.out.println();
        
        for(int i=0; i < maxIterations; i++){
            List<SalesmanGenome> selected = selection(population);
            
            population = createGeneration(selected);
            
            globalBestGenome = Collections.min(population); //the best path is the one with the smallest fitness value
            System.out.println("generation "+i+" begins:");
            
//            for (int k = 0; k < population.size(); k++) {
//                population.get(k).printGenome();
//                System.out.println(); 
//            }
            System.out.print("end generation "+ i + " The most fit individual has path 0-");
            globalBestGenome.printGenome();
            System.out.println("0");
            
            System.out.println("Most Fit indivisual has fitness: " + globalBestGenome.getFitness());
            
            System.out.print("The average fitness for this generation is: "); //print out the whole generations average fitness
            int avgFit = 0;
            for (int z = 0; z < population.size(); z++)
                avgFit += population.get(z).getFitness();
            avgFit /= population.size();
            System.out.println(avgFit);
            System.out.println();
                
            if(globalBestGenome.getFitness() < targetFitness)
                break;
        }
        return globalBestGenome;
    }

}
public class TravelingSalesman {
    
    public static void main(String[] args) {
        Random random = new Random();
        Scanner in = new Scanner(System.in);
        int numberOfCities;
        String res = "";
        do {
            System.out.print("How many Cities are there?: ");
            numberOfCities = in.nextInt();
            while (numberOfCities < 2) {
                System.out.println();
                System.out.print("Invalid, try again: ");
                numberOfCities = in.nextInt();
            }
            System.out.println();
            int[][] lengths = new int[numberOfCities][numberOfCities];
            for (int i = 0; i < numberOfCities; i++)
                for (int j = 0; j < numberOfCities; j++) {
                    if (j != i) {
//                        System.out.print("Enter the distance from city "+(i+1)+" to city "+(j+1)+": ");
                        lengths[i][j] = random.nextInt(9)+1;
                    }
                    else
                        lengths[i][j] = 0;
                }
            Trvsm geneticAlgorithm = new Trvsm(numberOfCities, lengths, 0, 0);
            geneticAlgorithm.optimize();
            System.out.println();
            System.out.print("Continue? y or n: ");
            res = in.next();
            
                    
        } while (res.toLowerCase().charAt(0) != 'n');
////        int[][] lengths = {
////                            {0, 14, 4, 10, 20},
////                            {14, 0, 7, 8, 7},
////                            {4, 5, 0, 7, 16},
////                            {11, 7, 9, 0, 2},
////                            {18, 7, 17, 4, 0}
////                            };
        
        
        
    }
    
}
