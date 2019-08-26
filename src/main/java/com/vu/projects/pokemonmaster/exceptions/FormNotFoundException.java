package com.vu.projects.pokemonmaster.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "No Pokemon Found for the specified FORM.")
public class FormNotFoundException extends Exception {

}
