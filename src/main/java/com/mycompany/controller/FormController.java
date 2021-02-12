package com.mycompany.controller;

import com.mycompany.model.Finder;
import com.mycompany.model.Found;
import com.mycompany.model.Form;
import com.mycompany.model.Mail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

@Controller
public class FormController {
    String[] strings = new String[7];
    String[] strings1 = new String[7];

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("form", new Form());
        return "form";
    }

    @PostMapping("/form")
    public String formSubmit(@ModelAttribute Form form) throws IOException {
        FileWriter writer = new FileWriter("information.txt", true);
        String del = "-";
        writer.write(form.getName() + del + form.getSurname() + del + form.getPatronymic() + del +
                form.getAge() + del + form.getSalary() + del + form.getEmail() + del + form.getPlaceOfWork() + "\n");
        writer.close();
        return "result";
    }

    @GetMapping("/finder")
    public String finder(Model model) {
        model.addAttribute("finder", new Finder());
        return "finder";
    }

    @PostMapping("/finder")
    public String finderSubmit(Finder finder, Model model) throws FileNotFoundException {
        FileReader reader = new FileReader("information.txt");
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNextLine()) {
            strings = scanner.nextLine().split("-");
            System.out.println();
            if (strings[0].equals(finder.getFindName()) && strings[1].equals(finder.getFindSurname())) {
                model.addAttribute("name", "Name: " + strings[0]);
                model.addAttribute("surname", "Surname: " + strings[1]);
                model.addAttribute("patronymic", "Patronymic: " + strings[2]);
                model.addAttribute("age", "Age: " + strings[3]);
                model.addAttribute("salary", "Salary: " + strings[4]);
                model.addAttribute("email", "E-mail: " + strings[5]);
                model.addAttribute("work", "Place of work: " + strings[6]);
                model.addAttribute("time", "Current time: " +
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")).toString());
                return "found";
            }
        }
        return "notfound";
    }

    @GetMapping("/found")
    public String found(Model model) {
        model.addAttribute("found", new Found());
        return "found";
    }



    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @ModelAttribute Form form, Model model) {


        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "form";
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            FileReader reader = new FileReader(fileName);
            Scanner scanner = new Scanner(reader);
            strings1 = scanner.nextLine().split("-");
            form.setName(strings1[0]);
            form.setSurname(strings1[1]);
            form.setPatronymic(strings1[2]);
            form.setAge(Integer.parseInt(strings1[3]));
            form.setSalary(Integer.parseInt(strings1[4]));
            form.setEmail(strings1[5]);
            form.setPlaceOfWork(strings1[6]);
            return "form";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "form";
    }

    @GetMapping("/mail")
    public String mail(Model model) {
        model.addAttribute("mail", new Mail());
        return "mail";
    }


    @PostMapping("/mail")
    public String sendMail(Model model, Mail mail) {
        mail.setUsername("ncedu.nn@gmail.com");
        mail.setPassword("nc_edu_2021");
        mail.send(mail.getSubject(), mail.getMessage(), "ncedu.nn@gmail.com", strings[5]);
        return "resultMail";
    }
}

