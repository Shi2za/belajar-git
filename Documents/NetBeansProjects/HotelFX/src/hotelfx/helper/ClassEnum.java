/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotelfx.helper;

/**
 *
 * @author ANDRI
 */
public class ClassEnum {

    public enum BasicControl{
        CREATE, UPDATE, DELETE, PRINT;
    }
    
    /*
     * APPLICANT STATUS
     */
    public enum ApplicantStatus {

        APPLICANT(0), EMPLOYEE(1);

        private final int value;

        private ApplicantStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ApplicantStatus valueOf(int value) {
            for (ApplicantStatus data : ApplicantStatus.values()) {
                if (data.getValue() == value) {
                    return data;
                }
            }
            return null;
        }

    }

    
    
}
