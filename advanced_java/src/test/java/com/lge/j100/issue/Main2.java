package com.lge.j100.issue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

public class Main2 {

    private static final String URL = "data/Complaints.csv";

    @Test
    public void quiz1() throws IOException {
        Path path = Paths.get(URL);

        var compaints = loadDataset(path);
        Map<String, Long> counter = new TreeMap<>();
        for (Complain2 c : compaints) {
            String key = c.company();
            counter.put(key,
                    counter.containsKey(key) ? counter.get(key) + 1
                            : Long.valueOf(1));
        }

        var cntList = new ArrayList<>(counter.entrySet());
        var cmp = new Comparator<Entry<String, Long>>() {
            @Override
            public int compare(Entry<String, Long> l,
                    Entry<String, Long> r) {
                return Long.compare(r.getValue(), l.getValue());
            }
        };

        cntList.sort(cmp);

        List<String> byCompany = new ArrayList<>();
        for (int i = 0; i < 10 && i < cntList.size(); ++i)
            byCompany.add(cntList.get(i).getKey());
        System.out.println(byCompany.size());

    }

    static final CSVFormat COMPLAIN_FORMAT = CSVFormat.Builder
            .create(CSVFormat.DEFAULT).setHeader(ComplainHeader.class)
            .setSkipHeaderRecord(true).setTrim(true).build();

    List<Complain2> loadDataset(Path path) throws IOException {
        List<Complain2> complains = new LinkedList<>();
        try (CSVParser csvParser = new CSVParser(
                Files.newBufferedReader(path), COMPLAIN_FORMAT)) {
            for (CSVRecord csvRecord : csvParser) {
                Complain2 fields = new Complain2(csvRecord);
                complains.add(fields);
            }
        }
        return complains;
    }

    @SuppressWarnings("resource")
    Stream<Complain2> newComplainStream(Path path)
            throws IOException {
        return new CSVParser(Files.newBufferedReader(path),
                COMPLAIN_FORMAT).stream().map(Complain2::new)
                        .parallel();
    }
}

record Complain2(LocalDate dataReceived, String product,
        String subproduct, String issue, String subissue,
        String consumerComplaintNarrative,
        String companyPublicResponse, String company, String state,
        String zipCode, String tags, String consumerConsentProvided,
        String submittedVia, LocalDate dateSentToCompany,
        String companyResponseToConsumer, boolean timelyResponse,
        boolean consumerDisputed, String complaintId) {

    public Complain2(CSVRecord r) {
        this(LocalDate.parse(r.get(ComplainHeader.DataReceived),
                DATE_FORMATTER), r.get(ComplainHeader.Product),
                r.get(ComplainHeader.Subproduct),
                r.get(ComplainHeader.Issue),
                r.get(ComplainHeader.Subissue),
                r.get(ComplainHeader.ConsumerComplaintNarrative),
                r.get(ComplainHeader.CompanyPublicResponse),
                r.get(ComplainHeader.Company),
                r.get(ComplainHeader.State),
                r.get(ComplainHeader.ZipCode),
                r.get(ComplainHeader.Tags),
                r.get(ComplainHeader.ConsumerConsentProvided),
                r.get(ComplainHeader.SubmittedVia),
                LocalDate.parse(
                        r.get(ComplainHeader.DateSentToCompany),
                        DATE_FORMATTER),
                r.get(ComplainHeader.CompanyResponseToConsumer),
                "Yes".equals(r.get(ComplainHeader.TimelyResponse)),
                "Yes".equals(r.get(ComplainHeader.ConsumerDisputed)),
                r.get(ComplainHeader.ComplaintId));
    }

    final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");
}
