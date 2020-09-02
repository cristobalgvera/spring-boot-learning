package cl.crisgvera.moviecatalogservice.model.rest;

import cl.crisgvera.moviecatalogservice.model.CatalogItem;

import java.util.List;

public class UserCatalogItem {

    private List<CatalogItem> userCatalogItems;

    public UserCatalogItem() {
    }

    public UserCatalogItem(List<CatalogItem> userCatalogItems) {
        this.userCatalogItems = userCatalogItems;
    }

    public List<CatalogItem> getUserCatalogItems() {
        return userCatalogItems;
    }

    public void setUserCatalogItems(List<CatalogItem> userCatalogItems) {
        this.userCatalogItems = userCatalogItems;
    }
}
