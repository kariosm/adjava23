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

public class Main {
    private static final String URL = "data/complaints.csv";

    @Test
    public void quiz1() throws IOException {
        long start = System.currentTimeMillis();

        Path path = Paths.get(URL);
        List<Complain> compaints = loadDataset(path);
        Map<String, Long> counter = new TreeMap<>();
        for (Complain c : compaints) {
            String key = c.getCompany();
            counter.put(key,
                    counter.containsKey(key) ? counter.get(key) + 1
                            : Long.valueOf(1));
        }

        List<Entry<String, Long>> cntList = new ArrayList<>(
                counter.entrySet());
        Comparator<Entry<String, Long>> cmp = new Comparator<Entry<String, Long>>() {
            @Override
            public int compare(Entry<String, Long> l,
                    Entry<String, Long> r) {
                return Long.compare(r.getValue(), l.getValue());
            }
        };
        cntList.sort(cmp);

        List<String> byCompany = new ArrayList<>();
        for (int i = 0; i < 10 && i < cntList.size(); ++i) {
            byCompany.add(cntList.get(i).getKey());
        }
        System.out.println(byCompany.size());
        System.out.println(System.currentTimeMillis() - start);
    }

    static final CSVFormat COMPLAIN_FORMAT = CSVFormat.Builder
            .create(CSVFormat.DEFAULT).setHeader(ComplainHeader.class)
            .setSkipHeaderRecord(true).setTrim(true).build();

    List<Complain> loadDataset(Path path) throws IOException {
        List<Complain> complains = new LinkedList<>();
        try (CSVParser csvParser = new CSVParser(
                Files.newBufferedReader(path), COMPLAIN_FORMAT)) {
            for (CSVRecord csvRecord : csvParser) {
                Complain fields = new Complain(csvRecord);
                complains.add(fields);
            }
        }
        return complains;
    }

    @SuppressWarnings("resource")
    Stream<Complain> newComplainStream(Path path) throws IOException {
        return new CSVParser(Files.newBufferedReader(path),
                COMPLAIN_FORMAT).stream().map(Complain::new)
                        .parallel();
    }
}

class Complain {
    private LocalDate dataReceived;
    private String product;
    private String subproduct;
    private String issue;
    private String subissue;
    private String consumerComplaintNarrative;
    private String companyPublicResponse;
    private String company;
    private String state;
    private String zipCode;
    private String tags;
    private String consumerConsentProvided;
    private String submittedVia;
    private LocalDate dateSentToCompany;
    private String companyResponseToConsumer;
    private boolean timelyResponse;
    private boolean consumerDisputed;
    private String complaintId;

    final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");

    public Complain(CSVRecord r) {
        this.dataReceived = LocalDate.parse(
                r.get(ComplainHeader.DataReceived), DATE_FORMATTER);
        this.product = r.get(ComplainHeader.Product);
        this.subproduct = r.get(ComplainHeader.Subproduct);
        this.issue = r.get(ComplainHeader.Issue);
        this.subissue = r.get(ComplainHeader.Subissue);
        this.consumerComplaintNarrative = r
                .get(ComplainHeader.ConsumerComplaintNarrative);
        this.companyPublicResponse = r
                .get(ComplainHeader.CompanyPublicResponse);
        this.company = r.get(ComplainHeader.Company);
        this.state = r.get(ComplainHeader.State);
        this.zipCode = r.get(ComplainHeader.ZipCode);
        this.tags = r.get(ComplainHeader.Tags);
        this.consumerConsentProvided = r
                .get(ComplainHeader.ConsumerConsentProvided);
        this.submittedVia = r.get(ComplainHeader.SubmittedVia);
        this.dateSentToCompany = LocalDate.parse(
                r.get(ComplainHeader.DateSentToCompany),
                DATE_FORMATTER);
        this.companyResponseToConsumer = r
                .get(ComplainHeader.CompanyResponseToConsumer);
        this.timelyResponse = "Yes"
                .equals(r.get(ComplainHeader.TimelyResponse));
        this.consumerDisputed = "Yes"
                .equals(r.get(ComplainHeader.ConsumerDisputed));
        this.complaintId = r.get(ComplainHeader.ComplaintId);
    }

    public LocalDate getDataReceived() {
        return dataReceived;
    }

    public String getProduct() {
        return product;
    }

    public String getSubproduct() {
        return subproduct;
    }

    public String getIssue() {
        return issue;
    }

    public String getSubissue() {
        return subissue;
    }

    public String getConsumerComplaintNarrative() {
        return consumerComplaintNarrative;
    }

    public String getCompanyPublicResponse() {
        return companyPublicResponse;
    }

    public String getCompany() {
        return company;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getTags() {
        return tags;
    }

    public String getConsumerConsentProvided() {
        return consumerConsentProvided;
    }

    public String getSubmittedVia() {
        return submittedVia;
    }

    public LocalDate getDateSentToCompany() {
        return dateSentToCompany;
    }

    public String getCompanyResponseToConsumer() {
        return companyResponseToConsumer;
    }

    public boolean isTimelyResponse() {
        return timelyResponse;
    }

    public boolean isConsumerDisputed() {
        return consumerDisputed;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public static DateTimeFormatter getFormatter() {
        return DATE_FORMATTER;
    }

    @Override
    public String toString() {
        return "Complain [dataReceived=" + dataReceived + ", product="
                + product + ", subproduct=" + subproduct + ", issue="
                + issue + ", subissue=" + subissue
                + ", consumerComplaintNarrative="
                + consumerComplaintNarrative
                + ", companyPublicResponse=" + companyPublicResponse
                + ", company=" + company + ", state=" + state
                + ", zipCode=" + zipCode + ", tags=" + tags
                + ", consumerConsentProvided="
                + consumerConsentProvided + ", submittedVia="
                + submittedVia + ", dateSentToCompany="
                + dateSentToCompany + ", companyResponseToConsumer="
                + companyResponseToConsumer + ", timelyResponse="
                + timelyResponse + ", consumerDisputed="
                + consumerDisputed + ", complaintId=" + complaintId
                + "]";
    }
}
