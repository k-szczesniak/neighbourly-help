package pl.lodz.p.ks.it.neighbourlyhelp.exception;

public class LoyaltyPointException extends AppBaseException {

    public LoyaltyPointException(String message) {
        super(message);
    }

    private static final String LOYALTY_POINTS_ACCOUNT_BALANCE_EXCEEDED = "exception.loyalty_point.loyalty_points_account_balance_exceeded";

    public static LoyaltyPointException loyaltyPointsAccountBalanceExceeded() {
        return new LoyaltyPointException(LOYALTY_POINTS_ACCOUNT_BALANCE_EXCEEDED);
    }
}