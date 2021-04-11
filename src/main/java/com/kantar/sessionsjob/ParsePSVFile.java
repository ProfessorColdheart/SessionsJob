package com.kantar.sessionsjob;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ParsePSVFile {
    private final Map<Integer, List<SessionsJobRecord>> mapOfSessions;

    public ParsePSVFile() {
        this.mapOfSessions = new LinkedHashMap<>();
    }

    public void parse(String fileName) {
        List<String> lines = readFile(fileName);

        for (String line : lines) {
            String[] splitted = Arrays.stream(line.split("\\|"))
                    .toArray(String[]::new);

            int homeNo = Integer.parseInt(splitted[0]);
            int channel = Integer.parseInt(splitted[1]);
            LocalDateTime startTime = LocalDateTime.parse(splitted[2], getDateTimeFormatter());
            String activity = splitted[3];

            SessionsJobRecord sessionsJob = new SessionsJobRecord(homeNo, channel, startTime, activity);
            addToMap(sessionsJob);
        }

        for (Integer homeNo : mapOfSessions.keySet()) {
            mapOfSessions.get(homeNo).sort(Comparator.comparing(SessionsJobRecord::getStartTime));
            countEndDatesFor(homeNo);
        }
    }

    private List<String> readFile(String fileName) {
        Path path = Paths.get(fileName);
        List<String> lines = null;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lines.size() < 2) {
            throw new EmptyFileException("The file " + fileName + " is empty!");
        } else {
            //delete first line with column names
            lines.remove(0);
        }
        return lines;
    }

    private DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    }

    private void countEndDatesFor(int homeNo) {
        List<SessionsJobRecord> sessionsOfHome = mapOfSessions.get(homeNo);
        LocalDateTime endTime;
        int lastIndex = sessionsOfHome.size() - 1;

        for (int i = 0; i < lastIndex; i++) {
            endTime = sessionsOfHome.get(i + 1).getStartTime().minusSeconds(1);
            sessionsOfHome.get(i).setEndTime(endTime);
        }

        endTime = countLastEndTime(sessionsOfHome.get(lastIndex).getStartTime());
        sessionsOfHome.get(lastIndex).setEndTime(endTime);
    }

    private LocalDateTime countLastEndTime(LocalDateTime startTime) {
        return LocalDateTime.of(startTime.toLocalDate(),
                LocalTime.MIDNIGHT.minusSeconds(1));
    }

    private void addToMap(SessionsJobRecord sessionsJob) {
        int homeNo = sessionsJob.getHomeNo();

        if (mapOfSessions.containsKey(homeNo)) {
            mapOfSessions.get(homeNo).add(sessionsJob);
        } else {
            List<SessionsJobRecord> sessionsOfHome = new ArrayList<>();
            sessionsOfHome.add(sessionsJob);
            mapOfSessions.put(homeNo, sessionsOfHome);
        }
    }

    public void writeOutputFile(String outputFileName) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter(fileWriter);

        printWriter.println("HomeNo|Channel|Starttime|Activity|EndTime|Duration");

        for (Integer homeNo : mapOfSessions.keySet()) {
            for (SessionsJobRecord session : mapOfSessions.get(homeNo)) {
                printWriter.print(session.getHomeNo() + "|");
                printWriter.print(session.getChannel() + "|");
                printWriter.print(session.getFormattedStartTime() + "|");
                printWriter.print(session.getActivity() + "|");
                printWriter.print(session.getFormattedEndTime() + "|");
                printWriter.println(session.getDuration());
            }
        }

        printWriter.close();
    }
}