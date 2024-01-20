package com.shop.ua.services;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Store;
import com.shop.ua.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
    @Autowired
    private RepositoryManager repositoryManager;

    public Store createStore(User owner, String storeName, String companyFullName, String mainOffice,
                             String customerSupport, String legalForm, int legalEntityCode, String location,
                             String authorisedPersons, String isRegistered) {
        Store store = new Store();
        store.setOwner(owner);
        store.setName(storeName);
        store.setCompanyFullName(companyFullName);
        store.setMainOffice(mainOffice);
        store.setCustomerSupport(customerSupport);
        store.setLegalForm(legalForm);
        store.setLegalEntityCode(legalEntityCode);
        store.setLocation(location);
        store.setAuthorisedPersons(authorisedPersons);
        store.setIsRegistered(isRegistered);
        return repositoryManager.getStoreRepository().save(store);
    }

    public Store getStoreByOwner(User owner) {
        return repositoryManager.getStoreRepository().findByOwner(owner);
    }
}
