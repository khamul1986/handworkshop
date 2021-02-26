package pl.khamul.handworkshop.service;

import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.entity.UserNames;

import javax.servlet.http.HttpServletRequest;

public interface UserServiceInterface {


    User getUser(HttpServletRequest request);

    void setDetails(HttpServletRequest request, UserNames details);
}
