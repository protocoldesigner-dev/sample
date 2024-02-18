package dev.protocoldesigner.example;

import dev.protocoldesigner.core.exec.ProtocolExecutor;
import dev.protocoldesigner.example.cohesive.CohesiveExec;
import dev.protocoldesigner.example.coupled.CoupledExec;

/**
 *
 * Main
 * <p>
 *
 * An simple example illustrates what {@linkplain ProtocolExecutor} do.
 * In this example there are two implementation of the scheduling of four-phase traffic cross.
 *
 * <h2>CoupledExec</h2>
 * 
 * {@linkplain CoupledExec} runs a protocol which exposes all the states of each part 
 * of the system to other parts.
 *
 * <h2>CohensiveExec</h2>
 *
 * {@linkplain CohesiveExec} simplifys the protocol by encapsulating the states which are 
 * unnecessary to other parts.
 *
 * Unnecessary: when there are no events transmitted to other parts of the system
 * (In ProtocolExecutor , it's represented by state jump cascades), then the opposed state is 
 * unecessary and can be encapsulated.
 */

public class Main {
    public static void main(String[] args) throws Exception{
        new CohesiveExec().run();
        // new CoupledExec().run();
    }
}
