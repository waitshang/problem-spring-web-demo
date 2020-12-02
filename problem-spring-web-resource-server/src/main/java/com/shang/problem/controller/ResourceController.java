package com.shang.problem.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by shangwei2009@hotmail.com on 2020/12/2 9:55
 */
@RestController
public class ResourceController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @PreAuthorize("hasAuthority('default')")
    @GetMapping("/echo")
    public String echo() {
        return LocalDateTime.now().format(FORMATTER);
    }

    @PreAuthorize("hasAuthority('deny')")
    @GetMapping("/deny")
    public String deny() {
        return "deny";
    }
}
