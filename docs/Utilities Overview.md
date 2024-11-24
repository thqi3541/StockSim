# Notes for latest update

## How this app works

There are three manager utilities which handle global access functionality:

1. `SessionManager`
   1. This is a singleton instance which runs on both server side and client side. When a user logs in, the server-side manager creates a unique token for this new session, keeps track of it, and sends the token back to the client side.
   2. The manager on the client side stores this token, and when requests are sent from client side, the input data is wrapped with the unique token to retrieve the current user.
   3. You don't need to worry about the implementation for now. If you need to add credentials for your use case when doing controller work, follow what I did in the execute buy controller - just add a dummy string.
2. `ServiceManager`
   1. Some classes like controller, interactor, and presenter need to be instantiated when the app runs, and UI components may need to call them to send or receive data. Instead of creating these instances inside the component, we create them in the app builder at start.
   2. Every class that needs to be instantiated and accessed should register itself in this manager in `AppBuilder`, and components should call the manager for this service. You may want to check the `OrderEntryPanel` for code samples.
3. `ViewManager`
   1. There are multiple causes that can trigger a UI component refresh. We wrap these causes with their new data as payload and type. All the items that may need to be handled by UI components are called view events.
   2. For example, after the interactor has finished its work, it sends the output data to the presenter. The presenter is responsible for identifying the event type, creating an event, and putting the output data inside the event.
   3. Then this event is sent to ViewManager. The presenter does not need to worry about handling other things, just prepare this event to call ViewManager. The ViewManager will then broadcast this event to all registered components.
   4. We say "register" to mean that a panel or component is created and added to the manager. The manager uses something called CardLayout in Java Swing to handle which component should be visible. So the SwitchPanel event is not handled by the component; it's handled by the manager.
   5. If you want to define a new event, first choose a type or add one in the EventType enum, then set up the XXXEvent class. Also, you need to go to the specific UI component and modify its code so that it can support this type, as well as define how the UI component should change according to the new data.
   6. New panels need to register themselves to ViewManager through the `AppBuilder` file, and you also need to set them up in the `Main` file to ensure that this build method is called when the app runs.
4. `StockMarket`
   1. This is a singleton instance which runs when the app runs and updates the stock data automatically.
   2. It updates the stock data every 2 minutes, ensuring that the stock data is always up-to-date.
   3. This will not exceed the API call limit for Finnhub.
