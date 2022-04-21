package vn.elca.training.model.exception;

public class GroupLeaderDoesNotExistException extends Exception {
    private final String groupLeaderVisa;

    public GroupLeaderDoesNotExistException(String groupLeaderVisa) {
        super(String.format("group leader %s does not exist. please select available group leader", groupLeaderVisa));
        this.groupLeaderVisa = groupLeaderVisa;
    }

    public String getGroupLeaderVisa() {
        return groupLeaderVisa;
    }
}
