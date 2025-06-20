package com.shivam.no_more_cringe.controller;

import com.shivam.no_more_cringe.model.RoastResponse;
import com.shivam.no_more_cringe.service.ResumeParseService;
import com.shivam.no_more_cringe.service.RoastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    @Autowired
    private ResumeParseService resumeParseService;

    @Autowired
    private RoastService roastService;

    @PostMapping("/upload")
    public ResponseEntity<RoastResponse> uploadResume(@RequestParam("file")MultipartFile file) {
        String resumeText = resumeParseService.extractText(file);

        RoastResponse response = roastService.roastResume(resumeText);

        return ResponseEntity.ok(response);
    }
}
