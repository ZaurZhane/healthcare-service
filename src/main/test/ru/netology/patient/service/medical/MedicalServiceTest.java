package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;

public class MedicalServiceTest {

    PatientInfoFileRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
    SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

    String patientId = "da0c1221-60bb-4506-8c51-cad1d2b45a97";

    @Test
    void checkBloodPressureTest(){

        String expected = "Warning, patient with id: null, need help";

        Mockito.when(patientInfoRepository.getById(anyString()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        BloodPressure currentPressure = new BloodPressure(60, 120);

        medicalService.checkBloodPressure(patientId, currentPressure);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService, atLeastOnce()).send(argumentCaptor.capture());

        Assertions.assertEquals(expected, argumentCaptor.getValue());

    }

    @Test
    void checkTemperatureTest() {

        String expected = "Warning, patient with id: null, need help";

        Mockito.when(patientInfoRepository.getById(anyString()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(patientId, new BigDecimal("35"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService, atLeastOnce()).send(argumentCaptor.capture());

        Assertions.assertEquals(expected, argumentCaptor.getValue());
    }

    @Test
    void checkTemperatureNormalTest() {

        Mockito.when(patientInfoRepository.getById(anyString()))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(patientId, new BigDecimal("36.6"));
        Mockito.verify(sendAlertService, Mockito.never()).send(anyString());

    }

}
