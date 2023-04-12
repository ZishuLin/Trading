package ca.dal.cs.csci3130.group16.courseproject;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class CCUnitTest {

    //Tests for checking the CC number type methods
    @Test
    public void isVisa_correct() {
        String num1 = "421";
        String num2 = "4349123412341234";
        String num3 = "2349123412341234";
        String num4 = "5349123412341234";
        String num5 = "0";
        String num6 = "";

        assertTrue(NewUser.isVisa(num1));
        assertTrue(NewUser.isVisa(num2));
        assertFalse(NewUser.isVisa(num3));
        assertFalse(NewUser.isVisa(num4));
        assertFalse(NewUser.isVisa(num5));
        assertFalse(NewUser.isVisa(num6));
    }
    @Test
    public void isMaster_correct() {
        String num1 = "521";
        String num2 = "5349123412341234";
        String num3 = "2349123412341234";
        String num4 = "1349123412341234";
        String num5 = "0";
        String num6 = "";

        assertTrue(NewUser.isMaster(num1));
        assertTrue(NewUser.isMaster(num2));
        assertFalse(NewUser.isMaster(num3));
        assertFalse(NewUser.isMaster(num4));
        assertFalse(NewUser.isMaster(num5));
        assertFalse(NewUser.isMaster(num6));
    }
    @Test
    public void isAmex_correct() {
        String num1 = "321";
        String num2 = "3349123412341234";
        String num3 = "2349123412341234";
        String num4 = "5349123412341234";
        String num5 = "0";
        String num6 = "";

        assertTrue(NewUser.isAmex(num1));
        assertTrue(NewUser.isAmex(num2));
        assertFalse(NewUser.isAmex(num3));
        assertFalse(NewUser.isAmex(num4));
        assertFalse(NewUser.isAmex(num5));
        assertFalse(NewUser.isAmex(num6));
    }

    //Tests for checking the CC number type methods
    @Test
    public void isCCNumber_correct() {
        String num1 = "321";
        String num2 = "3349123412341234";
        String num3 = "2349123412341234";
        String num4 = "534912341234123";
        String num5 = "0";
        String num6 = "";

        assertFalse(NewUser.isValidCCNumber(num1));
        assertTrue(NewUser.isValidCCNumber(num2));
        assertTrue(NewUser.isValidCCNumber(num3));
        assertFalse(NewUser.isValidCCNumber(num4));
        assertFalse(NewUser.isValidCCNumber(num5));
        assertFalse(NewUser.isValidCCNumber(num6));
    }
    @Test
    public void isCVV_correct() {
        String num1 = "321";
        String num2 = "33";
        String num3 = "0";
        String num4 = "";

        assertTrue(NewUser.isValidCVV(num1));
        assertFalse(NewUser.isValidCVV(num2));
        assertFalse(NewUser.isValidCVV(num3));
        assertFalse(NewUser.isValidCVV(num4));
    }
    @Test
    public void isMonth_correct() {
        String num1 = "3";
        String num2 = "12";
        String num3 = "01";
        String num4 = "";

        assertFalse(NewUser.isValidMonth(num1));
        assertTrue(NewUser.isValidMonth(num2));
        assertTrue(NewUser.isValidMonth(num3));
        assertFalse(NewUser.isValidMonth(num4));
    }
    @Test
    public void isYear_correct() {
        String num1 = "89";
        String num2 = "23";
        String num3 = "30";
        String num4 = "";
        String num5 = "8";

        assertFalse(NewUser.isValidYear(num1));
        assertTrue(NewUser.isValidYear(num2));
        assertTrue(NewUser.isValidYear(num3));
        assertFalse(NewUser.isValidYear(num4));
        assertFalse(NewUser.isValidYear(num5));
    }
}

