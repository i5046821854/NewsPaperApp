package edu.skku.cs.pa3.contract;

import edu.skku.cs.pa3.model.User;

public interface AuthContract {

    interface contractForView{
        void showResult(User response);
    }

    interface contractForModel{
        interface onFinished_Listener{
            void onFinished(User response);
        }

        void sendIdCheckRequest(onFinished_Listener onFinished_listener, String name);
        void sendLoginRequest(onFinished_Listener onFinished_listener, String json);
        void sendRegRequest(onFinished_Listener onFinished_listener, String json);

    }

    interface contractForPresenter{
        void onIdCheckBtnTouched(String name);
        void onLoginBtnTouched(String name, String pwd);
        void onRegBtnTouched(String name, String pwd, String category);

    }

}
