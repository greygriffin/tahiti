package net.greenpoppy.tahiti.berthassignment;

import lombok.Value;

import net.greenpoppy.tahiti.service.Update;


@Value
public class BerthAssignmentUpdate {
    Update<Integer> boatId;
    Update<Boolean> guardDuty;

    public boolean isEmpty() {
        return (boatId == null) && (guardDuty == null);
    }
}
