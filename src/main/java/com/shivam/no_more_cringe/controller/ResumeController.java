package com.shivam.no_more_cringe.controller;

import com.shivam.no_more_cringe.model.RoastResponse;
import com.shivam.no_more_cringe.service.ResumeParseService;
import com.shivam.no_more_cringe.service.RoastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ResumeController {

    @Autowired
    private ResumeParseService resumeParseService;

    @Autowired
    private RoastService roastService;

    @GetMapping("/")
    public String showForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file")MultipartFile file, Model model) {
        try {
            String resumeText = resumeParseService.extractText(file);

            RoastResponse response = roastService.roastResume(resumeText);

            model.addAttribute("roast", response);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to process resume: " + e.getMessage());
        }
        return "result";
    }
}
