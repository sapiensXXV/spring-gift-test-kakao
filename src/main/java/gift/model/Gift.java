package gift.model;

public class Gift {
    private final Long from;
    private final Long to;
    private final   Option option;
    private final int quantity;
    private final String message;

    public Gift(final Long from, final Long to, final Option option, final int quantity, final String message) {
        this.from = from;
        this.to = to;
        this.option = option;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
