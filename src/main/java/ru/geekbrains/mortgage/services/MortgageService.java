package ru.geekbrains.mortgage.services;

import org.springframework.stereotype.Service;
import ru.geekbrains.mortgage.entity.MortgageApplication;
import ru.geekbrains.mortgage.entity.ResolutionStatus;
import ru.geekbrains.mortgage.model.MortgageList;
import ru.geekbrains.mortgage.model.MortgageRequest;
import ru.geekbrains.mortgage.model.MortgageResponse;
import ru.geekbrains.mortgage.repository.MortgageApplicationRepository;

import java.util.List;

@Service
public class MortgageService {

    private final MortgageApplicationRepository repository;
    private final PoliceService policeService;
    private final TaxService taxService;

    public MortgageService(
        MortgageApplicationRepository repository,
        PoliceService policeService,TaxService taxService) {
        this.repository = repository;
        this.policeService = policeService;
        this.taxService = taxService;
    }

    public MortgageList getAllMortgages() {
        return new MortgageList(repository.findAll());
    }

    public MortgageList getAllSuccessfulMortgages() {
        List<MortgageApplication> allSuccessful =
            repository.getAllByStatusEquals(ResolutionStatus.SUCCESSFUL);

        return new MortgageList(allSuccessful);
    }

    public MortgageResponse registerApplication(MortgageRequest request) {
        ResolutionStatus status;
        MortgageApplication application = new MortgageApplication();
        application.setName(request.getName());

        if (policeService.getIsTerrorist(application)){
            status = ResolutionStatus.TERRORIST;
        }
        else {
            status = ResolutionStatus.SUCCESSFUL;
        }
        if (taxService.getIsLowBudget(application) && (status == ResolutionStatus.SUCCESSFUL)){
            status = ResolutionStatus.LOW_BUDGET;
        }

        application.setStatus(status);

        application = repository.save(application);
        MortgageResponse response = new MortgageResponse();
        response.setId(application.getId());
        response.setRequest(request);
        if ((status == ResolutionStatus.SUCCESSFUL)){
            response.setResolution("OK");
        }
        else{
            response.setResolution("NO");
        }

        return response;
    }
}



/*
    @Service
    @Repository
    @Controller

    @Component
 */