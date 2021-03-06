package com.kantar.sessionsjob;

public class Main {

    // See README.txt for usage example

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Missing arguments: <input-statements-file> <output-sessions-file>");
            System.exit(1);
        }

        // TODO: write application ...
        String inputFileName = args[0];
        String outputFileName = args[1];

        ParsePSVFile psvFile = new ParsePSVFile();
        psvFile.parse(inputFileName);
        psvFile.writeOutputFile(outputFileName);
    }
}
