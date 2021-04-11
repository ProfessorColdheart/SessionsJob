package com.kantar.sessionsjob;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParsePSVFileTest {

    @Test
    void givenInputFileWhenParseThenProperWrite() throws IOException {
        //Given
        String inputFileName = "src/test/resources/input-statements.psv";
        String outputFileName = "target/actual-sessions.psv";
        String expectedOutputFileName = "src/test/resources/expected-sessions.psv";
        ParsePSVFile psvFile = new ParsePSVFile();

        //When
        psvFile.parse(inputFileName);
        psvFile.writeOutputFile(outputFileName);

        //Then
        byte[] outputFileBytes = Files.readAllBytes(Paths.get(outputFileName));
        byte[] expectedOutputFileBytes = Files.readAllBytes(Paths.get(expectedOutputFileName));
        String output = new String(outputFileBytes, StandardCharsets.UTF_8);
        String expectedOutput = new String(expectedOutputFileBytes, StandardCharsets.UTF_8);
        assertEquals(expectedOutput, output);
    }
}