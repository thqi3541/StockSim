package view.view_events;

import entity.Portfolio;

public class UpdateAssetEvent extends ViewEvent {
  private final Portfolio portfolio;
  private final double balance;

  /**
   * Constructs an UpdatePortfolioEvent with a specified portfolio and balance, using only the
   * UPDATE_PORTFOLIO event type.
   *
   * @param portfolio The portfolio to update.
   * @param balance The balance associated with the portfolio.
   */
  public UpdateAssetEvent(Portfolio portfolio, double balance) {
    this.portfolio = portfolio;
    this.balance = balance;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public double getBalance() {
    return balance;
  }
}
