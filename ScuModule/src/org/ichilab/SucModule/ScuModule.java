package org.ichilab.SucModule;

import env.Agent;
import env.Spot;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by manabu on 2017/03/14.
 */
public class ScuModule {
    public static void assignMedicalTeam(HashSet<Agent> medicalTeamSet) {
        Iterator medicalTeamI = medicalTeamSet.iterator();
        Agent medicalTeam = (Agent) medicalTeamI.next();
        LinkedList<Agent> patientsList = Spot.getSpot("ScuEntrance").getEquip("patientsAssignWaitingList");
        Agent patient = patientsList.getFirst();
        ((HashSet<Agent>)medicalTeam.getEquip("patientsSet")).add(patient);
        patientsList.removeFirst();
        Spot.getSpot("ScuEntrance").setEquip("patientsAssignWaitingList", patientsList);
        patient.setKeyword("status", "assigned");
    }
}
