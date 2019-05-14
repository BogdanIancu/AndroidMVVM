package ro.facemsoft.coolweather.presenters;

import android.graphics.Bitmap;

public class MainActivityPresenter {
    private View view;

    public MainActivityPresenter(View view) {
        this.view = view;
    }

    public void searchCommand(String value) throws Exception {
        if(view.validateInput(value)) {
            view.performSearch(value);
        }
        else throw new Exception();
    }

    public interface View {
        boolean validateInput(String value);
        void performSearch(String value);
        void updateTemperature(int value);
        void updateDescription(String value);
        void updateImage(Bitmap value);
        void displayServiceErrorMessage();
    }
}
