package pl.lodz.p.ks.it.neighbourlyhelp.exception;

/**
 * Reprezentuje wyjątek dotyczący encji Advert
 */
public class ContractException extends AppBaseException {

    private static final String ACCESS_DENIED = "exception.contract.access_denied";
    private static final String ADVERT_IS_IN_PROGRESS = "exception.contract.advert_is_in_progress";
    private static final String ADVERT_IS_DISAPPROVED = "exception.contract.advert_is_disapproved";
    private static final String ADVERT_ALREADY_TAKEN = "exception.contract.advert_has_been_already_taken";
    private static final String MAX_TAKE_UP_ATTEMPTS_LIMIT_OVERDRAWN = "exception.contract.max_take_up_attempts_limit_overdrawn";
    private static final String IN_PROGRESS_CONTRACT_CANCEL = "exception.contract.in_progress_cancel_contract";
    private static final String TO_APPROVE_CONTRACT_CANCEL = "exception.contract.to_approve_cancel_contract";
    private static final String FINISHED_CONTRACT_CANCEL = "exception.contract.finished_cancel_contract";
    private static final String CANCELLED_CONTRACT = "exception.contract.cancelled_contract";
    private static final String NOT_STARTED_CONTRACT = "exception.contract.not_started";
    private static final String FORBIDDEN_TO_TAKE_OWN_ADVERT = "exception.contract.forbidden_to_take_own_advert";
    private static final String FORBIDDEN_TO_APPROVE_ENDED_MYSELF_CONTRACT = "exception.contract.forbidden_to_approve_ended_myself_contract";

    protected ContractException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ContractException(String message) {
        super(message);
    }

    /**
     * Wyjątek związany z brakień uprawnień do modyfikacji encji
     *
     * @return wyjątek AdvertException
     */
    public static ContractException accessDenied() {
        return new ContractException(ACCESS_DENIED);
    }

    /**
     * Wyjątek związany z faktem rezerwacji adverta
     *
     * @return wyjątek AdvertException
     */
    public static ContractException advertIsInProgress() {
        return new ContractException(ADVERT_IS_IN_PROGRESS);
    }

    /**
     * Wyjątek związany z faktem, że advert został usunięty
     *
     * @return wyjątek AdvertException
     */
    public static ContractException advertIsDisapproved() {
        return new ContractException(ADVERT_IS_DISAPPROVED);
    }

    public static ContractException advertAlreadyTaken() {
        return new ContractException(ADVERT_ALREADY_TAKEN);
    }

    public static ContractException maxTakeUpAttemptsLimitOverdrawn() {
        return new ContractException(MAX_TAKE_UP_ATTEMPTS_LIMIT_OVERDRAWN);
    }

    public static ContractException inProgressContractCancellation() {
        return new ContractException(IN_PROGRESS_CONTRACT_CANCEL);
    }

    public static ContractException toApproveContractCancellation() {
        return new ContractException(TO_APPROVE_CONTRACT_CANCEL);
    }

    public static ContractException finishedContractCancellation() {
        return new ContractException(FINISHED_CONTRACT_CANCEL);
    }

    public static ContractException contractAlreadyCancelled() {
        return new ContractException(CANCELLED_CONTRACT);
    }

    public static ContractException contractNotStartedYet() {
        return new ContractException(NOT_STARTED_CONTRACT);
    }

    public static ContractException forbiddenToTakeOwnAdvert() {
        return new ContractException(FORBIDDEN_TO_TAKE_OWN_ADVERT);
    }

    public static ContractException forbiddenToApproveEndedMyselfContract() {
        return new ContractException(FORBIDDEN_TO_APPROVE_ENDED_MYSELF_CONTRACT);
    }
}