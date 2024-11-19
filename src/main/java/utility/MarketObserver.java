package utility;

import data_access.InMemoryUserDataAccessObject;
import entity.User;
import utility.exceptions.ValidationException;
import view.view_events.UpdateAssetEvent;

public class MarketObserver {

    private static volatile MarketObserver instance = null;

    private MarketObserver() {
    }

    public static MarketObserver Instance() {
        if (instance == null) {
            synchronized (MarketObserver.class) {
                if (instance == null) {
                    instance = new MarketObserver();
                }
            }
        }
        return instance;
    }

    public void onMarketUpdate() {
        try {
            String credential = ClientSessionManager.Instance().getCredential();
            User user = ServiceManager.Instance().
                    getService(InMemoryUserDataAccessObject.class).
                    getUserWithCredential(credential);

            System.out.println("Current user: " + user.getUsername());
            ViewManager.Instance().broadcastEvent(new UpdateAssetEvent(
                    user.getPortfolio(),
                    user.getBalance()
            ));
        } catch (ValidationException e) {
            System.out.println("Failed to find current user.");
        }
    }
}
