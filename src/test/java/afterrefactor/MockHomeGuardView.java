package afterrefactor;

/**
 * Created by »ÆÏè on 15-9-3.
 */
public class MockHomeGuardView implements HomeGuardView {
    private boolean hasShowMessage;

    public boolean getHasShowMessage() {
        return hasShowMessage;
    }

    @Override
    public void showMessage(String message) {
        hasShowMessage = true;
    }
}
