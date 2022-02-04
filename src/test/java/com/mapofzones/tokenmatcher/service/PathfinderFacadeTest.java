package com.mapofzones.tokenmatcher.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class PathfinderFacadeTest {
    @InjectMocks
    private PathfinderFacade app = new PathfinderFacade(null, null, null, null);

    @Test
    void extractBaseDenom() {
        String privateMethodName = "extractBaseDenom";
        Object[][] tests = new Object[][]{
                {/*test name*/"Symbol only test", /*args*/"utoken", /*expected*/"utoken"},
                {/*test name*/"Symbol only with slash test", /*args*/"/utoken2", /*expected*/"utoken2"},
                {/*test name*/"Trace path test", /*args*/"transfer/channel-37/utoken3", /*expected*/"utoken3"},
                {/*test name*/"Big trace path test", /*args*/"transfer/channel-52/transfer/channel-147/transfer/channel-37/utoken4", /*expected*/"utoken4"},
                {/*test name*/"Big trace path w ibc module test", /*args*/"ibc/transfer/channel-147/transfer/channel-37/utoken5", /*expected*/"utoken5"},
        };
        for (Object[] objects: tests) {
            String testErrorMessage = String.valueOf(objects[0]);
            Object methodArg1 = objects[1];
            Object expected = objects[2];
            String actual = ReflectionTestUtils.invokeMethod(app, privateMethodName, methodArg1);
            assertEquals(expected, actual, testErrorMessage);
        }
    }
}