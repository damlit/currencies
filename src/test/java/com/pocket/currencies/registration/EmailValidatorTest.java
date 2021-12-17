package com.pocket.currencies.registration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @Test
    public void shouldValidate() {
        assertTrue(validator.test("test@test.pl"));
        assertTrue(validator.test("test@wp.pl"));
    }

    @Test
    public void shouldNotValidate() {
        assertFalse(validator.test("test.test.pl"));
        assertFalse(validator.test("test@wppl"));
        assertFalse(validator.test("testwppl"));
        assertFalse(validator.test("*te@wppl"));
    }
}
