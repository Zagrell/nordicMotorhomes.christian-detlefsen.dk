package grp1.motorhomes.service;

import grp1.motorhomes.model.Motorhome;
import grp1.motorhomes.repository.MotorhomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorhomeService {


    @Autowired
    MotorhomeRepo motorhomeRepo;

    /**
     * @author Patrick
     */
    public List<Motorhome> fetchAllMotorhomes() {
        return motorhomeRepo.fetchAllMotorhomes();
    }

    /**
     * @author Patrick
     * @param motorhome
     */
    public void createMotorhome(Motorhome motorhome) {
        motorhomeRepo.createMotorhome(motorhome);
    }

    /**
     * @author Patrick
     * @param licencePlate
     */
    public Motorhome findMotorhome(String licencePlate) {
        return motorhomeRepo.findMotorhome(licencePlate);
    }

    /**
     * @author Patrick
     * @param motorhome
     */
    public void editMotorhome(Motorhome motorhome) {
        motorhomeRepo.editMotorhome(motorhome);
    }

    /**
     * @author Patrick
     * @param licencePlate
     */
    public void deleteMotorhome(String licencePlate) {
        motorhomeRepo.deleteMotorhome(licencePlate);
    }


}