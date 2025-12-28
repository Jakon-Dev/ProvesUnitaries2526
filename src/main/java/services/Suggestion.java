package services;

import data.ProductID;
import medicalconsultation.TakingGuideline;

public class Suggestion {

    public enum SuggType {
        ADDITION,
        ELIMINATION,
        MODIFICATION
    }

    private final SuggType type;
    private final ProductID productID;
    private final TakingGuideline takingGuideline;

    public Suggestion(SuggType type, ProductID productID, TakingGuideline takingGuideline) {
        this.type = type;
        this.productID = productID;
        this.takingGuideline = takingGuideline;
    }

    public SuggType getType() {
        return type;
    }

    public ProductID getProductID() {
        return productID;
    }

    public TakingGuideline getTakingGuideline() {
        return takingGuideline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Suggestion that = (Suggestion) o;

        if (type != that.type) return false;
        if (productID != null ? !productID.equals(that.productID) : that.productID != null) return false;
        return takingGuideline != null ? takingGuideline.equals(that.takingGuideline) : that.takingGuideline == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (productID != null ? productID.hashCode() : 0);
        result = 31 * result + (takingGuideline != null ? takingGuideline.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "type=" + type +
                ", productID=" + productID +
                ", takingGuideline=" + takingGuideline +
                '}';
    }
}