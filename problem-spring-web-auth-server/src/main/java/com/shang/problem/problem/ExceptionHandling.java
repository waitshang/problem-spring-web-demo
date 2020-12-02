package com.shang.problem.problem;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

/**
 * Created by shangwei2009@hotmail.com on 2020/12/2 10:27
 */
@ControllerAdvice
public class ExceptionHandling implements ProblemHandling, SecurityAdviceTrait {

}
