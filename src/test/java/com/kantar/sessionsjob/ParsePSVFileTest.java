package com.kantar.sessionsjob;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ParsePSVFileTest {

    @Test
    void givenEmptyInputFileWhenParseThenThrowException() {
        String inputFileName = "src/test/resources/input-statements-without-records.psv";
        ParsePSVFile psvFile = new ParsePSVFile();

        Exception exception = assertThrows(EmptyFileException.class, () -> psvFile.parse(inputFileName));
    }
}