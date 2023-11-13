package pl.piokus.task.lsn;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        var options = new Options();

        var firsTask = Option.builder()
                .option("c")
                .argName("counter")
                .desc("First task: in input please provide space separated list of integers")
                .hasArgs()
                .build();
        var secondTask = Option.builder()
                .option("s")
                .argName("sum")
                .desc("Second task: in input please provide space separated list of integers")
                .hasArgs()
                .build();
        var thirdTask = Option.builder()
                .option("g")
                .argName("graph")
                .desc("proivide path to the file with input")
                .hasArgs()
                .build();
        var help = Option.builder()
                .option("h")
                .argName("help")
                .desc("Print help")
                .build();

        options.addOption(firsTask);
        options.addOption(secondTask);
        options.addOption(thirdTask);
        options.addOption(help);

        var parser = new DefaultParser();
        try {
            var cmd = parser.parse(options, args);
            if (cmd.hasOption(firsTask)) {
                countList(cmd.getOptionValues(firsTask));
                return;
            }
            if (cmd.hasOption(secondTask)) {
                processSecondTask(cmd.getOptionValues(secondTask));
                return;
            }
            if (cmd.hasOption(thirdTask)) {
                buildGraph(cmd.getOptionValue(thirdTask));
                return;
            }
            if (cmd.hasOption(help) || cmd.getOptions().length == 0) {
                printHelp(options);
            }
        } catch (NumberFormatException Exception) {
            System.err.println("Input contains not integer values please correct input.");
            System.err.println("Please use -h option");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Please use -h option");
            System.exit(1);
        }
    }

    private static void buildGraph(String fileName) throws IOException {
        int graphCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int edgeCount = Integer.parseInt(br.readLine());
            var graphFinder = new GraphFinder(edgeCount);
            graphFinder.buildGraph(br.lines());
            graphCount = graphFinder.findNumberOfGraphs();
        }
        System.out.println(graphCount);
    }

    private static int processLine(String line, int graphCount, HashSet<Integer> knownedNodes) {
        var nodes = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
        if (knownedNodes.contains(nodes.get(0)) || knownedNodes.contains(nodes.get(1))) {
            graphCount--;
        }
        knownedNodes.addAll(nodes);
        return graphCount;
    }

    private static void processSecondTask(String[] inputStrings) {
        var inputIntegers = Arrays.stream(inputStrings).map(Integer::parseInt).sorted().toList();
        var alreadyChecked = new ArrayList<Integer>();
        var result =  new ArrayDeque<String>();

        for (int i = 0; i < inputIntegers.size() ; i++) {
            int current = inputIntegers.get(i);
            int diff = 13-current;
            if (alreadyChecked.contains(diff)) {
                var howMany = alreadyChecked.stream().filter(integer -> integer.equals(diff)).count();
                for (int j = 0; j < howMany; j++) {
                    result.push(diff + " " + current);
                }
            }
            alreadyChecked.add(current);
        }

        result.forEach(System.out::println);
    }

    private static void countList(String[] inputStrings) {
        var inputIntegers = Arrays.stream(inputStrings).map(Integer::parseInt).distinct().sorted().toList();

        System.out.println("Count: " + inputStrings.length);
        System.out.println("Distinct: " + inputIntegers.size());
        System.out.println("min: " + inputIntegers.get(0));
        System.out.println("max: " + inputIntegers.get(inputIntegers.size()-1));
    }

    private static void printHelp(Options options) {
        var helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("LSN task:",options);
    }
}