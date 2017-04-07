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
        ((HashSet<Agent>)medicalTeam.getEquip("patientSelf")).add(patient);
        patientsList.removeFirst();
        Spot.getSpot("ScuEntrance").setEquip("patientsAssignWaitingList", patientsList);
        patient.setKeyword("status", "assigned");

    }

    public static String getDiseaseID(HashSet<Agent> patientSelf) {
        Iterator patientI = patientSelf.iterator();
        Agent patient = (Agent) patientI.next();
        return patient.getKeyword("nextTreatment");
    }

    public static void setPatientTreated(HashSet<Agent> patientSelf) {
        Iterator patientI = patientSelf.iterator();
        Agent patient = (Agent) patientI.next();
        patient.setKeyword("status", "treated");
    }

    public static void setPatientTreating(HashSet<Agent> patientSelf) {
        Iterator patientI = patientSelf.iterator();
        Agent patient = (Agent) patientI.next();
        patient.setKeyword("status", "treating");
    }

    public static boolean isContinueTreatment(HashSet<Agent> patientSelf) {
        Iterator patientI = patientSelf.iterator();
        Agent patient = (Agent) patientI.next();
        if(patient.getKeyword("status").equals("waitingNextTreatment"))
            return true;
        else
            return false;
    }

    public static boolean isFree(HashSet<Agent> medicalTeamSet, HashSet<Agent> patientsSet, HashSet<Agent> patientSelf) {
        Iterator medicalTeamI = medicalTeamSet.iterator();
        Agent medicalTeam = (Agent) medicalTeamI.next();
        Iterator patientI = patientsSet.iterator();
        while(patientI.hasNext()) {
            Agent patient = (Agent) patientI.next();
            if(patient.getKeyword("status").equals("waitingNextTreatment")) {
                ((HashSet<Agent>)medicalTeam.getEquip("patientSelf")).add(patient);
                return false;
            }
        }
        return true;
    }

    public static HashSet<Spot> getSpot(String str) {
        HashSet<Spot> set = new HashSet<>();
        set.add(Spot.forName(str));
        return set;
    }
}
