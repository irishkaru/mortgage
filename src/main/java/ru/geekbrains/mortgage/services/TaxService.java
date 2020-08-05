package ru.geekbrains.mortgage.services;

import org.springframework.stereotype.Service;
import ru.geekbrains.mortgage.entity.MortgageApplication;

@Service
public class TaxService {

    public boolean getIsLowBudget(MortgageApplication application) {

        return (Math.random() > .5 ? true : false);
    }
}
