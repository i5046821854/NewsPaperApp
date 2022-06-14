package edu.skku.cs.pa3.presenter;

import com.google.gson.Gson;

import edu.skku.cs.pa3.contract.AuthContract;
import edu.skku.cs.pa3.model.User;

public class AuthPresenter implements AuthContract.contractForPresenter{

    AuthContract.contractForView view;
    User user;

    public AuthPresenter(AuthContract.contractForView view)
    {
        this.view = view;
        this.user = new User();
    }

    @Override
    public void onIdCheckBtnTouched(String name) {

            user.sendIdCheckRequest(new AuthContract.contractForModel.onFinished_Listener() {
                @Override
                public void onFinished(User response) {
                    view.showResult(response);
                }
            }, name);
    }

    @Override
    public void onLoginBtnTouched(String name, String pwd) {

        User user = new User();
        user.setName(name);
        user.setPassword(pwd);
        Gson gson = new Gson();
        String json = gson.toJson(user, User.class);
        user.sendLoginRequest(new AuthContract.contractForModel.onFinished_Listener() {
            @Override
            public void onFinished(User response) {
                view.showResult(response);
            }
        }, json);
    }

    @Override
    public void onRegBtnTouched(String name, String pwd, String category) {
        User user = new User();
        user.setName(name);
        user.setPassword(pwd);
        user.setCategories(category);
        Gson gson = new Gson();
        String json = gson.toJson(user, User.class);
        user.sendRegRequest(new AuthContract.contractForModel.onFinished_Listener() {
            @Override
            public void onFinished(User response) {
                view.showResult(response);
            }
        }, json);
    }
}
