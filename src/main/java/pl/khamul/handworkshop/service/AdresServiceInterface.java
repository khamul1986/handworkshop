package pl.khamul.handworkshop.service;

import pl.khamul.handworkshop.entity.Adress;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdresServiceInterface {

    List adressList(HttpServletRequest request);

    void addAdress(Adress adress, HttpServletRequest request);
}
