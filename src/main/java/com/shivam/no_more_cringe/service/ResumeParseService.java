package com.shivam.no_more_cringe.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeParseService {

    public String extractText(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch(IOException e) {
            throw new RuntimeException("Failed to parse resume: " + e.getMessage(), e);
        }
    }
}
